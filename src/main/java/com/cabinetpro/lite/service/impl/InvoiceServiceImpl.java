// InvoiceServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.InvoiceDao;
import com.cabinetpro.lite.dao.InvoiceNumberDao;
import com.cabinetpro.lite.dao.MaterialDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.InvoiceDto;
import com.cabinetpro.lite.dto.InvoiceGenerateRequestDto;
import com.cabinetpro.lite.model.Invoice;
import com.cabinetpro.lite.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceDao invoiceDao;
    private final MaterialDao materialDao;
    private final ProjectDao projectDao;
    private final InvoiceNumberDao numberDao;

    public InvoiceServiceImpl(InvoiceDao invoiceDao, MaterialDao materialDao, ProjectDao projectDao, InvoiceNumberDao numberDao) {
        this.invoiceDao = invoiceDao;
        this.materialDao = materialDao;
        this.projectDao = projectDao;
        this.numberDao = numberDao;
    }

    private void assertProject(Long projectId) throws SQLException {
        if (projectDao.findById(projectId).isEmpty())
            throw new IllegalArgumentException("Project not found: " + projectId);
    }

    @Override
    public List<Invoice> listByProject(Long projectId) {
        try {
            assertProject(projectId);
            return invoiceDao.findByProjectId(projectId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * subtotal = Σ qty*unitPrice; gst=10%; total=subtotal+gst
     */
    @Override
    @Transactional
    public InvoiceDto generateForProject(Long projectId, InvoiceGenerateRequestDto req) throws SQLException {
        // نرخ GST: BigDecimal واقعی، نه double
        BigDecimal gstRate = (req != null && req.getGstRate() != null)
                ? req.getGstRate()
                : new BigDecimal("0.10"); // از String استفاده کن تا دقت حفظ شود

        var materials = materialDao.findByProjectId(projectId);
        if (materials.isEmpty()) {
            throw new IllegalStateException("No materials for this project");
        }

        // subtotal = Σ (qty * unitPrice)  ← همه BigDecimal
        BigDecimal subtotal = materials.stream()
                .map(m -> m.getQty().multiply(m.getUnitPrice()))   // BigDecimal × BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gst = subtotal.multiply(gstRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(gst).setScale(2, RoundingMode.HALF_UP);

        Long id = invoiceDao.create(new Invoice(
                null, projectId, subtotal, gst, total, "DRAFT", null,
                        String.valueOf(numberDao.nextForYear(java.time.LocalDate.now().getYear()))
)
        );

        return new InvoiceDto(id, subtotal, gst, total, "DRAFT");
    }

    private BigDecimal round2(BigDecimal v) {
        return v.multiply(BigDecimal.valueOf(100.0)).divide(BigDecimal.valueOf(100.0));
    }

    @Override
    @Transactional
    public boolean update(Invoice v) {
        try {
            if (v.getId() == null) throw new IllegalArgumentException("id required");
            if (v.getSubtotal().signum() < 0 || v.getGst().signum() < 0 || v.getTotal().signum() < 0)
                throw new IllegalArgumentException("money must be >= 0");
            return invoiceDao.update(v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            return invoiceDao.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        try {
            return invoiceDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public InvoiceDto issue(Long id) throws SQLException {
        Invoice inv = invoiceDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        if (!"DRAFT".equals(inv.getStatus())) throw new IllegalStateException("Only DRAFT can be issued");

        int year = java.time.LocalDate.now().getYear();
        long seq = numberDao.nextForYear(year);
        String number = String.format("INV-%d-%05d", year, seq); // e.g., INV-2025-00012

        Instant now = Instant.now();
        boolean ok = invoiceDao.assignNumberAndIssue(id, number, now);
        if (!ok) throw new IllegalStateException("Issue failed");

        // برگرداندن وضعیت نهایی
        Invoice issued = invoiceDao.findById(id).orElseThrow();
        return new InvoiceDto(issued.getId(), issued.getSubtotal(), issued.getGst(), issued.getTotal(), issued.getStatus());
    }

    @Override
    @Transactional
    public boolean markPaid(Long id) throws SQLException {
        // فقط روی ISSUED منطقی‌ست، ولی اگر خواستی constraint نرم بگذار
        return invoiceDao.updateStatus(id, "PAID");
    }

    @Override
    @Transactional
    public boolean voidInvoice(Long id) throws SQLException {
        return invoiceDao.updateStatus(id, "VOID");
    }
}



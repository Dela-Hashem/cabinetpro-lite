// InvoiceServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.InvoiceDao;
import com.cabinetpro.lite.dao.MaterialDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.InvoiceDto;
import com.cabinetpro.lite.dto.InvoiceGenerateRequestDto;
import com.cabinetpro.lite.model.Invoice;
import com.cabinetpro.lite.model.Material;
import com.cabinetpro.lite.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceDao invoiceDao;
    private final MaterialDao materialDao;
    private final ProjectDao projectDao;

    public InvoiceServiceImpl(InvoiceDao invoiceDao, MaterialDao materialDao, ProjectDao projectDao) {
        this.invoiceDao = invoiceDao;
        this.materialDao = materialDao;
        this.projectDao = projectDao;
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
        double gstRate = (req != null && req.getGstRate() != null) ? req.getGstRate() : 0.10;

        List<Material> materials = materialDao.findByProjectId(projectId);
        if (materials.isEmpty()) {
            throw new IllegalStateException("No materials for this project");
        }

        double subtotal = round2(materials.stream()
                .mapToDouble(m -> m.getQty().multiply(m.getUnitPrice()).doubleValue())
                .sum());
        double gst = round2(subtotal * gstRate);
        double total = round2(subtotal + gst);

        Long id = invoiceDao.create(new Invoice(
                null, projectId, BigDecimal.valueOf(subtotal), BigDecimal.valueOf(gst), BigDecimal.valueOf(total), "DRAFT", null
        ));

        return new InvoiceDto(id, subtotal, gst, total, "DRAFT");
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
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
}

// InvoiceServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.InvoiceDao;
import com.cabinetpro.lite.dao.MaterialDao;
import com.cabinetpro.lite.dao.ProjectDao;
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
        this.invoiceDao = invoiceDao; this.materialDao = materialDao; this.projectDao = projectDao;
    }

    private void assertProject(Long projectId) throws SQLException {
        if (projectDao.findById(projectId).isEmpty())
            throw new IllegalArgumentException("Project not found: " + projectId);
    }

    @Override
    public List<Invoice> listByProject(Long projectId) {
        try { assertProject(projectId); return invoiceDao.findByProjectId(projectId); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** subtotal = Σ qty*unitPrice; gst=10%; total=subtotal+gst */
    @Override @Transactional
    public Long createFromProject(Long projectId) {
        try {
            assertProject(projectId);
            List<Material> mats = materialDao.findByProjectId(projectId);

            BigDecimal subtotal = mats.stream()
                    .map(m -> m.getQty().multiply(m.getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal gst = subtotal.multiply(new BigDecimal("0.10")).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal total = subtotal.add(gst).setScale(2, BigDecimal.ROUND_HALF_UP);

            Invoice v = new Invoice();
            v.setProjectId(projectId);
            v.setSubtotal(subtotal);
            v.setGst(gst);
            v.setTotal(total);
            v.setStatus("DRAFT");
            return invoiceDao.create(v);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean update(Invoice v) {
        try {
            if (v.getId() == null) throw new IllegalArgumentException("id required");
            if (v.getSubtotal().signum() < 0 || v.getGst().signum() < 0 || v.getTotal().signum() < 0)
                throw new IllegalArgumentException("money must be >= 0");
            return invoiceDao.update(v);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean delete(Long id) {
        try { return invoiceDao.deleteById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        try { return invoiceDao.findById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}

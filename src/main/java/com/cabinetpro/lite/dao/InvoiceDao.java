package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.Invoice;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public interface InvoiceDao extends Dao<Invoice,Long>{
    List<Invoice> findByProjectId(Long projectId) throws SQLException;
    boolean updateStatus(Long id, String status) throws SQLException;
    boolean assignNumberAndIssue(Long id, String invoiceNumber, Instant issuedAt) throws SQLException;
}

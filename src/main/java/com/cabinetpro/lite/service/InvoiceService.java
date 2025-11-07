package com.cabinetpro.lite.service;

import com.cabinetpro.lite.dto.InvoiceDto;
import com.cabinetpro.lite.dto.InvoiceGenerateRequestDto;
import com.cabinetpro.lite.model.Invoice;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public interface InvoiceService {
    List<Invoice> listByProject(Long projectId);
    InvoiceDto generateForProject(Long projectId, InvoiceGenerateRequestDto req) throws SQLException;   // محاسبه از مواد پروژه
    boolean update(Invoice v);
    boolean delete(Long id);
    Optional<Invoice> findById(Long id);
}

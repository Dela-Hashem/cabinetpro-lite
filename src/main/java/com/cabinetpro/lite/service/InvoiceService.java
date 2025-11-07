package com.cabinetpro.lite.service;

import com.cabinetpro.lite.model.Invoice;

import java.util.List;
import java.util.Optional;
public interface InvoiceService {
    List<Invoice> listByProject(Long projectId);
    Long createFromProject(Long projectId);   // محاسبه از مواد پروژه
    boolean update(Invoice v);
    boolean delete(Long id);
    Optional<Invoice> findById(Long id);
}

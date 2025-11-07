package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.Invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDao extends Dao<Invoice,Long>{
    List<Invoice> findByProjectId(Long projectId) throws SQLException;

}

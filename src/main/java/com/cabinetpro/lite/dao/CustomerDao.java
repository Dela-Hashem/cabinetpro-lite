package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CustomerDao extends Dao<Customer, Long> {

    Optional<Customer> findByEmail(String email) throws SQLException;

    List<Customer> searchByName(String q) throws SQLException;
}

// service/CustomerService.java
package com.cabinetpro.lite.service;

import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.CustomerUpdateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import com.cabinetpro.lite.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Long create(CustomerCreateRequestDto req) throws SQLException;
    boolean update(Long id, CustomerUpdateRequestDto req) throws SQLException;
    boolean delete(Long id) throws SQLException;
    Optional<Customer> findById(Long id) throws SQLException;
    List<Customer> findAll() throws SQLException;
    List<Customer> searchByName(String name) throws SQLException;
    Long createCustomerWithFirstProject(CustomerCreateRequestDto customer, ProjectCreateForCustomerDto project) throws SQLException;

}

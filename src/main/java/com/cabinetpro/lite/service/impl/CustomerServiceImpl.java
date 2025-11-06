// service/impl/CustomerServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.CustomerUpdateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Customer;
import com.cabinetpro.lite.model.Project;
import com.cabinetpro.lite.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;
    private final ProjectDao projectDao;

    public CustomerServiceImpl(CustomerDao customerDao, ProjectDao projectDao) { this.customerDao = customerDao;
        this.projectDao = projectDao;
    }

    @Override
    @Transactional
    public Long create(CustomerCreateRequestDto req) throws SQLException {
        return customerDao.create(new Customer(null, req.getFullName(), req.getPhone(), req.getEmail()));
    }

    @Override
    @Transactional
    public boolean update(Long id, CustomerUpdateRequestDto req) throws SQLException {
        return customerDao.update(new Customer(id, req.getFullName(), req.getPhone(), req.getEmail()));
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws SQLException {
        return customerDao.deleteById(id);
    }
    @Override
    public List<Customer> searchByName(String name) throws SQLException {
        return customerDao.searchByName(name);
    }

    public Long createCustomerWithFirstProject(CustomerCreateRequestDto customer, ProjectCreateForCustomerDto project) throws SQLException {
        Long customerId = customerDao.create(new Customer(
                null, customer.getFullName(), customer.getPhone(), customer.getEmail()
        ));

        // customerId ورودی پروژه را override می‌کنیم تا از تقلب ورودی جلوگیری شود
        projectDao.create(new Project(
                null, customerId, project.getTitle(), project.getAddress(), null
        ));

        return customerId;
    }


    @Override public Optional<Customer> findById(Long id) throws SQLException { return customerDao.findById(id); }
    @Override public List<Customer> findAll() throws SQLException { return customerDao.findAll(); }

}

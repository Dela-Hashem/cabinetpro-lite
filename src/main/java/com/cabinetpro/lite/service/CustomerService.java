package com.cabinetpro.lite.service;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Customer;
import com.cabinetpro.lite.model.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final ProjectDao projectDao;

    public CustomerService(CustomerDao customerDao, ProjectDao projectDao) {
        this.customerDao = customerDao;
        this.projectDao = projectDao;
    }

    /** ایجاد ساده‌ی مشتری (بدون تراکنش چندمرحله‌ای) */
    public Long createCustomer(CustomerCreateRequestDto req) throws SQLException {
        return customerDao.create(new Customer(null, req.getFullName(), req.getPhone(), req.getEmail()));
    }

    /**
     * ایجاد «مشتری + اولین پروژه» در یک تراکنش اتمیک.
     * اگر هرکدام fail شود، کل عملیات rollback می‌شود.
     */
    @Transactional
    public Long createCustomerWithFirstProject(CustomerCreateRequestDto custReq,
                                               ProjectCreateForCustomerDto projReq) throws SQLException {
        Long customerId = customerDao.create(new Customer(
                null, custReq.getFullName(), custReq.getPhone(), custReq.getEmail()
        ));
        projectDao.create(new Project(
                null, customerId, projReq.getTitle(), projReq.getAddress(), null
        ));
        return customerId;
    }
}

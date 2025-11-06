// service/impl/ProjectServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Customer;
import com.cabinetpro.lite.model.Project;
import com.cabinetpro.lite.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;
    private final CustomerDao customerDao;

    public ProjectServiceImpl(ProjectDao projectDao, CustomerDao customerDao) {
        this.projectDao = projectDao; this.customerDao = customerDao;
    }

    @Override
    @Transactional
    public Long create(ProjectCreateRequestDto req) throws SQLException {
        return projectDao.create(new Project(null, req.getCustomerId(), req.getTitle(), req.getAddress(), null));
    }

    @Override
    public List<Project> findByCustomer(Long customerId) throws SQLException {
        return projectDao.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Long createCustomerWithFirstProject(CustomerCreateRequestDto custReq,
                                               ProjectCreateForCustomerDto projReq) throws SQLException {
        Long customerId = customerDao.create(new Customer(null, custReq.getFullName(), custReq.getPhone(), custReq.getEmail()));
        projectDao.create(new Project(null, customerId, projReq.getTitle(), projReq.getAddress(), null));
        return customerId;
    }
}

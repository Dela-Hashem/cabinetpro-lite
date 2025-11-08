// service/ProjectService.java
package com.cabinetpro.lite.service;

import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Project;
import com.cabinetpro.lite.model.TaskItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Long create(ProjectCreateRequestDto req) throws SQLException;
    List<Project> findByCustomer(Long customerId) throws SQLException;
    Long createCustomerWithFirstProject( // سناریوی اتمیک
                                         com.cabinetpro.lite.dto.CustomerCreateRequestDto customer,
                                         ProjectCreateForCustomerDto project) throws SQLException;
    Optional<Project> findById(Long id);

}

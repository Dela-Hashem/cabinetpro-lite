package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.Project;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProjectDao extends Dao<Project, Long> {
    List<Project> findByCustomerId(Long customerId) throws SQLException;
    Optional<Project> findLatestByCustomer(Long customerId) throws SQLException;
}

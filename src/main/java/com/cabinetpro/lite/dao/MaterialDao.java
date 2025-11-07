package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.Material;

import java.sql.SQLException;
import java.util.List;

public interface MaterialDao extends Dao<Material, Long> {
    List<Material> findByProjectId(Long projectId) throws SQLException;

}

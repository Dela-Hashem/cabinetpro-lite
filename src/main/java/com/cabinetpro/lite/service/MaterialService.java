package com.cabinetpro.lite.service;

import com.cabinetpro.lite.model.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    List<Material> listByProject(Long projectId);
    Long create(Material m);
    boolean update(Material m);
    boolean delete(Long id);
    Optional<Material> findById(Long id);
}

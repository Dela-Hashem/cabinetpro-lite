// MaterialServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.MaterialDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.model.Material;
import com.cabinetpro.lite.service.MaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialDao materialDao;
    private final ProjectDao projectDao;

    public MaterialServiceImpl(MaterialDao materialDao, ProjectDao projectDao) {
        this.materialDao = materialDao; this.projectDao = projectDao;
    }

    private void assertProject(Long projectId) throws SQLException {
        if (projectDao.findById(projectId).isEmpty())
            throw new IllegalArgumentException("Project not found: " + projectId);
    }

    @Override
    public List<Material> listByProject(Long projectId) {
        try { assertProject(projectId); return materialDao.findByProjectId(projectId); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public Long create(Material m) {
        try {
            assertProject(m.getProjectId());
            if (m.getQty().signum() < 0 || m.getUnitPrice().signum() < 0)
                throw new IllegalArgumentException("qty/unitPrice must be >= 0");
            return materialDao.create(m);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean update(Material m) {
        try {
            if (m.getId() == null) throw new IllegalArgumentException("id required");
            if (m.getQty().signum() < 0 || m.getUnitPrice().signum() < 0)
                throw new IllegalArgumentException("qty/unitPrice must be >= 0");
            return materialDao.update(m);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean delete(Long id) {
        try { return materialDao.deleteById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Material> findById(Long id) {
        try { return materialDao.findById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}

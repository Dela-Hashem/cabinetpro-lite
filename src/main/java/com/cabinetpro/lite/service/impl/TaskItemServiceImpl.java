// TaskItemServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dao.TaskItemDao;
import com.cabinetpro.lite.model.TaskItem;
import com.cabinetpro.lite.service.TaskItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskItemServiceImpl implements TaskItemService {

    private final TaskItemDao taskDao;
    private final ProjectDao projectDao;

    public TaskItemServiceImpl(TaskItemDao taskDao, ProjectDao projectDao) {
        this.taskDao = taskDao; this.projectDao = projectDao;
    }

    private void assertProject(Long projectId) throws SQLException {
        if (projectDao.findById(projectId).isEmpty())
            throw new IllegalArgumentException("Project not found: " + projectId);
    }

    @Override
    public List<TaskItem> listByProject(Long projectId) {
        try { assertProject(projectId); return taskDao.findByProjectId(projectId); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public Long create(TaskItem t) {
        try { assertProject(t.getProjectId()); return taskDao.create(t); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean update(TaskItem t) {
        try {
            if (t.getId() == null) throw new IllegalArgumentException("id required");
            return taskDao.update(t);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override @Transactional
    public boolean delete(Long id) {
        try { return taskDao.deleteById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<TaskItem> findById(Long id) {
        try { return taskDao.findById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}

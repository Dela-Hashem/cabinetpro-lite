package com.cabinetpro.lite.dao;

import com.cabinetpro.lite.model.TaskItem;

import java.sql.SQLException;
import java.util.List;

public interface TaskItemDao extends Dao<TaskItem, Long> {

    List<TaskItem> findByProjectId(Long projectId) throws SQLException;
}

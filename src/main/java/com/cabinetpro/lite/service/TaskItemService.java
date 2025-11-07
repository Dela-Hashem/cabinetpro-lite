package com.cabinetpro.lite.service;

import com.cabinetpro.lite.model.TaskItem;

import java.util.List;
import java.util.Optional;

public interface TaskItemService {

    List<TaskItem> listByProject(Long projectId);
    Long create(TaskItem t);
    boolean update(TaskItem t);
    boolean delete(Long id);
    Optional<TaskItem> findById(Long id);

}

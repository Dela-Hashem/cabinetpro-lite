package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.TaskItemCreateRequestDto;
import com.cabinetpro.lite.dto.TaskItemUpdateRequestDto;
import com.cabinetpro.lite.model.TaskItem;
import com.cabinetpro.lite.service.TaskItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskItemController {

    private final TaskItemService taskService;
    public TaskItemController(TaskItemService taskService) { this.taskService = taskService; }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<TaskItem>> listByProject(@PathVariable Long projectId) throws SQLException {
        return ResponseEntity.ok(taskService.listByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskItem> getById(@PathVariable Long id) throws SQLException {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody TaskItemCreateRequestDto req) throws SQLException {
        TaskItem t = new TaskItem();
        t.setProjectId(req.projectId);
        t.setTitle(req.title);
        t.setDone(req.done);
        t.setSortOrder(req.sortOrder);
        return ResponseEntity.ok(taskService.create(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id,
                                          @Valid @RequestBody TaskItemUpdateRequestDto req) throws SQLException {
        TaskItem t = new TaskItem();
        t.setId(id);
        t.setTitle(req.title);
        t.setDone(req.done);
        t.setSortOrder(req.sortOrder);
        return ResponseEntity.ok(taskService.update(t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(taskService.delete(id));
    }
}

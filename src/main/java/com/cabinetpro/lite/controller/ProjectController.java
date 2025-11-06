package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Project;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectDao projectDao;

    public ProjectController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProjectCreateRequestDto req) throws SQLException {
        Long id = projectDao.create(new Project(
                null,
                req.getCustomerId(),
                req.getTitle(),
                req.getAddress(),
                null
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<Project>> byCustomer(@PathVariable Long customerId) throws SQLException {
        return ResponseEntity.ok(projectDao.findByCustomerId(customerId));
    }
}

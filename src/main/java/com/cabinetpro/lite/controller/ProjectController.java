package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.CreateWithProjectRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateRequestDto;
import com.cabinetpro.lite.model.Project;
import com.cabinetpro.lite.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping public ResponseEntity<Long> create(@Valid @RequestBody ProjectCreateRequestDto body) throws SQLException {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(body));
    }

    @GetMapping("/by-customer/{id}")
    public List<Project> list(@PathVariable Long id) throws SQLException {
        return projectService.findByCustomer(id);
    }

    @PostMapping("/with-project")
    public ResponseEntity<Long> createWithProject(@Valid @RequestBody CreateWithProjectRequestDto body) throws SQLException {
        Long id = projectService.createCustomerWithFirstProject(body.getCustomer(), body.getProject());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}

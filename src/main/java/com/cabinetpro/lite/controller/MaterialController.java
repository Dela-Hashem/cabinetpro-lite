package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.MaterialCreateRequestDto;
import com.cabinetpro.lite.dto.MaterialUpdateRequestDto;
import com.cabinetpro.lite.model.Material;
import com.cabinetpro.lite.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
@Validated
public class MaterialController {

    private final MaterialService materialService;
    public MaterialController(MaterialService materialService) { this.materialService = materialService; }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<Material>> listByProject(@PathVariable Long projectId) throws SQLException {
        return ResponseEntity.ok(materialService.listByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) throws SQLException {
        return materialService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody MaterialCreateRequestDto req) throws SQLException {
        Material m = new Material();
        m.setProjectId(req.projectId);
        m.setName(req.name);
        m.setQty(req.qty);
        m.setUnitPrice(req.unitPrice);
        return ResponseEntity.ok(materialService.create(m));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id,
                                          @Valid @RequestBody MaterialUpdateRequestDto req) throws SQLException {
        Material m = new Material();
        m.setId(id);
        m.setName(req.name);
        m.setQty(req.qty);
        m.setUnitPrice(req.unitPrice);
        return ResponseEntity.ok(materialService.update(m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(materialService.delete(id));
    }
}

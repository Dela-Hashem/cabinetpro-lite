package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestParam String name,
                                       @RequestParam(required = false) String phone,
                                       @RequestParam(required = false) String email) throws SQLException {
        return ResponseEntity.ok(repo.create(name, phone, email));
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> names() throws SQLException {
        return ResponseEntity.ok(repo.findAllNames());
    }
}

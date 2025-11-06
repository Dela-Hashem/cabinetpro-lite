package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.CreateWithProjectRequestDto;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.CustomerUpdateRequestDto;
import com.cabinetpro.lite.model.Customer;
import com.cabinetpro.lite.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService; // اضافه شد

    public CustomerController( CustomerService customerService) {
        this.customerService = customerService;
    }


//    // نکته: به اینترفیس تزریق می‌کنیم، نه ایمپلمنتیشن. تست و تعویض آسان‌تر می‌شود.
//    public CustomerController(customerService customerService) {
//        this.customerService = customerService;
//    }

    // DTO ترکیبی فقط برای این endpoint
    public static class CreateWithProjectRequest {
        @Valid public com.cabinetpro.lite.dto.CustomerCreateRequestDto customer;
        @Valid public com.cabinetpro.lite.dto.ProjectCreateRequestDto project;
    }
    /**
     * ایجاد مشتری جدید
     * @param req بدنه JSON با ولیدیشن
     * @return id ساخته‌شده
     */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CustomerCreateRequestDto req) throws SQLException {
        Customer c = new Customer(null, req.getFullName(), req.getPhone(), req.getEmail());
        Long id = customerService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * لیست همه مشتری‌ها (ساده و بدون صفحه‌بندی؛ بعداً اضافه می‌کنیم)
     */
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() throws SQLException {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) throws SQLException {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws SQLException {
        boolean ok = customerService.delete(id);
        return ok ? ResponseEntity.noContent().build()         // 204
                : ResponseEntity.notFound().build();         // 404 اگر چیزی حذف نشد
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody CustomerUpdateRequestDto req) throws SQLException {
        Customer c = new Customer(id, req.getFullName(), req.getPhone(), req.getEmail());
        boolean ok = customerService.update(id,req);
        return ok ? ResponseEntity.noContent().build()  // 204
                : ResponseEntity.notFound().build();  // 404
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchByName(
            @RequestParam @NotBlank(message = "q is required") String q) throws SQLException {
        return ResponseEntity.ok(customerService.searchByName(q.trim()));
    }
}

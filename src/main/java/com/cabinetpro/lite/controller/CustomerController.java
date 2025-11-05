package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.model.Customer;
import jakarta.validation.Valid;
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

    private final CustomerDao customerDao;

    // نکته: به اینترفیس تزریق می‌کنیم، نه ایمپلمنتیشن. تست و تعویض آسان‌تر می‌شود.
    public CustomerController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * ایجاد مشتری جدید
     * @param req بدنه JSON با ولیدیشن
     * @return id ساخته‌شده
     */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CustomerCreateRequestDto req) throws SQLException {
        Customer c = new Customer(null, req.getFullName(), req.getPhone(), req.getEmail());
        Long id = customerDao.create(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * لیست همه مشتری‌ها (ساده و بدون صفحه‌بندی؛ بعداً اضافه می‌کنیم)
     */
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() throws SQLException {
        return ResponseEntity.ok(customerDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) throws SQLException {
        return customerDao.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws SQLException {
        boolean ok = customerDao.deleteById(id);
        return ok ? ResponseEntity.noContent().build()         // 204
                : ResponseEntity.notFound().build();         // 404 اگر چیزی حذف نشد
    }

}

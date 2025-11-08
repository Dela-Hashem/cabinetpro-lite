package com.cabinetpro.lite.controller;

import com.cabinetpro.lite.dto.InvoiceDto;
import com.cabinetpro.lite.dto.InvoiceGenerateRequestDto;
import com.cabinetpro.lite.dto.InvoiceUpdateRequestDto;
import com.cabinetpro.lite.model.Invoice;
import com.cabinetpro.lite.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Validated
public class InvoiceController {

    private final InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService) { this.invoiceService = invoiceService; }

//    @GetMapping("/by-project/{projectId}")
//    public ResponseEntity<List<Invoice>> listByProject(@PathVariable Long projectId) throws SQLException {
//        return ResponseEntity.ok(invoiceService.listByProject(projectId));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) throws SQLException {
        return invoiceService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // تولید فاکتور از مواد پروژه
    @PostMapping("/from-project/{projectId}")
    public ResponseEntity<Long> generateForProject(@PathVariable Long projectId,
                                                   @Valid @RequestBody(required = false) InvoiceGenerateRequestDto req) throws SQLException {
        InvoiceDto dto = invoiceService.generateForProject(projectId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id,
                                          @Valid @RequestBody InvoiceUpdateRequestDto req) throws SQLException {
        Invoice v = new Invoice();
        v.setId(id);
        v.setSubtotal(req.subtotal);
        v.setGst(req.gst);
        v.setTotal(req.total);
        v.setStatus(req.status);
        return ResponseEntity.ok(invoiceService.update(v));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(invoiceService.delete(id));
    }

    @GetMapping("/by-project/{projectId}")
    public List<Invoice> byProject(@PathVariable Long projectId) throws SQLException {
        return invoiceService.listByProject(projectId);
    }

    @PostMapping("/{id}/issue")
    public ResponseEntity<InvoiceDto> issue(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(invoiceService.issue(id));
    }

    @PostMapping("/{id}/paid")
    public ResponseEntity<Void> markPaid(@PathVariable Long id) throws SQLException {
        return invoiceService.markPaid(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/void")
    public ResponseEntity<Void> voidInv(@PathVariable Long id) throws SQLException {
        return invoiceService.voidInvoice(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }



}

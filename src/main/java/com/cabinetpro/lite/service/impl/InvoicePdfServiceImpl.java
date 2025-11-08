// service/impl/InvoicePdfServiceImpl.java
package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.service.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Map;

import static java.math.RoundingMode.HALF_UP;

@Service
public class InvoicePdfServiceImpl implements InvoicePdfService {

    private final TemplateEngine thymeleaf;
    private final InvoiceService invoiceService;
    private final ProjectService projectService;
    private final CustomerService customerService;
    private final MaterialService materialService;

    public InvoicePdfServiceImpl(TemplateEngine thymeleaf,
                                 InvoiceService invoiceService,
                                 ProjectService projectService,
                                 CustomerService customerService,
                                 MaterialService materialService) {
        this.thymeleaf = thymeleaf;
        this.invoiceService = invoiceService;
        this.projectService = projectService;
        this.customerService = customerService;
        this.materialService = materialService;
    }

    @Override
    public byte[] renderPdf(Long invoiceId) throws Exception {
        // 1) داده‌ها
        var inv = invoiceService.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        var proj = projectService.findById(inv.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        var cust = customerService.findById(proj.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // خطوط از materials
        var mats = materialService.listByProject(proj.getId()); // نام متد با سرویس تو هماهنگ است
        var lines = mats.stream().map(m -> Map.of(
                "description", m.getName(),
                "qty",   m.getQty().setScale(3, HALF_UP).toPlainString(),
                "unit",  m.getUnitPrice().setScale(2, HALF_UP).toPlainString(),
                "line",  m.getQty().multiply(m.getUnitPrice()).setScale(2, HALF_UP).toPlainString()
        )).toList();

        // 2) مدل Thymeleaf
        var ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("company", Map.of(
                "abn", "12 345 678 901",
                "bsb", "123-456",
                "acc", "12345678"
        ));
        ctx.setVariable("invoice", Map.of(
                "number", inv.getInvoiceNumber() == null ? "(DRAFT)" : inv.getInvoiceNumber(),
                "date",   inv.getIssuedAt() != null ? inv.getIssuedAt().toString() : ""
        ));
        ctx.setVariable("customer", Map.of("name", cust.getFullName()));
        ctx.setVariable("project",  Map.of("title", proj.getTitle()));
        ctx.setVariable("lines", lines);
        ctx.setVariable("totals", Map.of(
                "subtotal", inv.getSubtotal().setScale(2, HALF_UP).toPlainString(),
                "gst",      inv.getGst().setScale(2, HALF_UP).toPlainString(),
                "total",    inv.getTotal().setScale(2, HALF_UP).toPlainString()
        ));

        String html = thymeleaf.process("invoice", ctx); // resources/templates/invoice.html

        // 3) PDF
        try (var baos = new ByteArrayOutputStream()) {
            var builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        }
    }
}

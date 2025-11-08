// service/InvoicePdfService.java
package com.cabinetpro.lite.service;

public interface InvoicePdfService {
    byte[] renderPdf(Long invoiceId) throws Exception;
}

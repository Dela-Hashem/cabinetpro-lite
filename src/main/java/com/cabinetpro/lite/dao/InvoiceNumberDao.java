package com.cabinetpro.lite.dao;

import java.sql.SQLException;

public interface InvoiceNumberDao {
    long nextForYear(int y) throws SQLException; // افزایش شمارنده و برگرداندن مقدار جدید
}

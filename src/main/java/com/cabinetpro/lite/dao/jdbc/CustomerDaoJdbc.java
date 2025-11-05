package com.cabinetpro.lite.dao.jdbc;

import com.cabinetpro.lite.config.DatabaseConnectionManager;
import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.model.Customer;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * هدف: پیاده‌سازی JDBC برای CustomerDao
 */
@Repository
public class CustomerDaoJdbc implements CustomerDao {

    private final DatabaseConnectionManager cm;

    // constructor injection برای تست‌پذیری و حذف وابستگی پنهان
    public CustomerDaoJdbc(DatabaseConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public Long create(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (full_name, phone, email) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
                return null;
            }
        }
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT id, full_name, phone, email FROM customers ORDER BY id DESC";
        List<Customer> result = new ArrayList<>();

        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                result.add(c);
            }
        }

        return result;
    }
    @Override
    public Optional<Customer> findById(Long id) throws SQLException {
        String sql = "SELECT id, full_name, phone, email FROM customers WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Customer(
                            rs.getLong("id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // متدهای باقی‌مانده هنوز پیاده‌سازی نمی‌شن
    @Override public boolean update(Customer c) { return false; }
    @Override public long count() { return 0; }
    @Override public Optional<Customer> findByEmail(String email) { return Optional.empty(); }
    @Override public List<Customer> searchByName(String q) { return List.of(); }
}

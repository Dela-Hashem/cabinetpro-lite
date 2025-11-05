package com.cabinetpro.lite.repository;

import com.cabinetpro.lite.config.DatabaseConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository {

    private final DatabaseConnectionManager cm;

    public CustomerRepository(DatabaseConnectionManager cm) {
        this.cm = cm;
    }

    public long create(String fullName, String phone, String email) throws SQLException {
        String sql = "INSERT INTO customers(full_name, phone, email) VALUES (?,?,?) RETURNING id";
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public List<String> findAllNames() throws SQLException {
        String sql = "SELECT full_name FROM customers ORDER BY id DESC";
        List<String> out = new ArrayList<>();
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
        }
        return out;
    }
}

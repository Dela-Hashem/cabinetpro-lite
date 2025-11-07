package com.cabinetpro.lite.dao.jdbc;

import com.cabinetpro.lite.dao.InvoiceDao;

import com.cabinetpro.lite.model.Invoice;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
@Repository
public class InvoiceDaoJdbc implements InvoiceDao {
    private final DataSource ds;
    public InvoiceDaoJdbc(DataSource ds) { this.ds = ds; }

    @Override
    public Long create(Invoice e) throws SQLException {
        final String sql = "INSERT INTO invoices(project_id,subtotal,gst,total,status) VALUES(?,?,?,?,?) RETURNING id,issued_at";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, e.getProjectId());
            ps.setBigDecimal(2, e.getSubtotal());
            ps.setBigDecimal(3, e.getGst());
            ps.setBigDecimal(4, e.getTotal());
            ps.setString(5, e.getStatus());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                long id = rs.getLong("id");
                e.setId(id);
                Timestamp ts = rs.getTimestamp("issued_at");
                e.setIssuedAt(ts != null ? ts.toInstant() : null);
                return id;
            }
        }
    }

    @Override
    public Optional<Invoice> findById(Long id) throws SQLException {
        final String sql = "SELECT id,project_id,subtotal,gst,total,status,issued_at FROM invoices WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Invoice> findAll() throws SQLException {
        final String sql = "SELECT id,project_id,subtotal,gst,total,status,issued_at FROM invoices ORDER BY id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Invoice> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public boolean update(Invoice e) throws SQLException {
        final String sql = "UPDATE invoices SET subtotal=?, gst=?, total=?, status=? WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, e.getSubtotal());
            ps.setBigDecimal(2, e.getGst());
            ps.setBigDecimal(3, e.getTotal());
            ps.setString(4, e.getStatus());
            ps.setLong(5, e.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        final String sql = "DELETE FROM invoices WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public long count() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM invoices";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
    }

    @Override
    public List<Invoice> findByProjectId(Long projectId) throws SQLException {
        final String sql = "SELECT id,project_id,subtotal,gst,total,status,issued_at FROM invoices WHERE project_id=? ORDER BY id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Invoice> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private Invoice map(ResultSet rs) throws SQLException {
        Invoice v = new Invoice();
        v.setId(rs.getLong("id"));
        v.setProjectId(rs.getLong("project_id"));
        v.setSubtotal(rs.getBigDecimal("subtotal"));
        v.setGst(rs.getBigDecimal("gst"));
        v.setTotal(rs.getBigDecimal("total"));
        v.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("issued_at");
        v.setIssuedAt(ts != null ? ts.toInstant() : null);
        return v;
    }
}
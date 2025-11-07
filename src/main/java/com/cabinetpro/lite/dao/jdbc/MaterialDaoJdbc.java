package com.cabinetpro.lite.dao.jdbc;

import com.cabinetpro.lite.dao.MaterialDao;
import com.cabinetpro.lite.model.Material;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class MaterialDaoJdbc implements MaterialDao {

    private final DataSource ds;

    public MaterialDaoJdbc(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Long create(Material e) throws SQLException {
        final String sql = "INSERT INTO materials(project_id,name,qty,unit_price) VALUES(?,?,?,?) RETURNING id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, e.getProjectId());
            ps.setString(2, e.getName());
            ps.setBigDecimal(3, e.getQty());
            ps.setBigDecimal(4, e.getUnitPrice());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    e.setId(id);
                    return id;
                }
            }
            throw new SQLException("Insert failed (no id returned)");
        }
    }

    @Override
    public Optional<Material> findById(Long id) throws SQLException {
        final String sql = "SELECT id,project_id,name,qty,unit_price FROM materials WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Material> findAll() throws SQLException {
        final String sql = "SELECT id,project_id,name,qty,unit_price FROM materials ORDER BY id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Material> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public boolean update(Material e) throws SQLException {
        final String sql = "UPDATE materials SET name=?, qty=?, unit_price=? WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setBigDecimal(2, e.getQty());
            ps.setBigDecimal(3, e.getUnitPrice());
            ps.setLong(4, e.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        final String sql = "DELETE FROM materials WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public long count() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM materials";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next(); return rs.getLong(1);
        }
    }

    @Override
    public List<Material> findByProjectId(Long projectId) throws SQLException {
        final String sql = "SELECT id,project_id,name,qty,unit_price FROM materials WHERE project_id=? ORDER BY id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Material> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private Material map(ResultSet rs) throws SQLException {
        Material m = new Material();
        m.setId(rs.getLong("id"));
        m.setProjectId(rs.getLong("project_id"));
        m.setName(rs.getString("name"));
        m.setQty(rs.getBigDecimal("qty"));
        m.setUnitPrice(rs.getBigDecimal("unit_price"));
        return m;
    }
}

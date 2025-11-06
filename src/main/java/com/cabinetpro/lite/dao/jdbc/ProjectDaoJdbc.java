package com.cabinetpro.lite.dao.jdbc;

//import com.cabinetpro.lite.config.DatabaseConnectionManager;

import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.model.Project;
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
public class ProjectDaoJdbc implements ProjectDao {

    //    private final DatabaseConnectionManager cm;
//
//    public ProjectDaoJdbc(DatabaseConnectionManager cm) {
//        this.cm = cm;
//    }
    private final DataSource dataSource;

    public ProjectDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long create(Project p) throws SQLException {
        String sql = "INSERT INTO projects (customer_id, title, address) VALUES (?,?,?) RETURNING id";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, p.getCustomerId());
            ps.setString(2, p.getTitle());
            ps.setString(3, p.getAddress());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    @Override
    public List<Project> findByCustomerId(Long customerId) throws SQLException {
        String sql = "SELECT id, customer_id, title, address, created_at " +
                "FROM projects WHERE customer_id = ? ORDER BY id DESC";
        List<Project> out = new ArrayList<>();
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    @Override
    public Optional<Project> findLatestByCustomer(Long customerId) throws SQLException {
        String sql = "SELECT id, customer_id, title, address, created_at " +
                "FROM projects WHERE customer_id = ? ORDER BY created_at DESC, id DESC LIMIT 1";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Project> findAll() throws SQLException {
        String sql = "SELECT id, customer_id, title, address, created_at FROM projects ORDER BY id DESC";
        List<Project> out = new ArrayList<>();
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    @Override
    public Optional<Project> findById(Long id) throws SQLException {
        String sql = "SELECT id, customer_id, title, address, created_at FROM projects WHERE id = ?";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean update(Project p) throws SQLException {
        String sql = "UPDATE projects SET customer_id = ?, title = ?, address = ? WHERE id = ?";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, p.getCustomerId());
            ps.setString(2, p.getTitle());
            ps.setString(3, p.getAddress());
            ps.setLong(4, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM projects WHERE id = ?";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM projects";
        Connection c = DataSourceUtils.getConnection(dataSource);
        try (//cm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private Project map(ResultSet rs) throws SQLException {
        return new Project(
                rs.getLong("id"),
                rs.getLong("customer_id"),
                rs.getString("title"),
                rs.getString("address"),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}

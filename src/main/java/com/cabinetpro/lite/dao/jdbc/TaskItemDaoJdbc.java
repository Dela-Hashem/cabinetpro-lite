package com.cabinetpro.lite.dao.jdbc;

import com.cabinetpro.lite.dao.TaskItemDao;

import com.cabinetpro.lite.model.TaskItem;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
@Repository
public class TaskItemDaoJdbc implements TaskItemDao {
    private final DataSource ds;
    public TaskItemDaoJdbc(DataSource ds) { this.ds = ds; }

    @Override
    public Long create(TaskItem e) throws SQLException {
        final String sql = "INSERT INTO tasks(project_id,title,done,sort_order) VALUES(?,?,?,?) RETURNING id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, e.getProjectId());
            ps.setString(2, e.getTitle());
            ps.setBoolean(3, e.isDone());
            ps.setInt(4, e.getSortOrder());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); long id = rs.getLong(1); e.setId(id); return id;
            }
        }
    }

    @Override
    public Optional<TaskItem> findById(Long id) throws SQLException {
        final String sql = "SELECT id,project_id,title,done,sort_order FROM tasks WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<TaskItem> findAll() throws SQLException {
        final String sql = "SELECT id,project_id,title,done,sort_order FROM tasks ORDER BY sort_order,id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<TaskItem> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public boolean update(TaskItem e) throws SQLException {
        final String sql = "UPDATE tasks SET title=?, done=?, sort_order=? WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getTitle());
            ps.setBoolean(2, e.isDone());
            ps.setInt(3, e.getSortOrder());
            ps.setLong(4, e.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        final String sql = "DELETE FROM tasks WHERE id=?";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public long count() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM tasks";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
    }

    @Override
    public List<TaskItem> findByProjectId(Long projectId) throws SQLException {
        final String sql = "SELECT id,project_id,title,done,sort_order FROM tasks WHERE project_id=? ORDER BY sort_order,id";
        Connection c = DataSourceUtils.getConnection(ds);
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                List<TaskItem> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private TaskItem map(ResultSet rs) throws SQLException {
        TaskItem t = new TaskItem();
        t.setId(rs.getLong("id"));
        t.setProjectId(rs.getLong("project_id"));
        t.setTitle(rs.getString("title"));
        t.setDone(rs.getBoolean("done"));
        t.setSortOrder(rs.getInt("sort_order"));
        return t;
    }
}

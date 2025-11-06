package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dto.DbPoolPingResponse;
import com.cabinetpro.lite.service.DatabaseHealthService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DatabaseHealthServiceImpl implements DatabaseHealthService {

    private final DataSource dataSource;

    public DatabaseHealthServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DbPoolPingResponse poolPing() throws SQLException {
        long startedAt = System.currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1");
             ResultSet rs = statement.executeQuery()) {

            if (!rs.next()) {
                throw new SQLException("Pool ping query did not return any rows");
            }

            long latency = System.currentTimeMillis() - startedAt;
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProduct = metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion();
            String driver = metaData.getDriverName();

            return new DbPoolPingResponse("UP", latency, databaseProduct, driver);
        }
    }
}

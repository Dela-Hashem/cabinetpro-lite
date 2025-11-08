package com.cabinetpro.lite.dao.jdbc;

import com.cabinetpro.lite.dao.InvoiceNumberDao;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class InvoiceNumberDaoJdbc implements InvoiceNumberDao {
    private final DataSource ds;
    public InvoiceNumberDaoJdbc(DataSource ds){ this.ds = ds; }

    @Override
    public long nextForYear(int y) throws SQLException {
        String upsert = """
          INSERT INTO invoice_seq(y, n) VALUES (?,0)
          ON CONFLICT(y) DO NOTHING
        """;
        String inc = "UPDATE invoice_seq SET n = n + 1 WHERE y=? RETURNING n";
        var c = DataSourceUtils.getConnection(ds);
        try {
            try (PreparedStatement p1 = c.prepareStatement(upsert)) {
                p1.setInt(1, y);
                p1.executeUpdate();
            }
            try (PreparedStatement p2 = c.prepareStatement(inc)) {
                p2.setInt(1, y);
                try (ResultSet rs = p2.executeQuery()) {
                    rs.next(); return rs.getLong(1);
                }
            }
        } finally {
            DataSourceUtils.releaseConnection(c, ds);
        }
    }
}

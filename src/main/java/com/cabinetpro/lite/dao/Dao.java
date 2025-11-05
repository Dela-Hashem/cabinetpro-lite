package com.cabinetpro.lite.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * قرارداد عمومی DAO برای JDBC دستی.
 * توجه: فعلاً SQLException را bubble می‌دهیم تا مدیریت خطا شفاف بماند.
 */
public interface Dao<T, ID> {

    ID create(T entity) throws SQLException;

    Optional<T> findById(ID id) throws SQLException;

    List<T> findAll() throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean deleteById(ID id) throws SQLException;

    long count() throws SQLException;
}

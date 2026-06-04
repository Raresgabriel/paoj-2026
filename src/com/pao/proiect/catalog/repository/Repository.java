package com.pao.proiect.catalog.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfata generica de repository CRUD.
 * T  = tipul entitatii
 * ID = tipul cheii primare
 */
public interface Repository<T, ID> {
    void save(T entity)         throws SQLException;
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll()           throws SQLException;
    void update(T entity)       throws SQLException;
    void delete(ID id)          throws SQLException;
}

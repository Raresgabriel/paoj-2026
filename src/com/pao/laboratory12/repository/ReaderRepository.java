package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderRepository implements Repository<Reader, Long> {

    private Connection getConn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    private Reader mapRow(ResultSet rs) throws SQLException {
        Reader r = new Reader();
        r.setId(rs.getLong("id"));
        r.setName(rs.getString("name"));
        r.setEmail(rs.getString("email"));
        return r;
    }

    @Override
    public void save(Reader reader) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "INSERT INTO reader(name, email) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) reader.setId(keys.getLong(1));
            }
        }
    }

    @Override
    public Optional<Reader> findById(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM reader WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Reader> findAll() throws SQLException {
        List<Reader> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM reader ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public void update(Reader reader) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "UPDATE reader SET name=?, email=? WHERE id=?")) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getEmail());
            ps.setLong(3, reader.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("DELETE FROM reader WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

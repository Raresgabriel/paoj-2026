package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository implements Repository<Author, Long> {

    private Connection getConn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    private Author mapRow(ResultSet rs) throws SQLException {
        Author a = new Author();
        a.setId(rs.getLong("id"));
        a.setName(rs.getString("name"));
        a.setCountry(rs.getString("country"));
        return a;
    }

    @Override
    public void save(Author author) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "INSERT INTO author(name, country) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getName());
            ps.setString(2, author.getCountry());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) author.setId(keys.getLong(1));
            }
        }
    }

    @Override
    public Optional<Author> findById(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM author WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM author ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public void update(Author author) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "UPDATE author SET name=?, country=? WHERE id=?")) {
            ps.setString(1, author.getName());
            ps.setString(2, author.getCountry());
            ps.setLong(3, author.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("DELETE FROM author WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

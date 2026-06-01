package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements Repository<Book, Long> {

    private Connection getConn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthorId(rs.getLong("author_id"));
        b.setAvailable(rs.getInt("available") == 1);
        return b;
    }

    @Override
    public void save(Book book) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "INSERT INTO book(title, author_id, available) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthorId());
            ps.setInt(3, book.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) book.setId(keys.getLong(1));
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM book WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement("SELECT * FROM book ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public void update(Book book) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement(
                "UPDATE book SET title=?, author_id=?, available=? WHERE id=?")) {
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthorId());
            ps.setInt(3, book.isAvailable() ? 1 : 0);
            ps.setLong(4, book.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = getConn().prepareStatement("DELETE FROM book WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

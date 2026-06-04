package com.pao.proiect.catalog.repository;

import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterieRepository implements Repository<Materie, String> {

    private Connection conn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    @Override
    public void save(Materie m) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT OR IGNORE INTO materie(cod, nume) VALUES(?,?)")) {
            ps.setString(1, m.getCod());
            ps.setString(2, m.getNume());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Materie> findById(String cod) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT cod, nume FROM materie WHERE cod = ?")) {
            ps.setString(1, cod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Materie(rs.getString("nume"), rs.getString("cod")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Materie> findAll() throws SQLException {
        List<Materie> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT cod, nume FROM materie ORDER BY nume");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(new Materie(rs.getString("nume"), rs.getString("cod")));
        }
        return list;
    }

    @Override
    public void update(Materie m) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "UPDATE materie SET nume = ? WHERE cod = ?")) {
            ps.setString(1, m.getNume());
            ps.setString(2, m.getCod());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String cod) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM materie WHERE cod = ?")) {
            ps.setString(1, cod);
            ps.executeUpdate();
        }
    }
}

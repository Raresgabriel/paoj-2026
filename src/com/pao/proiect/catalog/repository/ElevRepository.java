package com.pao.proiect.catalog.repository;

import com.pao.proiect.catalog.model.Elev;
import com.pao.proiect.catalog.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElevRepository implements Repository<Elev, String> {

    private Connection conn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    @Override
    public void save(Elev e) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT OR IGNORE INTO elev(cnp, nume, prenume, clasa_id) VALUES(?,?,?,?)")) {
            ps.setString(1, e.getCnp());
            ps.setString(2, e.getNume());
            ps.setString(3, e.getPrenume());
            ps.setString(4, e.getClasaScolara() != null ? e.getClasaScolara().getId() : null);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Elev> findById(String cnp) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT cnp, nume, prenume, clasa_id FROM elev WHERE cnp = ?")) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Elev> findAll() throws SQLException {
        List<Elev> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT cnp, nume, prenume, clasa_id FROM elev ORDER BY nume, prenume");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public void update(Elev e) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "UPDATE elev SET nume = ?, prenume = ?, clasa_id = ? WHERE cnp = ?")) {
            ps.setString(1, e.getNume());
            ps.setString(2, e.getPrenume());
            ps.setString(3, e.getClasaScolara() != null ? e.getClasaScolara().getId() : null);
            ps.setString(4, e.getCnp());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String cnp) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM elev WHERE cnp = ?")) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        }
    }

    public List<String> eleviCuNrNote() throws SQLException {
        String sql = """
                SELECT e.nume, e.prenume, e.clasa_id, COUNT(n.id) AS nr_note
                FROM elev e
                LEFT JOIN nota n ON n.elev_cnp = e.cnp
                GROUP BY e.cnp
                ORDER BY nr_note DESC, e.nume
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("nume") + " " + rs.getString("prenume")
                        + " (clasa " + rs.getString("clasa_id") + ")"
                        + " - " + rs.getInt("nr_note") + " note");
            }
        }
        return result;
    }

    private Elev mapRow(ResultSet rs) throws SQLException {
        return new Elev(rs.getString("nume"), rs.getString("prenume"),
                rs.getString("cnp"), null);
    }
}

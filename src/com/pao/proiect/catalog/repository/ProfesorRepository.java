package com.pao.proiect.catalog.repository;

import com.pao.proiect.catalog.model.GradDidactic;
import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Profesor;
import com.pao.proiect.catalog.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfesorRepository implements Repository<Profesor, String> {

    private Connection conn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    @Override
    public void save(Profesor p) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT OR IGNORE INTO profesor(cnp, nume, prenume, grad, este_diriginte) VALUES(?,?,?,?,?)")) {
            ps.setString(1, p.getCnp());
            ps.setString(2, p.getNume());
            ps.setString(3, p.getPrenume());
            ps.setString(4, p.getGradDidactic().name());
            ps.setInt(5, 0);
            ps.executeUpdate();
        }
        for (Materie m : p.getMateriiPredate()) {
            saveMaterieAsociata(p.getCnp(), m.getCod());
        }
    }

    public void saveMaterieAsociata(String cnpProfesor, String codMaterie) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT OR IGNORE INTO profesor_materie(profesor_cnp, materie_cod) VALUES(?,?)")) {
            ps.setString(1, cnpProfesor);
            ps.setString(2, codMaterie);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Profesor> findById(String cnp) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM profesor WHERE cnp = ?")) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Profesor> findAll() throws SQLException {
        List<Profesor> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM profesor ORDER BY nume, prenume");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public void update(Profesor p) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "UPDATE profesor SET nume=?, prenume=?, grad=? WHERE cnp=?")) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getPrenume());
            ps.setString(3, p.getGradDidactic().name());
            ps.setString(4, p.getCnp());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String cnp) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM profesor WHERE cnp = ?")) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        }
    }

    public List<String> profesoriCuMaterii() throws SQLException {
        String sql = """
                SELECT p.nume, p.prenume, p.grad, m.nume AS materie
                FROM profesor p
                JOIN profesor_materie pm ON pm.profesor_cnp = p.cnp
                JOIN materie m ON m.cod = pm.materie_cod
                ORDER BY p.nume, p.prenume, m.nume
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("nume") + " " + rs.getString("prenume")
                        + " [" + rs.getString("grad") + "]"
                        + " -> " + rs.getString("materie"));
            }
        }
        return result;
    }

    private Profesor mapRow(ResultSet rs) throws SQLException {
        return new Profesor(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("cnp"),
                GradDidactic.valueOf(rs.getString("grad")));
    }
}

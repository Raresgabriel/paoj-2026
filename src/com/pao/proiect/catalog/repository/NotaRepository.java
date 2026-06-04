package com.pao.proiect.catalog.repository;

import com.pao.proiect.catalog.model.Elev;
import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Nota;
import com.pao.proiect.catalog.model.Profesor;
import com.pao.proiect.catalog.model.TipNota;
import com.pao.proiect.catalog.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotaRepository implements Repository<Nota, Integer> {

    private Connection conn() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException e) { throw new SQLException(e); }
    }

    @Override
    public void save(Nota nota) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "INSERT INTO nota(valoare, data, tip, elev_cnp, materie_cod, profesor_cnp) VALUES(?,?,?,?,?,?)")) {
            ps.setInt(1, nota.getValoare());
            ps.setString(2, nota.getData().toString());
            ps.setString(3, nota.getTip().name());
            ps.setString(4, nota.getElev().getCnp());
            ps.setString(5, nota.getMaterie().getCod());
            ps.setString(6, nota.getProfesor().getCnp());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Nota> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM nota WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("  Nota #" + rs.getInt("id")
                            + ": val=" + rs.getInt("valoare")
                            + " tip=" + rs.getString("tip")
                            + " data=" + rs.getString("data"));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Nota> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public void update(Nota nota) throws SQLException {
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM nota WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<String> noteWithJoin() throws SQLException {
        String sql = """
                SELECT n.id, n.valoare, n.data, n.tip,
                       e.nume AS elev_nume, e.prenume AS elev_prenume,
                       m.nume AS materie,
                       p.nume AS prof_nume, p.prenume AS prof_prenume
                FROM nota n
                JOIN elev     e ON e.cnp = n.elev_cnp
                JOIN materie  m ON m.cod = n.materie_cod
                JOIN profesor p ON p.cnp = n.profesor_cnp
                ORDER BY e.nume, e.prenume, m.nume, n.data
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add("#" + rs.getInt("id")
                        + " | " + rs.getString("elev_nume") + " " + rs.getString("elev_prenume")
                        + " | " + rs.getString("materie")
                        + " | val=" + rs.getInt("valoare")
                        + " (" + rs.getString("tip") + ")"
                        + " | prof=" + rs.getString("prof_nume") + " " + rs.getString("prof_prenume")
                        + " | " + rs.getString("data"));
            }
        }
        return result;
    }

    public void saveInTransaction(Nota nota) throws SQLException {
        Connection c = conn();
        c.setAutoCommit(false);
        try {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO nota(valoare, data, tip, elev_cnp, materie_cod, profesor_cnp) VALUES(?,?,?,?,?,?)")) {
                ps.setInt(1, nota.getValoare());
                ps.setString(2, nota.getData().toString());
                ps.setString(3, nota.getTip().name());
                ps.setString(4, nota.getElev().getCnp());
                ps.setString(5, nota.getMaterie().getCod());
                ps.setString(6, nota.getProfesor().getCnp());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT COUNT(*) FROM elev WHERE cnp = ?")) {
                ps.setString(1, nota.getElev().getCnp());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new SQLException("Elevul nu exista in DB: " + nota.getElev().getCnp());
                    }
                }
            }
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(true);
        }
    }
}

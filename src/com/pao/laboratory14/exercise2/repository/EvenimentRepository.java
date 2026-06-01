package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    private Connection conn() throws SQLException {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void initSchema() throws SQLException {
        try (Statement st = conn().createStatement()) {
            st.execute("DROP TABLE IF EXISTS evenimente");
            st.execute(
                "CREATE TABLE IF NOT EXISTS evenimente (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nume TEXT NOT NULL," +
                "data TEXT NOT NULL," +
                "capacitate INTEGER," +
                "tip TEXT)"
            );
        }
    }

    @Override
    public void save(Eveniment e) throws SQLException {
        String sql = "INSERT INTO evenimente(nume, data, capacitate, tip) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNume());
            ps.setString(2, e.getData());
            ps.setInt(3, e.getCapacitate());
            ps.setString(4, e.getTip().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM evenimente WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        List<Eveniment> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement("SELECT * FROM evenimente ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public void update(Eveniment e) throws SQLException {
        String sql = "UPDATE evenimente SET nume=?, data=?, capacitate=?, tip=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, e.getNume());
            ps.setString(2, e.getData());
            ps.setInt(3, e.getCapacitate());
            ps.setString(4, e.getTip().name());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM evenimente WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int deleteImpl(int id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM evenimente WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("SELECT COUNT(*) FROM evenimente");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Eveniment map(ResultSet rs) throws SQLException {
        return new Eveniment(
            rs.getInt("id"),
            rs.getString("nume"),
            rs.getString("data"),
            rs.getInt("capacitate"),
            TipBilet.valueOf(rs.getString("tip"))
        );
    }
}

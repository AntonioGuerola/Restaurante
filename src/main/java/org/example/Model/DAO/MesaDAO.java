package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO implements DAO<Mesa, Integer> {

    private static final String INSERT = "INSERT INTO mesa (tipo, numMesa, fecha, horaMesa, tiempo, cuenta) VALUES (?,?,?,?,?,?)";
    private static final String DELETE = "DELETE FROM mesa WHERE id = ?";
    private static final String FINDBYID = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa WHERE id = ?";
    private static final String FINDALL = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa";

    private final Connection con;

    public MesaDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public Mesa save(Mesa entity) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getTipo().name());
            ps.setInt(2, entity.getNumMesa());
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setTime(4, Time.valueOf(LocalTime.now()));
            ps.setInt(5, entity.getTiempo());
            ps.setDouble(6, entity.getCuenta() != null ? entity.getCuenta().getSumaTotal() : 0);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        }
        return entity;
    }

    @Override
    public Mesa delete(Mesa entity) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(DELETE)) {
            pst.setInt(1, entity.getId());
            pst.executeUpdate();
        }
        return entity;
    }

    @Override
    public Mesa findById(Integer key) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
            ps.setInt(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMesa(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Mesa> findAll() {
        List<Mesa> mesas = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FINDALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mesas.add(mapResultSetToMesa(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mesas;
    }

    @Override
    public void close() throws IOException {

    }

    private Mesa mapResultSetToMesa(ResultSet rs) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setId(rs.getInt("id"));
        mesa.setTipo(TipoMesa.valueOf(rs.getString("tipo")));
        mesa.setNumMesa(rs.getInt("numMesa"));
        mesa.setFecha(rs.getDate("fecha").toString());
        mesa.setHoraMesa(rs.getTime("horaMesa").toString());
        mesa.setTiempo(rs.getInt("tiempo"));
        return mesa;
    }
}

package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.Cuenta;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO implements DAO<Mesa, Integer> {

    private static final String INSERT = "INSERT INTO mesa (tipo, numMesa, fecha, tiempo, cuenta) VALUES (?,?,?,?,?)";
    private static final String UPDATE = "UPDATE mesa SET tiempo = ?, cuenta = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM mesa WHERE id = ?";
    private static final String FINDBYID = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa WHERE id = ?";
    private static final String FINDBYNUMMESA = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa WHERE numMesa = ? AND tipo LIKE ?";
    private static final String FINDALL = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa";
    private static final String CANTIDADTERRAZA = "SELECT COUNT(*) FROM mesa WHERE tipo = 'TERRAZA'";
    private static final String CANTIDADCAFETERIA = "SELECT COUNT(*) FROM mesa WHERE tipo = 'CAFETERIA'";
    private static final String UPDATEHORAMESA = "UPDATE mesa SET horaMesa = ? WHERE id = ?";
    private static final String UPDATETIEMPO = "UPDATE mesa SET tiempo = ? WHERE id = ?";

    private final Connection con;

    public MesaDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public Mesa save(Mesa entity) throws SQLException {
        if (entity.getId() == 0) {
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, entity.getTipo().name());
                    ps.setInt(2, entity.getNumMesa());
                    ps.setDate(3, Date.valueOf(LocalDate.now()));
                    ps.setInt(4, entity.getTiempo());
                    ps.setDouble(5, entity.getCuenta() != null ? entity.getCuenta().getSumaTotal() : 0);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            entity.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } else {
            try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
                ps.setInt(1, entity.getTiempo());
                ps.setDouble(2, entity.getCuenta() != null ? entity.getCuenta().getSumaTotal() : 0);
                ps.setInt(3, entity.getId());
                ps.executeUpdate();
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
        System.out.println(mesas);
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

        // Verificar si "horaMesa" es null antes de convertirlo
        Time horaMesa = rs.getTime("horaMesa");
        if (horaMesa != null) {
            mesa.setHoraMesa(horaMesa.toString());
        } else {
            mesa.setHoraMesa(""); // Asignar valor vacío si es null
        }

        mesa.setTiempo(rs.getInt("tiempo"));

        mesa.setCuenta(null);

        return mesa;
    }



    public int contarMesasTerraza() throws SQLException {
        int count = 0;
        try (PreparedStatement ps = con.prepareStatement(CANTIDADTERRAZA);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public int contarMesasCafeteria() throws SQLException {
        int count = 0;
        try (PreparedStatement ps = con.prepareStatement(CANTIDADCAFETERIA);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public Mesa findByNumMesa(Integer key, String tipo) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(FINDBYNUMMESA)) {
            ps.setInt(1, key);
            ps.setString(2, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMesa(rs);
                }
            }
        }
        return null;
    }

    public void actualizarHoraMesa(int idMesa, String horaActual) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(UPDATEHORAMESA)) {
            ps.setString(1, horaActual);
            ps.setInt(2, idMesa);
            ps.executeUpdate();
        }
    }

    public void actualizarTiempo(int idMesa, int tiempo) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(UPDATETIEMPO)) {
            ps.setInt(1, tiempo);
            ps.setInt(2, idMesa);
            ps.executeUpdate();
        }
    }
}

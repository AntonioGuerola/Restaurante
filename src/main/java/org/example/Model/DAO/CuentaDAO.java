package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.Cuenta;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;
import org.example.View.Controller;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO implements DAO<Cuenta, Integer> {
    private static final String INSERT = "INSERT INTO cuenta (idMesa, sumaTotal) VALUES (?,?)";
    private static final String UPDATE = "UPDATE cuenta SET sumaTotal = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM cuenta WHERE id = ?";
    private static final String FINDBYID = "SELECT id, idMesa, sumaTotal, horaCobro FROM cuenta WHERE id = ?";
    private static final String FINDALL = "SELECT id, idMesa, sumaTotal, horaCobro FROM cuenta";
    private static final String FINDBYMESAID = "SELECT id, idMesa, sumaTotal, horaCobro FROM cuenta WHERE idMesa = ?";

    private final Connection con;
    public CuentaDAO(){this.con = MySQLConnection.getConnection();}

    @Override
    public Cuenta save(Cuenta entity) throws SQLException {
        if (con != null) {
            if (entity.getId() == 0) {
                // Inserción nueva
                try (PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, entity.getMesa().getId());
                    ps.setDouble(2, entity.getSumaTotal());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            entity.setId(rs.getInt(1));
                        }
                    }
                }
            } else {
                // Actualización existente
                try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
                    ps.setDouble(1, entity.getSumaTotal());
                    ps.setInt(2, entity.getId()); // Corregido índice
                    ps.executeUpdate();
                }
            }
        }
        return entity;
    }


    @Override
    public Cuenta delete(Cuenta cuenta) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, cuenta.getId());
                ps.executeUpdate();
                return cuenta;
            }
        }
        return null;
    }

    @Override
    public Cuenta findById(Integer id) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToCuenta(rs);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List findAll() throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDALL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cuentas.add(mapResultSetToCuenta(rs));
                }
            }
        }
        return cuentas;
    }

    @Override
    public void close() throws IOException {

    }

    private Cuenta mapResultSetToCuenta(ResultSet rs) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(rs.getInt("id"));

        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesa = mesaDAO.findById(rs.getInt("idMesa"));
        cuenta.setMesa(mesa);

        // Asignar el valor directamente desde la base de datos
        cuenta.setSumaTotal(rs.getDouble("sumaTotal"));

        // Verificar si "horaCobro" es null antes de convertirlo
        Time horaCobro = rs.getTime("horaCobro");
        if (horaCobro != null) {
            cuenta.setHoraCobro(horaCobro.toString());
        } else {
            cuenta.setHoraCobro(""); // Asignar valor vacío si es null
        }
        return cuenta;
    }


    public Cuenta findByMesaId(int mesaId) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYMESAID)) {
                ps.setInt(1, mesaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToCuenta(rs); // Reutilizamos el método que mapea el ResultSet a una cuenta
                    }
                }
            }
        }
        return null; // Si no se encuentra una cuenta asociada a la mesa
    }
}

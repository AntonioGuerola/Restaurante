package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.Mesa;
import Model.Entity.Producto;
import Model.Entity.TipoMesa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO extends Mesa implements DAO {

    private final static String INSERT = "INSERT INTO mesa (tipo, numMesa, fecha, horaMesa, tiempo, cuenta) VALUES (?,?,?,?,?,?)";
    private static final String DELETE = "DELETE FROM mesa WHERE id = ?";
    private final static String FINDBYID = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa WHERE id = ?";
    private final static String FINDALL = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa";

    Connection con = MySQLConnection.getConnection();
    private Connection connection;
    public MesaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object save(Object entity) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                LocalDate fechaActual = LocalDate.now();
                LocalTime horaActual = LocalTime.now();

                String fechaString = fechaActual.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String horaString = horaActual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                ps.setString(1, this.getTipo().name());
                ps.setInt(2, this.getNumMesa());
                ps.setString(3, fechaString);
                ps.setString(4, horaString);
                ps.setInt(5, this.getTiempo());
                ps.setDouble(6, this.getCuenta());

                return ps.executeUpdate() > 0;
            }
        } else {
            return false;
        }
    }

    @Override
    public Object delete(Object entity) {
        Mesa tmp = (Mesa) entity;
        if (tmp != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setInt(1, tmp.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                tmp = null;
            }
        }
        return tmp;
    }

    @Override
    public Mesa findById(Object key) throws SQLException {
        Connection con = MySQLConnection.getConnection();

        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, (Integer) key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Mesa mesa = new Mesa();
                        mesa.setId(rs.getInt("id"));
                        mesa.setTipo(TipoMesa.valueOf(rs.getString("tipo")));
                        mesa.setNumMesa(rs.getInt("numMesa"));
                        mesa.setFecha(rs.getString("fecha"));
                        mesa.setHoraMesa(rs.getString("horaMesa"));
                        mesa.setTiempo(rs.getInt("tiempo"));
                        mesa.setCuenta(rs.getDouble("cuenta"));
                        return mesa;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List findAll() {
        List<Mesa> mesas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(FINDALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setTipo(TipoMesa.valueOf(rs.getString("tipo")));
                mesa.setNumMesa(rs.getInt("numMesa"));
                mesa.setFecha(rs.getString("fecha"));
                mesa.setHoraMesa(rs.getString("horaMesa"));
                mesa.setTiempo(rs.getInt("tiempo"));
                mesa.setCuenta(rs.getDouble("cuenta"));
                mesas.add(mesa);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mesas;
    }

    @Override
    public void close() throws IOException {

    }
}

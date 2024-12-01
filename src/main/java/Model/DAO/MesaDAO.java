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
    private final static String GETHORAMESA = "SELECT horaMesa FROM mesa WHERE id = ?";
    private final static String UPDATETIEMPO = "UPDATE mesa SET tiempo = ? WHERE id = ?";
    private final static String GETCUENTA = "SELECT SUM(p.precio) AS totalCuenta " +
            "FROM cesta c " +
            "JOIN cesta_producto cp ON c.id = cp.idCesta " +
            "JOIN producto p ON cp.idProducto = p.id " +
            "WHERE c.idMesa = ?";
    private final static String UPDATECUENTA = "UPDATE mesa SET cuenta = ? WHERE id = ?";

    Connection con = MySQLConnection.getConnection();

    public MesaDAO() {
        this.con = con;
    }


    @Override
    public Object save(Object entity) throws SQLException {
        Mesa tmp = (Mesa) entity;
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                LocalDate fechaActual = LocalDate.now();
                LocalTime horaActual = LocalTime.now();

                String fechaString = fechaActual.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String horaString = horaActual.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                ps.setString(1, tmp.getTipo().name());
                ps.setInt(2, tmp.getNumMesa());
                ps.setString(3, fechaString);
                ps.setString(4, horaString);
                ps.setInt(5, tmp.getTiempo());
                ps.setDouble(6, tmp.getCuenta());

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
        try (PreparedStatement ps = con.prepareStatement(FINDALL);
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

    public void actualizarTiempo(int idMesa) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(GETHORAMESA)) {
                ps.setInt(1, idMesa);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String horaMesaStr = rs.getString("horaMesa");
                        LocalTime horaMesa = LocalTime.parse(horaMesaStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

                        LocalTime horaActual = LocalTime.now();
                        long minutos = java.time.Duration.between(horaMesa, horaActual).toMinutes();

                        try (PreparedStatement psUpdate = con.prepareStatement(UPDATETIEMPO)) {
                            psUpdate.setInt(1, (int) minutos);
                            psUpdate.setInt(2, idMesa);
                            psUpdate.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void actualizarCuenta(int idMesa) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(GETCUENTA)) {
                ps.setInt(1, idMesa);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double totalCuenta = rs.getDouble("totalCuenta");

                        try (PreparedStatement psUpdate = con.prepareStatement(UPDATECUENTA)) {
                            psUpdate.setDouble(1, totalCuenta);
                            psUpdate.setInt(2, idMesa);
                            psUpdate.executeUpdate();
                        }
                    }
                }
            }
        }
    }


}

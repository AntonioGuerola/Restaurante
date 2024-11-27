package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.Mesa;
import Model.Entity.TipoMesa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MesaDAO extends Mesa implements DAO {

    private final static String INSERT = "INSERT INTO mesa (tipo, numMesa, fecha, horaMesa, tiempo, cuenta) VALUES (?,?,?,?,?,?)";
    private final static String FINDBYID = "SELECT id, tipo, numMesa, fecha, horaMesa, tiempo, cuenta FROM mesa WHERE id = ?";

    private Connection connection;

    public MesaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object save(Object entity) throws SQLException {
        Connection con = MySQLConnection.getConnection();
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
    public Object delete(Object entity) throws SQLException {
        return null;
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
                        mesa.setTipo(TipoMesa.valueOf(rs.getString("tipo"))); // Enum para tipo
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
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}

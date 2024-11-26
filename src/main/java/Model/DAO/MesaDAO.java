package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.Mesa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MesaDAO extends Mesa implements DAO {

    private final static String INSERT = "INSERT INTO mesa (tipo, numMesa, fecha, horaMesa, tiempo, cuenta) VALUES (?,?,?,?,?,?)";

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
    public Object findById(Object key) {
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

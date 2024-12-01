package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.Comanda;
import Model.Entity.Mesa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComandaDAO extends Comanda implements DAO{
    private final static String INSERT = "INSERT INTO cesta (horaComanda, idMesa) VALUES (?,?)";
    private final static String FINDBYID = "SELECT id, horaComanda, idMesa FROM cesta WHERE id = ?";

    Connection con = MySQLConnection.getConnection();

    @Override
    public Object save(Object entity) throws SQLException {
        Comanda tmp = (Comanda) entity;
        MesaDAO mesaDAO = new MesaDAO();
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                LocalTime horaActualComanda = LocalTime.now();
                String horaString = horaActualComanda.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                ps.setString(1, horaString);
                ps.setInt(2, tmp.getMesa().getId());

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
    public Object findById(Object key) throws SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Connection con = MySQLConnection.getConnection();

        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, (Integer) key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Comanda comanda = new Comanda();
                        comanda.setId(rs.getInt("id"));
                        comanda.setHoraComanda(rs.getString("horaComanda"));
                        try{
                            Mesa mesa = mesaDAO.findById("idMesa");
                            mesa.getId();
                        }catch (IllegalArgumentException e){
                            return null;
                        }
                        return comanda;
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

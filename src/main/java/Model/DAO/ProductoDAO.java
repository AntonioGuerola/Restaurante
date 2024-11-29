package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.Mesa;
import Model.Entity.Producto;
import Model.Entity.TipoMesa;
import Model.Entity.TipoProducto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductoDAO extends Producto implements DAO {
    private final static String INSERT = "INSERT INTO Producto (nombre, precio, tipoProducto, destino) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE Producto SET nombre = ?, precio = ?, tipoProducto = ?, destino = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Producto WHERE id = ?";
    private final static String FINDBYID = "SELECT id, nombre, precio, tipoProducto, destino FROM Producto WHERE id = ?";

    Connection con = MySQLConnection.getConnection();

    public ProductoDAO() {
        this.con = con;
    }

    @Override
    public Object save(Object entity) throws SQLException {
        Producto tmp = (Producto) entity;
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                ps.setString(1, tmp.getNombre());
                ps.setDouble(2, tmp.getPrecio());
                ps.setString(3, tmp.getTipo().getUrl());
                ps.setString(4, tmp.getDestino());

                return ps.executeUpdate() > 0;
            }
        } else {
            {
                try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
                    ps.setString(1, tmp.getNombre());
                    ps.setDouble(2, tmp.getPrecio());
                    ps.setString(3, tmp.getTipo().getUrl());
                    ps.setString(4, tmp.getDestino());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public Object delete(Object entity) {
        Producto tmp = (Producto) entity;
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
    public Object findById(Object key) throws SQLException {
        Connection con = MySQLConnection.getConnection();

        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, (Integer) key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Producto producto = new Producto();
                        producto.setId(rs.getInt("id"));
                        producto.setNombre(rs.getString("nombre"));
                        producto.setPrecio(rs.getDouble("precio"));
                        String tipoProductoString = rs.getString("tipoProducto");
                        try{
                            TipoProducto tipoProducto = TipoProducto.valueOf(tipoProductoString.toUpperCase());
                            producto.setTipo(tipoProducto);
                        }catch (IllegalArgumentException e){
                            return null;
                        }
                        return producto;
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

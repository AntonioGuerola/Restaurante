package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.TipoProducto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements DAO<Producto, Integer> {

    private static final String INSERT = "INSERT INTO producto (nombre, precio, tipoProducto, destino) VALUES (?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM producto WHERE id = ?";
    private static final String FINDBYID = "SELECT id, nombre, precio, tipoProducto, destino FROM producto WHERE id = ?";
    private static final String FINDALL = "SELECT id, nombre, precio, tipoProducto, destino FROM producto";
    private static final String UPDATE = "UPDATE producto SET nombre = ?, precio = ?, tipoProducto = ? WHERE id = ?";

    private Connection con = MySQLConnection.getConnection();

    public ProductoDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public Producto save(Producto producto) throws SQLException {
        if (producto.getId() == 0) {
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, producto.getNombre());
                    ps.setDouble(2, producto.getPrecio());
                    ps.setString(3, producto.getTipo().name());
                    ps.setString(4, producto.getDestino());

                    int affectedRows = ps.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                producto.setId(generatedKeys.getInt(1));
                            }
                        }
                    }
                }
            }
        } else {
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
                    ps.setString(1, producto.getNombre());
                    ps.setDouble(2, producto.getPrecio());
                    ps.setString(3, producto.getTipo().name());
                    ps.setInt(4, producto.getId());

                    ps.executeUpdate();
                }
            }
        }
        return producto;
    }

    @Override
    public Producto delete(Producto producto) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, producto.getId());
                ps.executeUpdate();
            }
        }
        return producto;
    }

    @Override
    public Producto findById(Integer id) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToProducto(rs);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDALL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    productos.add(mapResultSetToProducto(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productos;
    }

    @Override
    public void close() throws IOException {

    }

    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setTipo(TipoProducto.valueOf(rs.getString("tipoProducto")));
        return producto;
    }

    public static ProductoDAO build() {
        return new ProductoDAO();
    }

}

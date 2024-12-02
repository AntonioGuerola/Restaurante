package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.ComandaProducto;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.TipoProducto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaProductoDAO implements DAO<ComandaProducto, Integer> {

    private static final String INSERT = "INSERT INTO comanda_producto (comanda_id, producto_id, cantidad) VALUES (?, ?, ?)";
    private static final String DELETE = "DELETE FROM comanda_producto WHERE comanda_id = ? AND producto_id = ?";
    private static final String FINDALL = "SELECT cp.comanda_id, cp.producto_id, cp.cantidad, p.nombre, p.precio, p.tipo " +
            "FROM comanda_producto cp " +
            "JOIN producto p ON cp.producto_id = p.id " +
            "WHERE cp.comanda_id = ?";

    private Connection con = MySQLConnection.getConnection();

    public ComandaProductoDAO() {
        this.con = con;
    }

    @Override
    public ComandaProducto save(ComandaProducto comandaProducto) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                ps.setInt(1, comandaProducto.getCesta().getId());
                ps.setInt(2, comandaProducto.getProducto().getId());
                ps.setInt(3, comandaProducto.getCantidad());

                ps.executeUpdate();
            }
        }
        return comandaProducto;
    }

    @Override
    public ComandaProducto delete(ComandaProducto comandaProducto) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, comandaProducto.getCesta().getId());
                ps.setInt(2, comandaProducto.getProducto().getId());
                ps.executeUpdate();
            }
        }
        return comandaProducto;
    }

    @Override
    public ComandaProducto findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<ComandaProducto> findAll() {
        return null;
    }

    public List<ComandaProducto> findByComandaId(int comandaId) throws SQLException {
        List<ComandaProducto> comandaProductos = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDALL)) {
                ps.setInt(1, comandaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Comanda comanda = new Comanda();
                        comanda.setId(rs.getInt("comanda_id"));

                        Producto producto = new Producto();
                        producto.setId(rs.getInt("producto_id"));
                        producto.setNombre(rs.getString("nombre"));
                        producto.setPrecio(rs.getDouble("precio"));
                        producto.setTipo(rs.getString("tipo") != null ? TipoProducto.valueOf(rs.getString("tipo")) : null);

                        int cantidad = rs.getInt("cantidad");

                        ComandaProducto comandaProducto = new ComandaProducto(comanda, producto, cantidad);
                        comandaProductos.add(comandaProducto);
                    }
                }
            }
        }
        return comandaProductos;
    }

    @Override
    public void close() throws IOException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
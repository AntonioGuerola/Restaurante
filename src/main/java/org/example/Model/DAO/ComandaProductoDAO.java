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

    private static final String INSERT = "INSERT INTO comandaproducto (idComanda, cantidad, idProducto, nombreProducto) VALUES (?, ?, ?, ?)";
    private static final String EXISTEPRODUCTOENCOMANDA = "SELECT cantidad FROM comandaproducto WHERE idComanda = ? AND idProducto = ? AND nombreProducto = ?";
    private static final String UPDATECANTIDADPRODUCTOS = "UPDATE comandaproducto SET cantidad = ? WHERE idComanda = ? AND idProducto = ? AND nombreProducto = ?";
    private static final String DELETE = "DELETE FROM comandaproducto WHERE idComanda = ? AND idProducto = ? AND nombreProducto = ?";
    private static final String FINDBYIDCOMANDA = "SELECT cp.idComanda, cp.cantidad, cp.idProducto, p.nombre, p.precio, p.tipoProducto " +
            "FROM comandaproducto cp " +
            "JOIN producto p ON cp.idProducto = p.id " +
            "WHERE cp.idComanda = ?";

    private Connection con;

    public ComandaProductoDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public ComandaProducto save(ComandaProducto comandaProducto) throws SQLException {
        if (con != null) {
            try (PreparedStatement psCheck = con.prepareStatement(EXISTEPRODUCTOENCOMANDA)) {
                psCheck.setInt(1, comandaProducto.getComanda().getId());
                psCheck.setInt(2, comandaProducto.getProducto().getId());
                psCheck.setString(3, comandaProducto.getProducto().getNombre());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        // Producto ya existe: sumar cantidad
                        int nuevaCantidad = rs.getInt("cantidad") + comandaProducto.getCantidad();
                        try (PreparedStatement psUpdate = con.prepareStatement(UPDATECANTIDADPRODUCTOS)) {
                            psUpdate.setInt(1, nuevaCantidad);
                            psUpdate.setInt(2, comandaProducto.getComanda().getId());
                            psUpdate.setInt(3, comandaProducto.getProducto().getId());
                            psUpdate.setString(4, comandaProducto.getProducto().getNombre());
                            psUpdate.executeUpdate();
                        }
                    } else {
                        // Producto no existe: insertar nuevo registro
                        try (PreparedStatement psInsert = con.prepareStatement(INSERT)) {
                            psInsert.setInt(1, comandaProducto.getComanda().getId());
                            psInsert.setInt(2, comandaProducto.getCantidad());
                            psInsert.setInt(3, comandaProducto.getProducto().getId());
                            psInsert.setString(4, comandaProducto.getProducto().getNombre());
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
        }
        return comandaProducto;
    }

    public void updateCantidad(ComandaProducto comandaProducto) throws SQLException {
        if (con != null) {
            try (PreparedStatement psUpdate = con.prepareStatement(UPDATECANTIDADPRODUCTOS)) {
                psUpdate.setInt(1, comandaProducto.getCantidad());
                psUpdate.setInt(2, comandaProducto.getComanda().getId());
                psUpdate.setInt(3, comandaProducto.getProducto().getId());
                psUpdate.setString(4, comandaProducto.getProducto().getNombre());
                psUpdate.executeUpdate();
            }
        }
    }

    @Override
    public ComandaProducto delete(ComandaProducto comandaProducto) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, comandaProducto.getComanda().getId());
                ps.setInt(2, comandaProducto.getProducto().getId());
                ps.setString(3, comandaProducto.getProducto().getNombre());
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
            try (PreparedStatement ps = con.prepareStatement(FINDBYIDCOMANDA)) {
                ps.setInt(1, comandaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Mapear los datos de la comanda
                        Comanda comanda = new Comanda();
                        comanda.setId(rs.getInt("idComanda"));

                        // Mapear los datos del producto
                        Producto producto = new Producto();
                        producto.setId(rs.getInt("idProducto"));
                        producto.setNombre(rs.getString("nombre"));
                        producto.setPrecio(rs.getDouble("precio"));
                        producto.setTipo(rs.getString("tipoProducto") != null ? TipoProducto.valueOf(rs.getString("tipoProducto")) : null);

                        // Obtener la cantidad
                        int cantidad = rs.getInt("cantidad");

                        // Crear y agregar el ComandaProducto
                        ComandaProducto comandaProducto = new ComandaProducto(comanda, cantidad, producto, producto);
                        comandaProductos.add(comandaProducto);
                    }
                } catch (SQLException e) {
                    System.err.println("Error al buscar productos de la comanda: " + e.getMessage());
                    throw e;
                }
            }
        }
        return comandaProductos;
    }

    @Override
    public void close() throws IOException {

    }
}

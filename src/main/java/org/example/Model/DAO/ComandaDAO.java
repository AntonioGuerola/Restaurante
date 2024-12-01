package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO implements DAO<Comanda, Integer> {

    private static final String INSERT = "INSERT INTO comanda (horaComanda, idMesa) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM comanda WHERE id = ?";
    private static final String FINDBYID = "SELECT id, horaComanda, idMesa FROM comanda WHERE id = ?";
    private static final String FINDALL = "SELECT id, horaComanda, idMesa FROM comanda";
    private static final String INSERTPRODUCTOCOMANDA = "INSERT INTO comanda_producto (idComanda, idProducto, cantidad) VALUES (?, ?, ?)";

    private Connection con;

    public ComandaDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public Comanda save(Comanda comanda) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, comanda.getHoraComanda());
                ps.setInt(2, comanda.getMesa().getId());

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            comanda.setId(generatedKeys.getInt(1));
                        }
                    }
                    for (Comanda.ProductoCantidad pc : comanda.getProductos()) {
                        try (PreparedStatement psProducto = con.prepareStatement(INSERTPRODUCTOCOMANDA)) {
                            psProducto.setInt(1, comanda.getId());
                            psProducto.setInt(2, pc.getProducto().getId());
                            psProducto.setInt(3, pc.getCantidad());
                            psProducto.executeUpdate();
                        }
                    }
                    return comanda;
                }
            }
        }
        return null;
    }

    @Override
    public Comanda delete(Comanda comanda) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, comanda.getId());
                ps.executeUpdate();
                return comanda;
            }
        }
        return null;
    }

    @Override
    public Comanda findById(Integer id) throws SQLException {
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDBYID)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Comanda comanda = new Comanda();
                        comanda.setId(rs.getInt("id"));
                        comanda.setHoraComanda(rs.getString("horaComanda"));

                        MesaDAO mesaDAO = new MesaDAO();
                        Mesa mesa = mesaDAO.findById(rs.getInt("idMesa"));
                        comanda.setMesa(mesa);

                        List<Comanda.ProductoCantidad> productos = new ArrayList<>();
                        try (PreparedStatement psProducto = con.prepareStatement("SELECT idProducto, cantidad FROM comanda_producto WHERE idComanda = ?")) {
                            psProducto.setInt(1, comanda.getId());
                            try (ResultSet rsProducto = psProducto.executeQuery()) {
                                while (rsProducto.next()) {
                                    ProductoDAO productoDAO = new ProductoDAO();
                                    Producto producto = (Producto) productoDAO.findById(rsProducto.getInt("idProducto"));
                                    int cantidad = rsProducto.getInt("cantidad");
                                    productos.add(new Comanda.ProductoCantidad(producto, cantidad));
                                }
                            }
                        }
                        comanda.getProductos().addAll(productos);
                        return comanda;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Comanda> findAll() throws SQLException {
        List<Comanda> comandas = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(FINDALL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comanda comanda = new Comanda();
                    comanda.setId(rs.getInt("id"));
                    comanda.setHoraComanda(rs.getString("horaComanda"));

                    MesaDAO mesaDAO = new MesaDAO();
                    Mesa mesa = mesaDAO.findById(rs.getInt("idMesa"));
                    comanda.setMesa(mesa);

                    List<Comanda.ProductoCantidad> productos = new ArrayList<>();
                    try (PreparedStatement psProducto = con.prepareStatement("SELECT idProducto, cantidad FROM comanda_producto WHERE idComanda = ?")) {
                        psProducto.setInt(1, comanda.getId());
                        try (ResultSet rsProducto = psProducto.executeQuery()) {
                            while (rsProducto.next()) {
                                ProductoDAO productoDAO = new ProductoDAO();
                                Producto producto = (Producto) productoDAO.findById(rsProducto.getInt("idProducto"));
                                int cantidad = rsProducto.getInt("cantidad");
                                productos.add(new Comanda.ProductoCantidad(producto, cantidad));
                            }
                        }
                    }
                    comanda.getProductos().addAll(productos);
                    comandas.add(comanda);
                }
            }
        }
        return comandas;
    }

    @Override
    public void close() {

    }
}

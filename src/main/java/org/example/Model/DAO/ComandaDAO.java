package org.example.Model.DAO;

import org.example.Model.Connection.MySQLConnection;
import org.example.Model.Entity.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.Model.Entity.EstadoComanda.ABIERTA;

public class ComandaDAO implements DAO<Comanda, Integer> {

    private static final String INSERT = "INSERT INTO comanda (horaComanda, idMesa,  tipoMesa, numMesa, estado) VALUES (?, ?, ?, ?, ?)";
    private static final String ULTIMOIDINSERTADO = "SELECT LAST_INSERT_ID()";
    private static final String UPDATE = "UPDATE comanda SET horaComanda = ?, estado = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM comanda WHERE id = ?";
    private static final String FINDBYID = "SELECT id, horaComanda, idMesa, tipoMesa, numMesa, estado FROM comanda WHERE id = ?";
    private static final String FINDALL = "SELECT id, horaComanda, idMesa, tipoMesa, numMesa, estado FROM comanda";
    private static final String INSERTPRODUCTOCOMANDA = "INSERT INTO comandaproducto (idComanda, cantidad, idProducto, nombreProducto) VALUES (?, ?, ?, ?)";
    private static final String UPDATEPRODUCTOCOMANDA = "UPDATE comandaproducto SET cantidad = ?, idProducto = ?, nombreProducto = ? WHERE idComanda = ?";
    private static final String SELECTPRIMERACOMANDA = "SELECT horaComanda FROM comanda WHERE idMesa = ? ORDER BY horaComanda ASC LIMIT 1";
    private static final String FINDCOMANDAABIERTA = "SELECT * FROM comanda WHERE idMesa = ? AND estado = 'ABIERTA'";
    private Connection con;

    public ComandaDAO() {
        this.con = MySQLConnection.getConnection();
    }

    @Override
    public Comanda save(Comanda comanda) throws SQLException {
        if (comanda.getId() == 0) {
            // Crear una nueva comanda
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                    ps.setTime(1, Time.valueOf(LocalTime.now()));
                    ps.setInt(2, comanda.getMesa().getId());
                    ps.setString(3, comanda.getMesa().getTipo().name());
                    ps.setInt(4, comanda.getMesa().getNumMesa());
                    if (comanda.getEstadoComanda() == null) {
                        comanda.setEstadoComanda(ABIERTA); // Asignar un valor por defecto
                    }
                    ps.setString(5, comanda.getEstadoComanda().name());
                    int affectedRows = ps.executeUpdate();

                    if (affectedRows > 0) {
                        // Recuperar el último ID generado
                        try (PreparedStatement psLastId = con.prepareStatement(ULTIMOIDINSERTADO)) {
                            try (ResultSet rs = psLastId.executeQuery()) {
                                if (rs.next()) {
                                    comanda.setId(rs.getInt(1));
                                }
                            }
                        }

                        // Insertar o actualizar los productos de la comanda
                        try (ComandaProductoDAO cpDAO = new ComandaProductoDAO()) {
                            for (Comanda.ProductoCantidad pc : comanda.getProductos()) {
                                // Usar el DAO para verificar si el producto ya está en la comanda
                                ComandaProducto comandaProducto = new ComandaProducto(
                                        comanda,
                                        pc.getCantidad(),
                                        pc.getProducto(),
                                        pc.getProducto()
                                );
                                cpDAO.save(comandaProducto); // Guarda o actualiza el producto
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        return comanda;
                    }
                }
            }
        } else {
            // Actualizar una comanda existente
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
                    ps.setString(1, comanda.getHoraComanda());
                    ps.setString(2, comanda.getEstadoComanda().name());
                    ps.setInt(3, comanda.getId());
                    int affectedRows = ps.executeUpdate();

                    if (affectedRows > 0) {
                        // Actualizar los productos de la comanda
                        try (ComandaProductoDAO cpDAO = new ComandaProductoDAO()) {
                            for (Comanda.ProductoCantidad pc : comanda.getProductos()) {
                                // Usar el DAO para verificar si el producto ya está en la comanda
                                ComandaProducto comandaProducto = new ComandaProducto(
                                        comanda,
                                        pc.getCantidad(),
                                        pc.getProducto(),
                                        pc.getProducto()
                                );
                                cpDAO.save(comandaProducto); // Guarda o actualiza el producto
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return comanda;
                    }
                }
            }
        }
        return comanda;
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
                        return mapResultSetToComanda(rs);
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
                    comandas.add(mapResultSetToComanda(rs));
                }
            }
        }
        return comandas;
    }

    @Override
    public void close() {}

    public LocalTime obtenerHoraPrimeraComanda(int idMesa) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECTPRIMERACOMANDA)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTime("horaComanda").toLocalTime();
                }
            }
        }
        return null;
    }

    private Comanda mapResultSetToComanda(ResultSet rs) throws SQLException {
        Comanda comanda = new Comanda();
        comanda.setId(rs.getInt("id"));
        comanda.setHoraComanda(rs.getString("horaComanda"));

        // Asignar la mesa asociada
        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesa = mesaDAO.findById(rs.getInt("idMesa"));
        comanda.setMesa(mesa);

        // Asignar el estado
        String estado = rs.getString("estado");
        if (estado == null) {
            comanda.setEstadoComanda(ABIERTA); // Estado por defecto si es null
        } else {
            comanda.setEstadoComanda(EstadoComanda.valueOf(estado));
        }

        // Obtener los productos asociados a la comanda
        List<Comanda.ProductoCantidad> productos = new ArrayList<>();
        try (PreparedStatement psProducto = con.prepareStatement("SELECT cantidad, idProducto, nombreProducto FROM comandaproducto WHERE idComanda = ?")) {
            psProducto.setInt(1, comanda.getId());
            try (ResultSet rsProducto = psProducto.executeQuery()) {
                while (rsProducto.next()) {
                    ProductoDAO productoDAO = new ProductoDAO();
                    int cantidad = rsProducto.getInt("cantidad");
                    Producto producto = productoDAO.findById(rsProducto.getInt("idProducto"));
                    productos.add(new Comanda.ProductoCantidad(producto, cantidad));
                }
            }
        }

        // Inicializar la lista si es null y añadir los productos
        if (comanda.getProductos() == null) {
            comanda.setProductos(new ArrayList<>());
        }
        comanda.getProductos().addAll(productos);

        return comanda;
    }



    public Comanda findComandaAbiertaPorMesa(int idMesa) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(FINDCOMANDAABIERTA)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToComanda(rs);
                }
            }
        }
        return null; // No hay comanda abierta
    }
}

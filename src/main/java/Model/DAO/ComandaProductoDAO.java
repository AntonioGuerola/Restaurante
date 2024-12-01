package Model.DAO;

import Model.Connection.MySQLConnection;
import Model.Entity.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComandaProductoDAO extends ComandaProducto implements DAO {
    private static final String INSERT = "INSERT INTO cestaProducto (IdCesta, IdProducto, cantidad) VALUES (?,?,?)";
    private static final String OBTENERPPRODUCTOSCESTA = "SELECT cp.idCesta, cp.idProducto, cp.cantidad, p.id, p.nombre, p.precio, p.tipoProducto, p.destino, c.id, c.horaComanda, c.idMesa " +
            "FROM CestaProducto cp " +
            "JOIN Producto p ON cp.idProducto = p.id " +
            "JOIN Cesta c ON cp.idCesta = c.id " +
            "WHERE cp.idCesta = ?";

    Connection con = MySQLConnection.getConnection();
    public ComandaProductoDAO() {
        this.con = con;
    }

    public List<ComandaProducto> obtenerProductosDeCesta(int idCesta) throws SQLException {
        List<ComandaProducto> comandaProductos = new ArrayList<>();
        MesaDAO mesaDAO = new MesaDAO();

        try (PreparedStatement statement = con.prepareStatement(OBTENERPPRODUCTOSCESTA)) {
            statement.setInt(1, idCesta);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ComandaProducto comandaProducto = new ComandaProducto();

                    LocalTime horaActualComanda = LocalTime.now();
                    String horaString = horaActualComanda.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    Comanda comanda = new Comanda();
                    comanda.setId(rs.getInt("idCesta"));
                    comanda.setHoraComanda(horaString);

                    int idMesa = rs.getInt("idMesa");
                    Mesa mesa = mesaDAO.findById(idMesa);
                    comanda.setMesa(mesa);
                    comandaProducto.setCesta(comanda);

                    Producto producto = new Producto();
                    producto.setId(rs.getInt("idProducto"));
                    producto.setNombre(rs.getString("nombreProducto"));
                    producto.setPrecio(rs.getDouble("precio"));

                    String tipoProducto = rs.getString("tipo");
                    producto.setTipo(TipoProducto.valueOf(tipoProducto));

                    comandaProducto.setProducto(producto);

                    comandaProducto.setCantidad(rs.getInt("cantidad"));

                    comandaProductos.add(comandaProducto);
                }
            }
        }

        return comandaProductos;
    }

    @Override
    public Object save(Object entity) throws SQLException {
        ComandaProducto tmp = (ComandaProducto) entity;
        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                ps.setInt(1, tmp.getCesta().getId());
                ps.setInt(2, tmp.getProducto().getId());
                ps.setInt(3, tmp.getCantidad());

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
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

public class CestaProductoDAO extends CestaProducto implements DAO {
    private static final String OBTENERPPRODUCTOSCESTA = "SELECT cp.idCesta, cp.idProducto, cp.cantidad, p.id, p.nombre, p.precio, p.tipo, p.destino, c.id, c.horaComanda, c.mesa " +
            "FROM CestaProducto cp " +
            "JOIN Producto p ON cp.idProducto = p.id " +
            "JOIN Cesta c ON cp.idCesta = c.id " +
            "WHERE cp.idCesta = ?";

    private Connection connection;


    public CestaProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<CestaProducto> obtenerProductosDeCesta(int idCesta) throws SQLException {
        List<CestaProducto> cestaProductos = new ArrayList<>();
        MesaDAO mesaDAO = new MesaDAO(MySQLConnection.getConnection());

        try (PreparedStatement statement = connection.prepareStatement(OBTENERPPRODUCTOSCESTA)) {
            statement.setInt(1, idCesta);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    CestaProducto cestaProducto = new CestaProducto();

                    LocalTime horaActualComanda = LocalTime.now();
                    String horaString = horaActualComanda.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    // Crear el objeto Cesta
                    Cesta cesta = new Cesta();
                    cesta.setId(rs.getInt("idCesta"));
                    cesta.setHoraComanda(horaString);

                    int idMesa = rs.getInt("idMesa");
                    Mesa mesa = mesaDAO.findById(idMesa);
                    cesta.setMesa(mesa);
                    cestaProducto.setCesta(cesta);

                    Producto producto = new Producto();
                    producto.setId(rs.getInt("idProducto"));
                    producto.setNombre(rs.getString("nombreProducto"));
                    producto.setPrecio(rs.getDouble("precio"));

                    String tipoProducto = rs.getString("tipo");
                    producto.setTipo(TipoProducto.valueOf(tipoProducto));

                    cestaProducto.setProducto(producto);

                    cestaProducto.setCantidad(rs.getInt("cantidad"));

                    cestaProductos.add(cestaProducto);
                }
            }
        }

        return cestaProductos;
    }

    @Override
    public Object save(Object entity) throws SQLException {
        return null;
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
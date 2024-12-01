package Model.Test.CestaProducto;

import Model.DAO.ComandaProductoDAO;
import Model.Entity.ComandaProducto;

import java.sql.SQLException;
import java.util.List;

public class InsertCestaProducto {
    public static void main(String[] args) throws SQLException {
        ComandaProductoDAO cestaProductoDAO = new ComandaProductoDAO();

        List<ComandaProducto> productos = cestaProductoDAO.obtenerProductosDeCesta(1);

        for (ComandaProducto producto : productos) {
            System.out.println(producto);
        }

        if (!productos.isEmpty()) {
            ComandaProducto cestaAInsertar = productos.get(0); // Primer producto
            System.out.println(cestaProductoDAO.save(cestaAInsertar));
        } else {
            System.out.println("No se encontraron productos en la cesta.");
        }
    }
}
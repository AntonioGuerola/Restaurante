package Model.Test.Producto;

import Model.DAO.ProductoDAO;
import Model.Entity.Producto;

import java.sql.SQLException;

import static Model.Entity.TipoProducto.*;

public class deleteProducto {
    public static void main(String[] args) throws SQLException {
        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = (Producto) productoDAO.findById(6);
        System.out.println(productoDAO.delete(producto));
    }
}
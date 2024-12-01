package Model.Test.Mesa;

import Model.DAO.ProductoDAO;
import Model.Entity.Producto;

import java.sql.SQLException;

public class deleteMesa {
    public static void main(String[] args) throws SQLException {
        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = (Producto) productoDAO.findById(6);
        System.out.println(productoDAO.delete(producto));
    }
}
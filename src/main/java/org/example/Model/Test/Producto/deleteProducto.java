package org.example.Model.Test.Producto;

import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;

import java.sql.SQLException;

public class deleteProducto {
    public static void main(String[] args) throws SQLException {
        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = productoDAO.findById(10);
        System.out.println(productoDAO.delete(producto));
    }
}
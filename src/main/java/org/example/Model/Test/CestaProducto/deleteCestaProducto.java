package org.example.Model.Test.CestaProducto;

import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;

import java.sql.SQLException;

public class deleteCestaProducto {
    public static void main(String[] args) throws SQLException {
        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = (Producto) productoDAO.findById(6);
        System.out.println(productoDAO.delete(producto));
    }
}
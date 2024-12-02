package org.example.Model.Test.Producto;

import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.TipoProducto;

import java.sql.SQLException;

public class updateProducto {
    public static void main(String[] args) throws SQLException {
        ProductoDAO dao = ProductoDAO.build();
        Producto producto = dao.findById(13);

        producto.setNombre("Medio de Tertulia");
        producto.setPrecio(1.8);
        producto.setTipo(TipoProducto.VERDURA); // Enum válido

        try {
            dao.save(producto);
            System.out.println("Producto actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

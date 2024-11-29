package Model.Test.Producto;

import Model.DAO.ProductoDAO;
import Model.Entity.Producto;

import java.sql.SQLException;

import static Model.Entity.TipoProducto.*;

public class InsertProducto {
    public static void main(String[] args) throws SQLException {
        /*Producto producto1 = new Producto("Solomillo de cerdo", 11.5, CARNE);
        Producto producto2 = new Producto("Revuelto de espárragos", 9, VERDURA);
        Producto producto3 = new Producto("Coca Cola 350ml", 2.2, REFRESCO);
        Producto producto4 = new Producto("Alhambra 1925", 2.8, CERVEZA);
        Producto producto5 = new Producto("Medio de fino", 1.5, VINO);*/
        Producto producto6 = new Producto("Salmón al eneldo", 13, PESCADO);
        /*Producto producto7 = new Producto("Brownie de chocolate", 4.5, POSTRE);
         */
        ProductoDAO productoDAO = new ProductoDAO();
        /*System.out.println(productoDAO.save(producto1));
        System.out.println(productoDAO.save(producto2));
        System.out.println(productoDAO.save(producto3));
        System.out.println(productoDAO.save(producto4));
        System.out.println(productoDAO.save(producto5));
         */
        System.out.println(productoDAO.save(producto6));
        /*System.out.println(productoDAO.save(producto7));
         */
    }
}
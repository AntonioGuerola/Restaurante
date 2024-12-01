package Model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Comanda {
    private int id;
    private String horaComanda;
    private Mesa mesa;
    private List<ProductoCantidad> productos;

    public Comanda(String horaComanda, Mesa mesa) {
        this.horaComanda = horaComanda;
        this.mesa = mesa;
        this.productos = new ArrayList<>();
    }

    public Comanda(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraComanda() {
        return horaComanda;
    }

    public void setHoraComanda(String horaComanda) {
        this.horaComanda = horaComanda;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comanda comanda = (Comanda) object;
        return id == comanda.id && Objects.equals(horaComanda, comanda.horaComanda) && Objects.equals(mesa, comanda.mesa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaComanda, mesa);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", horaComanda=" + horaComanda +
                ", mesa=" + mesa +
                '}';
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productos.add(new ProductoCantidad(producto, cantidad));
    }

    public double calcularTotal() {
        double total = 0;
        for (ProductoCantidad pc : productos) {
            total += pc.getProducto().getPrecio() * pc.getCantidad();
        }
        return total;
    }

    private static class ProductoCantidad {
        private Producto producto;
        private int cantidad;

        public ProductoCantidad(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public ProductoCantidad(){

        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }
    }
}

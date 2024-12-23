package org.example.Model.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comanda implements Serializable {
    private int id;
    private String horaComanda;
    private Mesa mesa;
    private List<ProductoCantidad> productos;
    private EstadoComanda estadoComanda;

    public Comanda(String horaComanda, Mesa mesa) {
        this.horaComanda = horaComanda;
        this.mesa = mesa;
        this.productos = new ArrayList<>();
        this.estadoComanda = EstadoComanda.ABIERTA;
    }

    public Comanda(){
        this.productos = new ArrayList<>();
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getHoraComanda() {
        return horaComanda;
    }

    public void setHoraComanda(String horaComanda) {
        this.horaComanda = horaComanda;
    }

    @XmlElement
    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @XmlElement
    public List<ProductoCantidad> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCantidad> productos) {
        this.productos = productos;
    }

    @XmlElement
    public EstadoComanda getEstadoComanda() {
        return estadoComanda;
    }

    public void setEstadoComanda(EstadoComanda estadoComanda) {
        this.estadoComanda = estadoComanda;
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
                ", horaComanda='" + horaComanda + '\'' +
                ", mesa=" + mesa +
                ", productos=" + productos +
                ", estadoComanda=" + estadoComanda +
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

    public static class ProductoCantidad {
        private Producto producto;
        private int cantidad;

        public ProductoCantidad(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public ProductoCantidad(){

        }

        @XmlElement
        public Producto getProducto() {
            return producto;
        }

        @XmlElement
        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int i) {
        }
    }
}

package org.example.Model.Entity;

import java.util.Objects;

public class ComandaProducto {
    private Comanda comanda;
    private Producto producto;
    private int cantidad;

    public ComandaProducto(Comanda comanda, Producto producto, int cantidad) {
        this.comanda = comanda;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ComandaProducto() {
        cantidad = 0;
    }

    public Comanda getCesta() {
        return comanda;
    }

    public void setCesta(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ComandaProducto that = (ComandaProducto) object;
        return Objects.equals(comanda, that.comanda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comanda);
    }

    @Override
    public String toString() {
        return "ComandaProducto{" +
                "comanda=" + comanda +
                ", producto=" + producto +
                ", cantidad=" + cantidad +
                '}';
    }
}

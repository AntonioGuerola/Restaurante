package org.example.Model.Entity;

import java.util.Objects;

public class ComandaProducto {
    private Comanda comanda;
    private Producto producto;
    private int cantidad;
    private Producto nombreProducto;

    public ComandaProducto(Comanda comanda, int cantidad, Producto producto, Producto nombreProducto) {
        this.comanda = comanda;
        this.cantidad = cantidad;
        this.producto = producto;
        this.producto = nombreProducto;
    }

    public ComandaProducto() {
        cantidad = 0;
    }

    public Comanda getcomanda() {
        return comanda;
    }

    public void setcomanda(Comanda comanda) {
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

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producto getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(Producto nombreProducto) {
        this.nombreProducto = nombreProducto;
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
                ", nombreProducto=" + nombreProducto +
                '}';
    }
}

package Model.Entity;

import java.util.Objects;

public class CestaProducto {
    private Cesta cesta;
    private Producto producto;
    private int cantidad;

    public CestaProducto(Cesta cesta, Producto producto, int cantidad) {
        this.cesta = cesta;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public CestaProducto() {
        cantidad = 0;
    }

    public Cesta getCesta() {
        return cesta;
    }

    public void setCesta(Cesta cesta) {
        this.cesta = cesta;
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
        CestaProducto that = (CestaProducto) object;
        return Objects.equals(cesta, that.cesta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cesta);
    }

    @Override
    public String toString() {
        return "CestaProducto{" +
                "cesta=" + cesta +
                ", producto=" + producto +
                ", cantidad=" + cantidad +
                '}';
    }
}

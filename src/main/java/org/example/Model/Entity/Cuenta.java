package org.example.Model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cuenta {
    private int id;
    private Mesa mesa;
    private double sumaTotal;
    private String horaCobro;
    private List<Comanda> comandas;

    public Cuenta(Mesa mesa, String horaCobro) {
        this.mesa = mesa;
        this.horaCobro = horaCobro;
        this.comandas = new ArrayList<>();
    }

    public Cuenta(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public double getSumaTotal() {
        return sumaTotal;
    }

    public void setSumaTotal(double sumaTotal) {
        this.sumaTotal = sumaTotal; // Asignar directamente
    }

    public void setSumaTotal() {
        double total = 0.0;
        if (comandas != null && !comandas.isEmpty()) {
            for (Comanda comanda : comandas) {
                if (comanda.getProductos() != null) {
                    for (Comanda.ProductoCantidad productoCantidad : comanda.getProductos()) {
                        Producto producto = productoCantidad.getProducto();
                        if (producto != null) {
                            total += producto.getPrecio() * productoCantidad.getCantidad();
                        }
                    }
                }
            }
        }
        this.sumaTotal = total;
    }

    public String getHoraCobro() {
        return horaCobro;
    }

    public void setHoraCobro(String horaCobro) {
        this.horaCobro = horaCobro;
    }

    public List<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Cuenta cuenta = (Cuenta) object;
        return id == cuenta.id && Objects.equals(mesa, cuenta.mesa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mesa);
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", mesa=" + mesa +
                ", sumaTotal=" + sumaTotal +
                ", horaCobro='" + horaCobro + '\'' +
                ", comandas=" + comandas +
                '}';
    }

    public void addComanda(Comanda comanda) {
        if (this.comandas == null) {
            this.comandas = new ArrayList<>();
        }
        this.comandas.add(comanda);

        // Sumar el total de la nueva comanda a la sumaTotal existente
        if (comanda.getProductos() != null) {
            double totalNuevaComanda = 0.0;
            for (Comanda.ProductoCantidad productoCantidad : comanda.getProductos()) {
                Producto producto = productoCantidad.getProducto();
                if (producto != null) {
                    totalNuevaComanda += producto.getPrecio() * productoCantidad.getCantidad();
                }
            }
            this.sumaTotal += totalNuevaComanda; // Incrementar el total
        }
    }

}

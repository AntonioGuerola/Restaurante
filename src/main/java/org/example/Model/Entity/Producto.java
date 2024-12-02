package org.example.Model.Entity;

import java.util.Objects;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private TipoProducto tipo;
    private String destino;

    public Producto(String nombre, double precio, TipoProducto tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        setDestino();
    }

    public Producto(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public String getDestino() {
        return destino;
    }

    private void setDestino() {
        if (tipo == TipoProducto.REFRESCO || tipo == TipoProducto.CERVEZA || tipo == TipoProducto.VINO) {
            destino = "Barra";
        } else {
            destino = "Cocina";
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Producto producto = (Producto) object;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", tipo=" + tipo +
                ", destino?" + destino +
                '}';
    }
}
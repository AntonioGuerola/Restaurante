package org.example.Model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mesa {
    private int id;
    private TipoMesa tipo;
    private int numMesa;
    private String fecha;
    private String horaMesa;
    private int tiempo;
    private Cuenta cuenta;
    private List<Comanda> comandas;

    public Mesa(TipoMesa tipo, int numMesa, String fecha, String horaMesa) {
        this.tipo = tipo;
        this.numMesa = numMesa;
        this.fecha = fecha;
        this.horaMesa = horaMesa;
        this.tiempo = tiempo;
        this.comandas = new ArrayList<>();
    }

    public Mesa() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoMesa getTipo() {
        return tipo;
    }

    public void setTipo(TipoMesa tipo) {
        this.tipo = tipo;
    }

    public int getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraMesa() {
        return horaMesa;
    }

    public void setHoraMesa(String horaMesa) {
        this.horaMesa = horaMesa;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
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
        Mesa mesa = (Mesa) object;
        return id == mesa.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id + '\'' +
                ", tipo='" + tipo +
                ", numMesa=" + numMesa +
                ", fecha=" + fecha +
                ", horaMesa=" + horaMesa +
                ", tiempo=" + tiempo +
                ", cuenta=" + cuenta +
                '}';
    }

    public void agregarComanda(Comanda comanda) {
        comandas.add(comanda);
    }
}

package Model.Entity;

import java.util.List;

public class Mesa {
    private int id;
    private TipoMesa tipo;
    private int numMesa;
    private String fecha;
    private String horaMesa;
    private int tiempo;
    private double cuenta;

    public Mesa(TipoMesa tipo, int numMesa, String fecha, String horaMesa, int tiempo, double cuenta) {
        this.tipo = tipo;
        this.numMesa = numMesa;
        this.fecha = fecha;
        this.horaMesa = horaMesa;
        this.tiempo = tiempo;
        this.cuenta = cuenta;
    }

    public Mesa(){

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

    public void setTipo(TipoMesa tipo) {this.tipo = tipo; }

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

    public double getCuenta() {
        return cuenta;
    }

    public void setCuenta(double cuenta) {
        this.cuenta = cuenta;
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
}

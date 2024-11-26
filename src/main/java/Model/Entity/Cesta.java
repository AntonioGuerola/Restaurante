package Model.Entity;

import java.time.LocalTime;
import java.util.Objects;

public class Cesta {
    private int id;
    private LocalTime horaComanda;
    private Mesa mesa;

    public Cesta(int id, LocalTime horaComanda, Mesa mesa) {
        this.id = id;
        this.horaComanda = horaComanda;
        this.mesa = mesa;
    }

    public Cesta(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getHoraComanda() {
        return horaComanda;
    }

    public void setHoraComanda(LocalTime horaComanda) {
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
        Cesta cesta = (Cesta) object;
        return id == cesta.id && Objects.equals(horaComanda, cesta.horaComanda) && Objects.equals(mesa, cesta.mesa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaComanda, mesa);
    }

    @Override
    public String toString() {
        return "Cesta{" +
                "id=" + id +
                ", horaComanda=" + horaComanda +
                ", mesa=" + mesa +
                '}';
    }
}

package org.example.Model.Entity;

import org.example.Model.DAO.CuentaDAO;

import java.io.Serializable;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Mesa implements Serializable {
    private int id;
    private TipoMesa tipo;
    private int numMesa;
    private String fecha;
    private String horaMesa;
    private int tiempo;
    private Cuenta cuenta;

    public Mesa(TipoMesa tipo, int numMesa, String fecha, String horaMesa) {
        this.tipo = tipo;
        this.numMesa = numMesa;
        this.fecha = fecha;
        this.horaMesa = horaMesa;
        this.tiempo = tiempo;
    }

    public Mesa() {

    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public TipoMesa getTipo() {
        return tipo;
    }

    public void setTipo(TipoMesa tipo) {
        this.tipo = tipo;
    }

    @XmlElement
    public int getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;
    }

    @XmlElement
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @XmlElement
    public String getHoraMesa() {
        return horaMesa;
    }

    public void setHoraMesa(String horaMesa) {
        this.horaMesa = horaMesa;
    }

    @XmlElement
    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    @XmlElement
    public Cuenta getCuenta() {
        if (this.cuenta == null) {
            try {
                CuentaDAO cuentaDAO = new CuentaDAO();
                this.cuenta = cuentaDAO.findByMesaId(this.id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
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
}

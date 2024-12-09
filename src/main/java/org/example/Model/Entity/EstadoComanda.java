package org.example.Model.Entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum EstadoComanda implements Serializable {
    ABIERTA("abierta"),
    CERRADA("cerrada");

    private final String dbField;

    EstadoComanda(String url) {
        this.dbField = url;
    }

    @XmlElement
    public String getUrl() {
        return dbField;
    }
}


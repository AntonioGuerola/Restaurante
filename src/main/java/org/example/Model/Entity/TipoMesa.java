package org.example.Model.Entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TipoMesa implements Serializable {
    TERRAZA("terraza"),
    CAFETERIA("cafeteria");

    private final String dbField;

    TipoMesa(String url) {
        this.dbField = url;
    }

    @XmlElement
    public String getUrl() {
        return dbField;
    }
}


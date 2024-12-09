package org.example.Model.Entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TipoProducto implements Serializable {
    REFRESCO("refresco"),
    VINO("vino"),
    CERVEZA("cerveza"),
    CARNE("carne"),
    PESCADO("pescado"),
    VERDURA("verdura"),
    POSTRE("postre");

    private final String dbField;

    TipoProducto(String url) {
        this.dbField = url;
    }

    @XmlElement
    public String getUrl() {
        return dbField;
    }
}

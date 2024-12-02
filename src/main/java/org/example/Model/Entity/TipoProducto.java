package org.example.Model.Entity;

public enum TipoProducto {
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

    public String getUrl() {
        return dbField;
    }
}

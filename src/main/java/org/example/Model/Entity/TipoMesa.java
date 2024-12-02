package org.example.Model.Entity;

public enum TipoMesa {
    TERRAZA("terraza"),
    CAFETERIA("cafeteria");

    private final String dbField;

    TipoMesa(String url) {
        this.dbField = url;
    }

    public String getUrl() {
        return dbField;
    }
}


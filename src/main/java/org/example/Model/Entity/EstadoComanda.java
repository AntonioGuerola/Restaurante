package org.example.Model.Entity;

public enum EstadoComanda {
    ABIERTA("abierta"),
    CERRADA("cerrada");

    private final String dbField;

    EstadoComanda(String url) {
        this.dbField = url;
    }

    public String getUrl() {
        return dbField;
    }
}


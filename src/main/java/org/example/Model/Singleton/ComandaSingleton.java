package org.example.Model.Singleton;

import org.example.Model.Entity.Comanda;

public class ComandaSingleton {
    private static ComandaSingleton _instance;
    private Comanda currentComanda;

    private ComandaSingleton(Comanda comanda) {
        currentComanda = comanda;
    }

    public static ComandaSingleton getInstance() {
        return _instance;
    }

    public static ComandaSingleton getInstance(Comanda comandaToUse) {
        if (comandaToUse != null) {
            _instance = new ComandaSingleton(comandaToUse);
        }
        return _instance;
    }

    public static void closeSession() {
        _instance = null;
    }

    public Comanda getCurrentComanda() {
        return currentComanda;
    }
}

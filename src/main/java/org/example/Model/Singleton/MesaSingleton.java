package org.example.Model.Singleton;

import org.example.Model.Entity.Mesa;

public class MesaSingleton {
    private static MesaSingleton _instance;
    private Mesa currentMesa;

    private MesaSingleton(Mesa mesa) {
        currentMesa = mesa;
    }

    public static MesaSingleton getInstance() {
        return _instance;
    }

    public static MesaSingleton getInstance(Mesa mesaToUse) {
        if (mesaToUse != null) {
            _instance = new MesaSingleton(mesaToUse);
        }
        return _instance;
    }

    public static void closeSession() {
        _instance = null;
    }

    public Mesa getCurrentMesa() {
        return currentMesa;
    }
}

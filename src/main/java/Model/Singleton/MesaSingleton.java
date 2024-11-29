package Model.Singleton;


import Model.Entity.Mesa;

public class MesaSingleton {
    private static MesaSingleton _instance;
    private Mesa currentMesa;

    private MesaSingleton(Mesa mesa) {
        currentMesa = mesa;
    }

    public static MesaSingleton getInstance() {
        return _instance;
    }

    public static void getInstance(Mesa mesaToUse) {
        if (mesaToUse != null) {
            _instance = new MesaSingleton(mesaToUse);
        }
    }

    public static void closeSession() {
        _instance = null;
    }

    public Mesa getCurrentMesa() {
        return currentMesa;
    }
}

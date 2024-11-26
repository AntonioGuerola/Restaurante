package Model.Entity;

import java.util.HashMap;

public class CestaProducto {
    private Cesta cesta;
    private HashMap<Integer, Producto> producto;
    private int cantidad;
    private String destino;

    public CestaProducto(Cesta cesta, HashMap<Integer, Producto> producto, int cantidad, String destino) {
        this.cesta = cesta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.destino = destino;
    }

    public CestaProducto() {
        cantidad = 0;
        this.producto = new HashMap<>();
    }
}

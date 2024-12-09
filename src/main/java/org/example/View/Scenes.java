package org.example.View;

/**
 * Enumeration representing the scenes (views) in the application.
 */
public enum Scenes {
    ROOT("View/layout.fxml"),
    START("View/start.fxml"),
    INICIO("View/inicio.fxml"),
    AJUSTES("View/ajustes.fxml"),
    INSERTPRODUCTO("View/insertProducto.fxml"),
    DELETEPRODUCTO("View/deleteProducto.fxml"),
    UPDATEPRODUCTO("View/updateProducto.fxml"),
    MESASTERRAZA("View/mesasTerraza.fxml"),
    MESASCAFETERIA("View/mesasCafeteria.fxml"),
    CATEGORIAPRODUCTOS("View/categoriasProductos.fxml"),
    REFRESCOS("View/refrescos.fxml"),
    CUANTASTERRAZASCAFETERIAS  ("View/CuantasTerrazasCafeterias.fxml"),
    CERVEZA("View/cerveza.fxml"),
    VINO("View/vino.fxml"),
    CARNE("View/carne.fxml"),
    PESCADO("View/pescado.fxml"),
    VERDURA("View/verdura.fxml"),
    POSTRE("View/postre.fxml"),
    CONFIRMARCOMANDA("View/confirmarComanda.fxml"),
    MODIFICARCOMANDA("View/modificarComanda.fxml"),
    SHOWCUENTA("View/showCuenta.fxml");

    private String url;

    /**
     * Constructor for Scenes enumeration.
     * @param url The URL of the corresponding FXML file.
     */
    Scenes(String url) {
        this.url = url;
    }

    /**
     * Gets the URL of the scene.
     * @return The URL of the FXML file representing the scene.
     */
    public String getURL() {
        return url;
    }
}
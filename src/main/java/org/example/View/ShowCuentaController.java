package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import org.example.App;
import org.example.Model.DAO.ComandaProductoDAO;
import org.example.Model.DAO.CuentaDAO;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.ComandaProducto;
import org.example.Model.Entity.Cuenta;
import org.example.Model.Entity.Mesa;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ShowCuentaController extends Controller implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private Label numMesaLabel, tipoMesaLabel, tiempoLabel, sumaTotalLabel;

    private Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
    private Cuenta cuentaActual = mesaSeleccionada.getCuenta();
    private final CuentaDAO cuentaDAO = new CuentaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            cuentaActual = cuentaDAO.findByMesaId(mesaSeleccionada.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cuentaActual != null) {
            numMesaLabel.setText("Número de Mesa: " + mesaSeleccionada.getNumMesa());
            tipoMesaLabel.setText("Tipo de Mesa: " + mesaSeleccionada.getTipo());
//            tiempoLabel.setText("Tiempo: " + calcularTiempoCuenta());
            sumaTotalLabel.setText("Total: " + cuentaActual.getSumaTotal() + "€");

            generarProductosCuenta();
        }
    }

    private void generarProductosCuenta() {
        tilePane.getChildren().clear();

        // Crear un mapa para agrupar productos por nombre y sumar sus cantidades
        Map<String, Integer> productosAgrupados = new HashMap<>();

        // Obtener los productos de cada comanda
        for (Comanda comanda : cuentaActual.getComandas()) {
            try {
                ComandaProductoDAO comandaProductoDAO = new ComandaProductoDAO();
                // Cargar los productos de la comanda desde la base de datos
                List<ComandaProducto> productosComanda = comandaProductoDAO.findByComandaId(comanda.getId());

                // Convertir ComandaProducto a ProductoCantidad
                List<Comanda.ProductoCantidad> productosCantidad = new ArrayList<>();
                for (ComandaProducto comandaProducto : productosComanda) {
                    productosCantidad.add(new Comanda.ProductoCantidad(
                            comandaProducto.getProducto(),
                            comandaProducto.getCantidad()
                    ));
                }

                // Asignar los productos convertidos a la comanda
                comanda.setProductos(productosCantidad);

                // Agrupar los productos por nombre y sumar las cantidades
                for (Comanda.ProductoCantidad productoCantidad : comanda.getProductos()) {
                    String nombreProducto = productoCantidad.getProducto().getNombre();
                    int cantidad = productoCantidad.getCantidad();

                    productosAgrupados.put(nombreProducto, productosAgrupados.getOrDefault(nombreProducto, 0) + cantidad);
                }

            } catch (SQLException e) {
                System.err.println("Error al cargar los productos de la comanda: " + e.getMessage());
            }
        }

        // Ahora que tienes los productos agrupados, puedes mostrarlos en la UI
        for (Map.Entry<String, Integer> entry : productosAgrupados.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();

            HBox hbox = new HBox(10);
            hbox.setPrefWidth(440);
            hbox.setStyle("-fx-border-radius: 5; -fx-padding: 5;");

            // Etiqueta de cantidad
            Label cantidadLabel = new Label(String.valueOf(cantidad));
            cantidadLabel.setPrefWidth(50);
            cantidadLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT;");

            // Etiqueta de nombre del producto
            Label nombreProductoLabel = new Label(nombreProducto);
            nombreProductoLabel.setPrefWidth(200);
            nombreProductoLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-RIGHT;");

            hbox.getChildren().addAll(cantidadLabel, nombreProductoLabel);
            tilePane.getChildren().add(hbox);
        }
    }


    @FXML
    private void cobrarCuenta() throws IOException {
        System.out.println(cuentaActual.getComandas());
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }
}

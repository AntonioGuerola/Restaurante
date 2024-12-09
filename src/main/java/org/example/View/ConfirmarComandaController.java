package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.CuentaDAO;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.*;
import org.example.Model.Singleton.ComandaSingleton;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static org.example.Model.Entity.EstadoComanda.CERRADA;
import static org.example.Model.Utils.JavaFXUtils.getCurrentFormattedTime;

public class ConfirmarComandaController extends Controller implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private Label tipoMesaLabel, numMesaLabel, horaComandaLabel;

    private Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
    private Comanda comanda = ComandaSingleton.getInstance().getCurrentComanda();
    private final ComandaDAO comandaDAO = new ComandaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Actualizamos las etiquetas con los valores de la comanda y mesa
        tipoMesaLabel.setText("Tipo de Mesa: " + mesaSeleccionada.getTipo());
        numMesaLabel.setText("Número de Mesa: " + mesaSeleccionada.getNumMesa());
        horaComandaLabel.setText("Hora de la Comanda: " + comanda.getHoraComanda());

        // Generamos las filas de productos
        generarProductosComanda();
    }

    private void generarProductosComanda() {
        tilePane.getChildren().clear(); // Limpiar el TilePane

        // Obtener los productos de la comanda
        List<Comanda.ProductoCantidad> productosComanda = comanda.getProductos();

        // Crear un mapa para agrupar los productos por nombre
        Map<String, Integer> productosAgrupados = new HashMap<>();

        // Agrupar los productos y sumar las cantidades
        for (Comanda.ProductoCantidad comandaProducto : productosComanda) {
            String nombreProducto = comandaProducto.getProducto().getNombre();
            int cantidad = comandaProducto.getCantidad();

            // Sumar la cantidad al producto existente en el mapa
            productosAgrupados.put(nombreProducto, productosAgrupados.getOrDefault(nombreProducto, 0) + cantidad);
        }

        // Iterar por los productos agrupados y crear las filas
        for (Map.Entry<String, Integer> entry : productosAgrupados.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();

            HBox hbox = new HBox(10); // Crear un contenedor horizontal
            hbox.setPrefWidth(440); // Asegurarse de que las filas tengan un tamaño adecuado
            hbox.setStyle("-fx-border-radius: 5; -fx-padding: 5;"); // Estilo opcional

            // Etiqueta de cantidad (alineada a la izquierda)
            Label cantidadLabel = new Label(String.valueOf(cantidad));
            cantidadLabel.setPrefWidth(50);
            cantidadLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT;");

            // Etiqueta de nombre del producto (alineada a la derecha)
            Label nombreProductoLabel = new Label(nombreProducto);
            nombreProductoLabel.setPrefWidth(200);
            nombreProductoLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-RIGHT;");

            // Añadir los elementos al HBox
            hbox.getChildren().addAll(cantidadLabel, nombreProductoLabel);
            tilePane.getChildren().add(hbox); // Añadir la fila al TilePane
        }
    }


    @Override
    public void onOpen(Object input) throws IOException {
        comanda = ComandaSingleton.getInstance().getCurrentComanda();
    }

    @Override
    public void onClose(Object output) {
        // Manejar el cierre si es necesario
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }

    @FXML
    private void goToModificar() throws IOException {
        App.currentController.changeScene(Scenes.MODIFICARCOMANDA, null);
    }

    @FXML
    private void confirmarComanda() throws IOException, SQLException {
        // Obtener la comanda del Singleton
        comanda = ComandaSingleton.getInstance().getCurrentComanda();

        // Actualizar la hora de la mesa y la comanda
        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesa = mesaDAO.findById(comanda.getMesa().getId());

        String horaActual = getCurrentFormattedTime();

        if (mesa.getHoraMesa() == null || mesa.getHoraMesa().isEmpty()) {
            mesaDAO.actualizarHoraMesa(mesa.getId(), horaActual); // Solo actualizar si está a null
        }

        comandaDAO.actualizarHoraComanda(comanda.getId(), horaActual);
        comandaDAO.updateEstado(comanda.getId(), CERRADA);

        // Crear una nueva cuenta o actualizar la existente
        CuentaDAO cuentaDAO = new CuentaDAO();
        Cuenta nuevaCuenta = cuentaDAO.findByMesaId(mesa.getId()); // Buscar una cuenta existente para la mesa

        if (nuevaCuenta == null) {
            nuevaCuenta = new Cuenta(); // Crear nueva cuenta si no existe
            nuevaCuenta.setMesa(mesa); // Asociar la mesa a la cuenta
        }

        // Añadir la comanda cerrada a la cuenta
        nuevaCuenta.addComanda(comanda);

        cuentaDAO.save(nuevaCuenta);

        // Limpiar la sesión actual de comanda
        ComandaSingleton.closeSession();

        // Redirigir a la escena correspondiente según el tipo de mesa
        if (comanda.getMesa().getTipo() == TipoMesa.TERRAZA) {
            App.currentController.changeScene(Scenes.MESASTERRAZA, null);
        } else if (comanda.getMesa().getTipo() == TipoMesa.CAFETERIA) {
            App.currentController.changeScene(Scenes.MESASCAFETERIA, null);
        }
    }
}

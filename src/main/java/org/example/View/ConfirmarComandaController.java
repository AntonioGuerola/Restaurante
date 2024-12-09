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
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.ComandaProducto;
import org.example.Model.Singleton.ComandaSingleton;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

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

        // Iterar por los productos y crear las filas
        for (Comanda.ProductoCantidad comandaProducto : productosComanda) {
            HBox hbox = new HBox(10); // Crear un contenedor horizontal
            hbox.setPrefWidth(440); // Asegurarse de que las filas tengan un tamaño adecuado
            hbox.setStyle("-fx-border-radius: 5; -fx-padding: 5;"); // Estilo opcional

            // Etiqueta de cantidad (alineada a la izquierda)
            Label cantidadLabel = new Label(String.valueOf(comandaProducto.getCantidad()));
            cantidadLabel.setPrefWidth(50);
            cantidadLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT;");

            // Etiqueta de nombre del producto (alineada a la derecha)
            Label nombreProductoLabel = new Label(comandaProducto.getProducto().getNombre());
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
}

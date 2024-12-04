package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.TipoProducto;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InsertProductoController extends Controller implements Initializable {
    @FXML
    private TextField nombreText;

    @FXML
    private TextField precioValue;

    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    @Override
    public void onOpen(Object input) throws IOException {
        choiceBox.getItems().add("Refresco");
        choiceBox.getItems().add("Cerveza");
        choiceBox.getItems().add("Vino");
        choiceBox.getItems().add("Carne");
        choiceBox.getItems().add("Pescado");
        choiceBox.getItems().add("Verdura");
        choiceBox.getItems().add("Postre");
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saveProducto() throws SQLException, IOException {
        double priceValue;
        try {
            priceValue = Double.parseDouble(precioValue.getText());
        } catch (NumberFormatException e) {
            JavaFXUtils.showErrorAlert("ERROR", "El precio debe tener un precio válido.");
            return;
        }
        TipoProducto tipoSeleccionado;
        switch (choiceBox.getValue()) {
            case "Refresco":
                tipoSeleccionado = TipoProducto.REFRESCO;
                break;
            case "Cerveza":
                tipoSeleccionado = TipoProducto.CERVEZA;
                break;
            case "Vino":
                tipoSeleccionado = TipoProducto.VINO;
                break;
            case "Carne":
                tipoSeleccionado = TipoProducto.CARNE;
                break;
            case "Pescado":
                tipoSeleccionado = TipoProducto.PESCADO;
                break;
            case "Verdura":
                tipoSeleccionado = TipoProducto.VERDURA;
                break;
            case "Postre":
                tipoSeleccionado = TipoProducto.POSTRE;
                break;
            default:
                JavaFXUtils.showErrorAlert("ERROR", "Categoría inválida.");
                return;
        }
        Producto productoToAdd = new Producto(nombreText.getText(), priceValue, tipoSeleccionado);
        ProductoDAO.build().save(productoToAdd);
        App.currentController.changeScene(Scenes.AJUSTES, null);
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.AJUSTES, null);
    }
}

package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.TipoProducto;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjustesController extends Controller implements Initializable {
    @FXML
    private TextField nombreText;

    @FXML
    private TextField precioValue;

    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    ;

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

    @FXML
    private void goInsertarProducto() throws IOException {
        App.currentController.changeScene(Scenes.INSERTPRODUCTO, null);
    }

    @FXML
    private void goDeleteProducto() throws IOException {
        App.currentController.changeScene(Scenes.DELETEPRODUCTO, null);
    }

    @FXML
    private void goUpdateProducto() throws IOException {
        App.currentController.changeScene(Scenes.UPDATEPRODUCTO, null);
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.START, null);
    }
}
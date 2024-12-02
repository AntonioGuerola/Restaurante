package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.App;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController extends Controller implements Initializable {
    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void toInicio() throws IOException {
        try (MesaDAO mesaDAO = new MesaDAO()) {
            if (mesaDAO.findAll().isEmpty()) {
                JavaFXUtils.showErrorAlert("ERROR", "Debes de generar las mesas primero.");
            } else {
                App.currentController.changeScene(Scenes.INICIO, null);
            }
        }
    }

    @FXML
    private void toCuantasTerrazasCafeterias() throws IOException {
        try (MesaDAO mesaDAO = new MesaDAO()) {
            if (!mesaDAO.findAll().isEmpty()) {
                JavaFXUtils.showErrorAlert("ERROR", "Ya has generado las mesas");
            } else {
                App.currentController.changeScene(Scenes.CUANTASTERRAZASCAFETERIAS, null);
            }
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.START, null);
    }
}
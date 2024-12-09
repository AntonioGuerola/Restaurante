package org.example.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.App;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Singleton.ComandaSingleton;
import org.example.Model.Singleton.MesaSingleton;
import org.example.Model.Utils.JavaFXUtils;

public class StartController extends Controller implements Initializable {
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
        MesaDAO mesaDAO = new MesaDAO();
            if (mesaDAO.findAll().isEmpty()) {
                JavaFXUtils.showErrorAlert("ERROR", "Debes de generar las mesas primero.");
            } else {
                App.currentController.changeScene(Scenes.INICIO, null);
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
    private void toAjustes() throws IOException {
        App.currentController.changeScene(Scenes.AJUSTES, null);
    }

    @FXML
    private void close() throws IOException {
        MesaSingleton.closeSession();
        ComandaSingleton.closeSession();
        System.exit(0);
    }
}
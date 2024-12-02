package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MesasCafeteriaController extends Controller implements Initializable {
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
    private void goMesasTerrazas() throws IOException {
        App.currentController.changeScene(Scenes.MESASTERRAZA, null);
    }

    @FXML
    private void goMesasCafeterias() throws IOException {
        App.currentController.changeScene(Scenes.MESASCAFETERIA, null);
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.START, null);
    }
}
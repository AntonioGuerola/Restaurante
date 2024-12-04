package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmarComandaController extends Controller implements Initializable {




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }
}

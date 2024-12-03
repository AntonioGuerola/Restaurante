package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;
import org.example.Model.Entity.TipoProducto;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CategoriaProductosController extends Controller implements Initializable {
    Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();

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
    private void goBack() throws IOException {
        TipoMesa tipo = mesaSeleccionada.getTipo();
        if (tipo == TipoMesa.TERRAZA) {
            MesaSingleton.closeSession();
            App.currentController.changeScene(Scenes.MESASTERRAZA, null);
        } else {
            MesaSingleton.closeSession();
            App.currentController.changeScene(Scenes.MESASCAFETERIA, null);
        }
    }

    @FXML
    private void goToRefrescos() throws IOException {
        App.currentController.changeScene(Scenes.REFRESCOS, null);
    }

    @FXML
    private void goToCerveza() throws IOException {
        App.currentController.changeScene(Scenes.CERVEZA, null);
    }

    @FXML
    private void goToVino() throws IOException {
        App.currentController.changeScene(Scenes.VINO, null);
    }

    @FXML
    private void goToCarne() throws IOException {
        App.currentController.changeScene(Scenes.CARNE, null);
    }

    @FXML
    private void goToPescado() throws IOException {
        App.currentController.changeScene(Scenes.PESCADO, null);
    }

    @FXML
    private void goToVerdura() throws IOException {
        App.currentController.changeScene(Scenes.VERDURA, null);
    }

    @FXML
    private void goToPostre() throws IOException {
        App.currentController.changeScene(Scenes.POSTRE, null);
    }
}

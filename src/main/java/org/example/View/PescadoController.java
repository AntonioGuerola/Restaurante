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

public class PescadoController extends Controller implements Initializable {
    @FXML
    private VBox vbox;

    private final ProductoDAO pescadoDAO;

    public PescadoController() {
        this.pescadoDAO = new ProductoDAO();
    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Producto> productos = pescadoDAO.findAllPescado();

        for (Producto producto : productos) {
            Button button = new Button(producto.getNombre());

            button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

            button.setOnAction(event -> {
                System.out.println("Producto seleccionado: " + producto.getNombre());
            });

            vbox.getChildren().add(button);
        }

    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }
}

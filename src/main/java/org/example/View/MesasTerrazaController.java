package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.MesaDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MesasTerrazaController extends Controller implements Initializable {
    @FXML
    private VBox vbox;  // El VBox donde se agregarán los botones

    private final MesaDAO mesaDAO;

    public MesasTerrazaController() {
        this.mesaDAO = new MesaDAO();  // Creamos la instancia de MesaDAO
    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Llamamos al método contarMesasTerraza() para obtener la cantidad de mesas "Terraza"
            int cantidadMesasTerraza = mesaDAO.contarMesasTerraza();

            // Generamos los botones dinámicamente según el número de mesas "Terraza"
            for (int i = 1; i <= cantidadMesasTerraza; i++) {
                // Creamos un nuevo botón para cada mesa
                Button button = new Button("Mesa " + i);  // El nombre del botón es "Mesa X"

                // Definimos la acción cuando se presiona el botón
                int finalI = i;
                button.setOnAction(event -> {
                    // Aquí puedes agregar la lógica para lo que ocurre cuando se selecciona una mesa
                    System.out.println("Has seleccionado la mesa de tipo Terraza número " + finalI);
                });

                // Añadimos el botón al VBox
                vbox.getChildren().add(button);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de error si la consulta falla
        }
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
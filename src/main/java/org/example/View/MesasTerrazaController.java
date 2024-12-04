package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MesasTerrazaController extends Controller implements Initializable {
    @FXML
    private TilePane tilePane;

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
                Button button = new Button("Terra " + i);  // El nombre del botón es "Mesa X"

                // Estilo adicional para los botones (opcional, para hacerlo más vistoso)
                button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

                // Definimos la acción cuando se presiona el botón
                int finalI = i;  // Para usar en la lambda
                button.setOnAction(event -> {
                    try {
                        handleMesaSelection(finalI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // Añadimos el botón al VBox
                tilePane.getChildren().add(button);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de error si la consulta falla
        }
    }

    private void handleMesaSelection(int numMesa) throws IOException {
        try {
            // Carga la mesa completa desde la base de datos
            Mesa selectedMesa = mesaDAO.findByNumMesa(numMesa, TipoMesa.TERRAZA.name());

            // Configura el singleton con la mesa seleccionada
            MesaSingleton.getInstance(selectedMesa);

            // Cambia a la siguiente escena
            App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }}

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.INICIO, null);
    }
}

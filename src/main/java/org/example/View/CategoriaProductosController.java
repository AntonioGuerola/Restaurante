package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;
import org.example.Model.Entity.TipoProducto;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CategoriaProductosController extends Controller implements Initializable {
    @FXML
    private VBox infoMesaVBox;

    @FXML
    private Label tipoMesaLabel;

    @FXML
    private Label numMesaLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaMesaLabel;

    @FXML
    private Label tiempoLabel;

    @FXML
    private Label cuentaLabel;

    @FXML
    private TilePane tilePane;

    Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
    private final ComandaDAO comandaDAO = new ComandaDAO();



    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar la información de la mesa
        if (mesaSeleccionada != null) {
            tipoMesaLabel.setText("Tipo: " + (mesaSeleccionada.getTipo() != null ? mesaSeleccionada.getTipo().name() : "N/A"));
            numMesaLabel.setText("Número: " + mesaSeleccionada.getNumMesa());
            fechaLabel.setText("Fecha: " + (mesaSeleccionada.getFecha() != null ? mesaSeleccionada.getFecha() : "N/A"));
            horaMesaLabel.setText("Hora: " + (mesaSeleccionada.getHoraMesa() != null ? mesaSeleccionada.getHoraMesa() : "N/A"));

            try {
                int mesaId = MesaSingleton.getInstance().getCurrentMesa().getId();

                // Obtener la hora de la primera comanda
                LocalTime horaPrimeraComanda = comandaDAO.obtenerHoraPrimeraComanda(mesaId);

                if (horaPrimeraComanda != null) {
                    // Calcular la diferencia en minutos
                    long minutosTranscurridos = Duration.between(horaPrimeraComanda, LocalTime.now()).toMinutes();

                    // Mostrar el tiempo en la etiqueta
                    tiempoLabel.setText("Tiempo: " + minutosTranscurridos + " mins");
                } else {
                    // No hay comandas para esta mesa
                    tiempoLabel.setText("Tiempo: 0 mins");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                tiempoLabel.setText("Tiempo: Error");
            }

            cuentaLabel.setText("Cuenta: $" + (mesaSeleccionada.getCuenta() != null ? mesaSeleccionada.getCuenta().getSumaTotal() : "0.00"));
        }

        // Generar los botones para las categorías en el TilePane
        String[] categorias = {"REFRESCOS", "CERVEZA", "VINO", "CARNE", "PESCADO", "VERDURA", "POSTRE"};

        for (String categoria : categorias) {
            Button button = new Button(categoria);

            // Fijar tamaño de los botones
            button.setPrefWidth(150); // Ancho fijo
            button.setPrefHeight(100); // Alto fijo

            button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

            // Acción al hacer clic en el botón
            button.setOnAction(event -> {
                try {
                    switch (categoria) {
                        case "REFRESCOS":
                            goToRefrescos();
                            break;
                        case "CERVEZA":
                            goToCerveza();
                            break;
                        case "VINO":
                            goToVino();
                            break;
                        case "CARNE":
                            goToCarne();
                            break;
                        case "PESCADO":
                            goToPescado();
                            break;
                        case "VERDURA":
                            goToVerdura();
                            break;
                        case "POSTRE":
                            goToPostre();
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Añadir el botón al TilePane
            tilePane.getChildren().add(button);
        }
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

    @FXML
    private void enviarComanda() throws IOException {
        App.currentController.changeScene(Scenes.CONFIRMARCOMANDA, null);
    }
}

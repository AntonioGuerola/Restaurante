package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.CuentaDAO;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.*;
import org.example.Model.Singleton.ComandaSingleton;
import org.example.Model.Singleton.MesaSingleton;
import org.example.Model.Utils.JavaFXUtils;

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

                CuentaDAO cuentaDAO = new CuentaDAO();
                Cuenta cuenta = cuentaDAO.findByMesaId(mesaId);

                if (cuenta != null) {
                    cuentaLabel.setText("Cuenta: " + cuenta.getSumaTotal() + "€");
                } else {
                    cuentaLabel.setText("Cuenta: 0.0€");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                tiempoLabel.setText("Tiempo: Error");
                cuentaLabel.setText("Cuenta: Error");
            }

            cuentaLabel.setText("Cuenta: " + (mesaSeleccionada.getCuenta() != null ? mesaSeleccionada.getCuenta().getSumaTotal() : "0.00") + "€");
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
        try {
            // Intentar obtener la instancia del Singleton
            ComandaSingleton comandaSingleton = ComandaSingleton.getInstance();

            if (comandaSingleton != null && comandaSingleton.getCurrentComanda() != null) {
                Comanda comandaAbierta = comandaSingleton.getCurrentComanda();

                // Intentar eliminar la comanda de la base de datos
                try {
                    comandaDAO.delete(comandaAbierta);
                    System.out.println("Comanda abierta eliminada de la base de datos.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al eliminar la comanda abierta de la base de datos.");
                }

                // Limpiar el Singleton
                ComandaSingleton.closeSession();
                System.out.println("Comanda eliminada del Singleton.");
            } else {
                System.out.println("No hay una comanda activa en el Singleton.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar la comanda.");
        }

        // Cambiar la escena, independientemente de lo que sucedió antes
        TipoMesa tipo = mesaSeleccionada.getTipo();
        try {
            if (tipo == TipoMesa.TERRAZA) {
                MesaSingleton.closeSession();
                App.currentController.changeScene(Scenes.MESASTERRAZA, null);
            } else {
                MesaSingleton.closeSession();
                App.currentController.changeScene(Scenes.MESASCAFETERIA, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cambiar de escena.");
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
        // Verificar si la instancia del Singleton es válida
        ComandaSingleton comandaSingleton = ComandaSingleton.getInstance();
        if (comandaSingleton == null) {
            JavaFXUtils.showErrorAlert("Error", "El sistema no pudo procesar la comanda. Intente reiniciar la aplicación.");
            return;
        }

        // Verificar si hay una comanda activa
        if (comandaSingleton.getCurrentComanda() == null) {
            JavaFXUtils.showErrorAlert("Error", "Debes de incluir algún producto en la comanda antes de poder enviarla.");
            return;
        }

        // Proceder con el cambio de escena
        App.currentController.changeScene(Scenes.CONFIRMARCOMANDA, null);
    }

    @FXML
    private void goToCobrar() throws IOException {    Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
        if (mesaSeleccionada == null || mesaSeleccionada.getCuenta() == null) {
            // Mostrar una alerta de error si no hay una cuenta
            JavaFXUtils.showErrorAlert("Error", "Debes de enviar una comanda y crear la cuenta antes de poder cobrarla.");
            return; // Salir del método para evitar cambiar de escena
        }
        App.currentController.changeScene(Scenes.SHOWCUENTA, null);
    }
}

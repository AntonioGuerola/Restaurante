package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.Producto;
import org.example.Model.Singleton.MesaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class CervezaController extends Controller implements Initializable {
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
    private final ProductoDAO cervezaDAO;

    private final ComandaDAO comandaDAO = new ComandaDAO();

    public CervezaController() {
        this.cervezaDAO = new ProductoDAO();
    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        List<Producto> productos = cervezaDAO.findAllCerveza();

        for (Producto producto : productos) {
            Button button = new Button(producto.getNombre());

            button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

            button.setOnAction(event -> {
                System.out.println("Producto seleccionado: " + producto.getNombre());
            });

            tilePane.getChildren().add(button);
        }

    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }
}

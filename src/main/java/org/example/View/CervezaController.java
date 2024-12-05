package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.ComandaProductoDAO;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.ComandaProducto;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.Producto;
import org.example.Model.Singleton.ComandaSingleton;
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

    private Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
    private final ProductoDAO cervezaDAO = new ProductoDAO();
    private final ComandaDAO comandaDAO = new ComandaDAO();
    private Comanda comandaActual = null;

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
                int mesaId = mesaSeleccionada.getId();
                LocalTime horaPrimeraComanda = comandaDAO.obtenerHoraPrimeraComanda(mesaId);

                if (horaPrimeraComanda != null) {
                    long minutosTranscurridos = Duration.between(horaPrimeraComanda, LocalTime.now()).toMinutes();
                    tiempoLabel.setText("Tiempo: " + minutosTranscurridos + " mins");
                } else {
                    tiempoLabel.setText("Tiempo: 0 mins");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                tiempoLabel.setText("Tiempo: Error");
            }
            cuentaLabel.setText("Cuenta: $" + (mesaSeleccionada.getCuenta() != null ? mesaSeleccionada.getCuenta().getSumaTotal() : "0.00"));
        }

        try {
            // Buscar una comanda abierta para la mesa actual.
            comandaActual = comandaDAO.findComandaAbiertaPorMesa(mesaSeleccionada.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Producto> productos = cervezaDAO.findAllCerveza();
        for (Producto producto : productos) {
            Button button = new Button(producto.getNombre());
            button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
            button.setOnAction(event -> agregarProductoAComanda(producto));
            tilePane.getChildren().add(button);
        }
    }

    private void agregarProductoAComanda(Producto producto) {
        try {
            if (comandaActual == null) {
                // Crear una nueva comanda si no existe una abierta.
                comandaActual = new Comanda(LocalTime.now().toString(), mesaSeleccionada);
                comandaActual = comandaDAO.save(comandaActual);
            }

            // Agregar producto a la comanda.
            comandaActual.agregarProducto(producto, 1);

            // Guardar producto en la base de datos.
            ComandaProductoDAO comandaProductoDAO = new ComandaProductoDAO();
            comandaProductoDAO.save(new ComandaProducto(comandaActual, 1, producto, producto ));
            System.out.println("Producto agregado: " + producto.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() throws IOException {
        if (comandaActual != null) {
            // Guardar comanda en el singleton solo si se creó o utilizó una comanda.
            ComandaSingleton.getInstance(comandaActual);
        } else {
            ComandaSingleton.closeSession();
        }
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }
}

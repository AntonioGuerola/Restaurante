package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.App;
import javafx.scene.control.TextField;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;
import org.example.Model.Entity.TipoMesa;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CuantasTerrazasCafeteriasController extends Controller implements Initializable {
    @FXML
    private TextField terrazaText;

    @FXML
    private TextField cafeteriaText;

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        terrazaText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        terrazaText.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
        );

        cafeteriaText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        cafeteriaText.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
        );
    }

    @FXML
    private void generarMesas() throws IOException {
        try (MesaDAO mesaDAO = new MesaDAO()) {
            String terrazaTextValue = terrazaText.getText();
            String cafeteriaTextValue = cafeteriaText.getText();

            if (terrazaTextValue == null || terrazaTextValue.isEmpty()) {
                JavaFXUtils.showErrorAlert("ERROR", "El campo \"Terrazas\" no puede estar vacío.");
            }
            if (cafeteriaTextValue == null || cafeteriaTextValue.isEmpty()) {
                JavaFXUtils.showErrorAlert("ERROR", "El campo \"Cafeterias\" no puede estar vacío.");
            }

            int numTerrazas = Integer.parseInt(terrazaTextValue);
            int numCafeterias = Integer.parseInt(cafeteriaTextValue);

            for (int i = 1; i <= numTerrazas; i++) {
                Mesa mesa = new Mesa();
                mesa.setTipo(TipoMesa.TERRAZA);
                mesa.setNumMesa(i);
                mesa.setTiempo(0);
                mesaDAO.save(mesa);
            }

            for (int i = 1; i <= numCafeterias; i++) {
                Mesa mesa = new Mesa();
                mesa.setTipo(TipoMesa.CAFETERIA);
                mesa.setNumMesa(i);
                mesa.setTiempo(0);
                mesaDAO.save(mesa);
            }

            System.out.println("Mesas generadas y guardadas correctamente.");
        } catch (SQLException e) {
            JavaFXUtils.showErrorAlert("ERROR", "Error al interactuar con la base de datos.");
            throw new IOException("Error al generar las mesas en la base de datos.", e);
        } catch (NumberFormatException e) {
            JavaFXUtils.showErrorAlert("ERROR", "Error, deben de ser número enteros.");
            throw new IOException("Los campos deben contener números enteros.", e);
        }

        App.currentController.changeScene(Scenes.START, null);
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.START, null);
    }
}
package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;
import org.example.Model.Entity.TipoProducto;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateProductoController extends Controller implements Initializable {
    @FXML
    private TextField idProductoAUpdatearText;

    @FXML
    private TextField nombreProductoText;

    @FXML
    private TextField precioProductoText;

    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    private Producto productoToUpdatear;

    @Override
    public void onOpen(Object input) {
        choiceBox.getItems().add("Refresco");
        choiceBox.getItems().add("Cerveza");
        choiceBox.getItems().add("Vino");
        choiceBox.getItems().add("Carne");
        choiceBox.getItems().add("Pescado");
        choiceBox.getItems().add("Verdura");
        choiceBox.getItems().add("Postre");
    }

    @Override
    public void onClose(Object output) {
        // Aquí se pueden realizar acciones antes de cerrar la ventana.
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar el comportamiento inicial de la interfaz si es necesario.
    }

    /**
     * Busca un producto por ID y carga los datos en los TextField.
     */
    public void buscarId() {
        String idText = idProductoAUpdatearText.getText();

        if (idText.isEmpty()) {
            JavaFXUtils.showErrorAlert("Error", "El campo de ID no puede estar vacío.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            ProductoDAO dao = ProductoDAO.build();
            productoToUpdatear = dao.findById(id);

            if (productoToUpdatear != null) {
                cargarDatosProducto(productoToUpdatear);
            } else {
                JavaFXUtils.showErrorAlert("Error", "No se encontró ningún producto con ese ID.");
            }
        } catch (NumberFormatException e) {
            JavaFXUtils.showErrorAlert("Error", "El ID debe de ser un número entero.");
        } catch (SQLException e) {
            JavaFXUtils.showErrorAlert("Error", "Error al buscar el producto.");
        }
    }

    /**
     * Elimina el producto cargado de la base de datos.
     */
    public void updateProducto() throws IOException {
        if (productoToUpdatear == null) {
            JavaFXUtils.showErrorAlert("Error", "No hay ningún producto seleccionado para editar.");
            return;
        }

        // Confirmación de actualización
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar edición");
        confirmacion.setHeaderText("Está a punto de editar el producto:");
        confirmacion.setContentText("Nombre: " + productoToUpdatear.getNombre() +
                "\nPrecio: " + productoToUpdatear.getPrecio() +
                "\nTipo de Producto: " + productoToUpdatear.getTipo().name());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // Actualizar los valores del producto antes de enviarlo al DAO
                productoToUpdatear.setNombre(nombreProductoText.getText());
                productoToUpdatear.setPrecio(Double.parseDouble(precioProductoText.getText()));

                // Obtener el tipo de producto del ChoiceBox
                String selectedTipo = choiceBox.getValue();
                if (selectedTipo == null || selectedTipo.isEmpty()) {
                    throw new IllegalArgumentException("Debe seleccionar un tipo de producto válido.");
                }
                productoToUpdatear.setTipo(TipoProducto.valueOf(selectedTipo.toUpperCase()));

                ProductoDAO dao = ProductoDAO.build();
                dao.save(productoToUpdatear);

                JavaFXUtils.showErrorAlert("Éxito", "Producto editado correctamente.");
                limpiarCampos();
            } catch (NumberFormatException e) {
                JavaFXUtils.showErrorAlert("Error", "El precio debe ser un número válido.");
            } catch (IllegalArgumentException e) {
                JavaFXUtils.showErrorAlert("Error", "El tipo de producto no es válido.");
            } catch (SQLException e) {
                JavaFXUtils.showErrorAlert("Error", "No se ha podido editar el producto escogido.");
            }
        }
    }



    /**
     * Navega de vuelta a la pantalla anterior.
     */
    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.AJUSTES, null);
    }

    /**
     * Carga los datos del producto en los TextField.
     */
    private void cargarDatosProducto(Producto producto) {
        nombreProductoText.setText(producto.getNombre());
        precioProductoText.setText(String.valueOf(producto.getPrecio()));

        // Convertir el tipo de producto del enum a un valor compatible con el ChoiceBox
        String tipoProducto = producto.getTipo().name().substring(0, 1).toUpperCase() + producto.getTipo().name().substring(1).toLowerCase();
        choiceBox.setValue(tipoProducto); // Asignar el valor correspondiente al ChoiceBox
    }



    /**
     * Limpia los campos de texto y la referencia al producto cargado.
     */
    private void limpiarCampos() {
        idProductoAUpdatearText.clear();
        nombreProductoText.clear();
        precioProductoText.clear();

        // Limpiar la selección del ChoiceBox
        choiceBox.setValue(null);
        productoToUpdatear = null;
    }

}

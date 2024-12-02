package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Model.DAO.ProductoDAO;
import org.example.Model.Entity.Producto;
import org.example.Model.Utils.JavaFXUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeleteProductoController extends Controller implements Initializable {
    @FXML
    private TextField idProductoABorrarText;

    @FXML
    private TextField nombreProductoText;

    @FXML
    private TextField precioProductoText;

    @FXML
    private TextField tipoProductoText;

    private Producto productoToDelete;

    @Override
    public void onOpen(Object input) {
        // Aquí se puede cargar información inicial si se pasa algún parámetro.
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
        String idText = idProductoABorrarText.getText();

        if (idText.isEmpty()) {
            JavaFXUtils.showErrorAlert("Error", "El campo de ID no puede estar vacío.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            ProductoDAO dao = ProductoDAO.build();
            productoToDelete = dao.findById(id);

            if (productoToDelete != null) {
                cargarDatosProducto(productoToDelete);
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
    public void deleteProducto() throws IOException {
        if (productoToDelete == null) {
            JavaFXUtils.showErrorAlert("Error", "No hay ningún producto seleccionado para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Está a punto de eliminar el producto:");
        confirmacion.setContentText("Nombre: " + productoToDelete.getNombre() + "\nPrecio: " + productoToDelete.getPrecio() + "\nTipo de Producto: " + productoToDelete.getTipo().name());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                ProductoDAO dao = ProductoDAO.build();
                dao.delete(productoToDelete);
                JavaFXUtils.showErrorAlert("Éxito", "Producto eliminado correctamente.");
                limpiarCampos();
            } catch (SQLException e) {
                JavaFXUtils.showErrorAlert("Error", "No se ha podido eliminar el producto escogido.");
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
        tipoProductoText.setText(producto.getTipo().name());
    }

    /**
     * Limpia los campos de texto y la referencia al producto cargado.
     */
    private void limpiarCampos() {
        idProductoABorrarText.clear();
        nombreProductoText.clear();
        precioProductoText.clear();
        tipoProductoText.clear();
        productoToDelete = null;
    }
}

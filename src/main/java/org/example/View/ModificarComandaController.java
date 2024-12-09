package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Model.DAO.ComandaProductoDAO;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.ComandaProducto;
import org.example.Model.Singleton.ComandaSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModificarComandaController extends Controller implements Initializable {
    @FXML
    private TextField nombreProductoText;

    @FXML
    private TextField cantidadProductoText;

    @FXML
    private ChoiceBox<String> productosChoiceBox = new ChoiceBox<>();

    private final ComandaProductoDAO comandaProductoDAO = new ComandaProductoDAO();
    private ComandaProducto selectedProducto = null;
    private final Comanda comandaToUpdatear = ComandaSingleton.getInstance().getCurrentComanda();

    @Override
    public void onOpen(Object input) {
        cargarProductosEnChoiceBox(); // Cargar datos al abrir la escena
    }

    @Override
    public void onClose(Object output) {
        // No hay acciones específicas al cerrar
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productosChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarDetallesProducto(newValue);
            }
        });

        cantidadProductoText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cantidadProductoText.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void cargarProductosEnChoiceBox() {
        try {
            if (comandaToUpdatear != null && comandaToUpdatear.getId() > 0) {
                var productos = comandaProductoDAO.findByComandaId(comandaToUpdatear.getId());

                productosChoiceBox.getItems().clear();
                for (ComandaProducto producto : productos) {
                    if (producto.getProducto() != null) {
                        productosChoiceBox.getItems().add(producto.getProducto().getNombre());
                    }
                }
            } else {
                System.err.println("Comanda inválida o ID menor o igual a 0.");
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar productos", "No se pudieron cargar los productos de la comanda.");
            e.printStackTrace();
        }
    }

    private void cargarDetallesProducto(String nombreProducto) {
        try {
            var productos = comandaProductoDAO.findByComandaId(comandaToUpdatear.getId());
            selectedProducto = productos.stream()
                    .filter(p -> p.getProducto().getNombre().equals(nombreProducto))
                    .findFirst()
                    .orElse(null);

            if (selectedProducto != null) {
                nombreProductoText.setText(selectedProducto.getProducto().getNombre());
                cantidadProductoText.setText(String.valueOf(selectedProducto.getCantidad()));
                nombreProductoText.setEditable(false);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar detalles", "No se pudo cargar el producto seleccionado.");
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarProducto() {
        try {
            if (selectedProducto != null) {
                int unidadesAgregar = Integer.parseInt(cantidadProductoText.getText());
                if (unidadesAgregar > 0) {
                    selectedProducto.setCantidad(unidadesAgregar);
                    comandaProductoDAO.save(selectedProducto); // Suma la cantidad

                    // Actualizar productos en el ComandaSingleton
                    actualizarProductosEnComanda();

                    mostrarInfo("Producto actualizado", "Se añadieron unidades al producto correctamente.");
                } else {
                    mostrarError("Cantidad inválida", "No se puede añadir 0 o menos unidades.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            mostrarError("Error al agregar producto", "No se pudo añadir unidades al producto.");
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarCantidadProducto() {
        if (selectedProducto != null) {
            try {
                int nuevaCantidad = Integer.parseInt(cantidadProductoText.getText());

                if (nuevaCantidad == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar eliminación");
                    alert.setHeaderText("¿Está seguro de que desea eliminar este producto de la comanda?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        comandaProductoDAO.delete(selectedProducto);

                        // Actualizar productos en el ComandaSingleton
                        actualizarProductosEnComanda();

                        productosChoiceBox.getItems().remove(selectedProducto.getProducto().getNombre());
                        limpiarCampos();
                        mostrarInfo("Producto eliminado", "El producto se eliminó correctamente.");
                    }
                } else {
                    selectedProducto.setCantidad(nuevaCantidad);
                    comandaProductoDAO.updateCantidad(selectedProducto);

                    // Actualizar productos en el ComandaSingleton
                    actualizarProductosEnComanda();

                    mostrarInfo("Producto actualizado", "La cantidad del producto se actualizó correctamente.");
                }
            } catch (SQLException | NumberFormatException e) {
                mostrarError("Error al actualizar", "No se pudo actualizar el producto.");
                e.printStackTrace();
            }
        }
    }

    private void actualizarProductosEnComanda() throws SQLException {
        // Obtener la lista de ComandaProducto desde la base de datos
        List<ComandaProducto> productosActualizados = comandaProductoDAO.findByComandaId(comandaToUpdatear.getId());

        // Convertir la lista de ComandaProducto a ProductoCantidad
        List<Comanda.ProductoCantidad> productosCantidad = productosActualizados.stream()
                .map(cp -> new Comanda.ProductoCantidad(cp.getProducto(), cp.getCantidad()))
                .collect(Collectors.toList()); // Java 16+; si usas una versión más antigua, usa collect(Collectors.toList())

        // Actualizar los productos en la comanda
        comandaToUpdatear.setProductos(productosCantidad);
    }


    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CONFIRMARCOMANDA, null);
    }

    private void limpiarCampos() {
        nombreProductoText.clear();
        cantidadProductoText.clear();
        selectedProducto = null;
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }
}

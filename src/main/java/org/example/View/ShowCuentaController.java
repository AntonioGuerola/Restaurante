package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import org.example.App;
import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.ComandaProductoDAO;
import org.example.Model.DAO.CuentaDAO;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.*;
import org.example.Model.Singleton.MesaSingleton;
import org.example.Model.Utils.Serializator;
import org.example.Model.Utils.XMLSerializator;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.example.Model.Utils.JavaFXUtils.getCurrentFormattedTime;

public class ShowCuentaController extends Controller implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private Label numMesaLabel, tipoMesaLabel, tiempoLabel, sumaTotalLabel;

    private Mesa mesaSeleccionada = MesaSingleton.getInstance().getCurrentMesa();
    private Cuenta cuentaActual = mesaSeleccionada.getCuenta();
    private final CuentaDAO cuentaDAO = new CuentaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            cuentaActual = cuentaDAO.findByMesaId(mesaSeleccionada.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cuentaActual != null) {
            numMesaLabel.setText("Número de Mesa: " + mesaSeleccionada.getNumMesa());
            tipoMesaLabel.setText("Tipo de Mesa: " + mesaSeleccionada.getTipo());
//            tiempoLabel.setText("Tiempo: " + calcularTiempoCuenta());
            sumaTotalLabel.setText("Total: " + cuentaActual.getSumaTotal() + "€");

            generarProductosCuenta();
        }
    }

    private void generarProductosCuenta() {
        tilePane.getChildren().clear();

        // Crear un mapa para agrupar productos por nombre y sumar sus cantidades
        Map<String, Integer> productosAgrupados = new HashMap<>();

        // Obtener los productos de cada comanda
        for (Comanda comanda : cuentaActual.getComandas()) {
            try {
                ComandaProductoDAO comandaProductoDAO = new ComandaProductoDAO();
                // Cargar los productos de la comanda desde la base de datos
                List<ComandaProducto> productosComanda = comandaProductoDAO.findByComandaId(comanda.getId());

                // Convertir ComandaProducto a ProductoCantidad
                List<Comanda.ProductoCantidad> productosCantidad = new ArrayList<>();
                for (ComandaProducto comandaProducto : productosComanda) {
                    productosCantidad.add(new Comanda.ProductoCantidad(
                            comandaProducto.getProducto(),
                            comandaProducto.getCantidad()
                    ));
                }

                // Asignar los productos convertidos a la comanda
                comanda.setProductos(productosCantidad);

                // Agrupar los productos por nombre y sumar las cantidades
                for (Comanda.ProductoCantidad productoCantidad : comanda.getProductos()) {
                    String nombreProducto = productoCantidad.getProducto().getNombre();
                    int cantidad = productoCantidad.getCantidad();

                    productosAgrupados.put(nombreProducto, productosAgrupados.getOrDefault(nombreProducto, 0) + cantidad);
                }

            } catch (SQLException e) {
                System.err.println("Error al cargar los productos de la comanda: " + e.getMessage());
            }
        }

        // Ahora que tienes los productos agrupados, puedes mostrarlos en la UI
        for (Map.Entry<String, Integer> entry : productosAgrupados.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();

            HBox hbox = new HBox(10);
            hbox.setPrefWidth(440);
            hbox.setStyle("-fx-border-radius: 5; -fx-padding: 5;");

            // Etiqueta de cantidad
            Label cantidadLabel = new Label(String.valueOf(cantidad));
            cantidadLabel.setPrefWidth(50);
            cantidadLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-LEFT;");

            // Etiqueta de nombre del producto
            Label nombreProductoLabel = new Label(nombreProducto);
            nombreProductoLabel.setPrefWidth(200);
            nombreProductoLabel.setStyle("-fx-font-size: 18px; -fx-alignment: CENTER-RIGHT;");

            hbox.getChildren().addAll(cantidadLabel, nombreProductoLabel);
            tilePane.getChildren().add(hbox);
        }
    }


    @FXML
    private void cobrarCuenta() throws IOException, SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Cuenta cuenta = mesaSeleccionada.getCuenta();
        String horaActual = getCurrentFormattedTime();

        if (cuenta.getHoraCobro() == null || cuenta.getHoraCobro().isEmpty()) {
            cuentaDAO.actualizarHoraCobro(cuenta.getId(), horaActual); // Solo actualizar si está a null
        }

        String horaMesa = mesaSeleccionada.getHoraMesa();
        LocalTime horaMesaLocalTime = LocalTime.parse(horaMesa);
        LocalTime horaActualLocalTime = LocalTime.now();

        Duration diferencia = Duration.between(horaMesaLocalTime, horaActualLocalTime);

        int minutosTotales = (int) diferencia.toMinutes();
        if (mesaSeleccionada.getTiempo() == 0) {
            mesaDAO.actualizarTiempo(mesaSeleccionada.getId(), minutosTotales);
            System.out.println("El tiempo se ha seteado a " + minutosTotales + " minutos.");
        }

        Mesa mesaASerializar = mesaDAO.findById(mesaSeleccionada.getId());

        String xmlFilename = "mesa_" + mesaASerializar.getId() + ".xml";

        try {
            XMLSerializator.serializeObjectToXML(mesaASerializar, xmlFilename);
            System.out.println("Mesa y cuenta serializadas en " + xmlFilename);
        } catch (JAXBException e) {
            System.err.println("Error al serializar la mesa y la cuenta a XML: " + e.getMessage());
        }

        recrearMesa();

        App.currentController.changeScene(Scenes.INICIO, null);
    }

    @FXML
    private void goBack() throws IOException {
        App.currentController.changeScene(Scenes.CATEGORIAPRODUCTOS, null);
    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    private void recrearMesa() throws SQLException {
        // Obtener el tipo y número de la mesa seleccionada
        TipoMesa tipoMesa = mesaSeleccionada.getTipo();
        int numMesa = mesaSeleccionada.getNumMesa();

        // Crear un objeto MesaDAO para eliminar la mesa
        MesaDAO mesaDAO = new MesaDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();
        ComandaDAO comandaDAO = new ComandaDAO();

        // Eliminar las comandas asociadas a la mesa
        comandaDAO.deleteByMesaId(mesaSeleccionada.getId());
        System.out.println("Comandas eliminadas asociadas a la mesa ID: " + mesaSeleccionada.getId());

        // Eliminar la cuenta asociada a la mesa
        cuentaDAO.deleteByMesaId(mesaSeleccionada.getId());
        System.out.println("Cuenta eliminada asociada a la mesa ID: " + mesaSeleccionada.getId());

        // Eliminar la mesa que ya ha sido cobrada
        mesaDAO.delete(mesaSeleccionada);  // Elimina la mesa de la base de datos
        System.out.println("Mesa eliminada con ID: " + mesaSeleccionada.getId());

        // Crear una nueva mesa con los mismos parámetros
        Mesa nuevaMesa = new Mesa();
        nuevaMesa.setTipo(tipoMesa);
        nuevaMesa.setNumMesa(numMesa);
        nuevaMesa.setFecha(LocalDate.now().toString());  // Usamos la fecha actual
        nuevaMesa.setHoraMesa("");  // La hora es NULL (vacío)
        nuevaMesa.setTiempo(0);  // Tiempo en 0
        nuevaMesa.setCuenta(null);  // Cuenta en 0.00

        // Guardar la nueva mesa en la base de datos
        mesaDAO.save(nuevaMesa);  // Esto insertará la nueva mesa con un nuevo ID
        System.out.println("Mesa recreada con ID: " + nuevaMesa.getId());
    }
}

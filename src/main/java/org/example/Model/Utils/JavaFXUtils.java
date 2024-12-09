package org.example.Model.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.App;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JavaFXUtils {
    public static void showErrorAlert(String title, String textAboutAlert) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(title);

        // Crear un TextArea para mostrar el mensaje largo
        TextArea textArea = new TextArea(textAboutAlert);
        textArea.setEditable(false); // Hacerlo no editable
        textArea.setWrapText(true); // Habilitar el salto de línea
        textArea.setMaxWidth(350); // Ajusta el tamaño máximo si es necesario
        textArea.setMaxHeight(150); // Ajusta el tamaño máximo si es necesario

        // Establecer el TextArea como contenido en lugar del Label
        errorAlert.getDialogPane().setContent(textArea);

        errorAlert.showAndWait();
    }

    public static String getCurrentFormattedTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}

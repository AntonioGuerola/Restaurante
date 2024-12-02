package org.example.Model.Utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.App;

import java.util.Objects;

public class JavaFXUtils {
    public static void showErrorAlert(String title, String textAboutAlert) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        if (errorAlert.getDialogPane().getScene().getWindow() != null) {
            Stage alertStage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
        }
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(textAboutAlert);
        errorAlert.showAndWait();
    }
}

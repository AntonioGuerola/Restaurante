package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.Model.Singleton.ComandaSingleton;
import org.example.View.AppController;
import org.example.View.Scenes;
import org.example.View.View;

import java.io.IOException;
import java.util.Objects;

/**
 * The main class that launches the JavaFX application.
 */
public class App extends Application {
    public static Scene scene;
    public static AppController currentController;

    /**
     * The entry point for the JavaFX application.
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs during loading of FXML files.
     */
    @Override
    public void start(Stage stage) throws IOException {
        ComandaSingleton.closeSession();
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene, 900, 600);
        currentController = (AppController) view.controller;
        currentController.onOpen(null);
        stage.setTitle("ForYourBill");
        //stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("images/LogoG3DMedio.png"))));
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        stage.setMaxHeight(600);
        stage.setMaxWidth(900);
        stage.show();
    }

    /**
     * Static method to set the root scene.
     * @param fxml The FXML file to load as the root scene.
     * @throws IOException If an error occurs during loading of the FXML file.
     */
    public static void setRoot(String fxml) throws IOException {

    }

    /**
     * The main method to launch the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
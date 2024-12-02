package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.example.App;
import org.example.View.Controller;
import org.example.View.Scenes;
import org.example.View.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the main application view.
 */
public class AppController extends Controller implements Initializable {
    @FXML
    private BorderPane borderPane;
    private Controller centerController;

    /**
     * Change the scene to START when the controller is opened.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        changeScene(Scenes.START, null);
    }

    /**
     * Changes the scene to the specified scene.
     *
     * @param scene The scene to change to.
     * @param data  Optional data to pass to the new scene.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void changeScene(Scenes scene, Object data) throws IOException {
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }

    /**
     * Do something when the controller is going to close.
     */
    @Override
    public void onClose(Object output) {

    }

    /**
     * Initializer for some aspects.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Loads the FXML file for the specified scene.
     *
     * @param scenes The scene to load.
     * @return The loaded view containing the scene and controller.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static View loadFXML(Scenes scenes) throws IOException {
        String url = scenes.getURL();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }

    /**
     * Closes the application.
     */
    @FXML
    private void closeApp() {
        System.exit(0);
    }
}

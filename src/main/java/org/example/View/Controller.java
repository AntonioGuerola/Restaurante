package org.example.View;

import org.example.App;

import java.io.IOException;

/**
 * Abstract class representing a controller in the application.
 */
public abstract class Controller {
    App app;

    /**
     * Sets the main application reference.
     *
     * @param app The main application instance.
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Method called when the controller's associated view is opened.
     *
     * @param input The input data for the view.
     * @throws IOException If an error occurs during view opening.
     */
    public abstract void onOpen(Object input) throws IOException;

    /**
     * Method called when the controller's associated view is closed.
     *
     * @param output The output data from the view.
     */
    public abstract void onClose(Object output);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import vision.WorldState;

/**
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
public class ApplicationController {

    private MainWindow window;
    private Communication communication;
    private static ApplicationController appController;

    /**
     * Main method. All it does is instantiate a new ApplicationController
     */
    public static void main(String[] args) {
        appController = new ApplicationController();
    }

    /**
     * Constructor for the ApplicationController class. 
     * Instantiates a new Communication object.
     */
    public ApplicationController() {
        MainWindow.setup(this);
        vision.RunVision.main(null);
        communication = new Communication();
    }

    /**
     * Getter method for Communication object
     * @return Communication
     */
    public Communication getCommunicationController() {
        return communication;
    }

    /**
     * Connect method. Sends input to the UI if the connection was established or not.
     */
    public void connect() {
        window.setStatus(MainWindow.STATUS_CONNECTING);
        if (communication.connect()) {
            window.setStatus(MainWindow.STATUS_CONNECTED);
        } else {
            window.setStatus(MainWindow.STATUS_DISCONNECTED);
            window.showRetryDialog();
        }
    }

    /**
     * Method to disconnect properly the brick. Calls the disconnect method 
     * from Communication
     */
    public void disconnect() {
        communication.disconnect();
        window.setStatus(MainWindow.STATUS_DISCONNECTED);
    }

    /**
     * set method to initialise the MainWindow instance
     */
    public void setWindow(MainWindow newWindow) {
        window = newWindow;
    }

    /**
     * get method to return the MainWindow instance
     * @return MainWindow
     */
    public MainWindow getWindow() {
        return window;
    }

    /**
     * If someone closes the UI screen without disconnecting, this bit will 
     * close the connection cleanly
     */
    public void close() {
        disconnect();
    }
    
    public WorldState getWorldState() {
        return vision.RunVision.getWorldState();
    }
}

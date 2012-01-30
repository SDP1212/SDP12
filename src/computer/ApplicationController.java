/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;


/**
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
public class ApplicationController {
    private MainWindow window;
    private Communication communication;
    private static ApplicationController appController;
    
    public static void main(String[] args) {
        appController = new ApplicationController();
    }
    public ApplicationController() {
        MainWindow.setup(this);
        communication = new Communication();
    }
    
    public Communication getCommunicationController() {
        return communication;
    }
    
    public void connect() {
        window.setStatus(MainWindow.STATUS_CONNECTING);
        if (communication.connect()) {
            window.setStatus(MainWindow.STATUS_CONNECTED);
        } else {
            window.setStatus(MainWindow.STATUS_DISCONNECTED);
            window.showRetryDialog();
        }
    }
    
    public void disconnect() {
        communication.disconnect();
        window.setStatus(MainWindow.STATUS_DISCONNECTED);
    }
    
    public void setWindow(MainWindow newWindow) {
        window = newWindow;
    }
    
    public MainWindow getWindow() {
        return window;
    }
    
    public void close() {
        disconnect();
    }

}

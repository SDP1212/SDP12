/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import java.io.IOException;

/**
 *
 * @author s0935251
 */
public class ApplicationController {
    public static void main(String[] args) throws IOException {
        Communication comms = new Communication();
        comms.connect();
        MainWindow window = new MainWindow();
        window.setup();
    }
}

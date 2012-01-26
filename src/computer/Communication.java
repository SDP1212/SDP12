/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;
import lejos.pc.comm.*;
import java.io.*;
/**
 *
 * @author s0935251
 */
public class Communication {
    private NXTCommBluecove bluetoothLink;
    private NXTInfo info;
    private OutputStream outStream;
    private InputStream inStream;
    public void switchModeTo(int mode) {
        
    }
    
    public void connect() throws IOException {
        try {
            bluetoothLink = new NXTCommBluecove();
            info = new NXTInfo(NXTCommFactory.BLUETOOTH, null, "0016530BB5A3");
            bluetoothLink.open(info);
        } catch (NXTCommException e) {
            throw new IOException("Failed to connect " + e.toString());
        }
        inStream = bluetoothLink.getInputStream();
        outStream = bluetoothLink.getOutputStream();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;
import brick.Brick;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.*;
import java.io.*;
import java.nio.ByteBuffer;
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
    
    public boolean connect() {
        bluetoothLink = new NXTCommBluecove();
        info = new NXTInfo(NXTCommFactory.BLUETOOTH, null, "0016530BB5A3");
        try {
            bluetoothLink.open(info);
        } catch (NXTCommException e) {
            System.out.println(e.toString());
            return false;
        }
        inStream = bluetoothLink.getInputStream();
        outStream = bluetoothLink.getOutputStream();
        return true;
    }
    
    public void disconnect() {
        try {
            sendMessage(Brick.QUIT);
            bluetoothLink.close();
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public void forward() {
        sendMessage(Brick.FORWARDS);
    }
    
    public void backward() {
        sendMessage(Brick.BACKWARDS);
    }
    
    public void stop() {
        sendMessage(Brick.STOP);
    }
    
    public void kick() {
        sendMessage(Brick.KICK);
    }
    
    public void sendMessage(int message) {
        try {
            byte[] buf = ByteBuffer.allocate(4).putInt(message).array();
            outStream.write(buf);
            outStream.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}

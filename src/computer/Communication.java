package computer;
import brick.Brick;
import lejos.pc.comm.*;
import java.io.*;
import java.nio.ByteBuffer;
/**
 * Instances of this class provide communication with the NXT brick.
 * 
 * The class will be instantiated once for each application.
 * 
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
public class Communication implements Runnable {
    private NXTCommBluecove bluetoothLink;
    private NXTInfo info;
    private OutputStream outStream;
    private InputStream inStream;
    private Thread listener;
    private boolean connected = false;
    public void switchModeTo(int mode) {
        
    }
    /**
     * Connects to the brick
     * 
     * @return True if connection successful, false otherwise
     */
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
        listener = new Thread(this);
        listener.start();
        connected = true;
        return true;
    }
    /**
     * Disconnects from the brick
     */
    public void disconnect() {
        if (listener != null && listener.isAlive()) {
            listener.interrupt();
        }
        try {
            if (connected) {
                sendMessage(Brick.QUIT);
                bluetoothLink.close();
                outStream.close();
                inStream.close();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        connected = false;
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
    
    public void rotateLeft () {
        sendMessage(Brick.ROTATELEFT);
    }
    
    public void rotateRight () {
        sendMessage(Brick.ROTATERIGHT);
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

    public void run() {
        while(!Thread.interrupted()) {
            byte[] byteBuffer = new byte[4];
            ByteBuffer buffer = ByteBuffer.allocate(4);
            int n = Brick.DO_NOTHING;
            try {
                inStream.read(buffer.array());
                n = buffer.getInt();                
                switch (n){
                    case (Brick.COLLISION):
                        System.out.println("Collision!");
                        break;
                    case (Brick.OK):
                        System.out.println("Acknowledged");
                        break;
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        System.out.println("Interupted");
    }
}

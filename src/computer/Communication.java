package computer;
import brick.Brick;
import lejos.pc.comm.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
    
    /**
     * Send a message to the brick to go forward using the given speed
     * @param speed 
     */
    public void forward(int speed) {
        sendMessage(Brick.FORWARDS | speed);
    }
    
    /**
     * Send a message to the brick to go backward using the given speed
     * @param speed 
     */
    public void backward(int speed) {
        sendMessage(Brick.BACKWARDS | speed);
    }
    
    /**
     * Send a stop message to the brick
     */
    public void stop() {
        sendMessage(Brick.STOP);
    }
    
    /**
     * Send a kick message to the brick
     */
    public void kick() {
        sendMessage(Brick.KICK);
    }
    
    /**
     * Send a rotate left message to the brick
     */
    public void rotateLeft () {
        sendMessage(Brick.ROTATELEFT);
    }
    
    /**
     * Send a rotate left message to the brick
     */
    public void rotateRight () {
        sendMessage(Brick.ROTATERIGHT);
    }
    
    /**
     * Send message method. Creates a 4 byte buffer in which it adds the message 
     * and writes it to the output stream.
     * @param message 
     */
    public void sendMessage(int message) {
        try {
            byte[] buf = ByteBuffer.allocate(4).putInt(message).array();
            System.out.println(Arrays.toString(buf));
            outStream.write(buf);
            outStream.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Thread to read any answer that comes from the brick. 
     */
    public void run() {
        while(!Thread.interrupted()) {
            byte[] byteBuffer = new byte[4];
            ByteBuffer buffer = ByteBuffer.allocate(4);
            int n = Brick.DO_NOTHING;
            try {
                inStream.read(buffer.array());
                n = buffer.getInt();                
                /**
                 * If there is a collision or an acknowledged message 
                 * it prints it to standard output
                 */
     
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

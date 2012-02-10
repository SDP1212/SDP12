package computer;
import brick.Brick;
import computer.control.ControlInterface;
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
public class Communication implements Runnable, ControlInterface {
    private NXTCommBluecove bluetoothLink;
    private NXTInfo info;
    private OutputStream outStream;
    private InputStream inStream;
    private Thread listener;
    private boolean connected = false;
    private int commState = DISCONNECTED;
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
        commState = READY;
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
        commState = DISCONNECTED;
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
     * Send a rotate message to the brick. Positive anti-clockwise, negative 
     * clockwise.
     */

    public void rotate (double angle) {
        int opcode = Brick.ROTATE;
//        System.out.println("Opcode: " + opcode);
        int arg = composeAngleArgument(angle) << 8;
//        System.out.println("Arg " + arg);
//        System.out.println("Message " + Integer.toBinaryString(arg | opcode));
        sendMessage(arg | opcode);
    }
    
    /**
     * Send message method. Creates a 4 byte buffer in which it adds the message 
     * and writes it to the output stream.
     * @param message 
     */
    public void sendMessage(int message) {
        try {
            byte[] buf = intToByteArray(message);
            outStream.write(buf);
            outStream.flush();
            commState = WAITING;
        } catch (IOException e) {
            System.out.println(e.toString());
            commState = ERROR;
        }
    }
    
    public int composeAngleArgument(double angle) {
        int out = Math.round((float)Math.toDegrees(angle) + 180);
        System.out.println("Angle " + Integer.toString(out));
        return out;
        
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
                        commState = READY;
                        System.out.println("Acknowledged");
                        break;
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        System.out.println("Interupted");
    }
    public static byte[] intToByteArray(int in) {
        byte[] b = new byte[4];
        // For each position in the array
        for (int i = 0; i < 4; i++) {
            // Shift the integer by out current position in the array
            int shift = (3 - i) * 8;
            b[i] = (byte) (((in & 0xFFFFFFFF) >> shift) & 0x00000000FFFFFFFF);
        }
        return b;
    }
    
    public int getCommState() {
        return commState;
    }
}

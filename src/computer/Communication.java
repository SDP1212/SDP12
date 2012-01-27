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
 * @author s0935251
 */
public class Communication implements Runnable {
    private NXTCommBluecove bluetoothLink;
    private NXTInfo info;
    private OutputStream outStream;
    private InputStream inStream;
    private Thread listener;
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
        return true;
    }
    /**
     * Disconnects from the brick
     */
    public void disconnect() {
        listener.interrupt();
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
            byte[] byteBuffer = new byte[1];
            int n = Brick.DO_NOTHING;
            try {
                inStream.read(byteBuffer);
                n = byteBuffer[0];
                System.out.println(Integer.toString(n));
//                int opcode = ((n << 24) >> 24);
                int opcode = n;
                switch (opcode){
                    case -96:
                        System.out.println("Collision!");
                        break;
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        System.out.println("Interupted");
    }
}

package computer;
import brick.Brick;
import computer.ai.AI;
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
    private AI ai;
    public void switchModeTo(int mode) {
        
    }

    public Communication() {
        this.ai=null;
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
            System.out.println("Comm error" + e.toString());
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
            System.out.println("IO error" + e.toString());
        }
        connected = false;
        commState = DISCONNECTED;
    }
    
    /**
     * Send a message to the brick to go forward using the given speed
     * @param speed 
     */
    public void forward(int speed) {
        sendMessage(Brick.FORWARDS | (speed << 8));
    }
    
    /**
     * Send a message to the brick to go backward using the given speed
     * @param speed 
     */
    public void backward(int speed) {
        sendMessage(Brick.BACKWARDS | (speed << 8));
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
     * @param angle must be radians!!!!!
     */
    
    public void rotate (double angle) {
        int opcode = Brick.ROTATE;
        int arg = composeAngleArgument(angle) << 8;
//		System.out.println("Operation " + opcode);
//        System.out.println("Arg " + arg);
//        System.out.println("Message " + Integer.toBinaryString(arg | opcode));
        sendMessage(arg | opcode);
    }

	public void arcLeft (int radius) {
        int opcode = Brick.ARCLEFT;
		System.out.println("arc left");
        int arg = radius << 8;
        sendMessage(arg | opcode);
    }
	
    public void arcRight (int radius) {
        int opcode = Brick.ARCRIGHT;
		System.out.println("arc right");
        int arg = radius << 8;
        sendMessage(arg | opcode);
    }
	 
    public void rotateRight(int speed) {
        sendMessage(Brick.ROTATERIGHT | (speed << 8));
    }

    public void rotateLeft(int speed ) {
        sendMessage(Brick.ROTATELEFT | (speed << 8));
    }

	public void rotateTo(int heading) {
//		System.out.println("Opcode: " + Brick.ROTATETO);
//		System.out.println("Arg: " + heading);
		sendMessage(Brick.ROTATETO | (heading << 8));
//		System.out.println("Message: " + Integer.toBinaryString(Brick.ROTATETO | (heading << 8)));
	}

	public void setHeading(int heading) {
		sendMessage(Brick.LOCKHEADING | ((heading - 90) << 8));
	}

	public void unlockHeading() {
		sendMessage(Brick.UNLOCKHEADING);
	}
	
	
    
    public void dribble() {
        forward(Brick.MEDIUM);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            
        }
        stop();
    }
    
    /**
     * Send message method. Creates a 4 byte buffer in which it adds the message 
     * and writes it to the output stream.
     * @param message 
     */
    public void sendMessage(int message) {
        try {
            if (commState == READY) {
				//System.out.println("Message: " + Integer.toBinaryString(message));
                byte[] buf = intToByteArray(message);
//                System.out.println("Message " + Arrays.toString(buf));
                outStream.write(buf);
                outStream.flush();
                commState = WAITING;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            commState = ERROR;
        }
    }
    
	/**
	 * Convert a radian angle to an integer, suitable to be used as an argument
	 * @param angle an angle in radians
	 * @return an integer angle in degrees
	 */
    public int composeAngleArgument(double angle) {
        long out = Math.round(Math.toDegrees(angle) + 180);
//        System.out.println("Angle " + Long.toString(out));
        return (int)out;
        
    }

    /**
     * Thread to read any answer that comes from the brick. 
     */
    public void run() {
        while(!Thread.interrupted()) {
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
                        commState = WAITING;
                        if (ai != null) {
                            ai.robotCollided();
                        }
                        break;
                    case (Brick.OK):
                        commState = READY;
                        System.out.println("Acknowledged");
                        break;
//                    case (Brick.SENSING):
//                        System.out.println("Sensing");
//                        break;
                    case (Brick.SENSINGENDED):
                        System.out.println("Sensing ended");
                        break;
                }
            } catch (IOException e) {
                connected = false;
				disconnect();
				break;
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
            b[i] = (byte) ((in  >>> shift) & 0x000000FF);
        }
        return b;
    }
    
    public int getCommState() {
        return commState;
    }

    public void addAI(AI ai){
        this.ai=ai;
    }
    
}

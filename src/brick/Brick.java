package brick;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.InputStream;
import java.io.OutputStream;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.CompassPilot;

/**
 * Controller of the brick.
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
public class Brick {

    private static NXTConnection connection = null;
    private static InputStream in;
    private static OutputStream out;
    private static DifferentialPilot pilot;
    private static SensorListener sensorListener;
    private static ButtonListener buttonListener;
    private static Thread listenerThread;
    /*
     * Opcodes. The opcode occupies the last 2 bytes, its argument the first 2
     * They are extracted by bitmasking
     */
    public final static int DO_NOTHING = 0X00;
    public final static int QUIT = 0X01;
    public final static int FORWARDS = 0X02;
    public final static int BACKWARDS = 0X03;
    public final static int STOP = 0X04;
    public final static int KICK = 0X05;
    public final static int ROTATE = 0x06;
    
    public final static int ROTATERIGHT = 0x07;
    public final static int ROTATELEFT = 0x08;
	
	public final static int ROTATETO = 0xa;
	
    public final static int ARC = 0x09;
    
    public final static int SLOW = 180;
    public final static int MEDIUM = 360;
    public final static int FAST = 720;
    
    public final static int OPCODE = 0X000000FF;
    public final static int ARG = 0XFFFFFF00;
    
    public final static int COLLISION = 0xa0;
    public final static int OK = 0xa1;
    public final static int SENSING = 0xa2;
	public final static int SENSINGENDED = 0xa3;
    
    public final static char LEFT = 'l';
    public final static char RIGHT = 'r';
    
    public final static double trackWidth = 15.6;
    public final static double wheelDiameter = 6;
    public static FileOutputStream outLog = null;
    public static boolean connected = false;
    
    /**
     * Loops continuously until told to quit, which causes the brick to reboot.
     */
    public static void main(String[] args) {
        
        // Create the log file
        File file = new File("log.dat");
        try {
            if (!file.exists()) {
                    file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            } catch (IOException ex) {
        }
        try {
            outLog = new FileOutputStream(file, true);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to create output stream");
            System.exit(1);
        }
        
        // Set up robotics objects
        buttonListener = new ButtonListener();
        Button.ESCAPE.addButtonListener(buttonListener);
        Button.ENTER.addButtonListener(buttonListener);
        pilot = new DifferentialPilot(wheelDiameter, trackWidth , Motor.A, Motor.B);
//		pilot = new CompassPilot(new CompassHTSensor(SensorPort.S3), (float)wheelDiameter, (float)trackWidth , Motor.A, Motor.B);
        sensorListener = new SensorListener();
        listenerThread = new Thread(sensorListener);
        listenerThread.start();
        waitForConnection();
        if (!connected) {
            return;
        }
        int n = DO_NOTHING;

        while (n != QUIT) {
            try {
                // Read in 4 bytes from the bluetooth stream
                byte[] byteBuffer = new byte[4];
                in.read(byteBuffer);
                // Convert the 4 bytes to an integer and mask out the opcode and args
                n = byteArrayToInt(byteBuffer);
                int opcode = n & OPCODE;
                int arg = n & ARG;
                switch (opcode) {

                    case FORWARDS:
                        forwards(arg >> 8);
                        break;

                    case BACKWARDS:
                        backwards(arg >> 8);
                        break;

                    case ROTATE:
                        rotate(arg >> 8);
                        break;

                    case ROTATELEFT:
                        rotateLeft(arg >> 8);
                        break;
                        
                    case ROTATERIGHT:
                        rotateRight(arg >> 8);
                        break;
						
					case ROTATETO:
						rotateTo(arg >> 8);
						break;
						
                    case ARC:
                        arc(arg >> 8);
                        break;
						
                    case STOP:
                        stop();
                        break;

                    case KICK:
                        kick();
                        break;

                    case QUIT: // close connection
                        break;

                }

                // respond to say command was acted on
				Thread.sleep(200);
                sendMessage(OK);
                if (n == QUIT || !isConnected()) {
                    closeConnection();
                    outLog.close();
                }
            } catch (Throwable e) {
                logToFile(outLog, e.toString());
                n = QUIT;
            }
			if (!listenerThread.isAlive()) {
				Sound.playTone(1500, 1000);
			}
        }
        listenerThread.interrupt();
    }
    
    /**
     * Wait for a connection, and give feedback on status.
     */
    public static void waitForConnection() {
        LCD.drawString("Waiting for connection", 0, 0);
        Sound.playTone(2000, 1000);
        connection = Bluetooth.waitForConnection();
        if (getConnection().getClass() == BTConnection.class) {
            LCD.clear();
            in = getConnection().openInputStream();
            out = getConnection().openOutputStream();
            setConnected(true);
            LCD.drawString("Connected", 0, 0);
            logToFile(outLog, "Connected");
        }
    }
    
    /**
     * Close the connection, and give feedback on status.
     */
    public static void closeConnection() {
        LCD.clear();
        LCD.drawString("Quitting", 0, 0);
        try {
            in.close();
            out.close();
            connection.close();
            LCD.clear();

        } catch (IOException e) {
            logToFile(outLog, e.toString());
        }
        setConnected(false);
    }
    
    /**
     * The status of the connection.
     * 
     * @return True when connected, false otherwise.
     */
    public static boolean isConnected() {
        return connected;
    }
    
    
    /**
     * Update the status of the connection.
     * 
     * @param newConnected True if connected, false otherwise. 
     */
    private static void setConnected(boolean newConnected) {
        connected = newConnected;
    }
    
    /**
     * Send an opcode message to the computer.
     * 
     * @param message An opcode.
     */
    public synchronized static void sendMessage(int message) {
        if (isConnected()) {
            try {
                out.write(intToByteArray(message));
                out.flush();
            } catch (IOException e) {
                logToFile(outLog, e.toString());
            }
        }
    }
    
    /** 
     * Write a message to the log file. This will be prepended with the current time.
     * @param outLog The log file to write to.
     * @param message The message to write.
     */
    public synchronized static void logToFile(FileOutputStream outLog, String message) {
//        DataOutputStream dataOut = new DataOutputStream(outLog);
//        try { 
//            // write
//            dataOut.writeChars(System.currentTimeMillis() + " " + message+"\n");
//
//            outLog.flush(); // flush the buffer and write the file
//			dataOut.close();
//        } catch (IOException e) {
//            System.err.println("Failed to write to output stream");
//        }
		
    }
    /**
     * Send the robot forwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void forwards(int speed) {
        pilot.forward();
        pilot.setRotateSpeed(speed);
    }
    
    /**
     * Send the robot backwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void backwards(int speed) {
        pilot.backward();
        pilot.setRotateSpeed(speed);
    }
    
    /**
     * Pivot the robot on a point. Positive anti-clockwise, negative clockwise.
     */
    public static void rotate(int angle) {
        int finalAngle = angle - 180;
        double factor;
        if (finalAngle < 0) {
            factor = 1;
        } else {
            factor = 1;
        }
        pilot.rotate((finalAngle) * factor);
		pilot.setRotateSpeed(180);
    }
    
    public static void rotateRight(int speed) {
        Motor.A.backward();
		Motor.B.forward();
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
    }
    
    public static void rotateLeft(int speed) {
        Motor.B.backward();
		Motor.A.forward();
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
    }
	
	public static void rotateTo(int heading) {
		UltrasonicSensor compass = new UltrasonicSensor(SensorPort.S3);
		int currentHeading = compass.getDistance() * 2;
		int angle = currentHeading - heading;
		if (angle > 0) {
			pilot.rotateLeft();
		} else {
			pilot.rotateRight();
		}
		pilot.setRotateSpeed(50);
		while (true) {
			if (Math.abs(compass.getDistance() * 2 - heading) < 10) {
				break;
			}
		}
		pilot.stop();
	}
	
    public static void arc(int angle) {
	
        pilot.arcForward(angle);
			pilot.setRotateSpeed(SLOW / 2);
    }
    
    /**
     * Stop movement activity.
     */
    public static void stop() {
        pilot.stop();
    }
    
    /**
     * Kick the ball (if we are lucky).
     */
    public static void kick() {
        sensorListener.setKicking(true);
        Motor.C.setSpeed(720);
        Motor.C.rotate(-25);
        Motor.C.rotate(25);
        Motor.C.stop();
        sensorListener.setKicking(false);
    }
    
    /**
     * Reverse away from an obstruction.
     * @param The direction to pivot. 
     */
    public static void backOff(char direction) {
        pilot.stop();
        pilot.backward();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            
        }
        pilot.stop();
        sendMessage(OK);
    }

    public synchronized static NXTConnection getConnection() {
        return connection;
    }
    
    
    /**
     * Convert a byte array to an integer.
     * 
     * @param b An array of bytes.
     * @return An integer.
     */
    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        // For each position in the array
        for (int i = 0; i < 4; i++) {
            // Shift by an amount corresponding to our current position the byte at that position
            int shift = (4 - 1 - i) * 8;
            value += ((b[i] & 0xFF) << shift);
        }
        return value;
    }
    /**
     * Convert an integer to a byte array.
     * 
     * @param in An integer that can be stored within 4 bytes.
     * @return A byte array.
     */
    public static byte[] intToByteArray(int in) {
        byte[] b = new byte[4];
        // For each position in the array
        for (int i = 0; i < 4; i++) {
            // Shift the integer by out current position in the array
            int shift = (3 - i) * 8;
            b[i] = (byte) ((in & 0xFF) >> shift);
        }
        return b;
    }
}

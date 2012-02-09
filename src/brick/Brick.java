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
    
    public final static int SLOW = 0X000100;
    public final static int MEDIUM = 0X000200;
    public final static int FAST = 0X000300;
    
    public final static int OPCODE = 0X000000FF;
    public final static int ARG = 0XFFFFFF00;
    
    public final static int COLLISION = 0xa0;
    public final static int OK = 0xa1;
    
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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                
            }
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
                logToFile(outLog, "Message " + Integer.toBinaryString(n));
                int opcode = n & OPCODE;
                logToFile(outLog, "Opcode " + opcode);
                int arg = n & ARG;
                logToFile(outLog, "Arg " + arg);
                switch (opcode) {

                    case FORWARDS:
                        forwards(arg);
                        break;

                    case BACKWARDS:
                        backwards(arg);
                        break;

                    case ROTATE:
                        rotate(arg >> 8);
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
                sendMessage(OK);
                if (n == QUIT || !isConnected()) {
                    closeConnection();
                    outLog.close();
                }
            } catch (Throwable e) {
                logToFile(outLog, e.toString());
                n = QUIT;
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
        if (connection.getClass() == BTConnection.class) {
            LCD.clear();
            in = connection.openInputStream();
            out = connection.openOutputStream();
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
    public static void sendMessage(int message) {
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
    public static void logToFile(FileOutputStream outLog, String message) {
        DataOutputStream dataOut = new DataOutputStream(outLog);
        try { 
            // write
            dataOut.writeChars(System.currentTimeMillis() + " " + message+"\n");

            outLog.flush(); // flush the buffer and write the file
        } catch (IOException e) {
            System.err.println("Failed to write to output stream");
        }
    }
    /**
     * Send the robot forwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void forwards(int speed) {
        logToFile(outLog, "Forwards");
        pilot.forward();
        if (speed == SLOW) {
            pilot.setRotateSpeed(180);
        } else if (speed == MEDIUM) {
            pilot.setRotateSpeed(360);
        } else if (speed == FAST) {
            pilot.setRotateSpeed(720);
        }
    }
    
    /**
     * Send the robot backwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void backwards(int speed) {
        logToFile(outLog, "Backwards");
        pilot.backward();
        if (speed == SLOW) {
            pilot.setRotateSpeed(180);
        } else if (speed == MEDIUM) {
            Sound.playTone(2000, 500);
            pilot.setRotateSpeed(360);
        } else if (speed == FAST) {
            pilot.setRotateSpeed(720);
        }
    }
    
    /**
     * Pivot the robot on a point. Positive anti-clockwise, negative clockwise.
     */
    public static void rotate(int angle) {
        logToFile(outLog, "Rotate");
        int finalAngle = angle - 180;
        double factor;
        if (finalAngle < 0) {
            factor = 1.65;
        } else {
            factor = 1.35;
        }
        pilot.rotate((finalAngle) * factor);
    }
    
    /**
     * Stop movement activity.
     */
    public static void stop() {
        logToFile(outLog, "Stop");
        pilot.stop();
    }
    
    /**
     * Kick the ball (if we are lucky).
     */
    public static void kick() {
        sensorListener.setKicking(true);
        logToFile(outLog, "Kick");
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
        logToFile(outLog, "Backoff");
        pilot.stop();
        pilot.backward();
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            
        }
        pilot.stop();
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
            value += ((b[i] & 0xFFFFFFFF) << shift) & 0x00000000FFFFFFFF;
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

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
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.TouchFeatureDetector;
import lejos.nxt.Sound;

/**
 * Example leJOS Project with an ant build file
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
    // NXT Opcodes
    public final static int DO_NOTHING = 0X00;
    public final static int QUIT = 0X01;
    public final static int FORWARDS = 0X02;
    public final static int BACKWARDS = 0X03;
    public final static int STOP = 0X04;
    public final static int KICK = 0X05;
    public final static int ROTATELEFT = 0x06;
    public final static int ROTATERIGHT = 0x07;
    public final static int COLLISION = 0xa0;
    public final static int OK = 0xa1;
    public final static double trackWidth = 10.9;
    public final static double wheelDiameter = 6;
    public static FileOutputStream outLog = null;

    public static void main(String[] args) {
        
        File file = new File("log.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                // TODO  
            }
        }
        try {
            outLog = new FileOutputStream(file, true);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to create output stream");
            System.exit(1);
        }

        //logToFile(outLog, "Testing");

        pilot = new DifferentialPilot(wheelDiameter, trackWidth , Motor.A, Motor.B);
        sensorListener = new SensorListener();
        waitForConnection();
        int n = DO_NOTHING;
        //logToFile("Testing2");

        while (n
                != QUIT) {
            try {
                byte[] byteBuffer = new byte[4];
                in.read(byteBuffer);

                n = byteArrayToInt(byteBuffer);
                int opcode = ((n << 24) >> 24);
                switch (opcode) {

                    case FORWARDS:
                        pilot.setRotateSpeed(720);
                        pilot.forward();
                        //logToFile(outLog, "forwards");
                        //Motor.A.forward();
                        //Motor.B.forward();
                        break;

                    case BACKWARDS:
                        pilot.setRotateSpeed(720);
                        pilot.backward();
                        //logToFile(outLog, "backwards");
                        //Motor.A.backward();
                        //Motor.B.backward();
                        break;

                    case ROTATELEFT:
                        pilot.rotateLeft();
                        break;

                    case ROTATERIGHT:
                        pilot.rotateRight();
                        break;

                    case STOP:
                        stop();
                        //Motor.A.stop();
                        //Motor.B.stop();
                        //Motor.C.stop();
                        break;

                    case KICK:
                        Motor.C.setSpeed(720);
                        Motor.C.rotate(25);
                        Motor.C.rotate(-25);
                        Motor.C.stop();
                        break;

                    case QUIT: // close connection
                        break;

                }

                // respond to say command was acted on
                sendMessage(OK);
                if (n == QUIT) {
                    closeConnection();
                    outLog.close();
                }
            } catch (Throwable e) {
                logToFile(outLog, System.currentTimeMillis() + " " + e.toString());
                n = QUIT;
            }
        }

    }

    public static void waitForConnection() {
        LCD.drawString("Waiting for connection", 0, 0);
        Sound.playTone(2000, 1000);
        connection = Bluetooth.waitForConnection();
        LCD.clear();
        in = connection.openInputStream();
        out = connection.openOutputStream();
        LCD.drawString("Connected", 0, 0);
    }

    public static void closeConnection() {
        LCD.clear();
        LCD.drawString("Quitting", 0, 0);
        try {
            in.close();
            out.close();
            connection.close();
            LCD.clear();

        } catch (IOException e) {
            logToFile(outLog, System.currentTimeMillis() + " " + e.toString());
        }

    }

    public static void sendMessage(int message) {
        try {
            out.write(intToByteArray(message));
            out.flush();
        } catch (IOException e) {
            logToFile(outLog, System.currentTimeMillis() + " " + e.toString());
        }
    }

    public static void logToFile(FileOutputStream outLog, String message) {
        DataOutputStream dataOut = new DataOutputStream(outLog);
        try { // write
            dataOut.writeChars(message+"\n");

            outLog.flush(); // flush the buffer and write the file
        } catch (IOException e) {
            System.err.println("Failed to write to output stream");
        }
    }
    
    public static void stop() {
        pilot.stop();
    }
    
    public static void backOff() {
        pilot.setRotateSpeed(300);
        pilot.backward();
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            
        }
        pilot.rotate(300);
        pilot.setRotateSpeed(720);
    }
    
    /**
     * Returns an integer from a byte array
     */
    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static byte[] intToByteArray(int in) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            b[i] = (byte) ((in & 0xFF) >> shift);
        }
        return b;
    }
}

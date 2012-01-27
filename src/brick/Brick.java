package brick;

import java.io.IOException;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.InputStream;
import java.io.OutputStream;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Example leJOS Project with an ant build file
 *
 */
public class Brick {

    private static NXTConnection connection = null;
    private static InputStream in;
    private static OutputStream out;
    private static DifferentialPilot pilot;
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
    
    public final static double trackWidth = 10.9;
    public final static double wheelDiameter = 6;
    
    public static void main(String[] args) {
        pilot = new DifferentialPilot(wheelDiameter, trackWidth , Motor.A, Motor.B);
        waitForConnection();
        int n = DO_NOTHING;

        while (n != QUIT) {
            try {
                byte[] byteBuffer = new byte[4];
                in.read(byteBuffer);

                n = byteArrayToInt(byteBuffer);
                int opcode = ((n << 24) >> 24);
                switch (opcode) {

                    case FORWARDS:
                        pilot.setRotateSpeed(720);
                        pilot.forward();
                        //Motor.A.forward();
                        //Motor.B.forward();
                        break;

                    case BACKWARDS:
                        pilot.setRotateSpeed(720);
                        pilot.backward();
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
                        pilot.stop();
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
                out.write('o');
                out.flush();
                if (n == QUIT) {
                    closeConnection();
                }
            } catch (Throwable e) {
                n = QUIT;
            }
        }

    }

    public static void waitForConnection() {
        LCD.drawString("Waiting for connection", 0, 0);
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

        } catch (IOException ex) {
            System.err.println("Error " + ex.toString());
        }

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
}

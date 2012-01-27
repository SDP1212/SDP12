package brick;

import java.io.IOException;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Example leJOS Project with an ant build file
 *
 */
public class brick {

    private static NXTConnection connection = null;
    private static InputStream in;
    private static OutputStream out;
    // NXT Opcodes
    private final static int DO_NOTHING = 0X00;
    private final static int QUIT = 0X01;
    private final static int FORWARDS = 0X02;
    private final static int BACKWARDS = 0X03;
    private final static int STOP = 0X04;
    private final static int KICK = 0X05;

    public static void main(String[] args) {
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
                        Motor.A.forward();
                        Motor.B.forward();

                    case BACKWARDS:
                        Motor.A.backward();
                        Motor.B.backward();

                    case STOP:
                        Motor.A.stop();
                        Motor.B.stop();
                        Motor.C.stop();
                        break;

                    case KICK:
                        Motor.C.setSpeed(720);
                        Motor.C.rotate(60);
                        Motor.C.rotate(-60);


                    case QUIT: // close connection
                        closeConnection();
                        break;

                }

                // respond to say command was acted on
                out.write('o');
                out.flush();

            } catch (IOException e) {
                LCD.clear();
                LCD.drawString("Exception " + e.toString(), 0, 0);
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

    public static void closeConnection() throws IOException {
        LCD.clear();
        LCD.drawString("Quitting", 0, 0);
        in.close();
        out.close();
        connection.close();
        LCD.clear();
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

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

    private static SensorListener sensorListener;
    private static ButtonListener buttonListener;
    private static Thread listenerThread;
	private static Thread commThread;
	private static Thread movementThread;
	private static boolean quit = false;
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
    
    /**
     * Loops continuously until told to quit, which causes the brick to reboot.
     */
    public static void main(String[] args) {
        // Set up robotics objects
        buttonListener = new ButtonListener();
        Button.ESCAPE.addButtonListener(buttonListener);
        Button.ENTER.addButtonListener(buttonListener);
        sensorListener = new SensorListener();
        listenerThread = new Thread(sensorListener);
        listenerThread.start();
		commThread = new Thread(Communication.getInstance());
		commThread.start();
		movementThread = new Thread(Movement.getInstance());
		movementThread.start();
		while (!quit) {
			
		}
		commThread.interrupt();
        listenerThread.interrupt();
		movementThread.interrupt();
		Logger.getInstance().close();
    }

    public static synchronized void quit() {
		quit = true;
	}
    
}

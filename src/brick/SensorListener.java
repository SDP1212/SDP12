package brick;

import lejos.nxt.*;

/**
 *
 * @author Matthew Jeffryes
 */

/**
 * A thread that listens for input from the sensors. 
 */
public class SensorListener implements Runnable {
    private TouchSensor touchSensorL;
    private TouchSensor touchSensorR;
    private boolean pressed = false;
    private boolean kicking = false;
    
    /**
     * Constructor to initialise the touch sensors
     */
    public SensorListener() {
        touchSensorL = new TouchSensor(SensorPort.S1);
        touchSensorR = new TouchSensor(SensorPort.S2);
        
    }
    /**
     * Block the obstruction detection during kicking.
     * 
     * @param k True if kicking, false otherwise.
     */
    public void setKicking(boolean k) {
        kicking = k;
    }

    public void run() {
        try {
            while(!Thread.interrupted()) {
                if (Communication.getInstance() != null) Communication.getInstance().sendMessage(Brick.SENSING);
                if (pressed) {
                    if (Communication.getInstance() != null) Communication.getInstance().sendMessage(Brick.COLLISION);
                    Movement.getInstance().backOff(Brick.LEFT);
                    pressed = false;
                } else if ((touchSensorL.isPressed() || touchSensorR.isPressed())) {
                    pressed = true;
                }
                /**
                 * It backs off if the sensors have been pressed for half a second.
                 */
                Thread.sleep(300);
            }
        } catch (Throwable ex) {
			Sound.buzz();
            return;
        } finally {
            if (Communication.getInstance() != null) Communication.getInstance().sendMessage(Brick.SENSINGENDED);
        }
    }


}

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
    
    /**
     * Constructor to initialise the touch sensors
     */
    public SensorListener() {
        touchSensorL = new TouchSensor(SensorPort.S1);
        touchSensorR = new TouchSensor(SensorPort.S2);
        
    }

    public void run() {
        try {
            while(!Thread.interrupted()) {
                if (pressed) {
                    Brick.sendMessage(Brick.COLLISION);
                    Brick.backOff(Brick.LEFT);
                    pressed = false;
                } else if (touchSensorL.isPressed() || touchSensorR.isPressed()) {
                    pressed = true;
                }
                /**
                 * It backs off if the sensors have been pressed for half a second.
                 */
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
                return;
        }
    }


}

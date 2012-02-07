package brick;

import lejos.nxt.*;
import lejos.robotics.objectdetection.*;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 *
 * @author Matthew Jeffryes
 */
public class SensorListener implements Runnable {
//    private TouchFeatureDetector touchSensorDL;
//    private TouchFeatureDetector touchSensorDR;
    private TouchSensor touchSensorL;
    private TouchSensor touchSensorR;
    private boolean pressed;
    public SensorListener() {
        touchSensorL = new TouchSensor(SensorPort.S1);
        touchSensorR = new TouchSensor(SensorPort.S2);
//        touchSensorDL = new TouchFeatureDetector(touchSensorL);
//        touchSensorDR = new TouchFeatureDetector(touchSensorR);
//        touchSensorDL.addListener(this);
//        touchSensorDR.addListener(this);
        
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
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
                return;
        }
    }


}

package brick;

import lejos.nxt.*;
import lejos.robotics.objectdetection.*;

/**
 *
 * @author Matthew Jeffryes
 */
public class SensorListener implements FeatureListener {
    private TouchFeatureDetector touchSensorL;
    private TouchFeatureDetector touchSensorR;
    public SensorListener() {
        touchSensorL = new TouchFeatureDetector(new TouchSensor(SensorPort.S1));
        touchSensorR = new TouchFeatureDetector(new TouchSensor(SensorPort.S2));
        touchSensorL.addListener(this);
        touchSensorR.addListener(this);
    }

    public void featureDetected(Feature ftr, FeatureDetector fd) {
        Brick.sendMessage(Brick.COLLISION);
        if (fd == touchSensorL) {
            Brick.backOff(Brick.LEFT);
        } else {
            Brick.backOff(Brick.RIGHT);
        }
    }
}

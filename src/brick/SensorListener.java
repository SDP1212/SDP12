package brick;

import lejos.nxt.*;
import lejos.robotics.objectdetection.*;

/**
 *
 * @author Matthew Jeffryes
 */
public class SensorListener implements FeatureListener {

    public SensorListener() {
        TouchFeatureDetector touchSensor = new TouchFeatureDetector(new TouchSensor(SensorPort.S1));
        touchSensor.addListener(this);
    }

    public void featureDetected(Feature ftr, FeatureDetector fd) {
        Brick.sendMessage(Brick.COLLISION);
        Brick.backOff();
    }
}

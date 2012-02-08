/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

import computer.simulator.PixelCoordinates;
import vision.WorldState;
/**
 *
 * @author s0907806
 */

/**
 * This class will get the ball coordinates and direct the robot towards it.
 */

public class GoToBall {
    
    private static WorldState worldState;
    
    public PixelCoordinates blue;
    public PixelCoordinates yellow;
    public PixelCoordinates ball;
    
    public GoToBall (boolean isBlue) {
        worldState = new WorldState();
        if (isBlue == true) {
            this.blue = findBlueRobot();
        } else {
            this.yellow = findYellowRobot();
        }
        this.ball = findBallLocation();
    }
    
    public PixelCoordinates findBallLocation () {
        PixelCoordinates loc = worldState.getBallCoordinates();
        return loc;
    }
    
    public PixelCoordinates findBlueRobot () {
        PixelCoordinates loc = worldState.getBlueRobotCoordinates();
        return loc;
    }
    
    public PixelCoordinates findYellowRobot () {
        PixelCoordinates loc = worldState.getYellowRobotCoordinates();
        return loc;
    }
    
    
}

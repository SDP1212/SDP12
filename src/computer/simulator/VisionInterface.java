/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public interface VisionInterface {
    
    /**
     * Provides coordinates for the four corners.
     * 
     * @return the coordinates for the four corners, starting with top-left and going clockwise until bottom-left
     */
    public PixelCoordinates[] getPitchCornerCoordinates();
    
    public PixelCoordinates getYellowRobotCoordinates();
    
    public Direction getYellowRobotOrientation();
    
    public PixelCoordinates getBlueRobotCoordinates();
        
    public Direction getBlueRobotOrientation();
    
    /**
     * Provides coordinates for the left goal
     * 
     * @return the coordinates for the left goal posts in the image
     */
    public PixelCoordinates[] getLeftGoalCoordinates();
    
    /**
     * Provides coordinates for the right goal
     * 
     * @return the coordinates for the right goal posts in the image
     */
    public PixelCoordinates[] getRightGoalCoordinates();
    
    public PixelCoordinates getBallCoordinates();
    
}

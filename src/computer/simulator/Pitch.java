/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.ai.AI;
import computer.ai.DumbAI;

/**
 *
 * @author Dimo Petroff
 */
public class Pitch {
    
    public static final short TARGET_LEFT_GOAL=0,TARGET_RIGHT_GOAL=1;
    
    //should be counted clockwise, starting at top-left
    private PixelCoordinates[] corners;
    
    public Robot robotinho;
    public Robot nemesis;
    
    public Ball ball;
    
    public Goal leftGoal,rightGoal;
    
    protected Pitch(PixelCoordinates[] corners){
        this.corners=corners;
    }
    
    /**
     * Puts Robotinho into the world, using real-world/pixel coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     * @param ai an AI implementation to use for the robot
     */
    protected void insertRobotinho(PixelCoordinates coordinates,Direction orientation, short colour, AI ai){
        
        this.robotinho=new Robot(ai, true, colour); // Real coordinates, so obviously real robot.
        
        this.robotinho.setPosition((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                             ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        
        this.robotinho.setOrientation(orientation);
    }
    
    /**
     * Puts Robotinho into the world, using internal coordinates.
     * Assumes the robot is virtual!
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     * @param ai an AI implementation to use for the robot
     */
    protected void insertRobotinhoInternal(Coordinates coordinates,Direction orientation, short colour, AI ai){
        insertRobotinhoInternal(coordinates, orientation, false, colour, ai); // Not specified if robot is real, and using internal coordinates, so assume it's virtual.
    }
    
    /**
     * Puts Robotinho into the world, using internal coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param real specifies if this robot is backed by real hardware or should be simulated
     * @param colour what colour is it
     * @param ai an AI implementation to use for the robot
     */
    protected void insertRobotinhoInternal(Coordinates coordinates,Direction orientation, boolean real, short colour, AI ai){
        
        this.robotinho=new Robot(ai, real, colour);
        
        this.robotinho.setPosition(coordinates.getX(),coordinates.getY());
        
        this.robotinho.setOrientation(orientation);
    }
    
    /**
     * Puts the enemy robot into the world, using real-world/pixel coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     */
    protected void insertNemesis(PixelCoordinates coordinates,Direction orientation, short colour){
        
        this.nemesis=new Robot(new DumbAI(this), true, colour); // Real coordinates, so real robot, and DumbAI is best.
        
        this.nemesis.setPosition((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                  ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        
        this.nemesis.setOrientation(orientation);
    }
    
    /**
     * Puts the enemy robot into the world, using internal coordinates.
     * Assumes nemesis is virtual.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     * @param ai an AI implementation to use for nemesis
     */
    protected void insertNemesisInternal(Coordinates coordinates,Direction orientation, short colour, AI ai){
        insertNemesisInternal(coordinates, orientation, false, colour, ai);  // Not specified if robot is real, and using internal coordinates, so assume it's virtual.
    }
    
    /**
     * Puts the enemy robot into the world, using internal coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param real specifies if nemesis is backed by real hardware or should be simulated
     * @param colour what colour is it
     * @param ai an AI implementation to use for nemesis
     */
    protected void insertNemesisInternal(Coordinates coordinates,Direction orientation, boolean real, short colour, AI ai){
        this.nemesis=new Robot(ai, real, colour);
        
        this.nemesis.setPosition(coordinates.getX(),coordinates.getY());
        
        this.nemesis.setOrientation(orientation);
    }
    
    /**
     * Puts the ball into the world, using real-world/pixel coordinates.
     * 
     * @param coordinates where to put it
     */
    protected void insertBall(PixelCoordinates coordinates){
        this.ball=new Ball((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                            ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()),
                            true);
    }
    
    /**
     * Puts the ball into the world, using internal coordinates.
     * Assumes virtual ball.
     * 
     * @param coordinates where to put it
     */
    protected void insertBallInternal(Coordinates coordinates) {
        insertBallInternal(coordinates,false); // Not specified if ball is real, and using internal coordinates, so assume it's virtual.
    }
    
    /**
     * Puts the ball into the world, using internal coordinates.
     * 
     * @param coordinates where to put it
     * @param real specifies if ball is real or if it needs to be simulated
     */
    protected void insertBallInternal(Coordinates coordinates, boolean real) {
        this.ball=new Ball(coordinates, real);
    }
    
    /**
     * Puts the goals into the world, using real-world/pixel coordinates.
     * 
     * @param left the top and bottom post coordinates for the left goal
     * @param right the top and bottom post coordinates for the right goal
     * @param target designates which goal should be targeted for attack
     */
    protected void insertGoals(PixelCoordinates[] left, PixelCoordinates[] right, short target){
        
        for(PixelCoordinates coordinates : new PixelCoordinates[] {left[0],left[1],right[0],right[1]})
            if(!coordinates.isBarrelCorrected()){
                if(javax.swing.JOptionPane.NO_OPTION==javax.swing.JOptionPane.showConfirmDialog(null, "Barrel-distorted coordinates may induce undesirable side-effects such as itching, sweating, irritation, and/or death.\nTo put it more clearly, this makes it impossible to predic objects' motion so no attempt will be made. The robot will only know where things are (approximately), the direction they're facing and the current speed. Considering motion would not be in a straight line, the latter two probably won't be too helpful but it's better than nothing.\n\nWould you still like to continue? Click \"Yes,\" I dare you. No, I double-dare you.", "Don't panic!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE))
                    throw new Error("The operator has chickened out of using barrel-distorted coordinates. But this is a good thing since those are NOT optimally supported by the (simulator) system!");
                break;
            }
            else if(!coordinates.isOrientationCorrected()){
                if(javax.swing.JOptionPane.NO_OPTION==javax.swing.JOptionPane.showConfirmDialog(null, "The provided coordinates may include a slight rotational misalignment of the table. This should not reduce performance in any significant way. You should carry on, ignoring this message.\n\nWould you like to continue?", "Do NOT panic!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE))
                    throw new Error("The operator has chickened out of using a misaligned table... What a shame.");
                break;
            }
        
        this.leftGoal=new Goal((((float)left[0].getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                ((float)left[0].getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()),
                               (((float)left[1].getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                ((float)left[1].getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        
        this.rightGoal=new Goal((((float)right[0].getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                 ((float)right[0].getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()),
                                (((float)right[1].getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                 ((float)right[1].getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        
        switch(target){
            case TARGET_LEFT_GOAL:{
                leftGoal.setTarget();
                break;
            }
            case TARGET_RIGHT_GOAL:{
                rightGoal.setTarget();
                break;
            }
            default:{
                leftGoal.setTarget();
                break;
            }
        }
    
    }
    
    /**
     * Updates the state of Robotinho with real-world data.
     * 
     * @param coordinates
     * @param orientation 
     */
    protected void updateRobotinho(PixelCoordinates coordinates, Direction orientation) {
        this.robotinho.setPosition((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                             ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        this.robotinho.setOrientation(orientation);
    }
    
    /**
     * Updates the state of Nemesis with real-world data.
     * 
     * @param coordinates
     * @param orientation 
     */
    protected void updateNemesis(PixelCoordinates coordinates, Direction orientation) {
        this.nemesis.setPosition((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                                  ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
        this.nemesis.setOrientation(orientation);
    }

    /**
     * Updates the state of the ball with real-world data.
     * 
     * @param coordinates where to put it
     */
    protected void updateBall(PixelCoordinates coordinates) {
        this.ball.setPosition((((float)coordinates.getX()-corners[0].getX())/(corners[1].getX()-corners[0].getX()))*2,
                               ((float)coordinates.getY()-corners[0].getY())/(corners[3].getY()-corners[0].getY()));
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.ai.DumbAI;
import computer.control.ControlInterface;

/**
 * Used to represent the entire environment. Provides methods for populating and
 * updating it.
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
     * @param ai the class of AI to use for Robotinho
     * @param control the ControlInterface through which to control the brick
     */
    protected void insertRobotinho(PixelCoordinates coordinates,Direction orientation, short colour, Class ai, ControlInterface control){
        
        this.robotinho=new Robot(ai, control, this, true, colour); // Real coordinates, so obviously real robot.
        
        this.robotinho.setPosition(convertX(coordinates),convertY(coordinates));
        
        this.robotinho.setOrientation(orientation);
    }
    
    /**
     * Puts Robotinho into the world, using internal coordinates.
     * Assumes the robot is virtual!
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     * @param ai the class of AI to use for Robotinho
     * @param control the ControlInterface through which to control the brick
     */
    protected void insertRobotinhoInternal(Coordinates coordinates,Direction orientation, short colour, Class ai, ControlInterface control){
        insertRobotinhoInternal(coordinates, orientation, false, colour, ai, control); // Not specified if robot is real, and using internal coordinates, so assume it's virtual.
    }
    
    /**
     * Puts Robotinho into the world, using internal coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param real specifies if this robot is backed by real hardware or should be simulated
     * @param colour what colour is it
     * @param ai the class of AI to use for Robotinho
     * @param control the ControlInterface through which to control the brick
     */
    protected void insertRobotinhoInternal(Coordinates coordinates,Direction orientation, boolean real, short colour, Class ai, ControlInterface control){
        
        this.robotinho=new Robot(ai, control, this, real, colour);
        
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
        
        this.nemesis=new Robot(DumbAI.class, null, this, true, colour); // Real coordinates, so real robot, and DumbAI is best.
        
        this.nemesis.setPosition(convertX(coordinates),convertY(coordinates));
        
        this.nemesis.setOrientation(orientation);
    }
    
    /**
     * Puts the enemy robot into the world, using internal coordinates.
     * Assumes nemesis is virtual.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param colour what colour is it
     * @param ai the class of AI to use for Nemesis
     */
    protected void insertNemesisInternal(Coordinates coordinates,Direction orientation, short colour, Class ai){
        insertNemesisInternal(coordinates, orientation, false, colour, ai);  // Not specified if robot is real, and using internal coordinates, so assume it's virtual.
    }
    
    /**
     * Puts the enemy robot into the world, using internal coordinates.
     * 
     * @param coordinates where to put it
     * @param orientation where it's facing
     * @param real specifies if nemesis is backed by real hardware or should be simulated
     * @param colour what colour is it
     * @param ai the class of AI to use for Nemesis
     */
    protected void insertNemesisInternal(Coordinates coordinates,Direction orientation, boolean real, short colour, Class ai){
        this.nemesis=new Robot(ai, null, this, real, colour);
        
        this.nemesis.setPosition(coordinates.getX(),coordinates.getY());
        
        this.nemesis.setOrientation(orientation);
    }
    
    /**
     * Puts the ball into the world, using real-world/pixel coordinates.
     * 
     * @param coordinates where to put it
     */
    protected void insertBall(PixelCoordinates coordinates){
        this.ball=new Ball(convertX(coordinates), convertY(coordinates), true);
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
        
        this.leftGoal=new Goal(convertX(left[0]),convertY(left[0]),
                               convertX(left[1]),convertY(left[1]));
        
        this.rightGoal=new Goal(convertX(right[0]),convertY(right[0]),
                                convertX(right[1]),convertY(right[1]));
        
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
    protected void updateRobotinho(PixelCoordinates coordinates, Direction orientation, long timeDeltaInMilliseconds) {
        this.robotinho.setPosition(convertX(coordinates),convertY(coordinates));
        this.robotinho.setOrientation(orientation);
        this.robotinho.updateVelocity(timeDeltaInMilliseconds);
    }
    
    /**
     * Updates the state of Nemesis with real-world data.
     * 
     * @param coordinates
     * @param orientation 
     */
    protected void updateNemesis(PixelCoordinates coordinates, Direction orientation, long timeDeltaInMilliseconds) {
        this.nemesis.setPosition(convertX(coordinates),convertY(coordinates));
        this.nemesis.setOrientation(orientation);
        this.nemesis.updateVelocity(timeDeltaInMilliseconds);
    }

    /**
     * Updates the state of the ball with real-world data.
     * 
     * @param coordinates where to put it
     */
    protected void updateBall(PixelCoordinates coordinates, long timeDeltaInMilliseconds) {
        this.ball.setPosition(convertX(coordinates),convertY(coordinates));
        this.ball.updateVelocity(timeDeltaInMilliseconds);
    }
    
    private double convertX(PixelCoordinates coordinates){
        return ((double)coordinates.getX()-corners[3].getX())/(corners[2].getX()-corners[3].getX())*2;
    }
    
    private double convertY(PixelCoordinates coordinates){
        return (coordinates.getY()-(double)corners[3].getY())/(corners[0].getY()-corners[3].getY());
    }
    
    public Coordinates[] getCorners() {
        return new Coordinates[] {new Coordinates(0.0, 1.0),
                                  new Coordinates(2.0, 1.0),
                                  new Coordinates(2.0, 0.0),
                                  new Coordinates(0.0, 0.0)};
    }
    
    public Line getNorthWall(){
        return new Line(getCorners()[0],getCorners()[1]);
    }
    
    public Line getEastWall(){
        return new Line(getCorners()[1],getCorners()[2]);
    }
    
    public Line getSouthWall(){
        return new Line(getCorners()[2],getCorners()[3]);
    }
    
    public Line getWestWall(){
        return new Line(getCorners()[3],getCorners()[0]);
    }
    
    public Line[] getWalls(){
        return new Line[] {getNorthWall(),getEastWall(),
                           getSouthWall(),getWestWall()};
    }

    public Goal getTargetGoal() {
        if(leftGoal.isTarget())
            return leftGoal;
        else if(rightGoal.isTarget())
            return rightGoal;
        else return null;
    }

	
	public Goal getEnemyTargetGoal () {
		if(!leftGoal.isTarget())
            return leftGoal;
        else if(!rightGoal.isTarget())
            return rightGoal;
        else return null;
	}
		
		public Coordinates getCentreSpot() {
				return new Coordinates(0.5, 1);
		}
    
    public Goal getLeftGoal(){
        return leftGoal;
    }
    
    public Goal getRightGoal(){
        return rightGoal;
    }
    
}

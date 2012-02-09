/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.ai.AI;
import computer.control.ControlInterface;

/**
 * Used to represent a robot within the world with all its properties and
 * provides methods for taking actions.
 * 
 * @author Dimo Petroff
 */
public class Robot extends SimulatableObject implements ControlInterface{
    
    public static final short YELLOW_PLATE=0,BLUE_PLATE=1;
    private Direction orientation;
    private short colour;
    protected AI brain;
    
    /**
     * Allocates a Robot object representing a robot in its entirety.
     * 
     * @param ai the class of AI to use for this robot
     * @param pitch a reference to the pitch to be used with the AI
     * @param real determines if this robot is real or simulated
     * @param colour determines the colour of this robot's plate;
     * Should be either Robot.YELLOW_PLATE or Robot.BLUE_PLATE
     */
    protected Robot(Class ai, Pitch pitch, Boolean real, short colour){
        this.real=real;
        this.colour=colour;
        try{
            this.brain=(AI)ai.getConstructor(Pitch.class,Robot.class).newInstance(pitch,this);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            throw new Error("FATAL ERROR: Initialization of AI failed. Probably caused by: "+(ai==null ? "null" : ai.getName())+" is not a valid AI subclass - look at System.err for more details.");
        }
    }
    
    protected void setOrientation(Direction orientation) {
        this.orientation=orientation;
    }

    public short getColour() {
        return this.colour;
    }

    public void forward(int speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void backward(int speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void kick() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rotateLeft() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rotateRight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

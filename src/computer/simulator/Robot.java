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
    private ControlInterface control;
    private short colour;
    protected AI brain;
    
    /**
     * Allocates a Robot object representing a robot in its entirety.
     * 
     * @param ai the class of AI to use for this robot
     * @param control the ControlInterface through which to control the brick
     * @param pitch a reference to the pitch to be used with the AI
     * @param real determines if this robot is real or simulated
     * @param colour determines the colour of this robot's plate;
     * Should be either Robot.YELLOW_PLATE or Robot.BLUE_PLATE
     */
    protected Robot(Class ai, ControlInterface control, Pitch pitch, Boolean real, short colour){
        this.real=real;
        this.colour=colour;
        this.control=control;
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
    
    public Direction getOrientation(){
        System.out.println("Blue orientation: " + this.orientation.getDirectionDegrees());
        return this.orientation;
    }

    public short getColour() {
        return this.colour;
    }

    public void forward(int speed) {
        if(this.control!=null && this.isReal())control.forward(speed);
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public void backward(int speed) {
        if(this.control!=null && this.isReal())control.backward(speed);
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        if(this.control!=null && this.isReal())control.stop();
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public void kick() {
        if(this.control!=null && this.isReal())control.kick();
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rotate(double angle) {
        if(this.control!=null && this.isReal())control.rotate(angle);
        else throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void rotateRight() {
        if(this.control!=null && this.isReal())control.rotateRight();
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rotateLeft() {
        if(this.control!=null && this.isReal())control.rotateLeft();
        else throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCommState() {
        if(this.control!=null && this.isReal()) {
            return control.getCommState();
        }
        else throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}

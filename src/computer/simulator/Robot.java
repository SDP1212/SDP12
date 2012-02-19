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
public final class Robot extends SimulatableObject implements ControlInterface{
    
    public static final short YELLOW_PLATE=0,BLUE_PLATE=1;
    public static final double LINEAR_MOTION_SPEED=4/1.5; // 4 feet per 1.5 sec
    public static final double ROTARY_MOTION_SPEED=2*Math.PI/2; // 1 rotation per 2 seconds
    
    private Direction orientation;
    private ControlInterface control;
    private short colour;
    protected AI brain;
    private Pitch pitch;
    private int commState = READY;
    private int linMotionState=0;
    private double rotMotionState=0;
    private int arcMotionState=0;
    
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
        this.pitch=pitch;
        if(ai!=null)try{
            this.addAI((AI)ai.getConstructor(Pitch.class,Robot.class).newInstance(this.pitch,this));
            if(control!=null)control.addAI(this.brain);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            throw new Error("FATAL ERROR: Initialization of AI failed. Probably caused by: "+ai.getName()+" is not a valid AI subclass - look at System.err for more details.");
        }
    }
    
    protected void setOrientation(Direction orientation) {
        this.orientation=orientation;
    }
    
    public Direction getOrientation(){
        return this.orientation;
    }

    public short getColour() {
        return this.colour;
    }

    public void forward(int speed) {
        if(this.control!=null && this.isReal())control.forward(speed);
        else linMotionState=speed;
    }

    public void backward(int speed) {
        if(this.control!=null && this.isReal())control.backward(speed);
        else linMotionState=-speed;
    }

    public void stop() {
        if(this.control!=null && this.isReal())control.stop();
        else {
            linMotionState=0;
            rotMotionState=0.0;
            arcMotionState=0;
            commState=READY;
        }
    }

    public void kick() {
        if(this.control!=null && this.isReal())control.kick();
        else pitch.ball.kick(position, orientation);
    }

    public void rotate(double angle) {
        if(this.control!=null && this.isReal())control.rotate(angle);
        else {
            rotMotionState=angle;
            commState=WAITING;
        }
    }
    
    public void rotateRight() {
        if(this.control!=null && this.isReal())control.rotateRight();
        else {
            rotMotionState=Double.NEGATIVE_INFINITY;
            commState=WAITING;
        }
    }

    public void rotateLeft() {
        if(this.control!=null && this.isReal())control.rotateLeft();
        else {
            rotMotionState=Double.POSITIVE_INFINITY;
            commState=WAITING;
        }
    }
    
    public void arc(int radius) {
        if(this.control!=null && this.isReal())control.arc(radius);
        else {
            arcMotionState=radius;
        }
    }

    public int getCommState() {
        if(this.control!=null && this.isReal()) {
            return control.getCommState();
        }
        else return commState;
    }

    public void addAI(AI ai) {
        this.brain=ai;
    }
    
    protected void animate(long timeDeltaInMilliseconds){
        
        double ROT_FACTOR=ROTARY_MOTION_SPEED*timeDeltaInMilliseconds/1000;
        double MOV_FACTOR=LINEAR_MOTION_SPEED*0.25*timeDeltaInMilliseconds/1000;
        
        if(rotMotionState!=0){
            if(rotMotionState>0){
                if(rotMotionState>ROT_FACTOR){
                    orientation.alter(ROT_FACTOR);
                    rotMotionState-=ROT_FACTOR;
                }else{
                    orientation.alter(rotMotionState);
                    rotMotionState=0.0;
                    commState=READY;
                }
            }else{
                if(rotMotionState<-ROT_FACTOR){
                    orientation.alter(-ROT_FACTOR);
                    rotMotionState+=ROT_FACTOR;
                }else{
                    orientation.alter(rotMotionState);
                    rotMotionState=0.0;
                    commState=READY;
                }
            }
        }
        else if(linMotionState!=0){
            if(linMotionState>0){
                this.setPosition(position.getX()+(Math.cos(orientation.getDirectionRadians())*MOV_FACTOR),
                                 position.getY()+(Math.sin(orientation.getDirectionRadians())*MOV_FACTOR));
            }else{
                this.setPosition(position.getX()-(Math.cos(orientation.getDirectionRadians())*MOV_FACTOR),
                                 position.getY()-(Math.sin(orientation.getDirectionRadians())*MOV_FACTOR));
            }
        }
        else if(arcMotionState!=0){
            throw new UnsupportedOperationException("Simulated movement along an arc not implemented yet :(");
        }
    }
    
}

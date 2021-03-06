/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.ai.AI;
import computer.control.ControlInterface;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Used to represent a robot within the world with all its properties and
 * provides methods for taking actions.
 * 
 * @author Dimo Petroff
 */
public final class Robot extends SimulatableObject implements ControlInterface{
    
    public static final short YELLOW_PLATE=0,BLUE_PLATE=1;
    public static final double WHEEL_CIRCUMFERENCE=Math.PI*Coordinates.distanceFromCentimetres(8);
    public static final double WHEEL_SEPARATION=Coordinates.distanceFromCentimetres(16);
    
    private Direction orientation;
    private ControlInterface control;
    private short colour;
    protected AI brain;
    private Pitch pitch;
    private int commState = READY;
    private int linMotionState=0;
    private double linSpeed=4/1.5; // 4 feet per 1.5 sec
    private double rotMotionState=0;
    private double rotSpeed=2*Math.PI/2; // 1 rotation per 2 seconds
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
            System.err.println("Comm error" + e.getMessage());
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

    @Override
    public void forward(int speed) {
        if(this.control!=null && this.isReal())control.forward(speed);
        else{
            linMotionState=1;
            //TODO: look at DifferentialPilot and change below
            linSpeed=(speed/360.0)*WHEEL_CIRCUMFERENCE;
        }
    }

    @Override
    public void backward(int speed) {
        if(this.control!=null && this.isReal())control.backward(speed);
        else{
            linMotionState=-1;
            //TODO: look at DifferentialPilot and change below
            linSpeed=(speed/360.0)*WHEEL_CIRCUMFERENCE;
        }
    }

    @Override
    public void stop() {
        if(this.control!=null && this.isReal())control.stop();
        else {
            linMotionState=0;
            rotMotionState=0.0;
            arcMotionState=0;
            commState=READY;
        }
    }

    @Override
    public void kick() {
        if(this.control!=null && this.isReal())control.kick();
        else pitch.ball.kick(position, orientation);
    }

    @Override
    public void rotate(double angle) {
        if(this.control!=null && this.isReal())control.rotate(angle);
        else {
            rotMotionState=angle;
            rotSpeed=Math.toRadians(100);
            commState=WAITING;
        }
    }
    
    @Override
    public void rotateRight(int speed) {
        if(this.control!=null && this.isReal())control.rotateRight(speed);
        else {
            rotMotionState=Double.NEGATIVE_INFINITY;
            rotSpeed=(WHEEL_CIRCUMFERENCE*speed)/(180*WHEEL_SEPARATION); // Equivallent to: 2*Math.PI/((Math.PI*WHEEL_SEPARATION)/((speed/360.0)*WHEEL_CIRCUMFERENCE));
        }
    }

    @Override
    public void rotateLeft(int speed) {
        if(this.control!=null && this.isReal())control.rotateLeft(speed);
        else {
            rotMotionState=Double.POSITIVE_INFINITY;
            rotSpeed=(WHEEL_CIRCUMFERENCE*speed)/(180*WHEEL_SEPARATION); // Equivallent to: 2*Math.PI/((Math.PI*WHEEL_SEPARATION)/((speed/360.0)*WHEEL_CIRCUMFERENCE));
        }
    }

    @Override
    public void rotateTo(int heading) {
        if(this.control!=null && this.isReal())control.rotateTo(heading);
        else throw new UnsupportedOperationException("Simulator does not implement rotateTo() method.");
    }	
    
	@Override
    public void arcLeft(int radius) {
        if(this.control!=null && this.isReal())control.arcLeft(radius);
	}
	
	@Override
    public void arcRight(int radius) {
        if(this.control!=null && this.isReal())control.arcRight(radius);
        else {
            arcMotionState=radius;
        }
    }	
	
    @Override
    public void setHeading(int heading) {
        if(this.control!=null && this.isReal())control.setHeading(heading);
        else throw new UnsupportedOperationException("Simulator does not implement setHeading() method.");
    }

    @Override
    public void unlockHeading() {
        if(this.control!=null && this.isReal())control.unlockHeading();
        else throw new UnsupportedOperationException("Simulator does not implement unlockHeading() method.");
    }

    @Override
    public int getCommState() {
        if(this.control!=null && this.isReal()) {
            return control.getCommState();
        }
        else return commState;
    }

    @Override
    public void addAI(AI ai) {
        this.brain=ai;
    }
    
    @Override
    protected void animate(long timeDeltaInMilliseconds){
        
        double ROT_FACTOR=rotSpeed*timeDeltaInMilliseconds/1000;
        double MOV_FACTOR=linSpeed*0.25*timeDeltaInMilliseconds/1000;
        
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
                this.movePosition((Math.cos(orientation.getDirectionRadians())*MOV_FACTOR),
                                  (Math.sin(orientation.getDirectionRadians())*MOV_FACTOR));
            }else{
                this.movePosition(-(Math.cos(orientation.getDirectionRadians())*MOV_FACTOR),
                                  -(Math.sin(orientation.getDirectionRadians())*MOV_FACTOR));
            }
        }
        else if(arcMotionState!=0){
            double turn=(linSpeed/Coordinates.distanceFromCentimetres(arcMotionState))*(timeDeltaInMilliseconds/1000); // Equivallent to: ((Math.PI*2)/((Math.PI*2*arcMotionState)/LINEAR_MOTION_SPEED))*(timeDeltaInMilliseconds/1000);
            this.getOrientation().alter(turn);
            this.movePosition((Math.cos(orientation.getDirectionRadians())*MOV_FACTOR),
                              (Math.sin(orientation.getDirectionRadians())*MOV_FACTOR));
        }
    }

    @Override
    protected Shape[] getVisualisation(int width, int height) {
        double centerX=(Coordinates.distanceFromCentimetres(10))*width/2;
        double centerY=Coordinates.distanceFromCentimetres(10)*height;
        return new Shape[]{new Line2D.Double(-0.5*centerX, 0, 0.5*centerX, 0),
                           new Line2D.Double(0.25*centerX, -0.25*centerY, 0.5*centerX, 0),
                           new Line2D.Double(0.25*centerX, 0.25*centerY, 0.5*centerX, 0),
                           new Ellipse2D.Double(-centerX, -centerY, 2*centerX, 2*centerY)};
    }
    
}

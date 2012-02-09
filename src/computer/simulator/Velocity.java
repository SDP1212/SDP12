/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent velocity as an X component and Y component.
 * Measured in units per simulator tick.
 * 
 * @author Dimo Petroff
 */
public class Velocity {
    
    public static final short SIMULATOR_TICK_LENGTH_IN_MILLISECONDS=Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS;    
    private double vX,vY;
    private double vXold,vYold;
    private double vXmax,vYmax;
    
    public Velocity(Coordinates previousPosition, Coordinates currentPosition){
        this.vXmax=this.vXold=this.vX=currentPosition.getX()-previousPosition.getX();
        this.vYmax=this.vYold=this.vY=currentPosition.getY()-previousPosition.getY();
    }
    
    public void recalculate(Coordinates previousPosition, Coordinates currentPosition){
        this.vXold=this.vX;
        this.vYold=this.vY;
        this.vX=currentPosition.getX()-previousPosition.getX();
        this.vY=currentPosition.getY()-previousPosition.getY();
        if(this.vX>this.vXmax)this.vXmax=this.vX;
        if(this.vY>this.vYmax)this.vYmax=this.vY;
    }
    
    public double getXcomponent(){
        return this.vX;
    }
    
    public double getYcomonent(){
        return this.vY;
    }
    
    public double getPreviousXcomponent(){
        return this.vXold;
    }
    
    public double getPreviousYcomonent(){
        return this.vYold;
    }
    
    public double getMaximumObservedXcomponent(){
        return this.vXmax;
    }
    
    public double getMaximumObservedYcomponent(){
        return this.vYmax;
    }
    
}

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
    
    public Velocity(Coordinates previousPosition, Coordinates currentPosition){
        this.vXold=this.vX=currentPosition.getX()-previousPosition.getX();
        this.vYold=this.vY=currentPosition.getY()-previousPosition.getY();
    }
    
    public void recalculateVelocity(Coordinates previousPosition, Coordinates currentPosition){
        this.vXold=this.vX;
        this.vYold=this.vY;
        this.vX=currentPosition.getX()-previousPosition.getX();
        this.vY=currentPosition.getY()-previousPosition.getY();
    }
    
}

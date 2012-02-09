/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent acceleration as an X component and Y component.
 * Measured in units per simulator tick squared.
 * 
 * @author Dimo Petroff
 */
public class Acceleration {
    
    public static final short SIMULATOR_TICK_LENGTH_IN_MILLISECONDS=Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS;    
    private double aX,aY;
    
    public Acceleration(Velocity v){
        this.recalculate(v);
    }
    
    public void recalculate(Velocity v){
        this.aX=v.getXcomponent()-v.getPreviousXcomponent();
        this.aY=v.getYcomonent()-v.getPreviousYcomonent();
    }
    
    public double getXcomponent(){
        return this.aX;
    }
    
    public double getYcomonent(){
        return this.aY;
    }
    
}

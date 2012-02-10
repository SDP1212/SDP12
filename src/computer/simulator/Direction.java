/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent orientation in radians. Standard conventions apply, i.e.
 * 0 radians points precisely to the right, growing counter-clockwise.
 * Guaranteed to be in the [-2*pi,2*pi] range	  	
 *
 * @author Dimo Petroff
 */
public final class Direction {
    
    public double radians;
    
    public Direction(double radians){
        this.setDirection(radians);
    }
    
    public void setDirection(double radians){
        this.radians=radians%(2*Math.PI);
    }
    
    public double getDirectionRadians(){
        return radians;
    }
    
    public double getDirectionDegrees(){
        return radians*180/Math.PI;
    }
    
}

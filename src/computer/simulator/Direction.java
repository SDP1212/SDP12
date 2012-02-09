/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent orientation in radians. Standard conventions apply, i.e.
 * 0 radians points precisely to the right, growing counter-clockwise.
 * 
 * @author Dimo Petroff
 */
public class Direction {
    
    public double radians;
    
    public Direction(double radians){
        this.radians=radians%Math.PI;
        if(radians<0)
            this.radians=2*Math.PI+this.radians;
    }
    
    public void setDirection(double radians){
        this.radians=radians%Math.PI;
        if(radians<0)
            this.radians=2*Math.PI+this.radians;
    }
    
    public double getDirectionRadians(){
        return radians;
    }
    
    public double getDirectionDegrees(){
        return radians*180/Math.PI;
    }
    
}

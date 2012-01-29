/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Direction {
    
    public float radians;
    
    public Direction(){
        this(0);
    }
    
    public Direction(float radians){
        this.radians=radians;
    }
    
    public void setDirection(float radians){
       this.radians=radians;
    }
    
    public float getDirectionRadians(){
        return radians;
    }
    
    public float getDirectionDegrees(){
        return (float) (radians*180/Math.PI);
    }
    
}

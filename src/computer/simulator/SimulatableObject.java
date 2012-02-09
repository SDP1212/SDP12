/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent a template for a dynamic object within the world.
 * It keeps track of its current and previous location, as well as if it's a
 * real or simulated object.
 * 
 * @author Dimo Petroff
 */
public abstract class SimulatableObject {
    
    protected Boolean real;
    protected Coordinates position, oldPosition;

    protected void setPosition(double x, double y) {
        this.oldPosition=this.position;
        this.position=new Coordinates(x,y);
    }
    
    public boolean isReal(){
        return this.real;
    }
    
}

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
    protected Velocity v;

    protected void setPosition(double x, double y) {
        this.oldPosition=this.position;
        this.position=new Coordinates(x,y);
    }
    
    public Coordinates getPosition(){
        return this.position;
    }
    
    protected void updateVelocity(){
        this.v.recalculateVelocity(this.oldPosition, this.position);
    }
    
    public Velocity getV(){
        return this.v;
    }
    
    public boolean isReal(){
        return this.real;
    }
    
}

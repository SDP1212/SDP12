/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import java.awt.Shape;

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
    protected Acceleration a;

    public void setPosition(double x, double y) {
        if(this.position==null)
            this.oldPosition=this.position=new Coordinates(x,y);
        else{
            this.oldPosition=this.position;
            this.position=new Coordinates(x,y);
        }
    }
    
    public Coordinates getPosition(){
        return this.position;
    }
    
    protected void movePosition(double xAmount, double yAmount){
        this.position.set(this.position.getX()+xAmount,
                          this.position.getY()+yAmount);
    }
    
    protected void updateVelocity(long timeDeltaInMilliseconds){
        if(this.v==null)this.v=new Velocity(this.oldPosition, this.oldPosition, timeDeltaInMilliseconds);
        if(this.a==null)this.a=new Acceleration(this.v);
        this.v.recalculate(this.oldPosition, this.position, timeDeltaInMilliseconds);
        this.a.recalculate(this.v);
    }
    
    public Velocity getV(){
        return this.v;
    }
    
    public Acceleration getA(){
        return this.a;
    }
    
    public boolean isReal(){
        return this.real;
    }
    
    protected abstract void animate(long timeDeltaInMilliseconds);
    
    protected abstract Shape[] getVisualisation(int width, int height);
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public abstract class SimulatableObject {
    
    protected Boolean real;
    protected Coordinates position, oldPosition;

    protected void setPosition(float x, float y) {
        this.oldPosition=this.position;
        this.position=new Coordinates();
        this.position.setX(x);
        this.position.setY(y);
    }
    
    public boolean isReal(){
        return this.real;
    }
    
}

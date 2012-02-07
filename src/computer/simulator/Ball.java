/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Ball extends SimulatableObject{
    
    protected Ball(){
        this(1f,0.5f,false);
    }

    protected Ball(float x, float y, boolean real) {
        this.position=new Coordinates(x, y);
        this.real=real;
    }
    
    protected Ball(Coordinates coordinates, boolean real){
        this.position=coordinates;
        this.real=real;
    }
    
}

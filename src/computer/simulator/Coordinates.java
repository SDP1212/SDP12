/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Coordinates implements Cloneable {
    
    private float x,y;
    
    public Coordinates(){
        this(0,0);
    }
    
    public Coordinates(float x, float y){
        this.x=x;
        this.y=y;
    }
    
    public void setX(float x){
        this.x=x;
    }

    public void setY(float y){
        this.y=y;
    }
    
    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }
    
    public Coordinates clone (){
        Coordinates c = (Coordinates) this.clone();
        c.setX(this.getX());
        c.setY(this.getY());
        return c;
    }
    
}

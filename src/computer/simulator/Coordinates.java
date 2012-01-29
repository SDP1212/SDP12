/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Coordinates {
    
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
    
    public float getX(float x){
        return this.x;
    }

    public float getY(float y){
        return this.y;
    }
}

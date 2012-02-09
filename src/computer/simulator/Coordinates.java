/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent coordinates in the internal format. The origin is the
 * bottom-left corner of the pitch. 
 * X is the horizontal direction, growing to the right in the range 0 to 2.0
 * Y is the vertical direction, growing up in the range 0 to 1.0
 * 
 * Above, "up," "down," "left" and "right" refer to the directions from
 * the camera's point of view.
 * 
 * N.B. While the Coordinates within these ranges are guaranteed to be within
 * the Pitch, Coordinates may go outside these ranges if fish-eye/barrel effect
 * is present in coordinates provided by vision. Or when otherwise necessary.
 * 
 * @author Dimo Petroff
 */
public class Coordinates{
    
    private double x,y;
    
    public Coordinates(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    public Coordinates() {
        
    }
    
    public void set(double x, double y){
        this.setX(x);
        this.setY(y);
    }
    
    public void setX(double x){
        this.x=x;
    }

    public void setY(double y){
        this.y=y;
    }
    
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }
    
    public double distance(Coordinates point){
        return distance(this, point);
    }
    
    public static double distance(Coordinates pointA, Coordinates pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
    }
    
    public Coordinates clone (){
        Coordinates c = null;
        try {
            c = (Coordinates)super.clone();
            c.setX(this.getX());
            c.setY(this.getY());
        } catch (CloneNotSupportedException ex) {
            System.err.println("clone error " + ex.toString());
        }
        return c;
    }
    
}

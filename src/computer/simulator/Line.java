/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Documentation for the classes Line and LineTools is provided in the file SDP12/doc/LineClasses.pdf.
 * @author Evgeniya Sotirova
 */
public class Line {
    
    // Equation of a line: y = gradient * x + offset
    private double gradient; //The gradient cannot be -Infinity
    private double offset;
    
    // direction is true if the direction of the line is in the direction in which x increases.
    // If x is constant, i.e. the line is parallel to the y axis, then direction is true if the direction of the line is in the direction in which y increases.
    private boolean direction;
    
    // rangeMin and rangeMax define the range of x if the line is not vertical.
    // If the line is vertical, rangeMin and rangeMax define the range of y;
    private double rangeMin;
    private double rangeMax;
    
    private Coordinates firstPoint;
    private Coordinates secondPoint;
     
    // Defining a line by two points (the order of the points determines the direction of the line):
    public Line(Coordinates A, Coordinates B){
        
        // Set the gradient...
        if((B.getX()-A.getX()) == 0){
            gradient = Double.POSITIVE_INFINITY;
        }
        else gradient = (B.getY()-A.getY())/(B.getX()-A.getX());
        
        //Set the offset...
        offset = A.getY()-gradient*A.getX();
        
        // Set direction, rangeMin and rangeMax...
        if(B.getX()-A.getX() > 0){
            direction = true;
            rangeMin = B.getX();
            rangeMax = Double.POSITIVE_INFINITY;
        }
        else {
            if(B.getX()-A.getX() < 0){
                direction = false;
                rangeMin = Double.NEGATIVE_INFINITY;
                rangeMax = B.getX();
            }
            else{
                if(B.getY()-A.getY() > 0){
                    direction = true;
                    rangeMin = B.getY();
                    rangeMax = Double.POSITIVE_INFINITY;
                }
                else{
                    direction = false;
                    rangeMin = Double.NEGATIVE_INFINITY;
                    rangeMax = B.getY();
                }
            }
        }
        
        firstPoint = A;
        secondPoint = B;
    }
    
    public Line(Coordinates A, Coordinates B, double rangeMin, double rangeMax){
        
        // Set the gradient...
        if((B.getX()-A.getX()) == 0){
            gradient = Double.POSITIVE_INFINITY;
        }
        else gradient = (B.getY()-A.getY())/(B.getX()-A.getX());
        
        //Set the offset...
        offset = A.getY()-gradient*A.getX();
        
        // Set direction, rangeMin and rangeMax...
        if(B.getX()-A.getX() > 0)
            direction = true;
        else {
            if(B.getX()-A.getX() < 0)
                direction = false;
            else{
                if(B.getY()-A.getY() > 0)
                    direction = true;
                else
                    direction = false;
            }
        }
        
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        
        firstPoint = A;
        secondPoint = B;
    }
    
    public double getGradient(){
        return gradient;
    }
    public double getOffset(){
        return offset;
    }
    public boolean getDirection(){
        return direction;
    }
    public double getRangeMin(){
        return rangeMin;
    }
    public double getRangeMax(){
        return rangeMax;
    }
    public Coordinates getFirstPoint(){
        return firstPoint;
    }
    public Coordinates getSecondPoint(){
        return secondPoint;
    }
    
    public boolean isOnLineAndInRange(Coordinates point){
        if(gradient != Double.POSITIVE_INFINITY){
            if(point.getX()*gradient + offset == point.getY() && rangeMin <= point.getX() && point.getX() <= rangeMax)
                return true;
            else
                return false;
        }
        else
            if(point.getX() == firstPoint.getX() && rangeMin <= point.getY() && point.getY() <= rangeMax)
                return true;
            else
                return false;
    }    
}

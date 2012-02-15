/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * The objects in class Line are in fact rays.
 * For more information please read the .pdf file Line.pdf.
 * @author Evgeniya Sotirova
 */
public class Line {
    
    // Equation of a line: y = gradient * x + offset
    private double gradient;
    private double offset;
    
    // lineDirection is true if the direction of the line is in the direction in which x increases.
    // If x is constant, i.e. the line is parallel to the y axis, then lineDirection is true if the direction of the line is in the direction in which y increases.
    private boolean direction;
    
    // rangeXmin and rangeXmax define the range of x.
    private double rangeXmin;
    private double rangeXmax;
    
    private Coordinates firstPoint;
    private Coordinates secondPoint;
     
    // Defining a line by two points (the order of the points determines the direction of the line):
    public Line(Coordinates A, Coordinates B){
        
        if((B.getX()-A.getX())==0) gradient = Float.POSITIVE_INFINITY;
        else gradient = (B.getY()-A.getY())/(B.getX()-A.getX());
        
        offset = A.getY()-gradient*A.getX();
        
        // Setting the direction:
        if(B.getX()-A.getX() > 0) direction = true;
        else {
            if(B.getX()-A.getX() < 0) direction = false;
            else{
                if(B.getY()-A.getY() > 0) direction = true;
                else direction = false;
            }
        }
        
        rangeXmin = B.getX();
        rangeXmax = Double.POSITIVE_INFINITY;
        
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
    public double getXmin(){
        return rangeXmin;
    }
    public double getXmax(){
        return rangeXmax;
    }
    public Coordinates getFirstPoint(){
        return firstPoint;
    }
    public Coordinates getSecondPoint(){
        return secondPoint;
    }
    public double getLength() {
        return Math.sqrt(Math.pow(firstPoint.getX() - secondPoint.getX(), 2) + Math.pow(firstPoint.getY() - secondPoint.getY(), 2));
    }

    //If you want to change only the first point of a line.
    public void setFirstPoint(Coordinates point){
        firstPoint = point;
        
        if((secondPoint.getX()-firstPoint.getX())==0) gradient = Float.POSITIVE_INFINITY;
        else gradient = (secondPoint.getY()-firstPoint.getY())/(secondPoint.getX()-firstPoint.getX());
        
        offset = firstPoint.getY()-gradient*firstPoint.getX();
        
        // Setting the direction:
        if(secondPoint.getX()-firstPoint.getX() > 0) direction = true;
        else {
            if(secondPoint.getX()-firstPoint.getX() < 0) direction = false;
            else{
                if(secondPoint.getY()-firstPoint.getY() > 0) direction = true;
                else direction = false;
            }
        }
        
        rangeXmin = secondPoint.getX();
        rangeXmax = Double.POSITIVE_INFINITY;
    }
    
    //If you want to change the second point of a line.
    public void setSecondPoint(Coordinates point){
        secondPoint = point;
        
        if((secondPoint.getX()-firstPoint.getX())==0) gradient = Float.POSITIVE_INFINITY;
        else gradient = (secondPoint.getY()-firstPoint.getY())/(secondPoint.getX()-firstPoint.getX());
        
        offset = firstPoint.getY()-gradient*firstPoint.getX();
        
        // Setting the direction:
        if(secondPoint.getX()-firstPoint.getX() > 0) direction = true;
        else {
            if(secondPoint.getX()-firstPoint.getX() < 0) direction = false;
            else{
                if(secondPoint.getY()-firstPoint.getY() > 0) direction = true;
                else direction = false;
            }
        }
        
        rangeXmin = secondPoint.getX();
        rangeXmax = Double.POSITIVE_INFINITY;
    }
}

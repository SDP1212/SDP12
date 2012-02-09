/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Evgeniya Sotirova
 */
public class Line {
    
    // Equation of a line: y = lineGradient * x + lineOffset
    private double lineGradient;
    private double lineOffset;
    
    // lineDirection is true if the direction of the line is in the direction in which x increases.
    // If x is constant, i.e. the line is parallel to the y axis, then lineDirection is true if the direction of the line is in the direction in which y increases.
    private boolean lineDirection;
    
    // rangeXmin and rangeXmax define the range of x.
    private double rangeXmin;
    private double rangeXmax;
    
    private double rangeYmin;
    private double rangeYmax;
    
    private Coordinates firstPoint;
    private Coordinates secondPoint;
     
    // Defining a line by two points (the order of the points determines the direction of the line):
    public Line(Coordinates A, Coordinates B){
        
        if((B.getX()-A.getX())==0) lineGradient = Float.POSITIVE_INFINITY;
        else lineGradient = (B.getY()-A.getY())/(B.getX()-A.getX());
        
        lineOffset = A.getY()-lineGradient*A.getX();
        
        if(B.getX()-A.getX() > 0) lineDirection = true;
        else {
            if(B.getX()-A.getX() < 0) lineDirection = false;
            else{
                if(B.getY()-A.getY() > 0) lineDirection = true;
                else lineDirection = false;
            }
        }
        
        rangeXmin = Double.NEGATIVE_INFINITY;
        rangeXmax = Double.POSITIVE_INFINITY;
        rangeYmin = Double.NEGATIVE_INFINITY;
        rangeYmax = Double.POSITIVE_INFINITY;
        
        firstPoint = A;
        secondPoint = B;
    }
    
    // Define the line by the gradient,offset, direction and the range for x.
    /*public Line(double gradient, double offset, boolean direction){
        lineGradient = gradient;
        lineOffset = offset;
        lineDirection = direction;

        rangeXmin = Double.NEGATIVE_INFINITY;
        rangeXmax = Double.POSITIVE_INFINITY;
        rangeYmin = Double.NEGATIVE_INFINITY;
        rangeYmax = Double.POSITIVE_INFINITY;
    }*/
    
    public double getGradient(){
        return lineGradient;
    }
    public double getOffset(){
        return lineOffset;
    }
    public boolean getDirection(){
        return lineDirection;
    }
    public double getXmin(){
        return rangeXmin;
    }
    public double getXmax(){
        return rangeXmax;
    }
    public double getYmin(){
        return rangeYmin;
    }
    public double getYmax(){
        return rangeYmax;
    }
    public Coordinates getFirstPoint(){
        return firstPoint;
    }
    public Coordinates getSecondPoint(){
        return secondPoint;
    }

    public void setGradient(double  gradient){
        lineGradient = gradient;        
    }
    public void setOffset(double offset){
        lineOffset = offset;
    }
    public void setDirection(boolean direction){
        lineDirection = direction;
    }
    public void setXmin(double xMin){
        rangeXmin = xMin;        
    }
    public void setXmax(double xMax){
        rangeXmax = xMax;
    }
    public void setYmin(double yMin){
        rangeYmin = yMin;        
    }
    public void setYmax(double yMax){
        rangeYmax = yMax;
    }
}

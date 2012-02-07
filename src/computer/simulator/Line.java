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
    /**
     * The following representation of a line is used:
     * y = lineGradient * x + lineOffset
     * The line can also be defined by the the two given points linePointA and linePointB. linePointA is always considered as the FIRST point, i.e. (linePointB-linePointA) is the direction of the line.
     * linePointA and linePointB are also the end points of the line.
     * lineDirection is true if the direction of the line is in the direction in which x increases
     * The range of x is [rangeXmin, rangeXmax]
     */    
    private float lineGradient, lineOffset;
    private boolean lineDirection;
    private float rangeXmin, rangeXmax;
    private Coordinates linePointA, linePointB;
    
    /**
     * The default for a line is basically the origin of the coordinate system.
     */
    public Line(){
        lineGradient = 0;
        lineOffset = 0;
        lineDirection = true;
        rangeXmin = 0;
        rangeXmax = 2;
        linePointA.setX(0);
        linePointA.setY(0);
        linePointB.setX(0);
        linePointB.setY(0);
    }
    
    /**
     * If the you define a line by two points (in this case A and B) then the direction of the line is first point -> second point, i.e. A->B.
     * Also A and B may not be the end points.
     * @param A
     * @param B 
     */
    public Line(Coordinates A, Coordinates B){
        /**
         * ((A.getX()-B.getX())==0 means that the line we have is parallel to the y axis.
         * Then the gradient tends towards infinity, but it is enough to choose a big enough value for it, i.e. I fix it to be 10000.
         */
        if((B.getX()-A.getX())==0){
            lineGradient = 10000;
            
            //The x coordinate of point B has to be changed a little, so that we really have a gradient of 10000.
            B.setX((B.getY()-A.getY())/10000);
        }
        else{
            lineGradient = (B.getY()-A.getY())/(B.getX()-A.getX());
        }
        
        lineOffset = A.getY()-lineGradient*A.getX();
        
        if(B.getX()-A.getX() > 0) lineDirection = true;
        else lineDirection = false;
        
        rangeXmin = A.getX();
        rangeXmax = A.getX();
        if(A.getX() < B.getX()) rangeXmax = B.getX();
        else rangeXmin = B.getX();
        
        linePointA = A;
        linePointB = B;
                
    }
    
    //If you want the line to be parallel to the y axis, set the gradient to 10000 or -10000.
    public Line(float gradient, float offset, boolean direction){
        lineGradient = gradient;
        lineOffset = offset;
        lineDirection = direction;
        rangeXmin = 0;
        rangeXmax = 2;
        linePointA = new Coordinates(0,lineOffset);
        linePointB = new Coordinates(2,lineGradient*2+lineOffset);
    }
    
    public float getGradient(){
        return lineGradient;
    }
    public float getOffset(){
        return lineOffset;
    }
    public boolean getDirection(){
        return lineDirection;
    }
    public float getXmin(){
        return rangeXmin;
    }
    public float getXmax(){
        return rangeXmax;
    }
    public Coordinates getFirstPoint(){
        return linePointA;
    }
    public Coordinates getSecondPoint(){
        return linePointB;
    }
    
    
    
    
    public void setGradient(float gradient){
        lineGradient = gradient;        
    }
    public void setOffset(float offset){
        
    }
    public void setDirection(boolean Direction){
        
    }
    public void setXmin(float Xmin){
        
    }
    public void setXmax(float Xmax){
        
    }
    public void setFirstPoint(Coordinates firstPoint){
        
    }
    public void setSecondPoint(Coordinates secondPoint){
        
    }
}

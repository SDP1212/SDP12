/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Evgeniya Sotirova
 */
public class LineTools {
    /**
     * If the lines do not intersect then the function returns a point with coordinates (3,3);
     * @param l
     * @param m
     * @return 
     */
    public Coordinates intersectionOfLines(Line l, Line m){
        Coordinates intersectionPoint;
        intersectionPoint = new Coordinates(3,3);
        
        float x1 = l.getFirstPoint().getX();
        float y1 = l.getFirstPoint().getY();
        float x2 = l.getSecondPoint().getX();
        float y2 = l.getSecondPoint().getY();
        
        float x3 = m.getFirstPoint().getX();
        float y3 = m.getFirstPoint().getY();
        float x4 = m.getSecondPoint().getX();
        float y4 = m.getSecondPoint().getY();
        
        float parallel = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
        
        float ua = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3));
        float ub = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3));
        
        if(parallel!=0){
            ua = ua/parallel;
            ub = ub/parallel;
            if(ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1){
                intersectionPoint.setX(x1 + ua*(x2-x1));
                intersectionPoint.setY(y1 + ua*(y2-y1));
            }
        }
        return intersectionPoint;
    }
    
    
    public Coordinates symmetricalPoint(Coordinates point, Line line){
        Coordinates newPoint;
        newPoint = new Coordinates();
        
        float x = point.getX();
        float y = point.getY();
        float gamma = (float)Math.atan(line.getGradient());
        
        newPoint.setX(x*(float)Math.cos(-gamma) - y*(float)Math.sin(-gamma));
        newPoint.setY(x*(float)Math.sin(-gamma) + y*(float)Math.cos(-gamma));
        
        y = line.getOffset()*(float)Math.sin(gamma) - y;
        
        newPoint.setX(x*(float)Math.cos(gamma) - y*(float)Math.sin(gamma));
        newPoint.setY(x*(float)Math.sin(gamma) + y*(float)Math.cos(gamma));
        
        return newPoint;
    }
    
    public float distanceFromPointToLine(Coordinates point, Line line){
        float distance;
        
        // n=(nx,ny) is a normal vector to the given line
        float nx = line.getGradient();
        float ny = -1;
        
        float rx = 0 - point.getX();
        float ry = line.getOffset() - point.getY();
        
        distance = (nx*rx+ny*ry)/(float)Math.sqrt((nx*nx+ny*ny));
        
        return distance;
    }
}

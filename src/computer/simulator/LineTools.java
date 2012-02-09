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
        
        double x1 = l.getFirstPoint().getX();
        double y1 = l.getFirstPoint().getY();
        double x2 = l.getSecondPoint().getX();
        double y2 = l.getSecondPoint().getY();
        
        double x3 = m.getFirstPoint().getX();
        double y3 = m.getFirstPoint().getY();
        double x4 = m.getSecondPoint().getX();
        double y4 = m.getSecondPoint().getY();
        
        double parallel = (y4-y3)*(x2-x1)-(x4-x3)*(y2-y1);
        
        double ua = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3));
        double ub = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3));
        
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
        
        double x = point.getX();
        double y = point.getY();
        double gamma = (double)Math.atan(line.getGradient());
        
        newPoint.setX(x*(double)Math.cos(-gamma) - y*(double)Math.sin(-gamma));
        newPoint.setY(x*(double)Math.sin(-gamma) + y*(double)Math.cos(-gamma));
        
        y = line.getOffset()*(double)Math.sin(gamma) - y;
        
        newPoint.setX(x*(double)Math.cos(gamma) - y*(double)Math.sin(gamma));
        newPoint.setY(x*(double)Math.sin(gamma) + y*(double)Math.cos(gamma));
        
        return newPoint;
    }
    
    public double distanceFromPointToLine(Coordinates point, Line line){
        double distance;
        
        // n=(nx,ny) is a normal vector to the given line
        double nx = line.getGradient();
        double ny = -1;
        
        double rx = 0 - point.getX();
        double ry = line.getOffset() - point.getY();
        
        distance = (nx*rx+ny*ry)/(double)Math.sqrt((nx*nx+ny*ny));
        
        return distance;
    }
    
    public double angleBetweenLines(Line l, Line m){
        double gamma;
        
        gamma = Math.acos((1+l.getGradient()*m.getGradient())/(Math.sqrt(1+l.getGradient()*l.getGradient())*Math.sqrt(1+m.getGradient()*m.getGradient())));
        
        return gamma;
    }
}

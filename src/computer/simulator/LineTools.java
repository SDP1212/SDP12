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
    
    public static Coordinates intersectionOfLines(Line l, Line m){
        
        Coordinates intersectionPoint;
        intersectionPoint = new Coordinates();
        
        if(l.getGradient() == m.getGradient()){
            intersectionPoint.set(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
            intersectionPoint.setX((m.getOffset()-l.getOffset()) / (l.getGradient()-m.getGradient()));
            intersectionPoint.setY(l.getGradient()*intersectionPoint.getX() + l.getOffset());
        }
                
        return intersectionPoint;
    }
        
    public static Coordinates symmetricalPoint(Coordinates point, Line line){
        Coordinates newPoint;
        newPoint = new Coordinates();
        
        double x = point.getX();
        double y = point.getY();
        double gamma = Math.atan(line.getGradient());
        
        newPoint.setX(x*Math.cos(-gamma) - y*Math.sin(-gamma));
        newPoint.setY(x*Math.sin(-gamma) + y*Math.cos(-gamma));
        
        y = line.getOffset()*Math.sin(gamma) - y;
        
        newPoint.setX(x*Math.cos(gamma) - y*Math.sin(gamma));
        newPoint.setY(x*Math.sin(gamma) + y*Math.cos(gamma));
        
        return newPoint;
    }
    
    public static double distanceFromPointToLine(Coordinates point, Line line){
 
        double distance;
        if(line.getGradient() == Double.POSITIVE_INFINITY){
            distance = Math.abs(point.getX()-line.getXmin());
        } else {
            distance = (-line.getGradient()*point.getX()-(line.getOffset() - point.getY()))/Math.sqrt((line.getGradient()*line.getGradient()+1));
        }
        
        return distance;
    }
    
    public static double angleBetweenLineAndDirection(Line l, Direction direction){
        double gamma = Math.atan(l.getGradient());
        
        if(!l.getDirection() && gamma < 0) gamma = gamma + Math.PI;
        else{
            if(!l.getDirection() && gamma > 0) gamma = gamma - Math.PI;
        }
        
        gamma = direction.getDirectionRadians() - gamma;       
        //gamma = Math.acos((Math.cos(direction.getDirectionRadians())+l.getGradient()*Math.sin(direction.getDirectionRadians()))/Math.sqrt(1+l.getGradient()*l.getGradient()));
        if(gamma > Math.PI) gamma = gamma - 2*Math.PI;
        if(gamma < -Math.PI) gamma = gamma + 2*Math.PI;
        return gamma;
    }
    
    /**
     * Indicates from which side of the line is the point.
     * @return 1 : on the left
     * @return 0 : on the line
     * @return -1: on the right
     */
    public static int sideOfLine(Coordinates point, Line line){
        double x1 = 1;
        double y1 = line.getGradient();
        if(line.getDirection()){
            x1 = -x1;
            y1 = -y1;
        }
        double x2 = point.getX() - line.getFirstPoint().getX();
        double y2 = point.getY() - line.getFirstPoint().getY();
        
        double sinGamma = (x1*y2-y1*x2)/((x1*x1+y1*y1)*(x2*x2+y2*y2));
        
        if(sinGamma > 0) return 1;
        else {
            if(sinGamma == 0) return 0;
            else return -1;
        }
    }
    
    /**
     * @param point: centre of the rectangle
     * @param direction: direction of the rectangle
     * @param length: length of the rectangle. The length is the side which is parallel to the direction.
     * @param width
     * @return an array of lines which represents the rectangle around a given point.
     * Side 0 is always the side perpendicular to the vector defined by the direction of the rectangle.
     * The sides are numbered counterclockwise.
     */
    public static Line[] formRectagleAroundPoint(Coordinates point, Direction direction, double length, double width){
        Coordinates point1, point2;
        
        point1 = new Coordinates(point.getX()+Math.cos(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()+Math.sin(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        point2 = new Coordinates(point.getX()+Math.cos(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()+Math.sin(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        
        Line[] arrayOfLines = new Line[4];
        arrayOfLines[0] = new Line(point1, point2);
        
        point1.set(point2.getX(), point2.getY());
        point2.set(point.getX()-Math.cos(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()-Math.sin(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length) );
        arrayOfLines[1] = new Line(point1, point2);
        
        point1.set(point2.getX(), point2.getY());
        point2.set(point.getX()-Math.cos(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()-Math.sin(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        arrayOfLines[1] = new Line(point1, point2);

        point1.set(point2.getX(), point2.getY());
        point2.set(point.getX()+Math.cos(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()+Math.sin(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        arrayOfLines[1] = new Line(point1, point2);
        
        return arrayOfLines;
    }
    
    public static int lineIntersectingLines(Line line, Line[] lines){
        double distanceQuotient = Double.POSITIVE_INFINITY;
        int lineNum = -1;
        
        for(int i=0; i<lines.length; i++){
            double distanceQuotient2 = (intersectionOfLines(line, lines[i]).getX()-line.getFirstPoint().getX())/(line.getSecondPoint().getX()-line.getFirstPoint().getX());
            if(distanceQuotient2 > 0 && distanceQuotient2 < distanceQuotient){
                distanceQuotient = distanceQuotient2;
                lineNum = i+1;
            }
        }
        return lineNum;
    }

    /**
     * NOT FINISHED YET!!!
     * This function should tell which thing(robot or side of the table) the ball will hit first.
     */
    /*
    public Line ballOnTheTable(Coordinates ball, Direction dirBall, Coordinates robot1, Direction dirRobot1, double lengthRobot1, double widthRobot1, Coordinates robot2, Direction dirRobot2, double lengthRobot2, double widthRobot2){
        Coordinates point;
        point = new Coordinates(ball.getX()+1,Math.tan(dirBall.getDirectionRadians())+ball.getY());
        
        Line line = new Line(ball, point);
        Line[] lines = new Line[12];
        
        for(int i=0;i<4; i++){
            lines[i] = formRectagleAroundPoint(robot1,dirRobot1,lengthRobot1,widthRobot1)[i];
        }
        for(int i=0;i<4; i++){
            lines[i+4] = formRectagleAroundPoint(robot2,dirRobot2,lengthRobot2,widthRobot2)[i];
        }

        
        
        Line line = new Line(pointA, pointB);
        return line;
    }
     */
}
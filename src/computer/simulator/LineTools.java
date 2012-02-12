/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * LineTools contains functions for manipulating objects from class Line.
 * @author Evgeniya Sotirova
 */
public class LineTools {
    
    public static Coordinates intersectionOfLines(Line l, Line m){
        
        Coordinates intersectionPoint;
        intersectionPoint = new Coordinates();
        
        if(l.getGradient() == m.getGradient()){ // if(the lines are parallel)
            intersectionPoint.set(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        else {
            if(l.getGradient() == Double.POSITIVE_INFINITY || l.getGradient() == Double.NEGATIVE_INFINITY){
                intersectionPoint.setX(l.getFirstPoint().getX());
                intersectionPoint.setY(m.getGradient()*intersectionPoint.getX() + m.getOffset());
            }
            else{
                if(m.getGradient() == Double.POSITIVE_INFINITY || m.getGradient() == Double.NEGATIVE_INFINITY){
                    intersectionPoint.setX(m.getFirstPoint().getX());
                    intersectionPoint.setY(l.getGradient()*intersectionPoint.getX() + l.getOffset());
                }
                else{
                    intersectionPoint.setX((m.getOffset()-l.getOffset()) / (l.getGradient()-m.getGradient()));
                    intersectionPoint.setY(l.getGradient()*intersectionPoint.getX() + l.getOffset());
                }
            }
        }
                
        return intersectionPoint;
    }
        
    public static Coordinates symmetricalPoint(Coordinates point, Line line){
        Coordinates newPoint = new Coordinates(0,0);
        if(line.getGradient() == 0){
            newPoint.set(point.getX(), point.getY() + 2*(line.getOffset()-point.getY()));
        }
        else{
            if(line.getGradient() == Double.POSITIVE_INFINITY || line.getGradient() == Double.NEGATIVE_INFINITY){
                newPoint.set(point.getX() + 2*(line.getFirstPoint().getX()-point.getX()), point.getY());
            }
            else{
                double a = line.getGradient();
                double x = ((2*point.getY() + ((1/a)-a)*point.getX() - 2*line.getOffset())/((1/a)+a));
                newPoint.setX(x);
                newPoint.setY((-1/a)*x+(point.getY()+(1/a)*point.getX()));
            }
        }
        
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
        
        // Correct the angle with respect to the direction of the line...
        if(!l.getDirection()){
           if(gamma > 0) gamma = gamma - Math.PI;
           else gamma = gamma + Math.PI;
        }
        
        gamma = direction.getDirectionRadians() - gamma;       
        
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
        boolean result = true;
        // Check if the point is on the line...
        if(point.getX()*line.getGradient() + line.getOffset() == point.getY()){
            return 0;
        }
        else{
            // Check if the line is parallel to the y axis...
            if(line.getGradient() == Double.POSITIVE_INFINITY || line.getGradient() == Double.NEGATIVE_INFINITY){
                if(point.getX()<line.getFirstPoint().getX()) result = false;
            }
            else{
                // Check if the point is on the left of the line...
                if(point.getX()*line.getGradient()+line.getOffset() > point.getY()) result = false;
                // Check if the direction of the line is false.
                if(!line.getDirection()) result = !result;
            }
        }
        if(result == true) return 1;
        else return -1;
    }
    
    /**
     * This function returns an array of 4 points which are the vertices of a rectangle with given centre, length and width.
     * @param point: centre of the rectangle
     * @param direction: direction of the rectangle
     * The vertices of the rectangle are numbered counter clockwise so that the side which is formed by vertices 0 and 1 is perpendicular to the direction of the rectangle.
     */
    public static Coordinates[] formRectagleAroundPoint(Coordinates point, Direction direction, double length, double width){
        Coordinates[] points = new Coordinates[4];
        for(int i=0; i< 4; i++) points[i] = new Coordinates(0,0);
        points[0].set(point.getX()+Math.cos(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()+Math.sin(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        points[1].set(point.getX()+Math.cos(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()+Math.sin(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        points[2].set(point.getX()-Math.cos(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()-Math.sin(direction.getDirectionRadians()-Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        points[3].set(point.getX()-Math.cos(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length), point.getY()-Math.sin(direction.getDirectionRadians()+Math.atan(width/length))*0.5*Math.sqrt(width*width+length*length));
        
        return points;
    }
    
    /**
     * @return the index of the line from the array of lines which is first intersected by a given line.
     * The line "line" cannot intersect lines which are "behind" it, i.e. here the line is treated as a ray.
     */
    public static int lineIntersectingLines(Line line, Line[] lines){
        int numOfClosestLine = -1;
        double quotient = -1;
        double quotient2;
        double x = line.getSecondPoint().getX() - line.getFirstPoint().getX();
        double y = line.getSecondPoint().getY() - line.getFirstPoint().getY();
        
        for(int i=0; i<lines.length; i++){
            if(x == 0){
                quotient2 = (intersectionOfLines(line, lines[i]).getY() -line.getFirstPoint().getY())/y;
            }
            else{
                quotient2 = (intersectionOfLines(line, lines[i]).getX() -line.getFirstPoint().getX())/x;
            }
            if(quotient2 >= 1 && (quotient > quotient2 || quotient < 1)){
                quotient = quotient2;
                numOfClosestLine = i;
            }
        }
        return numOfClosestLine;
    }

    /**
     * This function returns the side of the table or the robots the ball will hit first.
     * @return 0-3 : the sides of the table;
     * @return 0 : the side from (0,0) to (2,0)
     * @return 1 : the side from (2,0) to (2,1)
     * @return 2 : the side from (2,1) to (0,1)
     * @return 3 : the side from (0,1) to (2,0)
     * 
     * @return 4-7 : robot1;
     * @return 4 : the side perpendicular to the direction of the robot
     * The rest of the sides are numbered in a counterclockwise order.
     * 
     * @return 8-11: robot2;
     * @return 8 : the side perpendicular to the direction of the robot
     * The rest of the sides are numbered in a counterclockwise order.
     */
    public static int ballOnTheTable(Line ball, Coordinates robot1, Direction dirR1, double lengthR1, double widthR1, Coordinates robot2, Direction dirR2, double lengthR2, double widthR2){
        //Basically, this function uses the function lineIntersectingLines().
        Line[] lines = new Line[12];
        int lineOfIntersection = -1;
        
        Coordinates[] tableCorners = new Coordinates[4];
        tableCorners[0] = new Coordinates(0,0);
        tableCorners[1] = new Coordinates(2,0);
        tableCorners[2] = new Coordinates(2,1);
        tableCorners[3] = new Coordinates(0,1);
        
        lines[0] = new Line(tableCorners[0],tableCorners[1]);
        lines[1] = new Line(tableCorners[1],tableCorners[2]);
        lines[2] = new Line(tableCorners[2],tableCorners[3]);
        lines[3] = new Line(tableCorners[3],tableCorners[0]);
        
        Coordinates[] robot1Coordinates = new Coordinates[4];
        robot1Coordinates = formRectagleAroundPoint(robot1, dirR1, lengthR1, widthR1);
        
        lines[4] = new Line(robot1Coordinates[0], robot1Coordinates[1]);
        lines[5] = new Line(robot1Coordinates[1], robot1Coordinates[2]);
        lines[6] = new Line(robot1Coordinates[2], robot1Coordinates[3]);
        lines[7] = new Line(robot1Coordinates[3], robot1Coordinates[0]);
        
        Coordinates[] robot2Coordinates = new Coordinates[4];
        robot2Coordinates = formRectagleAroundPoint(robot2, dirR2, lengthR2, widthR2);
        
        lines[8] = new Line(robot2Coordinates[0], robot2Coordinates[1]);
        lines[9] = new Line(robot2Coordinates[1], robot2Coordinates[2]);
        lines[10] = new Line(robot2Coordinates[2], robot2Coordinates[3]);
        lines[11] = new Line(robot2Coordinates[3], robot2Coordinates[0]);
        
        lineOfIntersection = lineIntersectingLines(ball, lines);
        
        return lineOfIntersection;
    }
}
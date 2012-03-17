/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Documentation for the classes Line and LineTools is provided in the file SDP12/doc/LineClasses.pdf.
 * @author Evgeniya Sotirova
 */
public class LineTools {
    
    public static Coordinates intersectionOfLines(Line l, Line m){
        
        Coordinates intersectionPoint;
        intersectionPoint = null;
        
        if(l.getGradient() != m.getGradient()){ // The lines should not be parallel.
            if(l.getGradient() == Double.POSITIVE_INFINITY){
                intersectionPoint = new Coordinates();
                intersectionPoint.setX(l.getFirstPoint().getX());
                intersectionPoint.setY(m.getGradient()*intersectionPoint.getX() + m.getOffset());
            }
            else{
                if(m.getGradient() == Double.POSITIVE_INFINITY){
                    intersectionPoint = new Coordinates();
                    intersectionPoint.setX(m.getFirstPoint().getX());
                    intersectionPoint.setY(l.getGradient()*intersectionPoint.getX() + l.getOffset());
                }
                else{
                    intersectionPoint = new Coordinates();
                    intersectionPoint.setX((m.getOffset()-l.getOffset()) / (l.getGradient()-m.getGradient()));
                    intersectionPoint.setY(l.getGradient()*intersectionPoint.getX() + l.getOffset());
                }
            }
            if(!l.isOnLineAndInRange(intersectionPoint) || !m.isOnLineAndInRange(intersectionPoint))
                intersectionPoint = null;
        }
        return intersectionPoint;
    }
        
    public static Coordinates symmetricalPoint(Coordinates point, Line line){
        Coordinates newPoint = new Coordinates(0,0);
        if(line.getGradient() == 0){
            newPoint.set(point.getX(), point.getY() + 2*(line.getOffset()-point.getY()));
        }
        else{
            if(line.getGradient() == Double.POSITIVE_INFINITY){
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
            distance = Math.abs(point.getX()-line.getRangeMin());
        } else {
            distance = Math.abs(-line.getGradient()*point.getX()-(line.getOffset() - point.getY()))/Math.sqrt((line.getGradient()*line.getGradient()+1));
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
        if((point.getX()*line.getGradient() + line.getOffset() == point.getY()||(line.getGradient()==Double.POSITIVE_INFINITY && point.getX()==line.getFirstPoint().getX()))){
            return 0;
        }
        else{
            // Check if the line is parallel to the y axis...
            if(line.getGradient() == Double.POSITIVE_INFINITY || line.getGradient() == Double.NEGATIVE_INFINITY){
                if(point.getX()>line.getFirstPoint().getX()) result = false;
                if(line.getDirection()==false) result = !result;
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
    
    /*
     * The function formLinesAroundPoint creates lines from the points that
     * the above function (formRectagleAroundPoint) returns.
     * It also adds range for each of the lines, so that they can be used as segments.
     * NOTE: LineToolsTest does not test the function formLinesAroundPoint.
     */
    public static Line[] formLinesAroundPoint(Coordinates point, Direction direction, double length, double width){
        Line[] lines = new Line[4];
        Coordinates[] points = formRectagleAroundPoint(point, direction, length, width);
        
        for(int i=0; i<3; i++){
            if(points[i].getX() == points[i+1].getX())
                lines[i] = new Line(points[i], points[i+1], Math.min(points[i].getY(),points[i+1].getY()), Math.max(points[i].getY(), points[i+1].getY()));
            else
                lines[i] = new Line(points[i], points[i+1], Math.min(points[i].getX(),points[i+1].getX()), Math.max(points[i].getX(), points[i+1].getX()));
        }
       if(points[3].getX() == points[0].getX())
           lines[3] = new Line(points[3], points[0], Math.min(points[3].getY(),points[0].getY()), Math.max(points[3].getY(), points[0].getY()));
       else
           lines[3] = new Line(points[3], points[0], Math.min(points[3].getX(),points[0].getX()), Math.max(points[3].getX(), points[0].getX()));
        
        return lines;
    }
    
    /**
     * @return the line from the array of lines which is first intersected by a given line.
     */
    public static Line lineIntersectingLines(Line line, Line[] lines){
        Line closestLine;
        closestLine = null;
        double minDistance = -1;
        double newDistance;
        
        Coordinates firstFeasiblePoint;
        if(line.getDirection())
            firstFeasiblePoint = new Coordinates(line.getRangeMin(), line.getRangeMin()*line.getGradient()+line.getOffset());
        else firstFeasiblePoint = new Coordinates(line.getRangeMax(), line.getRangeMax()*line.getGradient()+line.getOffset());
        
        for(int i=0; i<lines.length; i++){
            Coordinates intersectionPoint = intersectionOfLines(line, lines[i]);
            if(intersectionPoint != null){
                newDistance = firstFeasiblePoint.distance(intersectionPoint);
                if(minDistance > newDistance || minDistance < 0){
                    minDistance = newDistance;
                    closestLine = lines[i];
                }
            }            
        }
        
        return closestLine;
    }

    /**
     * This function returns the side of the table or the robots the ball will hit first.
     */
    public static Line ballOnTheTable(Line ball, Coordinates robot1, Direction dirR1, double lengthR1, double widthR1, Coordinates robot2, Direction dirR2, double lengthR2, double widthR2){
        //Basically, this function uses the function lineIntersectingLines().
        Line[] lines = new Line[12];
        
        Coordinates[] tableCorners = new Coordinates[4];
        tableCorners[0] = new Coordinates(0,0);
        tableCorners[1] = new Coordinates(2,0);
        tableCorners[2] = new Coordinates(2,1);
        tableCorners[3] = new Coordinates(0,1);
        
        lines[0] = new Line(tableCorners[0],tableCorners[1],0,2);
        lines[1] = new Line(tableCorners[1],tableCorners[2],0,1);
        lines[2] = new Line(tableCorners[2],tableCorners[3],0,2);
        lines[3] = new Line(tableCorners[3],tableCorners[0],0,1);
        
        lines[4] = formLinesAroundPoint(robot1, dirR1, lengthR1, widthR1)[0];
        lines[5] = formLinesAroundPoint(robot1, dirR1, lengthR1, widthR1)[1];
        lines[6] = formLinesAroundPoint(robot1, dirR1, lengthR1, widthR1)[2];
        lines[7] = formLinesAroundPoint(robot1, dirR1, lengthR1, widthR1)[3];
        
        lines[8] = formLinesAroundPoint(robot2, dirR2, lengthR2, widthR2)[0];
        lines[9] = formLinesAroundPoint(robot2, dirR2, lengthR2, widthR2)[1];
        lines[10] = formLinesAroundPoint(robot2, dirR2, lengthR2, widthR2)[2];
        lines[11] = formLinesAroundPoint(robot2, dirR2, lengthR2, widthR2)[3];
        
        return lineIntersectingLines(ball, lines);
    }
	
	public static double angleBetweenLines(Line l, Line m){
        double gamma;
        
//        gamma = Math.acos((1+l.getGradient()*m.getGradient())/(Math.sqrt(1+l.getGradient()*l.getGradient())*Math.sqrt(1+m.getGradient()*m.getGradient())));
		double lVectorX = l.getFirstPoint().getX() - l.getSecondPoint().getX();
		double lVectorY = l.getFirstPoint().getY() - l.getSecondPoint().getY();
		double mVectorX = m.getFirstPoint().getX() - m.getSecondPoint().getX();
		double mVectorY = m.getFirstPoint().getY() - m.getSecondPoint().getY();
        gamma = (Math.atan2(mVectorY, mVectorX) - Math.atan2(lVectorY, lVectorX)) % Math.PI;
        return gamma;
    }
	
	
	public static double getArcRadius (Coordinates nextWayPoint, Coordinates ourPosition, Direction ourOrientation) {
		Line l = new Line (ourPosition, nextWayPoint);
		if (Math.abs(l.getLength()) < 0.15) return 0;
//		System.out.println("Distance to waypoint " + l.getLength());
		double angle = Math.abs(angleBetweenLineAndDirection(l,ourOrientation));
		return Math.min(5 / Math.pow((Math.sin(angle/2)), 2), 30*nextWayPoint.distance(ourPosition)/(Math.sin(angle)));
		
	}
}
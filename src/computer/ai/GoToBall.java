package computer.ai;

import brick.Brick;
import computer.control.ControlInterface;
import computer.simulator.*;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
/**
 * This class will get the ball coordinates and direct the robot towards it.
 */
public class GoToBall extends AI {

    private Date date = new Date(0);
    private boolean rotatingRight = false;
    private boolean rotatingLeft = false;
    private boolean forward = false;
    private boolean complete = false;
    private Line north;
    private Line east;
    private Line south;
    private Line west;
    private final static double ANGLETHRESHOLD = Math.PI / 4;
    private final static double DISTANCETHRESHOLD = 0.3;

    public GoToBall(Pitch pitch, Robot robot) {
        super(pitch, robot);
        north = pitch.getNorthWall();
        east = pitch.getEastWall();
        south = pitch.getSouthWall();
        west = pitch.getWestWall();
    }

    @Override
    public void run() {
        if (((new Date().getTime() - date.getTime()) > 40) && !complete) {
            actionPlan = new ArrayList<Coordinates>();
            correctPlan();
            if (this.self.getCommState() == ControlInterface.READY) {
                issueCommands();
            }
        }
    }

    private void correctPlan() {
        Coordinates ballCoordinates = this.pitch.ball.getPosition();
        double x;
        double y;
        x = Math.min(ballCoordinates.getX(), 0.6);
        x = Math.max(ballCoordinates.getX(), 0.4);
        y = Math.min(ballCoordinates.getY(), 1.6);
        y = Math.max(ballCoordinates.getY(), 0.4);
        Coordinates coordinates = new Coordinates(x, y);
//        if ((new Line(ballCoordinates, this.self.getPosition())).getLength() < DISTANCETHRESHOLD) {
//            coordinates = ballCoordinates;
//        }
        if (this.actionPlan.isEmpty()) {
            actionPlan.add(coordinates);
        } else if (Coordinates.distance(this.actionPlan.get(0), coordinates) > 0.05) {
            this.actionPlan.set(0, coordinates);
        }
    }

    private void issueCommands() {
        date = new Date();
        Robot robotinho = this.self;
        Ball ball = this.pitch.ball;
//        System.out.println("Ball (" + ball.getCoordinates().getX() + ", " + ball.getCoordinates().getY() + ")");
        Line lineToBall = new Line(robotinho.getPosition(), this.actionPlan.get(0));
        double angle = LineTools.angleBetweenLineAndDirection(lineToBall, robotinho.getOrientation());
//        System.out.println("Current angle: " + robotinho.getOrientation().getDirectionDegrees() + " Rotating to " + angle);
        System.out.println("Distance to ball " + lineToBall.getLength());
//        if (Math.abs(angle) < ANGLETHRESHOLD && lineToBall.getLength() < DISTANCETHRESHOLD) {
//            robotinho.stop();
//            complete = true;
//            rotatingLeft = false;
//            rotatingRight = false;
//            forward = false;
//            return;
//        }
        if (Math.abs(angle) > ANGLETHRESHOLD) {
            if (angle < 0) {
                if (!rotatingRight) {
                    robotinho.rotateRight(100);
                    rotatingRight = true;
                    rotatingLeft = false;
                    forward = false;
                }
            } else {
                if (!rotatingLeft) {
                    robotinho.rotateLeft(100);
                    rotatingLeft = true;
                    rotatingRight = false;
                    forward = false;
                }
            }
        } else if (forward == false) {
            rotatingLeft = false;
            rotatingRight = false;
            robotinho.forward(Brick.MEDIUM);
            forward = true;
        }
    }

    public void robotCollided() {
        System.out.println("Collision");
        rotatingLeft = false;
        rotatingRight = false;
        forward = false;
    }
}
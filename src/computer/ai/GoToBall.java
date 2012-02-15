package computer.ai;

import brick.Brick;
import computer.ai.AI;
import computer.control.ControlInterface;
import computer.simulator.*;
import computer.simulator.PixelCoordinates;
import java.util.Date;
import java.util.ArrayList;
/**
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */

/**
 * This class will get the ball coordinates and direct the robot towards it.
 */

public class GoToBall extends AI{
   
    public PixelCoordinates blue;
    public PixelCoordinates yellow;
    public PixelCoordinates ball;
    private Date date = new Date(0);
    private boolean rotatingRight = false;
    private boolean rotatingLeft = false;
    private boolean forward = false;
    private boolean complete = false;
    
    public GoToBall (Pitch pitch, Robot robot) {
        super(pitch, robot);
    }

    @Override
    public void run() {
        if (((new Date().getTime() - date.getTime()) > 40 ) && !complete) {
            actionPlan = new ArrayList<Coordinates>();
            correctPlan();
            if (this.self.getCommState() == ControlInterface.READY) {
                issueCommands();
            }
        }
    }
    
    private void correctPlan() {
        Ball ball = this.pitch.ball;
        if (this.actionPlan.isEmpty()) {
            actionPlan.add(ball.getCoordinates());
        } else if (Coordinates.distance(this.actionPlan.get(0), ball.getCoordinates()) > 10){
            this.actionPlan.set(0, ball.getCoordinates().clone());
        }
    }
    
    private void issueCommands() {
        date = new Date();
        Robot robotinho = this.self;
        Ball ball = this.pitch.ball;
//        System.out.println("Ball (" + ball.getCoordinates().getX() + ", " + ball.getCoordinates().getY() + ")");
        Line lineToBall = new Line(robotinho.getPosition(), ball.getCoordinates());
        double angle = LineTools.angleBetweenLineAndDirection(lineToBall, robotinho.getOrientation());
//        System.out.println("Current angle: " + robotinho.getOrientation().getDirectionDegrees() + " Rotating to " + angle);
        System.out.println("Distance to ball " + lineToBall.getLength());
        if (Math.abs(angle) < Math.PI / 5 && lineToBall.getLength() < 0.3) {
            robotinho.stop();
            complete = true;
            rotatingLeft = false;
            rotatingRight = false;
            forward = false;
            return;
        }
        if (Math.abs(angle) > Math.PI / 3) {
            if (angle < 0) {
                if (!rotatingRight) {
                    robotinho.rotateRight();
                    rotatingRight = true;
                    rotatingLeft = false;
                    forward = false;
                }
            } else {
                if (!rotatingLeft) {
                    robotinho.rotateLeft();
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
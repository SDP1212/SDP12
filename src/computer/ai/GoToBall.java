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
    
    public GoToBall (Pitch pitch, Robot robot) {
        super(pitch, robot);
    }

    @Override
    public void run() {
        if (this.self.getCommState() == ControlInterface.READY && ((new Date().getTime() - date.getTime()) > 3000 ) ) {
            actionPlan = new ArrayList<Coordinates>();
            correctPlan();
            issueCommands();
        }
    }
    
    private void correctPlan() {
        Robot robotinho = this.self;
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
        Line lineToBall = new Line(ball.getCoordinates(), robotinho.getPosition());
        double angle = LineTools.angleBetweenLineAndDirection(lineToBall, robotinho.getOrientation());
        System.out.println("Current angle: " + robotinho.getOrientation().getDirectionDegrees() + " Rotating to " + angle);
        if (angle > Math.PI / 100) {
            robotinho.rotate(Math.max(Math.min(angle, Math.PI/100), -Math.PI / 100));
        } else {
            robotinho.forward(Brick.MEDIUM);
        }
    }
        
    
    
}
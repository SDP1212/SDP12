package computer.ai;

import brick.Brick;
import computer.ai.AI;
import computer.simulator.*;
import computer.simulator.PixelCoordinates;
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
    
    public GoToBall (Pitch pitch, Robot robot) {
        super(pitch, robot);
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
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
        Robot robotinho = this.self;
        Ball ball = this.pitch.ball;
        Line lineToBall = new Line(ball.getCoordinates(), robotinho.getPosition());
        double angle = LineTools.angleBetweenLineAndDirection(lineToBall, robotinho.getOrientation());
        robotinho.rotate(angle);
        robotinho.forward(Brick.MEDIUM);
    }
        
    
    
}
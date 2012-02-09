/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import computer.ai.AI;
import computer.simulator.*;
import computer.simulator.PixelCoordinates;
/**
 *
 * @author s0907806
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
        
        
        
        
        
        
    }
    
    private void correctPlan() {
        Robot robotinho = this.pitch.robotinho;
        Ball ball = this.pitch.ball;
        if (this.actionPlan.isEmpty()) {
            actionPlan.add(ball.getCoordinates());
        } else if (Coordinates.distance(this.actionPlan.get(0), ball.getCoordinates()) > 10){
            this.actionPlan.set(0, ball.getCoordinates().clone());
        }
        
    }
    
    private void issueCommands() {
        
    }
        
    
    
}
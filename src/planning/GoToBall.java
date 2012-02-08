/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

import computer.ai.AI;
import computer.simulator.*;
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
    
    
    public GoToBall (Pitch pitch) {
        super(pitch);
    }
    


    @Override
    public void run() {
        
        
        
        
        
        
    }
    
    private void correctPlan () {
        Robot robotinho = this.pitch.robotinho;
        Ball ball = this.pitch.ball;
        if (this.actionPlan.isEmpty()) {
            //actionPlan.a
        }
        
    }
    
}

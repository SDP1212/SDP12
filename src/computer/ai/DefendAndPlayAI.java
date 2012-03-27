package computer.ai;

import brick.Brick;
import computer.simulator.*;
import java.util.ArrayList;

import computer.ApplicationController;

/**
 * 
 * @author Callum Laird
 * /w 
 */

public class DefendAndPlayAI extends Penalty {

	Coordinates upperPost;
	Coordinates lowerPost;
	boolean penaltyMode = true;
        double ball_nemesis_distance;
        Coordinates ball_position;
        boolean transitive_behaviour = true;
        
        protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	protected static int state = SEARCHING;
	private TargetBall nextWayShape = new TargetBall();
	private TargetBall ballShape = new TargetBall();
        
	public DefendAndPlayAI(Pitch pitch, Robot self) {
		super(pitch, self);
                
		VisorRenderer.extraDrawables.clear();
		nextWayShape.setPosition(0, 0);
		ballShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(nextWayShape);
		VisorRenderer.extraDrawables.add(ballShape);
	}
	
	@Override
	public void run() {
	
            ball_nemesis_distance = Coordinates.distance(pitch.nemesis.getPosition(),pitch.ball.getPosition());
            
            // Checks conditions for an as yet untaken penalty.
            if (ball_nemesis_distance > 0.2) {
                penaltyMode = false;
            }
            
            // Does penalty stuff while penalty conditions are met.
            if (penaltyMode) {

                double x_Distance = Math.abs(self.getPosition().getX() - pitch.nemesis.getPosition().getX());
                double y_intercept;

                // Calculates the y_intercept using the opponent's position and orientaton.
                if (self.getPosition().getX() > 1) {
                    y_intercept =  pitch.nemesis.getPosition().getY() + Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
                }  else {
                    y_intercept =  pitch.nemesis.getPosition().getY() - Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
                }
                
                if (!inRange(y_intercept, self.getPosition().getY())) {
                    if (self.getPosition().getY() < (y_intercept) &&
                        self.getPosition().getY() < .66){

                            self.forward(Brick.SLOW);

                    } else if (self.getPosition().getY() > (y_intercept) &&
                               self.getPosition().getY() > .34) {

                            self.backward(Brick.SLOW);

                    }
                } else {
                        self.stop();
                }
                
            // Else does match stuff.
            } else {
                
                // Initial transitive behaviour from penalty to match - turns away from own goal.
                if (transitive_behaviour) {
                    if (facingTargetGoal()) {
                        transitive_behaviour = false;
                    } else {
                        // Conditions for either side of pitch.
                        if (self.getPosition().getX() < 1.0) {
                            self.rotateRight((int) (Brick.MEDIUM));
                        } else {
                            self.rotateLeft((int) (Brick.MEDIUM));
                        }
                    }
                
                // Normal match behaviour
                } else {
                    
                    ApplicationController apcon = new ApplicationController();
                    apcon.changeAI(self.getColour(), PathSearchAI.class);
                    
                }
            }
        }
        
        public boolean inRange(double y_intercept, double robot_y) {
            if (robot_y < (y_intercept + 0.04) &&
                robot_y > (y_intercept - 0.04)) {
                    return true;
            }
            return false;
        }
        
        protected boolean facingTargetGoal() {
		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
		return (LineTools.angleBetweenLineAndDirection(lineToLowerPost, self.getOrientation()) * LineTools.angleBetweenLineAndDirection(lineToUpperPost, self.getOrientation()) < 0);
	}
                
        @Override
	public void robotCollided() {
		
	}
        
        @Override
        public ArrayList<Coordinates> getActionPlan(){
            return null;
         }
        
        @Override
        protected boolean isNearPost(Coordinates post) {
		Line lineToPost = new Line(self.getPosition(), post);
		return lineToPost.getLength() < 0.4;
	}
}
package computer.ai;

import brick.Brick;
import computer.simulator.*;
import computer.vision.WorldState;

public class GoalkeeperAI extends Penalty {

	Coordinates upperPost;
	Coordinates lowerPost;
	
	public GoalkeeperAI(Pitch pitch, Robot self) {
		super(pitch, self);
	}
	
	@Override
	public void run() {
		
            // Need it to keep running until it somehow realises penalty has been taken.
            // Perhaps check ball position against nemesis position.  If it's too far then go back to match AI?

            double x_Distance = Math.abs(self.getPosition().getX() - pitch.nemesis.getPosition().getX());
            //System.out.println("DEBUG: x_distance = " + x_Distance);
            double y_intercept;

            // Calculates the y_intercept using the opponent's position and orientaton.
            if (self.getPosition().getX() > 1) {
                y_intercept =  pitch.nemesis.getPosition().getY() + Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
            }  else {
                y_intercept =  pitch.nemesis.getPosition().getY() - Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
            }
            //System.out.println("DEBUG: y position = " + self.getPosition().getY() + ", y intercept of the other robot = " + y_intercept); 

            if (!inRange(y_intercept, self.getPosition().getY())) {
                // Forward/Backward might be wrong way round...  Also check for both pitch sides.
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
	}

        public boolean inRange(double y_intercept, double robot_y) {
            if (robot_y < (y_intercept + 0.04) &&
                robot_y > (y_intercept - 0.04)) {
                    return true;
            }
            return false;
        }
        
        @Override
	public void robotCollided() {
		
	}
}
package computer.ai;

import brick.Brick;
import computer.simulator.*;

public class GoalkeeperAI extends Penalty {

	Coordinates upperPost;
	Coordinates lowerPost;
	
	public GoalkeeperAI(Pitch pitch, Robot self) {
		super(pitch, self);
	}
	
	@Override
	public void run() {

		// Calculates the x distance
		//int x_Distance = Math.abs(self.getPosition().getX() - pitch.nemesis.getPosition().getX());

		// Or we could just define it as a constant which would be safer...
                // Assuming distance is about 65.
		int x_Distance = 65;

		// Need it to keep running until it somehow realises penalty has been taken.
		// Perhaps check ball position against nemesis position.  If it's too far then go back to match AI?
		while (true) {
			// Calculates the y_intercept using the opponent's position and orientaton.
			int y_intercept = (int) (pitch.nemesis.getPosition().getY() + Math.atan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance);

			// Forward/Backward might be wrong way round...  Also check for both pitch sides.
			if (self.getPosition().getY() < (y_intercept + 10) &&
                            self.getPosition().getY() < pitch.getOurGoal().getUpperPostCoordinates().getY()){
                            
				self.forward(Brick.MEDIUM);
                                
			} else if (self.getPosition().getY() > (y_intercept - 10) &&
                                   self.getPosition().getY() > pitch.getOurGoal().getLowerPostCoordinates().getY()) {
                            
				self.backward(Brick.MEDIUM);
                                
			} else {
				self.stop();
			}
		}
	}

        /*
	protected boolean isNearPost(Coordinates post) {
		Line lineToPost = new Line(self.getPosition(), post);
		return lineToPost.getLength() < 0.4;
	}*/
        
        @Override
	public void robotCollided() {
		
	}
}
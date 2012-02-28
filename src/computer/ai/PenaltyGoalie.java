package computer.ai;

import brick.Brick;
import computer.simulator.*;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class PenaltyGoalie extends Penalty {
	
	Coordinates upperPost;
	Coordinates lowerPost;

	public PenaltyGoalie(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	@Override
	public void run() {
		if (isNearPost(pitch.getTargetGoal().getUpperPostCoordinates())) {
			self.backward(Brick.MEDIUM);
		} else if (isNearPost(pitch.getTargetGoal().getLowerPostCoordinates())) {
			self.forward(Brick.MEDIUM);
		}
	}

	@Override
	public void robotCollided() {
		
	}
	
}

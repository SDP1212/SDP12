package computer.ai;

import computer.simulator.Coordinates;
import computer.simulator.Line;
import computer.simulator.Pitch;
import computer.simulator.Robot;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public abstract class Penalty extends AI {

	public Penalty(Pitch pitch, Robot self) {
		super(pitch, self);
	}

	protected boolean isNearPost(Coordinates post) {
		Line lineToPost = new Line(self.getPosition(), post);
		return lineToPost.getLength() < 0.4;
	}
	
}

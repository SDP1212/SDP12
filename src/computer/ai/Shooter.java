/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import computer.simulator.*;

/**
 *
 * @author s0935251
 */
public class Shooter extends AI {

	public Shooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	
	@Override
	public void run() {
		Goal goal = pitch.getTargetGoal();
//		We're getting faulty goal coords.
//		Box shootingBox = new Box(goal.getLowerPostCoordinates(), 
//				new Coordinates(
//				goal.getUpperPostCoordinates().getX() + 0.5, goal.getUpperPostCoordinates().getY()));
		Box shootingBox = new Box(new Coordinates(0, 0), new Coordinates(0.5, 1));
		System.out.println("Goals" + goal);
		System.out.println("Box" + shootingBox);
		System.out.println("Robot" + self.getPosition());
		if (shootingBox.isPointInside(self.getPosition())) {
			self.kick();
		}
	}

	@Override
	public void robotCollided() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import computer.simulator.*;
import java.util.Date;

/**
 * An AI strategy.
 * 
 * Dribble the ball towards target goal. When in goal area, shoot.
 * 
 * @author Matt Jeffryes
 */
public class Shooter extends AI {
	private Date shotTime = new Date(0);

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
		if (shootingBox.isPointInside(self.getPosition()) && (new Date().getTime() - shotTime.getTime() > 2000)) {
			self.kick();
			shotTime = new Date();
		}
	}

	@Override
	public void robotCollided() {
		
	}
	
}

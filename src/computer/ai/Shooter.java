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
		Box shootingBox = new Box(goal.getLowerPostCoordinates(), 
				new Coordinates(
				goal.getUpperPostCoordinates().getX() + 0.5, goal.getUpperPostCoordinates().getY()));
		if (shootingBox.isPointInside(self.getPosition()) && (new Date().getTime() - shotTime.getTime() > 2000)) {
			self.kick();
			shotTime = new Date();
		}
	}

	@Override
	public void robotCollided() {
		
	}
	
}

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
	private static final int SEARCHING = 0;
	private static final int DRIBBLING = 1;
	private static final int SHOT = 2;
	
	private Date shotTime = new Date(0);
	private int state = SEARCHING;

	public Shooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	
	@Override
	public void run() {
//		Goal goal = pitch.getTargetGoal();
//		Box shootingBox = new Box(goal.getLowerPostCoordinates(), 
//				new Coordinates(
//				goal.getUpperPostCoordinates().getX() + 0.5, goal.getUpperPostCoordinates().getY()));
//		if (shootingBox.isPointInside(self.getPosition()) && (new Date().getTime() - shotTime.getTime() > 2000)) {
//			self.kick();
//			shotTime = new Date();
//		}
		updateState();
	}
	
	private void updateState() {
		switch (state) {
			case SEARCHING :
				if (facingBall() && nearBall()) {
					state = DRIBBLING;
				}
				break;
			case DRIBBLING :
				if (!facingBall() || !nearBall()) {
					state = SEARCHING;
				} else if (new Date().getTime() - shotTime.getTime() < 1500) {
					state = SHOT;
				}
				break;
			case SHOT :
				if (new Date().getTime() - shotTime.getTime() >= 1500) {
					state = SEARCHING;
				}
				break;
		}
		System.out.println("State: " + state);
	}
	
	private boolean facingBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 16) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		if (lineToBall.getLength() < 0.2) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized void robotCollided() {
		
	}
	
}

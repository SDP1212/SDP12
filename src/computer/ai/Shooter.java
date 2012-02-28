/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import brick.Brick;
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
	protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	
	private Date shotTime = new Date(0);
	private Date movementTime = new Date(0);
	private int state = SEARCHING;
	private int firstRun = 0;

	public Shooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	
	@Override
	public void run() {
		updateState();
		if (state == DRIBBLING) {
			if (inShootingBox() && facingGoal()) {
				self.kick();
				shotTime = new Date();
			}
		} else if (state == SEARCHING && (new Date().getTime() - movementTime.getTime() > 40)) {
			Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
			double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());

			if (firstRun < 20) {
				self.forward(Brick.FAST);
				firstRun++;
			} else {
	//			double angle = Math.toDegrees(LineTools.angleBetweenLineAndDirection(lineToBall, new Direction(0))) + 180;
	//			self.setHeading((int)angle);
//				System.out.println("Heading: " + angle);
				if (!facingBall()) {
					//System.out.println("Not facing ball");
					
					if (angle < 0) {
						self.arcLeft(8);
					} else {
						self.arcRight(8);
					}
                                        
				} else if (!nearBall()) {
					//System.out.println("Speed: " + Math.round(lineToBall.getLength() * Brick.FAST));
					self.forward((int)Math.round(lineToBall.getLength() * Brick.FAST * 0.5) + 200);
				}
				movementTime = new Date();
			}
		}
	}
	
	protected  void updateState() {
		switch (state) {
			case SEARCHING :
				if (facingBall() && nearBall()) {
					state = DRIBBLING;
				}
				break;
			case DRIBBLING :
				if (!nearBall()) {
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
	}
	
	protected boolean facingBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 8) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		if (lineToBall.getLength() < 0.2) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean inShootingBox() {
		Coordinates c1 = pitch.getTargetGoal().getLowerPostCoordinates();
		Coordinates c2 = new Coordinates(pitch.getTargetGoal().getUpperPostCoordinates().getX() * 0.2 + pitch.getCentreSpot().getX(), pitch.getTargetGoal().getUpperPostCoordinates().getY());
		Box shootingBox = new Box(c1, c2);
		return shootingBox.isPointInside(self.getPosition());
	}
	
	protected boolean facingGoal() {
            Coordinates centreOfGoal = new Coordinates((pitch.getTargetGoal().getLowerPostCoordinates().getX() +pitch.getTargetGoal().getUpperPostCoordinates().getX())/2 , pitch.getTargetGoal().getLowerPostCoordinates().getY());
            Line lineToCentreOfPitch = new Line (self.getPosition(), centreOfGoal);
            double angle = LineTools.angleBetweenLineAndDirection (lineToCentreOfPitch, self.getOrientation());
            if (Math.abs(angle) < Math.PI/4) {
                System.out.println("facing goal");
                return true;
            } else {
                return false;
            }
	}

	@Override
	public synchronized void robotCollided() {
		System.out.println("Collision");
	}
	
}

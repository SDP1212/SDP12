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
	
	private static final int NOTHING = 0;
	private static final int FORWARD = 1;
	private static final int ARCLEFT = 2;
	private static final int ARCRIGHT = 3;
	private int movementState = NOTHING;
	
	private Date shotTime = new Date(0);
	private Date movementTime = new Date(0);
	private Date rotatingTime = new Date(0);
	
	private int state = SEARCHING;
	private int firstRun = 0;
	private boolean stopped = false;
	private TargetBall targetShape = new TargetBall();
	private SimulateLine simLine = new SimulateLine();

	public Shooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
		targetShape.setPosition(0, 0);
		simLine.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(targetShape);
		VisorRenderer.extraDrawables.add(simLine);
	}

	
	@Override
	public void run() {
		updateState();

		if (state == DRIBBLING) {
			if ((inShootingBox() || unobstructedShot()) && facingGoal()) {
				self.kick();
				shotTime = new Date();
			}
		} else if (state == SEARCHING  && (new Date().getTime() - movementTime.getTime() > 10)) {
			Line lineToBall = new Line(self.getPosition(), target());
			double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());

			if (firstRun < 15) {
				self.forward(Brick.SLOW);
				movementState = FORWARD;
				firstRun++;
			} else {
//				double angle = Math.toDegrees(LineTools.angleBetweenLineAndDirection(lineToBall, new Direction(0))) + 180;
	//			self.setHeading((int)angle);
//				System.out.println("Heading: " + angle);
				if (!facingBall()) {
					stopped = false;
					//System.out.println("Not facing ball");
					
					if (angle < 0) {
						self.rotateLeft(Brick.SLOW);
						//self.arcLeft(8);
						movementState = ARCLEFT;
						rotatingTime = new Date();
					} else {
						self.rotateRight(Brick.SLOW);
						//self.arcRight(8);
						movementState = ARCRIGHT;
						rotatingTime = new Date();
					}                   
				} else if (!nearBall()) {
					//System.out.println("Speed: " + Math.round(lineToBall.getLength() * Brick.FAST));
//					self.forward((int)Math.round(lineToBall.getLength() * Brick.FAST * 0.5) + 200);
//					rotatingTime = new Date(0);
					if (!stopped) {
						self.stop();
						stopped = true;
					} else {
						self.forward(Brick.SLOW);
						movementState = FORWARD;
						self.stop();
					}
				} else {
					self.stop();
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
				} else if (new Date().getTime() - shotTime.getTime() < 1000) {
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
		Coordinates targetPoint;
		if (onTarget()) {
			System.out.println("On target");
			targetPoint = pitch.ball.getPosition();
		} else {
			targetPoint = target();
		}
		Line lineToBall = new Line(self.getPosition(), targetPoint);
		simLine.setPosition(self.getPosition().getX(), self.getPosition().getY());
		//simLine.setPosition((self.getPosition().getX()+ targetPoint.getX())/2, (self.getPosition().getY()+ targetPoint.getY())/2);
		simLine.setP1(self.getPosition());
		simLine.setP2(targetPoint);
		
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
//		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 10) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		if (lineToBall.getLength() < 0.15) {
			System.out.println("Near ball");
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean onTarget (){
		Line lineToTarget = new Line (self.getPosition(), target());
		return lineToTarget.getLength() < 0.1;
	}
	
	protected boolean unobstructedShot() {
		Line lineToBall = new Line(self.getPosition(), target());
		Line lineToEnemy = new Line(self.getPosition(), pitch.nemesis.getPosition());
		double angle = LineTools.angleBetweenLines(lineToBall, lineToEnemy);
		return (Math.abs(angle) > Math.PI / 6);
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
	
	protected Coordinates target() {
		double x = (pitch.getCentreSpot().getX() - pitch.getTargetGoal().getUpperPostCoordinates().getX()) * 0.1 + pitch.ball.getPosition().getX();
		double y = (pitch.ball.getPosition().getY() - 0.5) * 0.3 + pitch.ball.getPosition().getY();
		targetShape.setPosition(x, y);
		//System.out.println("Target: X = " + x + " Y = " + y);
		return new Coordinates(x,y);
	}

	@Override
	public synchronized void robotCollided() {
		System.out.println("Collision");
		movementState = NOTHING;
		
	}
	
}

package computer.ai;

import brick.Brick;
import computer.Communication;
import computer.simulator.*;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class PenaltyShooter extends Penalty {

	boolean penalty = false;
	boolean penalty2 = true;

	public PenaltyShooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	@Override
	public void run() {

		if (penalty) {
			java.util.Random gen = new Random();
			long date = new Date().getTime();
			int time = gen.nextInt(3000) + 500;
			int direction = gen.nextInt(2);
			int angleplus = gen.nextInt(30) + 1;
			while (new Date().getTime() - date < time) {
			}

			if (direction == 1) {
				self.rotate(Math.toRadians(angleplus));
			} else {
				self.rotate(-Math.toRadians(angleplus));
			}

			long wait = new Date().getTime();
			while (new Date().getTime() - wait < 1000) {
			}
			self.forward(Brick.SLOW);
			wait = new Date().getTime();
			while (new Date().getTime() - wait < 250) {
			}
			System.out.println("kick");
			self.kick();
			penalty = false;
		}
		if (penalty2) {
			long wait = new Date().getTime();
			while (new Date().getTime() - wait < 1500) {
			}
//			if (unobstructedShot()) {
//				self.forward(Brick.SLOW);
//				wait = new Date().getTime();
//				while (new Date().getTime() - wait < 250) {
//				}
//				self.kick();
//				penalty2 = false;
//			} else {
				/*
				 * Note to self for morning: none of this has been debugged
				 * Need to test:
				 * 1) That rotate function turns towards the proper goals
				 * 2) That the rotate function faces a "scorable opportunity"
				 * 3) That the delay is long enough for the enemy robot to enter "following zone"
				 * 4) That the "following zone" is large enough
				 * 5) Check if subtract variable is correct (guesstimation for values)
				 */



				//Identify if the robot follows the ball or not
				//First we identify the corners
//			double yDiffLow = Math.abs(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getY() - self.getPosition().getY());
//			double xDiffLow = Math.abs(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX() - self.getPosition().getX());
//			double yDiffHigh = Math.abs(pitch.getEnemyTargetGoal().getUpperPostCoordinates().getY() - self.getPosition().getY());
//			double xDiffHigh = Math.abs(pitch.getEnemyTargetGoal().getUpperPostCoordinates().getX() - self.getPosition().getX());
//			double angleToLow = Math.toRadians(Math.atan2(yDiffLow, xDiffLow));
//			double angleToHigh = Math.toRadians(Math.atan2(yDiffHigh, xDiffHigh));
				double angleToLower = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates()), self.getOrientation());
				double angleToUpper = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates()), self.getOrientation());
				System.out.println("angleToLower = " + angleToLower + "; angleToupper = " + angleToUpper);
				boolean down = false;
				boolean up = false;

				//Rotate to the lower end of the goal to see if the robot follows
				self.rotate(angleToLower);

				//Make sure the other robot has time to follow
				while (new Date().getTime() - wait < 1000) {
				}

				//Check if the robot is near the ball
				if (isNearGoal(pitch.nemesis.getPosition(), pitch.getEnemyTargetGoal().getLowerPostCoordinates(), .2)) {
					down = true;
				}

				//Rotate to the higher end of the goal to see if the robot follows
				self.rotate(angleToUpper);

				//Make sure the other robot has time to follow
				wait = new Date().getTime();
				while (new Date().getTime() - wait < 1000) {
				}
				if (isNearGoal(pitch.nemesis.getPosition(), pitch.getEnemyTargetGoal().getUpperPostCoordinates(), .2)) {
					up = true;
				}

				if (up && down) {
					//Following - rotate to the op
					angleToLower = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates()), self.getOrientation());
					self.rotate(angleToLower);
					wait = new Date().getTime();
					while (new Date().getTime() - wait < 1000) {
					}
					self.forward(Brick.SLOW);
					wait = new Date().getTime();
					while (new Date().getTime() - wait < 250) {
					}
					self.kick();
					penalty2 = false;


				} else {
					//Not Following
					Coordinates current = pitch.nemesis.getPosition();
					double neartop = current.distance(pitch.getEnemyTargetGoal().getLowerPostCoordinates());
					double nearbottom = current.distance(pitch.getEnemyTargetGoal().getUpperPostCoordinates());
					//Check if in middle
					double subtract = Math.abs(neartop - nearbottom);

					if (nearbottom < neartop && subtract > .1) {
						//Near bottom - shoot to the top
						angleToUpper = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates()), self.getOrientation());
						self.rotate(angleToUpper);
						wait = new Date().getTime();
						while (new Date().getTime() - wait < 1000) {
						}
						self.forward(Brick.SLOW);
						wait = new Date().getTime();
						while (new Date().getTime() - wait < 250) {
						}
						self.kick();
						penalty2 = false;

					} else if (nearbottom > neartop && subtract > .1) {
						//Near top - shoot to the bottom
						angleToLower = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates()), self.getOrientation());
						self.rotate(angleToLower);
						wait = new Date().getTime();
						while (new Date().getTime() - wait < 1000) {
						}
						self.forward(Brick.SLOW);
						wait = new Date().getTime();
						while (new Date().getTime() - wait < 250) {
						}
						self.kick();
						penalty2 = false;

					} else {
						//in the middle - check if its moving up or down
						wait = new Date().getTime();
						while (new Date().getTime() - wait < 1000) {
						}
						Coordinates current2 = pitch.nemesis.getPosition();
						if ((current2.getY() - current.getY()) > 0) {
							//moving up - shoot to the bottom
							angleToLower = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates()), self.getOrientation());
							self.rotate(angleToLower);
							wait = new Date().getTime();
							while (new Date().getTime() - wait < 1000) {
							}
							self.forward(Brick.SLOW);
							wait = new Date().getTime();
							while (new Date().getTime() - wait < 250) {
							}
							self.kick();
							penalty2 = false;
						} else {
							//moving down - shoot to the top
							angleToUpper = LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates()), self.getOrientation());
							self.rotate(angleToUpper);
							wait = new Date().getTime();
							while (new Date().getTime() - wait < 1000) {
							}
							self.forward(Brick.SLOW);
							wait = new Date().getTime();
							while (new Date().getTime() - wait < 250) {
							}
							self.kick();
							penalty2 = false;
						}
					}
				}

				//Check if the robot follows
//			}
		}
	}

	@Override
	public void robotCollided() {
	}

	public boolean isNearGoal(Coordinates p, Coordinates q, double distance) {
		//Arbitrary value - needs debugging
		return (p.distance(q) < distance);
	}

	protected boolean unobstructedShot() {
		Line lineToBall = new Line(self.getPosition(), pitch.getTargetGoal().getCentre());
		Line lineToEnemy = new Line(self.getPosition(), pitch.nemesis.getPosition());
		double angle = LineTools.angleBetweenLines(lineToBall, lineToEnemy);
		return (Math.abs(angle) > Math.PI / 6);
	}
}

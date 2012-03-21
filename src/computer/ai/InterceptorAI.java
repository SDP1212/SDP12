package computer.ai;

import brick.Brick;
import computer.ai.pathsearch.PathSearch;
import computer.simulator.Coordinates;
import computer.simulator.Line;
import computer.simulator.LineTools;
import computer.simulator.Pitch;
import computer.simulator.Robot;
import computer.simulator.TargetBall;
import computer.simulator.VisorRenderer;
import java.util.Date;

/**
 *
 * @author s0907806
 */
public class InterceptorAI extends AI {
	
	protected static final int BEGIN = 3;
	protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	protected int state = BEGIN;
	private Date shotTime = new Date(0);
	private Date movementDate = new Date(0);
	private TargetBall nextWayShape = new TargetBall();
	private TargetBall ballShape = new TargetBall();
	private int firstRun = 1;
	private Coordinates nextWayPoint = new Coordinates(1, 0.5);
	private Coordinates ballPosition0;
	private Coordinates ballFuturePos;
	private int time0 = 1;
	private int timeDelta = 3;
	private int timeToBall = 0;
	private double robotSpeed = 1.0 / 75;
	private Coordinates ballV = new Coordinates(0, 0);
	private Coordinates robotV = new Coordinates(0, 0);
	private Coordinates robotPosition0;
	
	public InterceptorAI(Pitch pitch, Robot self) {
		super(pitch, self);
		VisorRenderer.extraDrawables.clear();
		nextWayShape.setPosition(0, 0);
		ballShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(nextWayShape);
		VisorRenderer.extraDrawables.add(ballShape);
	}
	
	@Override
	public void run() {
		if (firstRun == 1) {
			ballFuturePos = pitch.ball.getPosition();
			ballPosition0 = pitch.ball.getPosition();
			robotPosition0 = self.getPosition();
			nextWayPoint = self.getPosition();
			nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
			self.setSpeed(Brick.FAST);
		}
		if (firstRun > time0 + timeDelta) {
			if (ballPosition0.distance(pitch.ball.getPosition()) < 0.03) {
				ballV.set(0, 0);
			} else {
				//calculate the velocity of the ball
				ballV.set((pitch.ball.getPosition().getX() - ballPosition0.getX()) / (firstRun - time0), (pitch.ball.getPosition().getY() - ballPosition0.getY()) / (firstRun - time0));
			}
			if (robotPosition0.distance(self.getPosition()) < 0.2) {
//				robotV.set(1.0/75, 1.0/75);
				robotSpeed = 1.0 / 100;
			} else {
				//calculate the velocity of the robot
				robotV.set((self.getPosition().getX() - robotPosition0.getX()) / (firstRun - time0), (self.getPosition().getY() - robotPosition0.getY()) / (firstRun - time0));
				robotSpeed = robotV.distance(new Coordinates(0, 0));
			}
			//calculate time to get to ball
			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotSpeed));
//			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotV.getX() * Math.cos(self.getOrientation().getDirectionRadians())));
			double distanceToWaypoint = new Line(self.getPosition(), nextWayPoint).getLength();
			System.out.println("Waypoint: " + nextWayPoint);
			System.out.println("Robot: " + self.getPosition());
			System.out.println("Distance to waypoint: " + distanceToWaypoint);
			if (distanceToWaypoint < 0.2 && state != BEGIN) {
				ballFuturePos.set(ballPosition0.getX() + ballV.getX() * timeToBall, ballPosition0.getY() + ballV.getY() * timeToBall);
				bumpingIntoWalls(ballFuturePos);
				nextWayPoint = ballFuturePos;
				nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
			}
//			System.out.println("update nextwaypoint" + nextWayPoint.getX() + ", " + nextWayPoint.getY());

			ballPosition0 = pitch.ball.getPosition();
			time0 = firstRun;
			robotPosition0 = self.getPosition();
			
		}
		updateState();
//		updateVelocities();

		if (state == BEGIN) {
			self.stop();
		} else if ((state == SEARCHING) && ((new Date().getTime() - movementDate.getTime() > 2))) {
			if (!facingWayPoint()) {
				Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
				Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
				double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
				if (blockedByWall()) {
					self.backward(Brick.SLOW);
				} else {
//					boolean sideOfBall = LineTools.sideOfLine(pitch.ball.getPosition(), lineToWayPoint) > 0;
//					boolean sideOfRobot = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation()) > 0;
//					System.out.println("sideOfball: "+sideOfBall+"; sideOfrobot: "+sideOfRobot);
					int radius = createRadius(nextWayPoint);

//					if (self.getPosition().distance(nextWayPoint) > 0.3 && !((sideOfBall && sideOfRobot) || (!sideOfBall && !sideOfRobot))) { // Arcing
					if (Math.abs(LineTools.angleBetweenLines(lineToBall, lineToWayPoint)) < Math.PI / 16) {
						if (angle < 0) {
//							self.setSpeed(Brick.FAST);
							self.arcLeft(radius);
						} else {
//							self.setSpeed(Brick.FAST);
							self.arcRight(radius);
						}
					} else {
						self.rotate(LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation()));
					}
//					} else { // Normal rotation
//						int speed;
//						if (Math.abs(angle) > Math.PI / 6) {
//							speed = Brick.SLOW;
//						} else {
//							speed = Brick.SLOW;
//						}
//						if (angle < 0) {
//							self.rotateRight(speed);
//						} else {
//							self.rotateLeft(speed);
//						}
//					}
				}
			} else if (!onWayPoint()) {
				self.forward(Brick.FAST);
			} else if (onWayPoint()) {
				self.stop();
			}
			
		} else if (state == DRIBBLING) {
			if (!facingBall()) {
				Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
				double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
				if (angle < 0) {
					self.rotateLeft(Brick.SLOW / 2);
				} else {
					self.rotateRight(Brick.SLOW / 2);
				}
			}
			
		} else if (state == SHOT) {
			if (new Date().getTime() - shotTime.getTime() < 1000) {
				self.forward(Brick.FAST);
			} else {
				self.kick();
			}
			
		}
		movementDate = new Date();
		firstRun++;
	}
	
	protected void updateVelocities() {
		
		if (firstRun > time0 + timeDelta && state != BEGIN) {
			if (ballPosition0.distance(pitch.ball.getPosition()) < 0.05) {
				ballV.set(0, 0);
			} else {
				//calculate the velocity of the ball
				ballV.set((pitch.ball.getPosition().getX() - ballPosition0.getX()) / (firstRun - time0), (pitch.ball.getPosition().getY() - ballPosition0.getY()) / (firstRun - time0));
			}
			if (robotPosition0.distance(self.getPosition()) < 0.2) {
//				robotV.set(1.0/75, 1.0/75);
				robotSpeed = 1.0 / 75;
			} else {
				//calculate the velocity of the robot
				robotV.set((self.getPosition().getX() - robotPosition0.getX()) / (firstRun - time0), (self.getPosition().getY() - robotPosition0.getY()) / (firstRun - time0));
				robotSpeed = robotV.distance(new Coordinates(0, 0));
			}
			//calculate time to get to ball
			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotSpeed * Math.cos(self.getOrientation().getDirectionRadians())));
//			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotV.getX() * Math.cos(self.getOrientation().getDirectionRadians())));

			ballFuturePos.set(ballPosition0.getX() + ballV.getX() * timeToBall, ballPosition0.getY() + ballV.getY() * timeToBall);
			bumpingIntoWalls(ballFuturePos);
			
			nextWayPoint = ballFuturePos;
			nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
			System.out.println("Update");
//			System.out.println("update nextwaypoint" + nextWayPoint.getX() + ", " + nextWayPoint.getY());

			ballPosition0 = pitch.ball.getPosition();
			time0 = firstRun;
			robotPosition0 = self.getPosition();
			
		}
	}
	
	protected Coordinates target() {
		double ballX = pitch.ball.getPosition().getX();
		double ballY = pitch.ball.getPosition().getY();
		double goalX = pitch.getTargetGoal().getUpperPostCoordinates().getX();
		
		double hackedLambda = 0;
		if (pitch.getTargetGoal() == pitch.leftGoal) {
			hackedLambda = 0.3 * 1.5;
		} else {
			hackedLambda = 0.1 * 1.5;
		}
		if (ballY < 0.15 || ballY > 0.85) {
			hackedLambda *= 0.35;
		}
		double deltaX = (pitch.getCentreSpot().getX() - goalX) * hackedLambda;
		
		double x = deltaX + ballX;
		double y = (ballY - 0.5) / (ballX - goalX) * deltaX + ballY;
		
		if (ballY < 0.15 || ballY > 0.85) {
			y = 2 * (ballY - y) + y;
		}
		ballShape.setPosition(x, y);
		return new Coordinates(x, y);
	}
	
	private void getNextWayPoint() {
		nextWayPoint = PathSearch.getNextWaypoint(1, pitch.ball.getPosition(), target(), self.getPosition(), self.getOrientation().getDirectionRadians(), pitch.nemesis.getPosition());
		nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
	}
	
	private void updateState() {
		switch (state) {
			case BEGIN:
				System.out.println("Begin");
				if (ballPosition0.distance(pitch.ball.getPosition()) > 0.05) {
					state = SEARCHING;
				}
				break;
			case SEARCHING:
				System.out.println("Searching");
				if (onTarget() && nearBall()) {
					state = DRIBBLING;
				}
				break;
			case DRIBBLING:
				System.out.println("Dribbling");
				Date now = new Date();
				if (!nearBall()) {
					state = SEARCHING;
				} else if (facingBall()) {
					state = SHOT;
					shotTime = new Date();
				}
				break;
			case SHOT:
				System.out.println("Shot");
				if (new Date().getTime() - shotTime.getTime() >= 2000) {
					state = SEARCHING;
				}
				break;
		}
	}
	
	protected boolean onTarget() {
		Line lineToTarget = new Line(self.getPosition(), target());
		double distanceToBall = new Line(self.getPosition(), pitch.ball.getPosition()).getLength();
		return lineToTarget.getLength() < 0.1;
	}
	
	protected boolean facingTarget() {
		Coordinates targetPoint;
		targetPoint = target();
		
		Line lineToBall = new Line(self.getPosition(), targetPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 16) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean facingBall() {
		Coordinates targetPoint;
		targetPoint = pitch.ball.getPosition();
		Line lineToBall = new Line(self.getPosition(), targetPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		return (Math.abs(angle) < Math.PI / 18);
	}
	
	private boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		return lineToBall.getLength() < 0.3;
	}
	
	private boolean facingWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
		return Math.abs(angle) < Math.PI / 6;
	}
	
	private boolean onWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		return lineToWayPoint.getLength() < 0.2;
	}
	
	private boolean facingTargetGoal() {
		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
		return (LineTools.angleBetweenLineAndDirection(lineToLowerPost, self.getOrientation()) * LineTools.angleBetweenLineAndDirection(lineToUpperPost, self.getOrientation()) < 0);
	}
	
	private boolean blockedByWall() {
		Coordinates nWallPoint = new Coordinates(self.getPosition().getX(), 1);
		Coordinates sWallPoint = new Coordinates(self.getPosition().getX(), 0);
		Coordinates closePoint = null;
		if (new Line(self.getPosition(), nWallPoint).getLength() < 0.2) {
			closePoint = nWallPoint;
		} else if (new Line(self.getPosition(), sWallPoint).getLength() < 0.2) {
			closePoint = sWallPoint;
		}
		if (closePoint != null) {
			return Math.abs(LineTools.angleBetweenLineAndDirection(new Line(self.getPosition(), closePoint), self.getOrientation())) < Math.PI / 4;
		}
		return false;
	}
	
	protected boolean unobstructedShot() {
		Line lineToBall = new Line(self.getPosition(), target());
		Line lineToEnemy = new Line(self.getPosition(), pitch.nemesis.getPosition());
		double angle = LineTools.angleBetweenLines(lineToBall, lineToEnemy);
		return (Math.abs(angle) > Math.PI / 6);
	}
	
	protected int createRadius(Coordinates nextPoint) {
		double radius = LineTools.getArcRadius(nextPoint, self.getPosition(), self.getOrientation());
//		System.out.println("Radius " + (int) radius);
		return (int) radius;
	}
	
	protected int ballInOurCorner() {
		Coordinates b = pitch.ball.getPosition();
		double d1 = b.distance(new Coordinates(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX(), 0));
		double d2 = b.distance(new Coordinates(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX(), 1));
		if (d1 < 0.3) {
			return 0;
		} else if (d2 < 0.3) {
			return 1;
		}
		return -1;
	}
	
	protected boolean ballInEnemyCorner() {
		Coordinates b = pitch.ball.getPosition();
		double d1 = b.distance(new Coordinates(pitch.getTargetGoal().getLowerPostCoordinates().getX(), 0));
		double d2 = b.distance(new Coordinates(pitch.getTargetGoal().getLowerPostCoordinates().getX(), 1));
		return (d1 < 0.2 || d2 < 0.2);
	}
	
	@Override
	public void robotCollided() {
		getNextWayPoint();
		System.out.println("Collision");
	}
	
	public void bumpingIntoWalls(Coordinates point) {
		while (point.getX() > 2 || point.getX() < 0 || point.getY() > 1 || point.getY() < 0) {
			if (point.getX() > 2) {
				point.setX(4 - point.getX());
			}
			if (point.getX() < 0) {
				point.setX(-point.getX());
			}
			if (point.getY() > 1) {
				point.setY(2 - point.getY());
			}
			if (point.getY() < 0) {
				point.setY(-point.getY());
			}
		}
	}
}

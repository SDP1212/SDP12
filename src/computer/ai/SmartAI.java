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
public class SmartAI extends AI {

	protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	protected static int state = SEARCHING;
	private Date shotTime = new Date(0);
	private Date movementDate = new Date(0);
	private TargetBall nextWayShape = new TargetBall();
	private TargetBall ballShape = new TargetBall();
	private int firstRun = 0;
	private Coordinates nextWayPoint = new Coordinates(1, 0.5);

	public SmartAI(Pitch pitch, Robot self) {
		super(pitch, self);
		VisorRenderer.extraDrawables.clear();
		nextWayShape.setPosition(0, 0);
		ballShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(nextWayShape);
		VisorRenderer.extraDrawables.add(ballShape);
	}

	@Override
	public void run() {
		if (firstRun < 10) {
			self.forward(Brick.FAST);
			firstRun++;
		} else if (firstRun == 10) {
			getNextWayPoint();
		}
		updateState();
		if (state == SEARCHING && (new Date().getTime() - movementDate.getTime() > 10)) {
			
			if (ballInEnemyCorner()) {
				self.stop();
			}
			
			double xTarget = pitch.getTargetGoal().getLowerPostCoordinates().getX();
			double xOwn = pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX();
			double distanceFromCorner0 = self.getPosition().distance(new Coordinates(xOwn, 0));
			double distanceFromCorner1 = self.getPosition().distance(new Coordinates(xTarget, 1));
			
			
			if (ballInOurCorner() == 0 && distanceFromCorner0 > 0.15) {
				nextWayPoint = pitch.ball.getPosition();
			} else if (ballInOurCorner() == 0 &&  distanceFromCorner0 <= 0.15) {
				self.stop();
			} 
			
			if (ballInOurCorner() == 1 &&  distanceFromCorner1 > 0.15) {
				nextWayPoint = pitch.ball.getPosition();
			} else if (ballInOurCorner() == 1 &&  distanceFromCorner1 <= 0.15) {
				self.stop();
			} 
			
			
			if (!facingWayPoint()) {
				Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
				double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
				if (blockedByWall()) {
					self.backward(Brick.SLOW);
				} else {
					if (angle < 0) {
//						self.arcLeft(createRadius(nextWayPoint));
						self.rotateLeft(Brick.SLOW);
					} else {
//						self.arcRight(createRadius(nextWayPoint));
						self.rotateRight(Brick.SLOW);
					}
				}
			} else if (!onWayPoint()) {
				self.forward(Brick.MEDIUM);
			} else {
				getNextWayPoint();
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
	}

	protected Coordinates target() {
		double ballX = pitch.ball.getPosition().getX();
		double ballY = pitch.ball.getPosition().getY();
		double goalX = pitch.getTargetGoal().getUpperPostCoordinates().getX();

		double hackedLambda = 0;
		if (pitch.getTargetGoal() == pitch.leftGoal) {
			hackedLambda = 0.3;
		} else {
			hackedLambda = 0.1;
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
//		return lineToTarget.getLength() < Math.max(Math.sqrt(distanceToBall), 0.1);
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
		return (Math.abs(angle) < Math.PI / 14);
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
		return lineToWayPoint.getLength() < 0.1;
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
	
	protected int ballInOurCorner () {
		Coordinates b = pitch.ball.getPosition();
		double d1 = b.distance(new Coordinates(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX(), 0));
		double d2 = b.distance(new Coordinates(pitch.getEnemyTargetGoal().getLowerPostCoordinates().getX(), 1));	
		if (d1 < 0.3 ) {
			return 0;
		} else if ( d2 < 0.3) {
			return 1;
		}
		return -1;
	}

	protected boolean ballInEnemyCorner () {
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
}

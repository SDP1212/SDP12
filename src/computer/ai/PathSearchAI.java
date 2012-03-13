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
public class PathSearchAI extends AI {

	protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	protected static int state = SEARCHING;
	private Date shotTime = new Date(0);
	private Date movementDate = new Date(0);
	private TargetBall nextWayShape = new TargetBall();
	private TargetBall ballShape = new TargetBall();
	private boolean firstRun = true;
	private Coordinates nextWayPoint = new Coordinates(1, 0.5);
	private int runs = 0;
	private Date dribbleStart = new Date(0);
	private Date pauseBeforeShoot = new Date(0);
//	private boolean kickImmediately = false;

	public PathSearchAI(Pitch pitch, Robot self) {
		super(pitch, self);
		VisorRenderer.extraDrawables.clear();
		nextWayShape.setPosition(0, 0);
		ballShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(nextWayShape);
		VisorRenderer.extraDrawables.add(ballShape);
	}

	@Override
	public void run() {
		if (firstRun) {
			getNextWayPoint();
			firstRun = false;
		}
		updateState();
		if (state == SEARCHING && (new Date().getTime() - movementDate.getTime() > 10)) {
			if (!facingWayPoint()) {
				Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
				double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
				if (angle < 0) {
					self.rotateLeft(Brick.MEDIUM);
				} else {
					self.rotateRight(Brick.MEDIUM);
				}
			} else if (!onWayPoint()) {
				self.forward(Brick.MEDIUM / 2);
			} else {
//				self.stop();
				getNextWayPoint();
			}
		} else if (state == DRIBBLING) {
			if (!facingBall()) {
				Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
				double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
				if (angle < 0) {
					self.rotateLeft(Brick.MEDIUM / 3);
				} else {
					self.rotateRight(Brick.MEDIUM/ 3);
				}
			}
		} else if (state == SHOT) {
			if (new Date().getTime() - shotTime.getTime() < 1000) {
				self.forward(Brick.FAST);
			} else {
				self.kick();
			}
			self.stop();
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
//		kickImmediately = false;
		if (ballY < 0.15 || ballY > 0.85) {
//			kickImmediately = true;
//			x = ballX;
//			y = ballY;
			y = 2 * (ballY - y) + y;
		}
		ballShape.setPosition(x, y);
		return new Coordinates(x, y);
	}

	private void getNextWayPoint() {
		System.out.println("Getting waypoint");
		System.out.println("Our position: " + self.getPosition());
		System.out.println("Nemesis position: " + pitch.nemesis.getPosition());
		//int goal = Pitch.TARGET_RIGHT_GOAL;
		//if (pitch.getTargetGoal().getLowerPostCoordinates().getX() == 0) goal = Pitch.TARGET_LEFT_GOAL;

		nextWayPoint = PathSearch.getNextWaypoint(1, pitch.ball.getPosition(), target(), self.getPosition(), self.getOrientation().getDirectionRadians(), pitch.nemesis.getPosition());
		nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
	}

	private void updateState() {
		switch (state) {
			case SEARCHING:
				if (onTarget() && nearBall()) {
					state = DRIBBLING;
					dribbleStart = new Date();
				}
				break;
			case DRIBBLING:
				Date now = new Date();
				if (!nearBall()) {
					state = SEARCHING;
				} else if (facingBall()) {
					state = SHOT;
					shotTime = new Date();
				}
				break;
			case SHOT:
				if (new Date().getTime() - shotTime.getTime() >= 2000) {
					state = SEARCHING;
				}
				break;
		}
	}

	protected boolean onTarget() {
		Line lineToTarget = new Line(self.getPosition(), target());
		return lineToTarget.getLength() < 0.1;
	}

	protected boolean facingTarget() {
		Coordinates targetPoint;
		targetPoint = target();

		Line lineToBall = new Line(self.getPosition(), targetPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 10) {
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
		return Math.abs(angle) < Math.PI / 10;
	}

	private boolean onWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		return lineToWayPoint.getLength() < 0.1;
	}

	private boolean facingTargetGoal() {
		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
		return (LineTools.angleBetweenLineAndDirection(lineToLowerPost, self.getOrientation())*LineTools.angleBetweenLineAndDirection(lineToUpperPost, self.getOrientation()) < 0);
//		Coordinates centreGoal = new Coordinates(pitch.getTargetGoal().getLowerPostCoordinates().getX(), pitch.getCentreSpot().getY());
//		Line lineToGoal = new Line (self.getPosition(), centreGoal);
//		
//		double angle = LineTools.angleBetweenLineAndDirection(lineToGoal, self.getOrientation());
//		
//		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
//		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
//		double angleToUpperPost = LineTools.angleBetweenLines(lineToGoal, lineToUpperPost);
//		double angleToLowerPost = LineTools.angleBetweenLines(lineToGoal, lineToLowerPost);
//		
//		
//		return ((angleToUpperPost <= angle && angle <= angleToLowerPost) || (angleToUpperPost >= angle && angle >= angleToLowerPost));
//		return Math.abs(angle) < Math.PI/2;
	}
	
	
	@Override
	public void robotCollided() {
		getNextWayPoint();
		System.out.println("Collision");
	}
}

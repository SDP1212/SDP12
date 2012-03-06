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
	private Date pauseBeforeShoot = new Date (0);

	public PathSearchAI(Pitch pitch, Robot self) {
		super(pitch, self);
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
					self.rotateLeft(Brick.SLOW / 2);
				} else {
					self.rotateRight(Brick.SLOW / 2);
				}
			} else if (!onWayPoint()) {
				self.forward(Brick.SLOW / 2);
			} else {
//				self.stop();
				getNextWayPoint();
			}
		} else if (state == DRIBBLING) {
			if (!facingBall()) {
				Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
				double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
				if (angle < 0) {
					self.rotateLeft(Brick.SLOW / 3);
				} else {
					self.rotateRight(Brick.SLOW / 3);
				}
			} 
		} else if (state == SHOT) {
			if (new Date().getTime() - shotTime.getTime() < 1500) {
				self.forward(Brick.SLOW);
			} else {
				self.kick();
			}
			self.stop();
		}
		movementDate = new Date();
	}

	protected Coordinates target() {
		double deltaX = pitch.getCentreSpot().getX() - pitch.getTargetGoal().getUpperPostCoordinates().getX();
		double x = (deltaX)*0.3 + pitch.ball.getPosition().getX();//deltaX * 0.3 + pitch.ball.getPosition().getX();
		double y = (pitch.ball.getPosition().getY() - 0.5)/(pitch.ball.getPosition().getX()) *deltaX*0.3 + pitch.ball.getPosition().getY();
		ballShape.setPosition(x, y);
		//System.out.println("Target: X = " + x + " Y = " + y);
		return new Coordinates(x, y);
	}

	private void getNextWayPoint() {
		System.out.println("Getting waypoint");
		System.out.println("Our position: " + self.getPosition());
		System.out.println("Nemesis position: " + pitch.nemesis.getPosition());
		nextWayPoint = PathSearch.getNextWaypoint(0, pitch.ball.getPosition(),target(), self.getPosition(), self.getOrientation().getDirectionRadians(), pitch.nemesis.getPosition());
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
		return (Math.abs(angle) < Math.PI / 16);
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

	@Override
	public void robotCollided() {
		System.out.println("Collision");
	}
}

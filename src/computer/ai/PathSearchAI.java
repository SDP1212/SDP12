/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
	private Date movementDate = new Date (0);
	private TargetBall targetShape = new TargetBall();
	private boolean firstRun = true;
	private Coordinates nextWayPoint = new Coordinates(1, 0.5);

	public PathSearchAI(Pitch pitch, Robot self) {
		super(pitch, self);
		targetShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(targetShape);
	}

	@Override
	public void run() {
		if (firstRun) {
			getNextWayPoint();
			firstRun = false;
		}
		updateState();
		if (state == SEARCHING  && (new Date().getTime() - movementDate.getTime() > 10)) {
			if (!facingWayPoint()) {
				Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
				double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
				if (angle < 0) {
					self.rotateLeft(Brick.SLOW);
				} else {
					self.rotateRight(Brick.SLOW);
				}
			} else if (!onWayPoint()) {
				self.forward(Brick.SLOW);
			} else {
//				self.stop();
				getNextWayPoint();
			}
		} else if (state == DRIBBLING) {
			self.kick();
			self.stop();
		} else if (state == SHOT) {
			self.stop();
		}
		movementDate = new Date ();
	}

	private void getNextWayPoint () {
		nextWayPoint = PathSearch.getNextWaypoint(0, pitch.ball.getPosition(), self.getPosition(), self.getOrientation().getDirectionRadians(), pitch.nemesis.getPosition());
		targetShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
	}
	
	private void updateState() {
		switch (state) {
			case SEARCHING:
				if (facingBall() && nearBall()) {
					state = DRIBBLING;
				}
				break;
			case DRIBBLING:
				if (!nearBall()) {
					state = SEARCHING;
				} else if (new Date().getTime() - shotTime.getTime() < 1000) {
					state = SHOT;
				}
				break;
			case SHOT:
				if (new Date().getTime() - shotTime.getTime() >= 1500) {
					state = SEARCHING;
				}
				break;
		}
	}

	protected boolean facingBall() {
		Coordinates targetPoint;
		targetPoint = pitch.ball.getPosition();
		Line lineToBall = new Line(self.getPosition(), targetPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToBall, self.getOrientation());
		if (Math.abs(angle) < Math.PI / 10) {
			return true;
		} else {
			return false;
		}
	}

	private boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		if (state == SEARCHING) {
			return lineToBall.getLength() < 0.15;
		} else {
			return lineToBall.getLength() < 0.3;
		}
	}

	private boolean facingWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
		return Math.abs(angle) < Math.PI / 10;
	}

	private boolean onWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		return lineToWayPoint.getLength() < 0.2;
	}

	@Override
	public void robotCollided() {
		System.out.println("Collision");
	}
}

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
	protected Date shotTime = new Date(0);
	protected Date movementDate = new Date(0);
	protected TargetBall nextWayShape = new TargetBall();
	protected TargetBall ballShape = new TargetBall();
	protected int firstRun = 1;
	protected Coordinates nextWayPoint = new Coordinates(1, 0.5);
	protected Coordinates ballPosition0;
	protected Coordinates ballFuturePos;
	protected int time0 = 1;
	protected int timeDelta = 3;
//	protected int timeDelta2 = 4;
	protected boolean first = false;
	protected int timeToBall = 0;
	protected double robotSpeed = 1.0 / 75;
	protected Coordinates ballV = new Coordinates(0, 0);
	protected Coordinates robotV = new Coordinates(0, 0);
	protected Coordinates robotPosition0;

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
            
            /*
             * Scott's 1 AM Edits - since I can't test, here's some code to get started with tomorrow
             * Kicker line: y = m (x - Kx) + Ky
             * Robot XLine: y = Ry
             * Robot YLine: x = Rx
             * 
             * Intersection1: (Rx, y=m(Rx-Kx)+Ky)
             * Intersection2: (((Ry-Ky)/m)+Kx, Ry)
             * Check that both points are on the pitch (if neither are, use current stuff. If one is, use it)
             * (If both are, use the one further away (giving us enough time to get to the ball)
             * (If near our robot, its facing us - pick a point along the line of the kicker)
             * 
             * 
             */
            
            /*
            Coordinates intersectone;
            Coordinates intersecttwo;
            if(pitch.nemesis.getOrientation().getDirectionRadians() != 90 || pitch.nemesis.getOrientation().getDirectionRadians() != 270) {
                intersectone = new Coordinates(self.getPosition().getX(), (Math.tan(pitch.nemesis.getOrientation().getDirectionRadians()) * (self.getPosition().getX() - pitch.nemesis.getPosition().getX())) - pitch.nemesis.getPosition().getY());
            } else {
                //Off the pitch
                intersectone = new Coordinates(3, 3);
            }
            if(Math.tan(pitch.nemesis.getOrientation().getDirectionRadians()) != 0) {
                intersecttwo = new Coordinates(((self.getPosition().getY() - pitch.nemesis.getPosition().getY()) / Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())) + pitch.nemesis.getPosition().getX() , self.getPosition().getY());
            } else {
                //Off the pitch
                intersecttwo = new Coordinates(3,3);
            }
            if(intersectone.getY() > 2 || intersectone.getY() < 0) { //Since the Xpoint will always be good
                //Turn to face intersecttwo
            } else if (intersecttwo.getX() > 2 || intersecttwo.getX() < 0) {//Since the Ypoint will always be good
                //Turn to face intersectone
            } else { //Both points are valid
                if (self.getPosition().distance(intersectone) < (self.getPosition().distance(intersecttwo))) {
                    //Turn to face intersecttwo
                } else {
                    //Turn to face intersectone
                }
            }
            
            */
            
		if (firstRun == 1) {
			ballFuturePos = pitch.ball.getPosition();
			ballPosition0 = pitch.ball.getPosition();
			robotPosition0 = self.getPosition();
			nextWayPoint = self.getPosition();
			nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
			self.setSpeed(Brick.FAST);
		}
		if (firstRun == time0 + timeDelta) {
			if (ballPosition0.distance(pitch.ball.getPosition()) < 0.03) {
				ballV.set(0, 0);
			} else {
				//calculate the velocity of the ball
				ballV.set((pitch.ball.getPosition().getX() - ballPosition0.getX()) / (firstRun - time0), (pitch.ball.getPosition().getY() - ballPosition0.getY()) / (firstRun - time0));
			}
			if (robotPosition0.distance(self.getPosition()) < 0.2) {
				robotSpeed = 1.0 / 75;
			} else {
				//calculate the velocity of the robot
				robotV.set((self.getPosition().getX() - robotPosition0.getX()) / (firstRun - time0), (self.getPosition().getY() - robotPosition0.getY()) / (firstRun - time0));
				robotSpeed = robotV.distance(new Coordinates(0, 0));
			}
			// 12 fast ball's speed   (-0.025821596244131495,0.008574490889603418)
			// 10  ball's speed   (-0.03521126760563383,0.0021436227224008544)  ball's speed   (-0.03521126760563383,0.015005359056806)
			// 8 ball's speed   (-0.025821596244131495,0.01071811361200429)

			// 
			// 

			//calculate time to get to ball
			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotSpeed));
			double distanceToWaypoint = new Line(self.getPosition(), nextWayPoint).getLength();

			if (distanceToWaypoint < 0.2 && state != BEGIN) {
				if (ballV.distance(new Coordinates(0, 0)) >= Math.sqrt(0.035 * 0.035 + 0.00214 * 0.00214)) {
					ballFuturePos.set(ballPosition0.getX() + ballV.getX() * timeToBall, ballPosition0.getY() + ballV.getY() * timeToBall);
				} else {
					ballFuturePos.set(ballPosition0.getX() + ballV.getX() * timeToBall, ballPosition0.getY() + ballV.getY() * timeToBall + 0.2);
				}
//				System.out.println("ball's speed   " + ballV.toString());
				bumpingIntoWalls(ballFuturePos);
				nextWayPoint = ballFuturePos;
				nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
			}
			ballPosition0 = pitch.ball.getPosition();
			time0 = firstRun;
			robotPosition0 = self.getPosition();
		}
//		if (firstRun == time0 + timeDelta2 ) {
//			if (ballPosition0.distance(pitch.ball.getPosition()) < 0.03) {
//				ballV.set(0, 0);
//			} else {
//				//calculate the velocity of the ball
//				ballV.set((pitch.ball.getPosition().getX() - ballPosition0.getX()) / (firstRun - time0), (pitch.ball.getPosition().getY() - ballPosition0.getY()) / (firstRun - time0));
//			}
//			if (robotPosition0.distance(self.getPosition()) < 0.2) {
//				robotSpeed = 1.0 / 75;
//			} else {
//				//calculate the velocity of the robot
//				robotV.set((self.getPosition().getX() - robotPosition0.getX()) / (firstRun - time0), (self.getPosition().getY() - robotPosition0.getY()) / (firstRun - time0));
//				robotSpeed = robotV.distance(new Coordinates(0, 0));
//			}
//			//calculate time to get to ball
//			timeToBall = (int) ((robotPosition0.getX() - ballPosition0.getX()) / (ballV.getX() - robotSpeed));
//			double distanceToWaypoint = new Line(self.getPosition(), nextWayPoint).getLength();
//
//			if (distanceToWaypoint < 0.2 && state != BEGIN) {
//				ballFuturePos.set(ballPosition0.getX() + ballV.getX() * timeToBall, ballPosition0.getY() + ballV.getY() * timeToBall);
//				bumpingIntoWalls(ballFuturePos);
//				nextWayPoint = ballFuturePos;
//				nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
//			}
//			ballPosition0 = pitch.ball.getPosition();
//			time0 = firstRun;
//			robotPosition0 = self.getPosition();
//		}
		updateState();
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
					int radius = createRadius(nextWayPoint);

					if (angle < 0) {
						self.arcLeft(radius);
					} else {
						self.arcRight(radius);
					}
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

	protected void getNextWayPoint() {
		nextWayPoint = PathSearch.getNextWaypoint(1, pitch.ball.getPosition(), target(), self.getPosition(), self.getOrientation().getDirectionRadians(), pitch.nemesis.getPosition());
		nextWayShape.setPosition(nextWayPoint.getX(), nextWayPoint.getY());
	}

	protected void updateState() {
		switch (state) {
			case BEGIN:
				System.out.println("Begin");
				if (ballPosition0.distance(pitch.ball.getPosition()) > 0.03) {
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

	protected boolean nearBall() {
		Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
		return lineToBall.getLength() < 0.3;
	}

	protected boolean facingWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
		return Math.abs(angle) < Math.PI / 6;
	}

	protected boolean onWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		return lineToWayPoint.getLength() < 0.2;
	}

	protected boolean facingTargetGoal() {
		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
		return (LineTools.angleBetweenLineAndDirection(lineToLowerPost, self.getOrientation()) * LineTools.angleBetweenLineAndDirection(lineToUpperPost, self.getOrientation()) < 0);
	}

	protected boolean blockedByWall() {
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

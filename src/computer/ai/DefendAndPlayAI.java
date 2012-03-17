package computer.ai;

import brick.Brick;
import computer.simulator.*;

import computer.ai.pathsearch.PathSearch;
import java.util.Date;
/**
 * 
 * @author Callum Laird
 * @author Scott Hofmam
 * @author Lavanya Jeyratnam
 * @author Evgeniya Sotirova
 */

public class DefendAndPlayAI extends Penalty {

	Coordinates upperPost;
	Coordinates lowerPost;
	boolean penaltyMode = true;
        double ball_nemesis_distance;
        Coordinates ball_position;
        boolean transitive_behaviour = true;
        
        protected static final int SEARCHING = 0;
	protected static final int DRIBBLING = 1;
	protected static final int SHOT = 2;
	protected static int state = SEARCHING;
	private Date shotTime = new Date(0);
	private Date movementDate = new Date(0);
	private TargetBall nextWayShape = new TargetBall();
	private TargetBall ballShape = new TargetBall();
	private Coordinates nextWayPoint = new Coordinates(1, 0.5);
        
	public DefendAndPlayAI(Pitch pitch, Robot self) {
		super(pitch, self);
                ball_position = pitch.ball.getPosition();
                ball_nemesis_distance = Coordinates.distance(pitch.nemesis.getPosition(),ball_position);
                
		VisorRenderer.extraDrawables.clear();
		nextWayShape.setPosition(0, 0);
		ballShape.setPosition(0, 0);
		VisorRenderer.extraDrawables.add(nextWayShape);
		VisorRenderer.extraDrawables.add(ballShape);
	}
	
	@Override
	public void run() {
	
            // Checks conditions for an as yet untaken penalty.
            if ((ball_nemesis_distance > 0.2) &&
                (Coordinates.distance(ball_position,pitch.ball.getPosition()) > 0.2)) {
                penaltyMode = false;
            }
            
            // Does penalty stuff while penalty conditions are met.
            if (penaltyMode) {
                // Need it to keep running until it somehow realises penalty has been taken.
                // Perhaps check ball position against nemesis position.  If it's too far then go back to match AI?

                double x_Distance = Math.abs(self.getPosition().getX() - pitch.nemesis.getPosition().getX());
                double y_intercept;

                // Calculates the y_intercept using the opponent's position and orientaton.
                if (self.getPosition().getX() > 1) {
                    y_intercept =  pitch.nemesis.getPosition().getY() + Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
                }  else {
                    y_intercept =  pitch.nemesis.getPosition().getY() - Math.tan(pitch.nemesis.getOrientation().getDirectionRadians())*x_Distance;
                }
                
                if (!inRange(y_intercept, self.getPosition().getY())) {
                    if (self.getPosition().getY() < (y_intercept) &&
                        self.getPosition().getY() < .66){

                            self.forward(Brick.SLOW);

                    } else if (self.getPosition().getY() > (y_intercept) &&
                               self.getPosition().getY() > .34) {

                            self.backward(Brick.SLOW);

                    }
                } else {
                        self.stop();
                }
                
            // Else does match stuff.
            } else {
                
                // Initial transitive behaviour from penalty to match - turns away from own goal.
                if (transitive_behaviour) {
                    if (facingTargetGoal()) {
                        nextWayPoint = pitch.ball.getPosition();
                        transitive_behaviour = false;
                    } else {
                        // Conditions for either side of pitch.
                        if (self.getPosition().getX() < 1.0) {
                            self.rotateRight((int) (Brick.MEDIUM));
                        } else {
                            self.rotateLeft((int) (Brick.MEDIUM));
                        }
                    }
                
                // Normal match behaviour
                } else {
                    
                    updateState();
                    if (state == SEARCHING && (new Date().getTime() - movementDate.getTime() > 10)) {

                        if (!facingWayPoint()) {
                            Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
                            double angle = LineTools.angleBetweenLineAndDirection(lineToWayPoint, self.getOrientation());
                            if (blockedByWall()) {
                                self.backward(Brick.SLOW);
                            } else {
                                if (self.getPosition().distance(nextWayPoint) > 0.3) { // Arcing
                                    if (angle < 0) {
                                        self.arcLeft(createRadius(nextWayPoint));
                                    } else {
                                        self.arcRight(createRadius(nextWayPoint));
                                    }
                                } else { // Normal rotation
                                    if (angle < 0) {
                                        self.rotateLeft(Brick.SLOW);
                                    } else {
                                        self.rotateRight(Brick.SLOW);
                                    }
                                }
                            }
                        } else if (!onWayPoint()) {
                            self.forward(Brick.MEDIUM);
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
            }
	}

        public boolean inRange(double y_intercept, double robot_y) {
            if (robot_y < (y_intercept + 0.04) &&
                robot_y > (y_intercept - 0.04)) {
                    return true;
            }
            return false;
        }
        
        private boolean facingTargetGoal() {
		Line lineToUpperPost = new Line(self.getPosition(), pitch.getTargetGoal().getUpperPostCoordinates());
		Line lineToLowerPost = new Line(self.getPosition(), pitch.getTargetGoal().getLowerPostCoordinates());
		return (LineTools.angleBetweenLineAndDirection(lineToLowerPost, self.getOrientation()) * LineTools.angleBetweenLineAndDirection(lineToUpperPost, self.getOrientation()) < 0);
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
		System.out.println("Getting waypoint");
		System.out.println("Our position: " + self.getPosition());
		System.out.println("Nemesis position: " + pitch.nemesis.getPosition());

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
                    if (!nearBall()) {
                        state = SEARCHING;
                    } else if (facingBall() && facingTargetGoal()) { // changed by callum and lavanya
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
		return Math.abs(angle) < Math.PI / 6;
	}

	private boolean onWayPoint() {
		Line lineToWayPoint = new Line(self.getPosition(), nextWayPoint);
		return lineToWayPoint.getLength() < 0.1;
	}
	
	private boolean blockedByWall() {
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
		System.out.println("Radius " + (int) radius);
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
		return (d1 < 0.3 || d2 < 0.3);
	}
	
	@Override
	public void robotCollided() {
		getNextWayPoint();
		System.out.println("Collision");
	}
}
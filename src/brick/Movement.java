package brick;

import lejos.nxt.*;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.*;


/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */


public class Movement implements Runnable {
	
	private static final int STOPPED = 0;
	private static final int FORWARD = 1;
	private static final int BACKWARD = 2;
	private static final int ROTATE = 3;
	private static final int ARC = 4;
	
	private static DifferentialPilot pilot;
	public final static double trackWidth = 15.6;
    public final static double wheelDiameter = 6;
	private final Object movementLock = new Object();
	
	private int state = STOPPED;
	private boolean lockHeading = false;
	private int heading = 0;
	private float speed = 0;
	private CompassHTSensor compass;
	
	
	private Movement() {
		pilot = new DifferentialPilot(wheelDiameter, trackWidth , Motor.A, Motor.B);
		compass = new CompassHTSensor(SensorPort.S3);
//		compass.startCalibration();
//		pilot.setRotateSpeed(50);
//		pilot.rotate(600);
//		compass.stopCalibration();
	}
	
	public static Movement getInstance() {
		return MovementHolder.INSTANCE;
	}

	/**
	 * @return the heading
	 */
	public int getHeading() {
		return (heading + 20) % 360;
	}

	/**
	 * @param heading the heading to set
	 */
	public void setHeading(int heading) {
		this.heading = (heading - 20) % 360;
		lockHeading();
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		pilot.setRotateSpeed(speed);
		this.speed = speed;
	}
	
	private static class MovementHolder {
		private static final Movement INSTANCE = new Movement();
	}
	
	/**
     * Send the robot forwards.
     * 
     * @param speed A travel speed opcode.
     */
    public void forwards(int speed) {
		synchronized(movementLock) {
			pilot.forward();
			setSpeed(speed);
			state = FORWARD;
		}
    }
    
    /**
     * Send the robot backwards.
     * 
     * @param speed A travel speed opcode.
     */
    public void backwards(int speed) {
		synchronized(movementLock) {
			pilot.backward();
			setSpeed(speed);
			state = BACKWARD;
		}
    }
    
    /**
     * Pivot the robot on a point. Positive anti-clockwise, negative clockwise.
     */
    public void rotate(int angle) {
		pilot.stop();
        int finalAngle = angle - 180;
        double factor;
		// This doesn't do what you think it does
        if (finalAngle < 0) {
            factor = 1;
        } else {
            factor = 1;
        }
		LCD.clear();
		LCD.drawInt(finalAngle, 0, 0);
		synchronized(movementLock) {
			setSpeed(100);
			pilot.rotate((finalAngle) * factor);
			state = ROTATE;
		}
    }
    
    public void rotateRight(int speed) {
		synchronized(movementLock) {
			Motor.A.backward();
			Motor.B.forward();
			Motor.A.setSpeed(speed);
			Motor.B.setSpeed(speed);
			state = ROTATE;
		}
    }
    
    public void rotateLeft(int speed) {
		synchronized(movementLock) {
			Motor.B.backward();
			Motor.A.forward();
			Motor.A.setSpeed(speed);
			Motor.B.setSpeed(speed);
			state = ROTATE;
		}
    }
	
	public void rotateTo(int heading) {
		if (Math.abs(compass.getDegreesCartesian() - heading) > 20) {
			int currentHeading = (int)compass.getDegreesCartesian();
			int angle = heading - currentHeading;
			synchronized (movementLock) {
				if (Math.sin(Math.toRadians(angle)) < 0) {
					pilot.rotateLeft();
				} else {
					pilot.rotateRight();
				}
				setSpeed(30);
			}
		} else {
			synchronized (movementLock) {
				pilot.quickStop();
			}
		}
	}
	
    public void arc(int angle) {
		synchronized(movementLock) {
			pilot.arcForward(angle);
			setSpeed(Brick.SLOW / 2);
			state = ARC;
		}
    }
	
	public void arcTo(int heading) {
		if (Math.abs(compass.getDegreesCartesian() - heading) > 20) {
			int currentHeading = (int)compass.getDegreesCartesian();
			int angle = heading - currentHeading;
			synchronized (movementLock) {
				if (Math.sin(Math.toRadians(angle)) < 0) {
					Motor.B.setSpeed(getSpeed());
					Motor.A.setSpeed((float)(getSpeed() * (1 / (Math.log(Math.abs(angle)) + 1))));
				} else {
					Motor.A.setSpeed(getSpeed());
					Motor.B.setSpeed((float)(getSpeed() * (1 / (Math.log(Math.abs(angle)) + 1))));
				}
			}
		} else {
			synchronized (movementLock) {
				Motor.B.setSpeed(getSpeed());
				Motor.A.setSpeed(getSpeed());
			}
		}
	}
    
    /**
     * Stop movement activity.
     */
    public void stop() {
		synchronized(movementLock) {
			pilot.stop();
			state = STOPPED;
		}
    }
    
    /**
     * Kick the ball (if we are lucky).
     */
    public void kick() {
//        sensorListener.setKicking(true);
		synchronized(movementLock) {
			Motor.C.setSpeed(720);
			Motor.C.rotate(-25);
			Motor.C.rotate(25);
			Motor.C.stop();
		}
//        sensorListener.setKicking(false);
    }
    
    /**
     * Reverse away from an obstruction.
     * @param The direction to pivot. 
     */
    public void backOff(char direction) {
		synchronized(movementLock) {
			pilot.stop();
			pilot.backward();
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {

			}
			pilot.stop();
			state = STOPPED;
		}
        Communication.getInstance().sendMessage(Brick.OK);
    }
	
	public void lockHeading() {
		lockHeading = true;
	}
	
	public void unlockHeading() {
		lockHeading = false;
	}
	
	public void run() {
		while (!Thread.interrupted()) {
			if (lockHeading) {
				switch (state) {
					case STOPPED:
						rotateTo(heading);
						break;
					case FORWARD:
						arcTo(heading);
						break;
				}
			}
		}
	}
}

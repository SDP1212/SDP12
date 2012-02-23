package brick;

import lejos.nxt.*;
import lejos.robotics.navigation.*;


/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */


public class Movement implements Runnable {
	private static DifferentialPilot pilot;
	public final static double trackWidth = 15.6;
    public final static double wheelDiameter = 6;
	
	private Movement() {
		pilot = new DifferentialPilot(wheelDiameter, trackWidth , Motor.A, Motor.B);
	}
	
	public static Movement getInstance() {
		return MovementHolder.INSTANCE;
	}
	
	private static class MovementHolder {

		private static final Movement INSTANCE = new Movement();
	}
	
	/**
     * Send the robot forwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void forwards(int speed) {
        pilot.forward();
        pilot.setRotateSpeed(speed);
    }
    
    /**
     * Send the robot backwards.
     * 
     * @param speed A travel speed opcode.
     */
    public static void backwards(int speed) {
        pilot.backward();
        pilot.setRotateSpeed(speed);
    }
    
    /**
     * Pivot the robot on a point. Positive anti-clockwise, negative clockwise.
     */
    public static void rotate(int angle) {
        int finalAngle = angle - 180;
        double factor;
        if (finalAngle < 0) {
            factor = 1;
        } else {
            factor = 1;
        }
        pilot.rotate((finalAngle) * factor);
		pilot.setRotateSpeed(180);
    }
    
    public static void rotateRight(int speed) {
        Motor.A.backward();
		Motor.B.forward();
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
    }
    
    public static void rotateLeft(int speed) {
        Motor.B.backward();
		Motor.A.forward();
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
    }
	
	public static void rotateTo(int heading) {
		UltrasonicSensor compass = new UltrasonicSensor(SensorPort.S3);
		int currentHeading = compass.getDistance() * 2;
		int angle = currentHeading - heading;
		if (angle > 0) {
			pilot.rotateLeft();
		} else {
			pilot.rotateRight();
		}
		pilot.setRotateSpeed(50);
		while (true) {
			if (Math.abs(compass.getDistance() * 2 - heading) < 10) {
				break;
			}
		}
		pilot.stop();
	}
	
    public static void arc(int angle) {
	
        pilot.arcForward(angle);
			pilot.setRotateSpeed(Brick.SLOW / 2);
    }
    
    /**
     * Stop movement activity.
     */
    public static void stop() {
        pilot.stop();
    }
    
    /**
     * Kick the ball (if we are lucky).
     */
    public static void kick() {
//        sensorListener.setKicking(true);
        Motor.C.setSpeed(720);
        Motor.C.rotate(-25);
        Motor.C.rotate(25);
        Motor.C.stop();
//        sensorListener.setKicking(false);
    }
    
    /**
     * Reverse away from an obstruction.
     * @param The direction to pivot. 
     */
    public synchronized static void backOff(char direction) {
        pilot.stop();
        pilot.backward();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            
        }
        pilot.stop();
        Communication.getInstance().sendMessage(Brick.OK);
    }
	
	public void run() {
		while (!Thread.interrupted()) {
			
		}
	}
}

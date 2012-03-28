package computer.simulator;

import java.util.Date;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class AngularVelocity {
	double velocity;
	Date currentTime;
	Direction currentDirection;

	/**
	 * Construct an angular velocity object for calculating the angle of a
	 * rotating object at a future date.
	 * 
	 * @param previousDirection
	 * @param currentDirection
	 * @param previousTime
	 * @param currentTime 
	 */
	public AngularVelocity(Direction previousDirection, Direction currentDirection, Date previousTime, Date currentTime) {
		velocity = (currentDirection.radians - previousDirection.radians) / (currentTime.getTime() - previousTime.getTime());
//		System.out.println("Velocity " + velocity + " previous " + previousDirection.radians + " at " + previousTime + " Current " + currentDirection.radians + " at " + currentTime + " over " + (currentTime.getTime() - previousTime.getTime()));
		this.currentTime = currentTime;
		this.currentDirection = currentDirection;
	}
	
	/**
	 * Return the direction the an object rotating at the rate described by
	 * this object at the passed future date.
	 * 
	 * @param futureTime
	 * @return 
	 */
	public Direction directionAt(Date futureTime) {
//		System.out.println("Future " + futureTime + " Interval " + ((futureTime.getTime() - currentTime.getTime()) + currentDirection.radians));
//		System.out.println("Future angle " + velocity * (futureTime.getTime() - currentTime.getTime()) + currentDirection.radians);
		return new Direction((velocity * (futureTime.getTime() - currentTime.getTime()) + currentDirection.radians + (2 * Math.PI)) % (2 * Math.PI));
	}
}

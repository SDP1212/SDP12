package computer.ai;

import brick.Brick;
import computer.simulator.*;
import java.util.Date;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class Creeper extends Shooter {

	private boolean firstRun = true;
	private Coordinates previousPosition;
	private Date date = new Date(0);
	private boolean rotated = false;
	private int lastRotation = 0;
	
	public Creeper (Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}
	
	@Override
	public void run() {
		updateState();
		Date currentDate = new Date();
		if (firstRun) {
			self.forward(Brick.MEDIUM);
			firstRun = false;
			rotated = false;
//			System.out.println("First run");
			lastRotation = 0;
			previousPosition = self.getPosition();
		} else {
			long timeDifference = currentDate.getTime() - date.getTime();
			Line lineToBall = new Line(self.getPosition(), pitch.ball.getPosition());
			Line movementLine = new Line(previousPosition, self.getPosition());
			double movementAngle = LineTools.angleBetweenLines(lineToBall, movementLine);
			if ( rotated && (timeDifference > Math.max(Math.abs(lastRotation) * 10, 500))) {
//				System.out.println("Not rotating");
				rotated = false;
				self.forward(Brick.MEDIUM);
				previousPosition = self.getPosition();
				date = new Date();
			} else if (!rotated && (timeDifference > 500)) {
//				System.out.println("Maybe rotating");
//				System.out.println("Angle " + movementAngle);
				if (Math.abs(movementAngle) > Math.PI / 10) {
					System.out.println("Rotate");
					self.rotate(movementAngle);
					lastRotation = (int)Math.toDegrees(movementAngle);
					rotated = true;
				} else {
					self.forward(Brick.MEDIUM);
				}
				previousPosition = self.getPosition();
				date = new Date();
			}
		}
	}
	
}

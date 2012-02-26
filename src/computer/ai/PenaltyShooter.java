package computer.ai;

import computer.simulator.*;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class PenaltyShooter extends Penalty {
	
	public PenaltyShooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
	}

	@Override
	public void run() {
		java.util.Random gen = new Random();
		long date = new Date().getTime();
		int time = gen.nextInt(3000) + 500;
		while (new Date().getTime() - date < time) {
			
		}
		self.kick();
	}

	@Override
	public void robotCollided() {
		
	}
	
}

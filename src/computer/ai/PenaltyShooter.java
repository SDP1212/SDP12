package computer.ai;

import brick.Brick;
import computer.simulator.*;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class PenaltyShooter extends Penalty {
    boolean penalty = false;

	
	public PenaltyShooter(Pitch pitch, Robot self) {
		super(pitch, self);
		this.pitch = pitch;
		this.self = self;
                 
	}

	@Override
	public void run() {
           
            if(!penalty) {
		java.util.Random gen = new Random();
		long date = new Date().getTime();
		int time = gen.nextInt(3000) + 500;
                int direction = gen.nextInt(2);
                int angleplus = gen.nextInt(10)+1;
		while (new Date().getTime() - date < time) {

		}
                if(direction==1) {
                    self.rotate(self.getOrientation().getDirectionRadians()+Math.toRadians(angleplus));
                } else {
                    self.rotate(self.getOrientation().getDirectionRadians()-Math.toRadians(angleplus));
                }
                long wait = new Date().getTime();
                self.forward(Brick.SLOW);    
                while(new Date().getTime() - wait < 250) {
                   
                }
                
		self.kick();
                penalty = true;
            }
	}

	@Override
	public void robotCollided() {
		
	}
	
}

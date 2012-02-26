package brick;

import lejos.nxt.Button;
import lejos.nxt.comm.Bluetooth;

/**
 *
 * @author Matthew Jeffryes
 */
public class ButtonListener implements lejos.nxt.ButtonListener  {

    public void buttonPressed(Button button) {
        
    }

    public void buttonReleased(Button button) {
		Brick.quit();
    }
    
}

package brick;

import lejos.nxt.*;
import lejos.robotics.*;
import lejos.nxt.comm.*;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Example leJOS Project with an ant build file
 *
 */
public class brick {
    //private TachoMotor kicker;
    //public void shoot() {
    //  this.kicker.rotate(-144, true);
    //}
    private static InputStream is;
    private static OutputStream os; 

    public static void main(String[] args) {
        //Motor.A.forward();
        //LCD.drawString("FORWARD", 0, 0);
        /*TouchSensor touchL = new TouchSensor(SensorPort.S1);
        TouchSensor touchR = new TouchSensor(SensorPort.S2);
//                while (Button.waitForPress()!=0 ) {
        while (!touchL.isPressed() && !touchR.isPressed()  ) {
            Motor.A.backward();
            Motor.B.backward();
            
        }
        
        int i =0;
        
        while (i<1000){
            Motor.A.forward();
            Motor.B.forward();
            i++;
        }*/
        
//        Motor.C.rotate(-30);
//        Motor.C.rotate(30);
        Motor.C.setSpeed(720);
        Motor.C.rotate(60);
        Motor.C.rotate(-60);
//        Motor.A.setSpeed(720);
//        Motor.B.setSpeed(720);
//        Motor.A.forward();
//        Motor.B.forward();
        Button.waitForPress();
//        
//        Motor.B.setSpeed(1000);
        
        //Button.waitForPress();
        /*
        LCD.drawString("FORWARD", 0, 0);
        Button.waitForPress();
        Motor.A.backward(); // you could use  Motor.A.reverseDirection  instead
        LCD.drawString("BACKWARD", 0, 1);
        Button.waitForPress();
        //Motor.A.reverseDirection();
        //LCD.drawString("FORWARD", 0, 2);
        //Button.waitForPress();
        Motor.A.stop();
        Motor.C.rotate(-30);
        // Motor.C.rotate(30);
        //              }*/
    }
}
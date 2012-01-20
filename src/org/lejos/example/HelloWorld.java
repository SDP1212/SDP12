package org.lejos.example;

import lejos.nxt.*;
import lejos.robotics.*;

/**
 * Example leJOS Project with an ant build file
 *
 */
public class HelloWorld {

	public static void main(String[] args) {
                Motor.A.forward();
                LCD.drawString("FORWARD", 0, 0);
                Button.waitForPress();
	}
}

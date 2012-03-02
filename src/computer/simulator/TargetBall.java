/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author s0907806
 */
public class TargetBall extends SimulatableObject {

	@Override
	protected void animate(long timeDeltaInMilliseconds) {
	}

	@Override
	protected Shape[] getVisualisation(int width, int height) {
		Shape[] shapeArray = new Shape[1];
		Shape shape = new Ellipse2D.Double(-1.5, -1.5, 3, 3);
		shapeArray[0] = shape;
		return shapeArray;
	}
	
}

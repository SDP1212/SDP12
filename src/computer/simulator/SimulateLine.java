/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 *
 * @author s0907806
 */
public class SimulateLine extends SimulatableObject {
	private Coordinates p1 = new Coordinates(1, 1);
	private Coordinates p2 = new Coordinates(2, 1);

	@Override
	protected void animate(long timeDeltaInMilliseconds) {
	}

	@Override
	protected Shape[] getVisualisation(int width, int height) {
		Shape [] shapeArray = new Shape[1];
		Shape circle = new Ellipse2D.Double(-10, -10, 20, 20);
		double centerX=(Coordinates.distanceFromCentimetres(2))*width/2;
        double centerY=Coordinates.distanceFromCentimetres(2)*height;
		
//		-centerX, -centerY, 2*centerX, 2*centerY
		//Shape shape = new Line2D.Double(p1.getX() * (-centerX), p1.getY() * (-centerY), p2.getX() * 2*centerX, p2.getY() * 2*centerY);
		Shape shape = new Line2D.Double(p1.getX() *(centerX), p1.getY() *(centerY), -2*p2.getX()*centerX, -2*p2.getY()*centerY);
		shapeArray[0] = shape;
		//shapeArray[1] = circle;
		return shapeArray;
	}

	/**
	 * @return the p1
	 */
	public Coordinates getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Coordinates p1) {
		this.p1 = p1;
	}

	/**
	 * @return the p2
	 */
	public Coordinates getP2() {
		return p2;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Coordinates p2) {
		this.p2 = p2;
	}
	
}

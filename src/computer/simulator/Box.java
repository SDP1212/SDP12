/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Represents the box perpendicular to the axes with two defining corners passed
 * in its constructor.
 * @author 
 */
public class Box {
	private Coordinates[] corners = new Coordinates[2];

	public Box(Coordinates c0, Coordinates c1) {
		corners[0] = c0;
		corners[1] = c1;
	}
	/**
	 * Indicates whether a given point inside this box.
	 * @param A point.
	 * @return true : The point is inside this box
	 * @return false : The point is not inside this box.
	 */
	public boolean  isPointInside(Coordinates p) {
		Coordinates c0 = corners[0];
		Coordinates c1 = corners[1];
		boolean xInside = (c0.getX() <= p.getX() || c1.getX() <= p.getX()) && (c0.getX() >= p.getX() || c1.getX() >= p.getX());
		boolean yInside = (c0.getY() <= p.getY() || c1.getY() <= p.getY()) && (c0.getY() >= p.getY() || c1.getY() >= p.getY());
		return xInside && yInside;
	}

	@Override
	public String toString() {
		return corners[0].toString() + " " + corners[1].toString();
	}
	
	
}

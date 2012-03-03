/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai.pathsearch;

/**
 *
 * @author Evgeniya Sotirova
 */
import java.awt.Point;

@SuppressWarnings("serial")
public class GridCoordinates extends Point{
	
	GridCoordinates parent;
	int movementCost;
	int heuristicCost;
	int totalCost;
	
	public GridCoordinates(int x,int y) {
		super(x,y);
		movementCost = 0;
		heuristicCost = 0;
		totalCost = 0;
	}
	
	public void setParent(GridCoordinates parent) {
		this.parent = parent;
	}
	public GridCoordinates getParent() {
		return parent;
	}
	
	public void setMovementCost(int cost) {
		this.movementCost = cost;
	}
	
	public int getMovementCost() {
		return movementCost;
	}
	
	public void setHeuristicCost(int cost) {
		this.heuristicCost = cost;
	}
	
	public int getHeuristicCost() {
		return heuristicCost;
	}
	
	public void setTotalCost(int cost) {
		this.totalCost = cost;
	}
	
	public int getTotalCost() {
		return totalCost;
	}
}
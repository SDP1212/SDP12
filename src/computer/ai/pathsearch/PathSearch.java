/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai.pathsearch;
import computer.simulator.Coordinates;

/**
 *
 * @author Evgeniya Sotirova
 */
import computer.simulator.Pitch;

import java.awt.Point;
import java.util.ArrayList;

/**
 * A* path planner. Partitions the pitch into many grids
 * to find the "least cost" path between two points.
 * 
 * Avoids the opponent.
 * Blocks the opponent.
 * Does not plan points outside pitch.
 * Attempts to avoid own goal.
 * Attempts to align to shoot.
 * 
 * getNextWaypoint returns the next waypoint in the path
 * getPath returns the entire path
 * 
 * Both take as parameter the angle in RADIANS.
 **/

public class PathSearch {

	public static GridCoordinates ourGridPosition;
	public static GridCoordinates oppGridPosition;
	public static GridCoordinates ballGridPosition;
	public static double ourAngle = 0;

	private static int PITCH_WIDTH = 2;
	private static int PITCH_HEIGHT = 1;

	// ideally, we want squares and we want them relatively small
	// default is 21x21 for fast computation 30 & 16
	private static int widthInGrids = (int) (32 * 1.5);
	private static int heightInGrids = (int) (16 * 1.5);
	private static double gridWidth = (double) PITCH_WIDTH/widthInGrids;
	private static double gridHeight = (double) PITCH_HEIGHT/heightInGrids;
	private static GridCoordinates startGridPoint;
	private static GridCoordinates endGridPoint;
	private static GridPointComparator comparator = new GridPointComparator();

	private static Coordinates startCoorPoint;
	private static Coordinates endCoorPoint;

	private static ArrayList<GridCoordinates> path;
	private static ArrayList<GridCoordinates> validGrids;
	private static ArrayList<GridCoordinates> invalidGrids;

	private final static int LEFT = Pitch.TARGET_LEFT_GOAL;
	private final static int RIGHT = Pitch.TARGET_RIGHT_GOAL;

	public static int ourSide;

	// return a list of Points to go through
	public static ArrayList<Coordinates> getPath(int shootingLeft, Coordinates _aimPosition, Coordinates _ourPosition, double _ourAngle, Coordinates _obstaclePosition) {
		
            if (shootingLeft == LEFT)
			ourSide = RIGHT;
            else
			ourSide = LEFT;

	    startCoorPoint = _ourPosition;
		
            // translation from pitch coordinate system to grids
            oppGridPosition = translateCoordinatesToGrid(_obstaclePosition);
            ourGridPosition = translateCoordinatesToGrid(_ourPosition);
            ballGridPosition = translateCoordinatesToGrid(_aimPosition);
            endCoorPoint = _aimPosition;
            ourAngle = _ourAngle;

            path = new ArrayList<GridCoordinates>();
            validGrids = new ArrayList<GridCoordinates>();
            invalidGrids = new ArrayList<GridCoordinates>();

            startGridPoint = translateCoordinatesToGrid(_ourPosition);
            endGridPoint = translateCoordinatesToGrid(_aimPosition);

            validGrids.add(startGridPoint);
            search(startGridPoint, endGridPoint);

            path = optimisePath(path);

            ArrayList<Coordinates> waypoints = translateGridsToCoordinates(path);

            return waypoints;
	}

	public static Coordinates getNextWaypoint(int shootingLeft, Coordinates _ballPosition, Coordinates _aimPosition, Coordinates _ourPosition, double _ourAngle, Coordinates _oppPosition) {
		if (shootingLeft == LEFT)
			ourSide = RIGHT;
		else
			ourSide = LEFT;

		startCoorPoint = _ourPosition;
		
                // translation from pitch coordinate system to grids
		oppGridPosition = translateCoordinatesToGrid(_oppPosition);
		ourGridPosition = translateCoordinatesToGrid(_ourPosition);
		ballGridPosition = translateCoordinatesToGrid(_ballPosition);
		endCoorPoint = _aimPosition;
		ourAngle = _ourAngle;

		path = new ArrayList<GridCoordinates>();
		validGrids = new ArrayList<GridCoordinates>();
		invalidGrids = new ArrayList<GridCoordinates>();

		startGridPoint = translateCoordinatesToGrid(_ourPosition);
		endGridPoint = translateCoordinatesToGrid(_aimPosition);

		validGrids.add(startGridPoint);
		search(startGridPoint, endGridPoint);

		path = optimisePath(path);

		ArrayList<Coordinates> waypoints = translateGridsToCoordinates(path);
		System.out.println("Waypoint: " + waypoints.get(1));
		return waypoints.get(1);

	}

	private static ArrayList<GridCoordinates> optimisePath(ArrayList<GridCoordinates> path) {
		if (path.size()>3) {
			ArrayList<GridCoordinates> newPath = new ArrayList<GridCoordinates>();
				newPath.add(path.get(0));
				for (int i = 1; i < path.size(); i++) {

					if (//(oppGridPosition.distance(path.get(i)) > 8 && newPath.get(newPath.size() - 1).distance(path.get(i)) > 8) ||
						newPath.get(newPath.size() - 1).distance(path.get(i)) > 5) {
						newPath.add(path.get(i));
					}
				}

				// optimise angles repeatedly 3 times
				for (int j = 0; j < 3; j++) {
					for (int i = 0; i < newPath.size() - 2; i++) {
						// remove points that hardly change in gradient
						if (Math.abs((getAngle(newPath.get(i), newPath.get(i + 1))) - (getAngle(newPath.get(i + 1), newPath.get(i + 2)))) < Math.PI / 6) {
							newPath.remove(i + 2);
						}
					}
				}

				return newPath;
		}
		return path;

	}

	private static void search(GridCoordinates currentPoint, GridCoordinates endPoint) {

		// go through each of the 8 adjacent squares to the currentPoint
		for (int x = currentPoint.x - 1; x < currentPoint.x + 2; x++) {
			for (int y = currentPoint.y - 1; y < currentPoint.y + 2; y++) {

				GridCoordinates pt = new GridCoordinates(x, y);
				// check whether grid is on the "blacklist"
				if (!invalidGrids.contains(pt)) {
					// check in range of grids
					if (x > 0 && y > 0 && x <= widthInGrids && y <= heightInGrids) {
						// if it's not already on check list, add it
						if (!validGrids.contains(pt)) {
							validGrids.add(pt);
							pt.setParent(currentPoint);
							if ((pt.x == endGridPoint.x) && (pt.y == endGridPoint.y)) {
								endGridPoint.setParent(currentPoint);
							}
							pt.setMovementCost(pt.getParent().getMovementCost()	+ calcMovementCost(currentPoint, pt));
							pt.setHeuristicCost(calcHeuristicCost(pt,endPoint));
							pt.setTotalCost(pt.getMovementCost()+ pt.getHeuristicCost());
						}
						if (validGrids.contains(pt)) {
							if (pt.getMovementCost() > calcMovementCost(currentPoint, pt)) {
								pt.setParent(currentPoint);
								pt.setMovementCost(pt.getParent().getMovementCost()+ calcMovementCost(currentPoint, pt));
								pt.setTotalCost(pt.getMovementCost()+ pt.getHeuristicCost());
							}
						}
					}
				}
			}
		}
		validGrids = comparator.sortGridPoints(validGrids);
		if (validGrids.size() > 0) {
			GridCoordinates closestPt = validGrids.get(0);

			validGrids.remove(closestPt);
			if ((closestPt.x == endGridPoint.x) && (closestPt.y == endGridPoint.y)) {
				tracePath(startGridPoint, endGridPoint);
			} else {
				invalidGrids.add(closestPt);
				search(closestPt, endPoint);
			}
		} else
			return;
	}

	private static int calcMovementCost(GridCoordinates currentPoint,GridCoordinates newPoint) {
		int value = 0;
		if (oppGridPosition.distance(newPoint) < 6) {
			// discourage it heavily, to not crash into opponent
			value+= 400;
		}
		
		if(ballGridPosition.distance(newPoint) < 5){
			value+= 500;
		}
		if (ourSide == LEFT) {
			if (Math.abs(newPoint.y - ballGridPosition.y) < 6 && newPoint.x >= ballGridPosition.x)
				value+= 65;
		}
		if (ourSide == RIGHT) {
			if (Math.abs(newPoint.y - ballGridPosition.y) < 6 && newPoint.x <= ballGridPosition.x)
				value+= 65;
		}
		if(ourGridPosition.y < 3 || ourGridPosition.y > 21){
			value+=500;
		}
		
		if(ourGridPosition.x < 3 || ourGridPosition.x > 45) {
			value+=500;
		}

		//if (Math.abs(oppGridPosition.y - newPoint.y) < 5) {
		//	value+= 18;
		//}
		// horizontal and vertical movements
		if (Math.abs(newPoint.x - currentPoint.x) + Math.abs(newPoint.y - currentPoint.y) == 1) {
			value+= 10;
		}

		// diagonal movements
		if (Math.abs(newPoint.x - currentPoint.x) + Math.abs(newPoint.y - currentPoint.y) == 2) {
			value+= 14;
		}

		return value;
	}

	// Diagonal shortcut or Manhattan distance
	// Manhattan distance has the nice side effect of aiming for the goal
	private static int calcHeuristicCost(GridCoordinates currentPoint, GridCoordinates endPoint) {
		return 10 * (Math.abs(endPoint.x - currentPoint.x) + Math.abs(endPoint.y - currentPoint.y));
	}

	private static void tracePath(GridCoordinates startPoint, GridCoordinates endPoint) {
		path.add(0, endPoint);
		if (endPoint.getParent() != null) {
			tracePath(startPoint, endPoint.getParent());
		}
	}

	private static GridCoordinates translateCoordinatesToGrid(Coordinates pt) {
		int gridX = (int) Math.ceil(pt.getX() / gridWidth);
		int gridY = (int) Math.ceil(pt.getY() / gridHeight);
		GridCoordinates gridPoint = new GridCoordinates(gridX, gridY);
		return gridPoint;
	}

	public static ArrayList<Coordinates> translateGridsToCoordinates(ArrayList<GridCoordinates> foundPath) {
		ArrayList<Coordinates> coordinateList = new ArrayList<Coordinates>();
		// add starting coordinate to the list
		coordinateList.add(0, new Coordinates(startCoorPoint.getX(), startCoorPoint.getY()));
		// translate every gridpoint except the first and last into coordinates
		if (foundPath.size() > 1) {
			for (int i = 1; i < foundPath.size() - 1; ++i) {
				GridCoordinates gp = foundPath.get(i);
				Coordinates gridMidPoint = new Coordinates();
				gridMidPoint.setX(((gp.getX() - 1) * gridWidth) + gridWidth/2.0);
				gridMidPoint.setY(((gp.getY() - 1) * gridHeight) + gridHeight/2.0);
				coordinateList.add(gridMidPoint);
			}
		}
		// add end coordinate to the list
		coordinateList.add(coordinateList.size(), new Coordinates(endCoorPoint.getX(), endCoorPoint.getY()));
		return coordinateList;
	}

	public static ArrayList<GridCoordinates> getInvalid() {
		return invalidGrids;
	}

	private static double getAngle(Point a, Point b) {
		return Math.atan2((a.y - b.y), (b.x - a.x));
	}
}

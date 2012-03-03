/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai.pathsearch;

/**
 *
 * @author Evgeniya Sotirova
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class GridPointComparator implements Comparator {

	
    @Override
    public int compare(Object o1, Object o2) {
		if (((GridCoordinates) o1).getTotalCost() < ((GridCoordinates) o2).getTotalCost())
			return -1;
		else
			return 1;
    }
    public ArrayList<GridCoordinates> sortGridPoints(ArrayList<GridCoordinates> grids) {
		Collections.sort(grids, this);
		return grids;
	}
}
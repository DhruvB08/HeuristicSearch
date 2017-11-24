package solver;

import java.util.ArrayList;
import grids.Cell;

public class AStar extends AbstractHeuristic {

	public AStar(Heuristic h) {
		super();
		weight1 = 1.0;
		heuristic = h;
	}
	
	@Override
	public ArrayList<Cell> solve(Cell start, Cell end) {
		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 160; j++) {
				grid[i][j].gvals[0] = Integer.MAX_VALUE;
				grid[i][j].fvals[0] = Integer.MAX_VALUE;
				grid[i][j].parents[0] = null;
			}
		}
		
		start.gvals[0] = 0;
		fringes[0] = new Fringe(0);
		visited.get(0).clear();
		start.fvals[0] = (float) (start.gvals[0] + (weight1 * heuristicValue(start, end, heuristic)));
		fringes[0].insert(start);
		
		while (fringes[0].heapSize > 0) {
			Cell cell = fringes[0].peek();
			if (cell == end) {
				return getPath(cell, 0);
			} 
			else {
				expandCell(cell, 0, heuristic, end);
			}
		}
		
		return null;
	}
	
	//implementation of solve method
	/*
	 Upon initialization
	 	assumes the g-value and f-value of every vertex to be infinity and the parent of every vertex to NULL
	 	sets the g-value of the start vertex to zero and the parent of the start vertex to itself
	 sets the fringe and closed lists to the empty lists
	 inserts the start vertex into the fringe list with its ƒ-value as its priority
	 
	  repeatedly executes the following statements:
	  	 If the fringe list is empty
	  	 	no path available, return
	  	 identifies a vertex s with the smallest ƒ-value in the fringe list
	  	 If this vertex is the goal vertex
	  	 	found a path from the start vertex to the goal vertex
	  	 	create arraylist of squares
	  	 	add goal to arraylist, and keep adding parent of square to list until parent == start vertex
	  	 	reverse this arraylist, return arraylist
	  	 call expand cell using 0 as index
	 */
}

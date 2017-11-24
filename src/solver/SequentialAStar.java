package solver;

import java.util.ArrayList;

import grids.Cell;
import grids.Grid;

public class SequentialAStar extends AbstractHeuristic {

	public SequentialAStar(double w1, double w2, Heuristic h) {
		super();
		weight1 = w1;
		weight2 = w2;
		heuristic = h;
	}
	
	@Override
	public ArrayList<Cell> solve(Cell start, Cell end) {
		for (int i = 0; i < 5; i++) {
			fringes[i] = new Fringe(i);
			visited.get(i).clear();
			
			for (int j = 0; j < Grid.ROWS; j++) {
				for (int k = 0; k < Grid.COLUMNS; k++) {
					grid[j][k].gvals[i] = Integer.MAX_VALUE;
					grid[j][k].fvals[i] = Integer.MAX_VALUE;
					grid[j][k].parents[i] = null;
				}
			}
			
			start.gvals[i] = 0;
			start.fvals[i] = (float) (weight1 * heuristicValue(start, end, heuristics[i]));
			fringes[i].insert(start);
		}
		
		while (fringes[0].peek().fvals[0] < Integer.MAX_VALUE) {
			for (int i = 1; i < 5; i++) {
				if (fringes[i].peek().fvals[i] < weight2 * fringes[0].peek().fvals[0]) {
					if (end == fringes[i].peek()) {
						return getPath(end, i);
					}
					else {
						expandCell(fringes[i].peek(), i, heuristics[i], end);
					}
				}
				else {
					if (end == fringes[0].peek()) {
						return getPath(end, i);
					}
					else {
						expandCell(fringes[0].peek(), 0, heuristics[0], end);
					}
				}
			}
		}
		
		return null;
	}

	//implementation of solve method
	/*
	for loop from 0 to 5
		Fringe[i] = new Fringe
		visited[i] = new hashset
		
		for every cell in grid
			gvals[i] = max int
			fvals[i] = max int
			parents[i] = null
		
		start.gvals[i] = 0
		start.fvals[i] = applyHeuristic(start, end, heuristic[i])
		Fringe[i].insert(start, start.fvals[i])
		
	while fringe[0].min < max int
		for loop from 1 to 5
			if fringe[i].min <= w2 * fringe[0].min
				if goal.gvals[i] <= fringe[i].min && goal.gvals[i] < max int
					create new arraylist of squares
					keep adding current square to list until parent[i] == null
					reverse arraylist, return it
				else
					expand(fringe[i].peek, i)
			else
				if goal.gvals[0] <= fringe[0].min && goal.gvals[0] < max int
					create new arraylist of squares
					keep adding square to list until parent[i] == null
					reverse arraylist, return it
				else
					expand(fringe[0].peek, 0)
	
	return null
	 */
}

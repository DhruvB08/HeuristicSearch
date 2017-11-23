package solver;

public class SequentialAStar {

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

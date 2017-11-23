package solver;

public abstract class AbstractHeuristic {

	//currently used heuristic
	//currently used grid
	//number of nodes expanded
	
	//Fringe[] field
	//hashset[] for expanded vertices
	//Heuristic[] for different heuristics
	
	//enums for each of 5 heuristics
	
	//abstract solve method
		//takes grid, start square, end square as inputs
		//returns arraylist containing path from start to end
	
	//apply heuristic method
		//takes start square and end square as inputs
		//estimates cost from start to end using given heuristic
	
	//expand cell method
		/*
		takes cell, and index as inputs
		 
 	 	remove the vertex from the fringe list
	 	generating each of its unexpanded successors (s0)
	 		checks whether the g-value of vertex s plus the straight-line distance from vertex s to vertex s0 is smaller than g-value of vertex s0
			sets the g-value of vertex s0 to the g-value of vertex s plus the straight-line distance from vertex s to vertex s0
			sets the parent of vertex s0 to vertex s
			remove (s0, s0.f-value) from fringe
			set s0.f-value to g-value plus heuristic from this cell to end
			inserts vertex s0 into the fringe list with its ƒ-value as its priority
		add s to visited set
		*/
	
	//getpath method
		//takes square and index i as inputs
		//returns arraylist from square to null, following parent at index i
}

package solver;

public class AStar extends AbstractHeuristic {
	
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

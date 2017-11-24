package solver;

import java.util.ArrayList;

import grids.Cell;

public class UniformCost extends AStar {

	public UniformCost(Heuristic h) {
		super(h);
		weight1 = 0;
	}
}

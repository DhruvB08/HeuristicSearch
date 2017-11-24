package solver;

import java.util.ArrayList;

import grids.Cell;

public class UniformCost extends AbstractHeuristic {

	@Override
	public ArrayList<Cell> solve(Cell start, Cell end) {
		WeightedAStar solver = new WeightedAStar(0, Heuristic.AVOIDHARD);
		solver.grid = grid;
		return solver.solve(start, end);
	}

}

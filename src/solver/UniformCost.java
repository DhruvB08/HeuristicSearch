package solver;

public class UniformCost extends AStar {

	public UniformCost(Heuristic h) {
		super(h);
		weight1 = 0;
	}
}

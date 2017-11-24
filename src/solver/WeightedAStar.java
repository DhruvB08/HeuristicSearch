package solver;

public class WeightedAStar extends AStar {

	public WeightedAStar(double weight, Heuristic h) {
		super(h);
		weight1 = weight;
	}
	
	//call solve from A-star with specified weight
}

package grids;

public class Square {
	boolean isBlocked;
	boolean hardTraverse;
	boolean isRiver;
	int x, y;
	//each square should contain a rectangle object
	//create 1px black border on each rectangle
	//float[] for gvals
	//float[] for fvals
	//Square[] for parents
	
	//create enums to describe each square type
	
	//square constructors using either coords or enum type
	
	/**
	 * Get the cost of traversing to another block, from this block
	 * 
	 * @param other		square to traverse to
	 * @return			cost required
	 */
	public double traverseTo(Square other) {
		if (isBlocked && other.isBlocked) {
			return 0;
		}
		
		double res1 = 1;
		if (hardTraverse) {
			res1 *= 2;
		}
		if (x != other.x && y != other.y) {
			res1 *= Math.sqrt(2.0);
		}
		
		double res2 = 1;
		if (other.hardTraverse) {
			res2 *= 2;
		}
		if (x != other.x && y != other.y) {
			res2 *= Math.sqrt(2.0);
		}
		
		double res = (res1 + res2) / 2;
		if (isRiver && other.isRiver) {
			return res / 4;
		}
		
		return res;
	}
	
	/**
	 * Show this square onto the screen
	 */
	public void showSquare() {
		
	}
	
	//print this cell as a string
	
	//add equals method to check if two cells equal
}

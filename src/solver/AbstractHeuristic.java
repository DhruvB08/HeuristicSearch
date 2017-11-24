package solver;

import grids.Cell;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractHeuristic {

	public Heuristic heuristic;					//currently used heuristic
	public Cell[][] grid;						//currently used grid
	public Fringe[] fringes;					//Fringe[] field
	public ArrayList<HashSet<Cell>> visited;	//hashset[] for expanded vertices
	public Heuristic[] heuristics;				//Heuristic[] for different heuristics
	public double weight1;
	public double weight2;
	
	//enums for each of 5 heuristics
	public enum Heuristic {
		MINDISTANCE, TWOMOVES, AVOIDHARD, GOFORRIVER, NORMALCOST
	}
	
	//blank constructorSS
	public AbstractHeuristic() {
		fringes = new Fringe[5];
		for (int i = 0; i < 5; i++) {
			fringes[i] = new Fringe(i);
		}
		
		visited = new ArrayList<HashSet<Cell>>();
		for (int i = 0; i < 5; i++) {
			visited.add(new HashSet<Cell>());
		}
		
		heuristics = new Heuristic[5];
		heuristics[0] = Heuristic.MINDISTANCE;
		heuristics[1] = Heuristic.TWOMOVES;
		heuristics[2] = Heuristic.AVOIDHARD;
		heuristics[3] = Heuristic.GOFORRIVER;
		heuristics[4] = Heuristic.NORMALCOST;
		
		weight1 = 0;
		weight2 = 0;
	}
	
	//number of nodes expanded
	public int nodesExpanded() {
		int res = 0;
		
		for (int i = 0; i < visited.size(); i++) {
			if (visited.get(i) != null) {
				res += visited.get(i).size();
			}
		}
		
		return res;
	}
	
	//abstract solve method
		//takes grid, start square, end square as inputs
		//returns arraylist containing path from start to end
	public abstract ArrayList<Cell> solve(Cell start, Cell end);
	
	//apply heuristic method
		//takes start square and end square as inputs
		//estimates cost from start to end using given heuristic
	public float heuristicValue(Cell start, Cell end, Heuristic h) {
		switch (h) {
		case AVOIDHARD:
			return avoidhard(start, end);
		case GOFORRIVER:
			return goforriver(start, end);
		case MINDISTANCE:
			return minDist(start, end);
		case NORMALCOST:
			return normalcost(start, end);
		case TWOMOVES:
			return twomoves(start, end);
		default:
			return 5;
		}
	}
	
	//mindistance heuristic
	private float minDist(Cell start, Cell end) {
		float f = 0;
		float rise = Math.abs(start.x - end.x);
		float run = Math.abs(start.y - end.y);
		f = (float) Math.sqrt((rise * rise) + (run * run));
		return f/4;
	}
	
	//twomoves heuristic
	private float twomoves(Cell start, Cell end) {
		float rise = Math.abs(start.x - end.x);
		float run = Math.abs(start.y - end.y);
		return rise + run;
	}
	
	//avoidhard heuristic
	private float avoidhard(Cell start, Cell end) {
		float res = 0;
		
		if (start.x > end.x) {
			int x1 = start.x;
			
			while (x1 > end.x) {
				if (grid[x1][start.y].hardTraverse()) {
					res += 4;
				} else {
					res += 1;
				}
				
				x1--;
			}
		}
		else {
			int x1 = start.x;
			
			while (x1 < end.x) {
				if (grid[x1][start.y].hardTraverse()) {
					res += 4;
				} else {
					res += 1;
				}
				
				x1++;
			}
		}
		
		if (start.y > end.y) {
			int y1 = start.y;
			
			while (y1 > end.y) {
				if (grid[end.x][y1].hardTraverse()) {
					res += 4;
				} else {
					res += 1;
				}
				
				y1--;
			}
		}
		else {
			int y1 = start.y;
			
			while (y1 < end.y) {
				if (grid[end.x][y1].hardTraverse()) {
					res += 4;
				} else {
					res += 1;
				}
				
				y1++;
			}
		}
		
		return res;
	}
	
	//goforriver heuristic
	private float goforriver(Cell start, Cell end) {
		float res = 0;
		
		if (start.x > end.x) {
			int x1 = start.x;
			
			while (x1 > end.x) {
				if (grid[x1][start.y].isRiver()) {
					res += 0.25;
				} else {
					res += 1;
				}
				
				x1--;
			}
		}
		else {
			int x1 = start.x;
			
			while (x1 < end.x) {
				if (grid[x1][start.y].isRiver()) {
					res += 0.25;
				} else {
					res += 1;
				}
				
				x1++;
			}
		}
		
		if (start.y > end.y) {
			int y1 = start.y;
			
			while (y1 > end.y) {
				if (grid[end.x][y1].isRiver()) {
					res += 0.25;
				} else {
					res += 1;
				}
				
				y1--;
			}
		}
		else {
			int y1 = start.y;
			
			while (y1 < end.y) {
				if (grid[end.x][y1].isRiver()) {
					res += 0.25;
				} else {
					res += 1;
				}
				
				y1++;
			}
		}
		
		return res;
	}
	
	//normalcost heuristic
	private float normalcost(Cell start, Cell end) {
		float res = 0;
		
		if (end.x > start.x) {
			int x1 = start.x;
			while (end.x > x1) {
				res += grid[x1][start.y].costTo(grid[x1 + 1][start.y]);
				x1++;
			}
		}
		else {
			int x1 = start.x;
			while (end.x < x1) {
				res += grid[x1][start.y].costTo(grid[x1 - 1][start.y]);
				x1--;
			}
		}
		
		if (end.y > start.y) {
			int y1 = start.y;
			while (end.y > y1) {
				res += grid[end.x][y1].costTo(grid[end.x][y1 + 1]);
				y1++;
			}
		}
		else {
			int y1 = start.y;
			while (end.y < y1) {
				res += grid[end.x][y1].costTo(grid[end.x][y1 - 1]);
				y1--;
			}
		}
		
		return res;
	}
	
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
	public void expandCell(Cell given, int index, Heuristic h, Cell end) {
		fringes[index].remove(given);
		Set<Cell> succ = getSuccessors(given);
		
		for (Cell c : succ) {
			float g2 = given.gvals[index] + given.costTo(c);
			if (g2 >= c.gvals[index]) {
				continue;
			}
			if (visited.get(index).contains(c)) {
				continue;
			}
			
			c.gvals[index] = g2;
			c.parents[index] = given;
			fringes[index].remove(c);
			c.fvals[index] = (float) (c.gvals[index] + weight1 * heuristicValue(c, end, h));
			fringes[index].insert(c);
		}
		
		visited.get(index).add(given);
	}
	
	//get all successors of given cell
	private Set<Cell> getSuccessors(Cell curr) {
		Set<Cell> set = new HashSet<Cell>();
		
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				
				int xVal = curr.x + i;
				int yVal = curr.y + j;
				if (xVal < 0 || xVal > 119 || yVal < 0 || yVal > 159) {
					continue;
				}
				
				if (grid[xVal][yVal].isBlocked()) {
					continue;
				}
				
				set.add(grid[xVal][yVal]);
			}
		}
		
		return set;
	}
	
	//getpath method
		//takes square and index i as inputs
		//returns arraylist from square to null, following parent at index i
	public ArrayList<Cell> getPath(Cell end, int index) {
		ArrayList<Cell> res = new ArrayList<Cell>();
		Cell curr = end;
		
		while (curr != null) {
			res.add(curr);
			curr = curr.parents[index];
		}
		
		Collections.reverse(res);
		return res;
	}
}

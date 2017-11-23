package grids;

import java.util.Random;

import grids.Cell.CellType;
import javafx.scene.shape.Rectangle;

public class Grid {

	//dimensions are 160 columns by 120 rows
	public static final int COLUMNS = 160;
	public static final int ROWS = 120;
	public Cell[][] grid;
	public Cell[] hardCenters;
	
	//grid constructor using height/width
	public Grid() {
		grid = createGrid();
	}
	
	//create whole grid method
	public Cell[][] createGrid() {
		Cell[][] grid = new Cell[ROWS][COLUMNS];
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].rect = new Rectangle(10, 10);
				grid[i][j].rect.setX(i * 10);
				grid[i][j].rect.setY(j * 10);
			}
		}
		
		return grid;
	}
	
	//create hard cells method
	/*
	select eight coordinates randomly (rnd, yrnd)
	For each coordinate pair (rnd, yrnd), consider the 31x31 region centered at this coordinate pair. 
	For every cell inside this region, choose with probability 50% to mark it as a hard to traverse cell
	*/
	public void addHardCells() {
		hardCenters = new Cell[8];
		Random random = new Random();
		
		for (int i = 0; i < 8; i++) {
			int x = random.nextInt(ROWS);
			int y = random.nextInt(COLUMNS);
			hardCenters[i] = grid[x][y];
			
			for (int j = -15; j < 16; j++) {
				for (int k = - 15; k < 16; k++) {
					int x2 = Math.min(Math.max(0, x + j), ROWS - 1);
					int y2 = Math.min(Math.max(0, y + k), COLUMNS - 1);
					
					if (random.nextBoolean()) {
						grid[x2][y2].convertTo(CellType.HARD);
					}
				}
			}
		}
	}
	
	//create highways method
	/*
	  select four paths on the map that allow the agent to move faster along them
	  these highways are allowed to propagate only horizontally or vertically
	  For each one of these paths, start with a random cell at the boundary of the grid world
	  move in a random horizontal or vertical direction for 20 cells but away 
	  	from the boundary and mark this sequence of cells as containing a highway
	  To continue, with 60% probability select to move in the same direction and 20% probability select to move in a perpendicular direction
	  	(turn the highway left or turn the highway right)
	  mark 20 cells as a highway along the selected direction
	  If you hit a cell that is already a highway in this process, reject the path and start the process again
	  Continue marking cells in this manner, until you hit the boundary again
	  If the length of the path is less than 100 cells when you hit the boundary, then reject the path and start the process again
	  If you cannot add a highway given the placement of the previous rivers, start the process from the beginning
	 */
	public boolean addRivers() {
		int MAXTRIES = 15;
		int FAILS = 0;
		
		for (int j = 0; j < 4; j++) {
			if (!addRiver()) {
				FAILS++;
				j--;
			}
			
			if (FAILS >= MAXTRIES) {
				return false;
			}
		}
		
		return true;
	}
	
	//add one whole river
	private boolean addRiver() {
		int riverLength = 0;
		Cell start = randomBoundaryCell();
		String firstDir = randomDirection();
	}
	
	//pick a random cell on the boundary
	private Cell randomBoundaryCell() {
		Random random = new Random();
		
		int x = 0;
		if (random.nextBoolean()) {
			x = ROWS - 1;
		}
		
		int y = 0;
		if (random.nextBoolean()) {
			y = COLUMNS - 1;
		}
		
		return grid[x][y];
	}
	
	private String randomDirection() {
		Random random = new Random();
		int choice = random.nextInt(4);
		
		if (choice == 0) {
			return "RIGHT";
		}
		else if (choice == 1) {
			return "LEFT";
		}
		else if (choice == 2) {
			return "UP";
		}
		return "DOWN";
	}
	
	//create blocked cells method
	/*
	  select randomly 20% of the total number of cells (i.e., 3,840 cells) to mark as blocked
	  you should not mark any cell that has a highway as a blocked cell
	 */
	
	//place start and goal vertices onto grid
	/*
	 randomly select unblocked cell in 20x20 region in any of four corners
	 place start cell there
	 randomly select unblocked cell in 20x20 region in any of four corners
	 place goal cell there
	 if distance between start and goal is less than 100, select new position for goal
	 */
	
	//create a grid from a file
	/*
	 given the filename as a string, open/read the file and assign cells accordingly
	 The first line provides the coordinates of sstart
	 The second line provides the coordinates of sgoal
	 The next eight lines provide the coordinates of the centers of the hard to traverse regions
	 provide 120 rows with 160 characters each that indicate the type of terrain for the map
		 – Use ’0’ to indicate a blocked cell
		 – Use ’1’ to indicate a regular unblocked cell
		 – Use ’2’ to indicate a hard to traverse cell
		 – Use ’a’ to indicate a regular unblocked cell with a highway
		 – Use ’b’ to indicate a hard to traverse cell with a highway
	 */
	
	//translate this grid into a file
	/*
	 given the name of what file to output as, create the file and write into it
	 use same format as above to write into file
	 */
	
	//display grid onto screen
	public void showGrid() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j].showCell();
			}
		}
	}
	
	//hide grid from screen
	public void hideGrid() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j].rect.setVisible(false);
			}
		}
	}
}

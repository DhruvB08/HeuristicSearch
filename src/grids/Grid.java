package grids;

import java.util.ArrayList;
import java.util.List;
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
		int MAXTRIES = 25;
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
		List<Cell> newRiver = new ArrayList<Cell>();
		int countTo20 = 0;
		Cell start = randomBoundaryCell();
		int startX = start.x;
		int startY = start.y;
		Cell nextCell = grid[startX][startY];
		String directionGoing = randomDirection();
		// This long thing of if statements checks the 8 cases of borders
		// if it for example wants to go left while starting at column zero, or anything similar to that
		// this long chunk says NOPE and gives it a new starting direction... Gives less fails.
		if(startX == 0){
			if(startY==0){	
				while(directionGoing.equals("UP") || directionGoing.equals("LEFT")){
					directionGoing=randomDirection();
				}
			}else if(startY==159){
				while(directionGoing.equals("UP") || directionGoing.equals("RIGHT")){
					directionGoing=randomDirection();
				}
			}else{
				while(directionGoing.equals("UP")){
					directionGoing=randomDirection();
				}
			}
		}else if(startX == 119){
			if(startY==0){	
				while(directionGoing.equals("DOWN") || directionGoing.equals("LEFT")){
					directionGoing=randomDirection();
				}
			}else if(startY==159){
				while(directionGoing.equals("DOWN") || directionGoing.equals("RIGHT")){
					directionGoing=randomDirection();
				}
			}else{
				while(directionGoing.equals("DOWN")){
					directionGoing=randomDirection();
				}
			}
		}else if(startY==0){
			while(directionGoing.equals("LEFT")){
				directionGoing=randomDirection();
			}
		}else if(startY==159){
			while(directionGoing.equals("RIGHT")){
				directionGoing=randomDirection();
			}
		}
		do{
			newRiver.add(nextCell);
			if(nextCell.type.equals(CellType.UNBLOCKED)){
				nextCell.convertTo(CellType.RIVER_UNBLOCKED);
			}else if(nextCell.type.equals(CellType.HARD)){
				nextCell.convertTo(CellType.RIVER_HARD);
			}else{
				System.out.println("I am starting at an already existing river cell!");
				return false;
			}
			if(directionGoing.equals("RIGHT")){
				startY++;
			}
			if(directionGoing.equals("LEFT")){
				startY--;
			}
			if(directionGoing.equals("UP")){
				startX--;
			}
			if(directionGoing.equals("DOWN")){
				startX++;
			}
			countTo20++;
			if(countTo20==20){
				directionGoing = randomDirectionContinued(directionGoing);
				countTo20=0;
			}
			if(startX < 0 || startX >119 || startY < 0 || startY > 159 ){
				break;
			}else{
				nextCell = grid[startX][startY];
			}
		}while(!nextCell.isRiver());
		System.out.println("This attempt's length was: " + newRiver.size());
		if(newRiver.size()>=100){
			System.out.println("Successful River!");
			return true;
		}else{
			for(int i=0; i<newRiver.size();i++){
				if(newRiver.get(i).type.equals(CellType.RIVER_UNBLOCKED)){
					newRiver.get(i).convertTo(CellType.UNBLOCKED);
				}else if(newRiver.get(i).type.equals(CellType.RIVER_HARD)){
					newRiver.get(i).convertTo(CellType.HARD);
				}else{
					System.out.println("I should never get hereasdf");
				}
			}
		
			System.out.println("Unsuccessful River. :(");
			return false;
		}
	}
	
	//pick a random cell on the boundary
	private Cell randomBoundaryCell() {
		Random random = new Random();
		int choice = random.nextInt(4);
		int rowRandom = random.nextInt(120);
		int columnRandom = random.nextInt(160);
		int x = 0;
		int y = 0;
		if (choice == 0) {
			// Start at top edge
			x = 0;
			y = columnRandom;
		}
		else if (choice == 1) {
			// Start at right edge
			x = rowRandom;
			y = 159;
		}
		else if (choice == 2) {
			// Start at bottom edge
			x = 119;
			y = columnRandom;
		}else if(choice ==3){
			// Start at left edge
			x = rowRandom;
			y = 0;
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
	private String randomDirectionContinued(String directionGoing){
		Random random = new Random();
		int choice = random.nextInt(10);
		if(directionGoing.equals("RIGHT")){
			if(choice <=5){
				return "RIGHT";
			}else if(choice == 6 || choice == 7){
				return "UP";
			}else if(choice == 8 || choice == 9){
				return "DOWN";
			}
		}
		if(directionGoing.equals("LEFT")){
			if(choice <=5){
				return "LEFT";
			}else if(choice == 6 || choice == 7){
				return "UP";
			}else if(choice == 8 || choice == 9){
				return "DOWN";
			}
		}
		if(directionGoing.equals("UP")){
			if(choice <=5){
				return "UP";
			}else if(choice == 6 || choice == 7){
				return "LEFT";
			}else if(choice == 8 || choice == 9){
				return "RIGHT";
			}
		}
		if(directionGoing.equals("DOWN")){
			if(choice <=5){
				return "DOWN";
			}else if(choice == 6 || choice == 7){
				return "LEFT";
			}else if(choice == 8 || choice == 9){
				return "RIGHT";
			}
		}
		return null;
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
	public static void main(String[] args){
		Grid testing = new Grid();
		boolean test = testing.addRivers();
	}
}

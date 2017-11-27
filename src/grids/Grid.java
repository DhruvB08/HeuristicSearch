package grids;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
	public static int cellSize;
	public List<List<Cell>> rivers;
	public Cell start;
	public Cell end;
	
	//blank grid constructor, wont add start/end cells
	public Grid() {
		grid = createGrid();
		addHardCells();
		
		rivers = new ArrayList<List<Cell>>();
		while (!addRivers()) {
			eraseRivers();
		}
		
		addBlockedCells();
	}
	
	//grid constructor using grid
	public Grid(Grid other) {
		grid = new Cell[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j] = new Cell(other.grid[i][j]);
			}
		}
		
		hardCenters = new Cell[other.hardCenters.length];
		for (int i = 0; i < hardCenters.length; i++) {
			hardCenters[i] = new Cell(other.hardCenters[i]);
		}
		
		rivers = new ArrayList<List<Cell>>();
		for (int i = 0; i < other.rivers.size(); i++) {
			rivers.add(new ArrayList<Cell>(other.rivers.get(i)));
		}
		
		this.addStartEnd();
	}
	
	//create whole grid method
	public Cell[][] createGrid() {
		Cell[][] grid = new Cell[ROWS][COLUMNS];
		cellSize = 10;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].rect = new Rectangle(cellSize, cellSize);
				grid[i][j].rect.setX(i * cellSize);
				grid[i][j].rect.setY(j * cellSize);
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
			if (!addRiver(j)) {
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
	private boolean addRiver(int numRiver) {
		ArrayList<Cell> newRiver = new ArrayList<Cell>();
		int countTo20 = 0;
		Cell start = randomBoundaryCell();
		String directionGoing = firstDirection(start);
		newRiver.add(start);
		Cell prevCell = start;
		
		if (start.type.equals(CellType.UNBLOCKED)) {
			start.convertTo(CellType.RIVER_UNBLOCKED);
		} else {
			start.convertTo(CellType.RIVER_HARD);
		}
		
		while (true) {
			int newX = prevCell.x;
			int newY = prevCell.y;
			
			if (directionGoing.equals("UP")) {
				newX--;
			}
			else if (directionGoing.equals("DOWN")) {
				newX++;
			}
			else if (directionGoing.equals("RIGHT")) {
				newY++;
			}
			else {
				newY--;
			}
			
			Cell nextCell = grid[newX][newY];
			
			if (nextCell.isRiver()) {
				eraseRiver(newRiver);
				return false;
			}
			
			newRiver.add(nextCell);
			countTo20++;
			if (nextCell.type.equals(CellType.UNBLOCKED)) {
				nextCell.convertTo(CellType.RIVER_UNBLOCKED);
			}
			else {
				nextCell.convertTo(CellType.RIVER_HARD);
			}
			
			if (nextCell.onBoundary()) {
				break;
			}
			
			if (countTo20 == 20) {
				countTo20 = 0;
				directionGoing = randomDirectionContinued(directionGoing);
			}
			
			prevCell = nextCell;
		}
		
		if (newRiver.size() > 100) {
			rivers.add(new ArrayList<Cell>(newRiver));
			return true;
		}
		
		eraseRiver(newRiver);
		return false;
		
		/*
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
		*/
	}
	
	//pick a random cell on the boundary, not in a corner
	private Cell randomBoundaryCell() {
		Random random = new Random();
		int choice = random.nextInt(4);
		int rowRandom = random.nextInt(ROWS);
		int columnRandom = random.nextInt(COLUMNS);
		int x = 0;
		int y = 0;
		
		if (choice == 0) {
			y = columnRandom;
			
			if (y == 0 || y == COLUMNS - 1) {
				return randomBoundaryCell();
			}
		}
		else if (choice == 1) {
			x = rowRandom;
			y = COLUMNS - 1;
			
			if (x == 0 || x == ROWS - 1) {
				return randomBoundaryCell();
			}
		}
		else if (choice == 2) {
			x = ROWS - 1;
			y = columnRandom;
			
			if (y == 0 || y == COLUMNS - 1) {
				return randomBoundaryCell();
			}
		} 
		else if(choice == 3) {
			x = rowRandom;
			
			if (x == 0 || x == ROWS - 1) {
				return randomBoundaryCell();
			}
		}
		
		return grid[x][y];
	}
	
	//get the first direction to go to
	private String firstDirection(Cell start) {
		if (start.x == 0) {
			return "DOWN";
		}
		else if (start.x == ROWS - 1) {
			return "UP";
		}
		else if (start.y == 0) {
			return "RIGHT";
		}
		
		return "LEFT";
	}
	
	//get next direction
	private String randomDirectionContinued(String directionGoing){
		Random random = new Random();
		double choice = random.nextDouble();
		
		//60% chance of staying same direction
		if (choice <= 0.6) {
			return directionGoing;
		}
		
		//20% chance of perpendicular
		else if (choice <= 0.8) {
			if (directionGoing.equals("UP") || directionGoing.equals("DOWN")) {
				return "RIGHT";
			}
			
			return "UP";
		}
		
		//20% chance of other perpendicular
		if (directionGoing.equals("UP") || directionGoing.equals("DOWN")) {
			return "LEFT";
		}
		
		return "DOWN";
	}
	
	//erase all rivers
	private void eraseRivers() {
		for (int i = 0; i < rivers.size(); i++) {
			List<Cell> currentRiver = rivers.get(i);
			eraseRiver(currentRiver);
		}
		
		while (!rivers.isEmpty()) {
			rivers.remove(0);
		}
	}
	
	//erase given river
	private void eraseRiver(List<Cell> river) {
		for (int i = 0; i < river.size(); i++) {
			Cell curr = river.get(i);
			
			if (curr.type.equals(CellType.RIVER_HARD)) {
				curr.convertTo(CellType.HARD);
			}
			else {
				curr.convertTo(CellType.UNBLOCKED);
			}
		}
	}
	
	//create blocked cells method
	/*
	  select randomly 20% of the total number of cells (i.e., 3,840 cells) to mark as blocked
	  you should not mark any cell that has a highway as a blocked cell
	 */
	public void addBlockedCells() {
		int numBlocked = (ROWS * COLUMNS) / 5;
		for (int i = 0; i < numBlocked; i++) {
			Cell picked = randomCell();
			
			if (!picked.isRiver() && !picked.isBlocked()) {
				picked.convertTo(CellType.BLOCKED);
			}
			else {
				i--;
			}
		}
	}
	
	//get any random cell on the grid
	private Cell randomCell() {
		Random random = new Random();
		int x = random.nextInt(ROWS);
		int y = random.nextInt(COLUMNS);
		return grid[x][y];
	}
	
	//place start and goal vertices onto grid
	/*
	 randomly select unblocked cell in 20x20 region in any of four corners
	 place start cell there
	 randomly select unblocked cell in 20x20 region in any of four corners
	 place goal cell there
	 if distance between start and goal is less than 100, select new position for goal
	 */
	public void addStartEnd() {
		if (this.start != null) {
			if (this.start.type.equals(CellType.START_HARD)) {
				this.start.convertTo(CellType.HARD);
			} else {
				this.start.convertTo(CellType.UNBLOCKED);
			}
		}
		
		if (this.end != null) {
			if (this.end.type.equals(CellType.END_HARD)) {
				this.end.convertTo(CellType.HARD);
			} else {
				this.end.convertTo(CellType.UNBLOCKED);
			}
		}
		
		Cell start = cornerRegion();
		this.start = start;
		if (start.type.equals(CellType.HARD) || start.type.equals(CellType.RIVER_HARD)) {
			start.convertTo(CellType.START_HARD);
		}
		else {
			start.convertTo(CellType.START_UNBLOCKED);
		}
		
		double dist = 0;
		Cell goal = null;
		while (dist <= 100) {
			goal = cornerRegion();
			double rise = Math.abs(start.x - goal.x);
			double run = Math.abs(start.y - goal.y);
			dist = Math.sqrt((rise * rise) + (run * run));
		}
		
		if (goal.type.equals(CellType.HARD) || goal.type.equals(CellType.RIVER_HARD)) {
			goal.convertTo(CellType.END_HARD);
		}
		else {
			goal.convertTo(CellType.END_UNBLOCKED);
		}
		this.end = goal;
	}
	
	//get a random cell from one of the 20x20 corner regions
	private Cell cornerRegion() {
		Random random = new Random();
		int ymin = 0, ymax = 0;
		int xmax = 0, xmin = 0;
		double res = random.nextDouble();
		
		if (res <= 0.25) {
			ymin = 0;
			ymax = 20;
			xmin = 0;
			xmax = 20;
		}
		else if (res <= 0.5) {
			ymin = 0;
			ymax = 20;
			xmin = ROWS - 20;
			xmax = ROWS;
		}
		else if (res <= 0.5) {
			ymin = COLUMNS - 20;
			ymax = COLUMNS;
			xmin = 0;
			xmax = 20;
		}
		else {
			ymin = COLUMNS - 20;
			ymax = COLUMNS;
			xmin = ROWS - 20;
			xmax = ROWS;
		}
		
		int x = random.nextInt(xmax - xmin) + xmin;
		int y = random.nextInt(ymax - ymin) + ymin;
		return grid[x][y];
	}
	
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
	public void createFromFile(String filename) {
		try {
			String line = null;
			FileReader reader = new FileReader(filename);
			BufferedReader reader2 = new BufferedReader(reader);
			
			int i = 1;
			int startX = 0, startY = 0;
			int goalX = 0, goalY = 0;
			while ((line = reader2.readLine()) != null) {
				if (i == 1) {
					String[] temp = line.split(" ");
					startX = Integer.parseInt(temp[0]);
					startY = Integer.parseInt(temp[1]);
				}
				else if (i == 2) {
					String[] temp = line.split(" ");
					goalX = Integer.parseInt(temp[0]);
					goalY = Integer.parseInt(temp[1]);
				}
				else if (i < 11) {
					String[] temp = line.split(" ");
					hardCenters[i - 3] = new Cell(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				}
				else {
					String[] temp = line.split(" ");
					
					for (int j = 0; j < temp.length; j++) {
						switch (temp[j]) {
						case "0":
							grid[i - 11][j].convertTo(CellType.BLOCKED);
							break;
						case "1":
							grid[i - 11][j].convertTo(CellType.UNBLOCKED);
							break;
						case "2":
							grid[i - 11][j].convertTo(CellType.HARD);
							break;
						case "a":
							grid[i - 11][j].convertTo(CellType.RIVER_UNBLOCKED);
							break;
						case "b":
							grid[i - 11][j].convertTo(CellType.RIVER_HARD);
							break;
						default:
							break;
						}
					}
				}
				
				i++;
			}
			
			this.start = grid[startX][startY];
			if (grid[startX][startY].type.equals(CellType.UNBLOCKED) || grid[startX][startY].type.equals(CellType.RIVER_UNBLOCKED)) {
				grid[startX][startY].convertTo(CellType.START_UNBLOCKED);
			} else {
				grid[startX][startY].convertTo(CellType.START_HARD);
			}
			
			this.end = grid[goalX][goalY];
			if (grid[goalX][goalY].type.equals(CellType.UNBLOCKED) || grid[goalX][goalY].type.equals(CellType.RIVER_UNBLOCKED)) {
				grid[goalX][goalY].convertTo(CellType.END_UNBLOCKED);
			} else {
				grid[goalX][goalY].convertTo(CellType.END_HARD);
			}
			
			reader2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//translate this grid into a file
	/*
	 given the name of what file to output as, create the file and write into it
	 use same format as above to write into file
	 */
	public void writeToFile(String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			BufferedWriter writer2 = new BufferedWriter(writer);
			
			writer2.write(start.x + " " + start.y + "\n");
			writer2.write(end.x + " " + end.y + "\n");
			
			for (int i = 0; i < hardCenters.length; i++) {
				writer2.write(hardCenters[i].x + " " + hardCenters[i].y + "\n");
			}
			
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					char res = '1';
					
					switch (grid[i][j].type) {
					case BLOCKED:
						res = '0';
						break;
					case END_HARD:
						res = '2';
						break;
					case END_UNBLOCKED:
						res = '1';
						break;
					case HARD:
						res = '2';
						break;
					case RIVER_HARD:
						res = 'b';
						break;
					case RIVER_UNBLOCKED:
						res = 'a';
						break;
					case START_HARD:
						res = '2';
						break;
					case START_UNBLOCKED:
						res = '1';
						break;
					case UNBLOCKED:
						res = '1';
						break;
					default:
						break;
					}
					
					writer2.write(res + " ");
				}
				
				writer2.write("\n");
			}
			
			writer2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//display grid onto screen
	public void showGrid() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				grid[i][j].rect.setY(i * cellSize);
				grid[i][j].rect.setX(j * cellSize);
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

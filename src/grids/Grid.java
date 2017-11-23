package grids;

public class Grid {

	//dimensions are 160 columns by 120 rows
	
	//grid constructor using height/width
	
	//create whole grid method
	
	//create hard cells method
	/*
	select eight coordinates randomly (rnd, yrnd)
	For each coordinate pair (rnd, yrnd), consider the 31x31 region centered at this coordinate pair. 
	For every cell inside this region, choose with probability 50% to mark it as a hard to traverse cell
	*/
	
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
	
	//hide grid from screen
}

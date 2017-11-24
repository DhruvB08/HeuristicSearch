package application;

import grids.Grid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import solver.AbstractHeuristic;

public class MainController {

	Grid[] grids;					//array of 50 grids
	Grid currGrid;					//currently displayed grid
	AbstractHeuristic searchAlgo;	//current search algorithm (use abstract class for this)
	
	@FXML Pane gridPaneView;
	@FXML ScrollPane gridPane;
	@FXML TextField gridIndex;
	
	//onclick for previous grid
	public void prevGrid(ActionEvent event) {
		int curr = Integer.parseInt(gridIndex.getText());
		
		if (curr == 0) {
			displayGrid(49);
		}
		else {
			displayGrid(Math.max(0, curr - 1));
		}
	}
	
	//onclick for next grid
	public void nextGrid(ActionEvent event) {
		int curr = Integer.parseInt(gridIndex.getText());
		
		if (curr == 49) {
			displayGrid(0);
		}
		else {
			displayGrid(Math.min(curr + 1, 49));
		}
	}
	
	//onclick for specified grid
	public void thisGrid(ActionEvent event) {
		int selected = Integer.parseInt(gridIndex.getText());
		displayGrid(Math.min(49, Math.max(0, selected)));
	}
	
	//private method to select/display a given grid index
	private void displayGrid(int index) {
		if (currGrid != null) {
			currGrid.hideGrid();
		}
		
		currGrid = grids[index];
		gridPaneView = new Pane();
		
		for (int i = 0; i < currGrid.grid.length; i++) {
			for (int j = 0; j < currGrid.grid[0].length; j++) {
				gridPaneView.getChildren().add(currGrid.grid[i][j].rect);
			}
		}
		
		currGrid.showGrid();
		gridIndex.setText(Integer.toString(index));
		gridPane.setContent(gridPaneView);;
	}
	
	//onclicks for each menuitem to select search algorithm
		//looks at text in textbox for weight
	
	//onclicks for each menuitem to select heuristic
		//sets the heuristicInUse field in AbstractHeuristic
	
	//onclick for displaying cell info
		//looks at text in textboxes for x/y coords
		//uses selected search algorithm and heuristic
		//displays f/g/h values in relevant textboxes
	
	//onclick for solve button
		//uses selected search algorithm and heuristic
		//displays solution path on textarea
	
	//onclick for generate puzzles button
	public void genPuzzles(ActionEvent event) {
		grids = new Grid[50];
		
		for (int i = 0; i < 5; i++) {
			Grid temp = new Grid();
			
			for (int j = 0; j < 10; j++) {
				grids[i * 10 + j] = new Grid(temp);
			}
		}
		
		displayGrid(0);
	}
	
	//onclick for export puzzle
		//finds filename from associated textbox
		//calls translate to file method on current grid
	
	//onclick for import puzzle
		//finds filename from associated textbox
		//calls translate from file method on current grid
	
	//onclick for get stats
		//use currently selected AbstractHeuristic with associated heuristic
		//create counters for runtime, resulting solution path length, number of nodes expanded, solved searches
		//for loop from 0 to 50
			//perform AbstractHeuristic on array[i]
			//add results to each counter
		//divide value in each counter by 50 (for average)
		//display search algorithm/heuristic and results in console
}

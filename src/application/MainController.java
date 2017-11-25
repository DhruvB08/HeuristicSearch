package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import grids.Cell;
import grids.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import solver.AStar;
import solver.AbstractHeuristic;
import solver.AbstractHeuristic.Heuristic;
import solver.SequentialAStar;
import solver.UniformCost;
import solver.WeightedAStar;

public class MainController {

	Grid[] grids;					//array of 50 grids
	Grid currGrid;					//currently displayed grid
	AbstractHeuristic searchAlgo;	//current search algorithm (use abstract class for this)
	
	@FXML Pane gridPaneView;
	@FXML ScrollPane gridPane;
	@FXML MenuButton searchAlgoMenu;
	@FXML MenuButton heuristicMenu;
	@FXML TextArea solutionBox;
	@FXML TextField gridIndex;
	@FXML TextField weight1;
	@FXML TextField weight2;
	@FXML TextField importName;
	@FXML TextField exportName;
	@FXML TextField gdisplay;
	@FXML TextField hdisplay;
	@FXML TextField fdisplay;
	@FXML TextField xbox;
	@FXML TextField ybox;
	
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
				Cell curr = currGrid.grid[i][j];
				gridPaneView.getChildren().add(curr.rect);
				curr.rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg) {
						showCellInfo(curr);
					}
				});
			}
		}
		
		currGrid.showGrid();
		gridIndex.setText(Integer.toString(index));
		gridPane.setContent(gridPaneView);;
	}
	
	private void showCellInfo(Cell cell) {
		searchAlgo.grid = currGrid.grid;
		searchAlgo.solve(currGrid.start, cell);
		float g = cell.gvals[0];
		float h = searchAlgo.heuristicValue(cell, currGrid.end, searchAlgo.heuristic);
		float f = g + h;
		
		gdisplay.setText(Float.toString(g));
		hdisplay.setText(Float.toString(h));
		fdisplay.setText(Float.toString(f));
	}
	
	//onclicks for each menuitem to select search algorithm
		//looks at text in textbox for weight
	public void setUniform(ActionEvent event) {
		searchAlgo = new UniformCost(searchAlgo.heuristic);
		searchAlgoMenu.setText("Uniform Cost");
	}
	
	public void setAStar(ActionEvent event) {
		searchAlgo = new AStar(searchAlgo.heuristic);
		searchAlgoMenu.setText("A*");
	}
	
	public void setWeightedA(ActionEvent event) {
		double w = Double.parseDouble(weight1.getText());
		searchAlgo = new WeightedAStar(w, searchAlgo.heuristic);
		searchAlgoMenu.setText("Weighted A*");
	}
	
	public void setSequential(ActionEvent event) {
		double w1 = Double.parseDouble(weight1.getText());
		double w2 = Double.parseDouble(weight2.getText());
		searchAlgo = new SequentialAStar(w1, w2, searchAlgo.heuristic);
		searchAlgoMenu.setText("Sequential A*");
	}
	
	//onclicks for each menuitem to select heuristic
		//sets the heuristicInUse field in AbstractHeuristic
	public void setHminDist(ActionEvent event) {
		searchAlgo.heuristic = Heuristic.MINDISTANCE;
		heuristicMenu.setText("Min Distance");
	}
	
	public void setH2Moves(ActionEvent event) {
		searchAlgo.heuristic = Heuristic.TWOMOVES;
		heuristicMenu.setText("Two Moves");
	}
	
	public void setHAvoidHard(ActionEvent event) {
		searchAlgo.heuristic = Heuristic.AVOIDHARD;
		heuristicMenu.setText("Avoid Hard");
	}
	
	public void setHRivers(ActionEvent event) {
		searchAlgo.heuristic = Heuristic.GOFORRIVER;
		heuristicMenu.setText("Go for rivers");
	}
	
	public void setHNormalCost(ActionEvent event) {
		searchAlgo.heuristic = Heuristic.NORMALCOST;
		heuristicMenu.setText("Normal Cost");
	}
	
	//onclick for displaying cell info
		//looks at text in textboxes for x/y coords
		//uses selected search algorithm and heuristic
		//displays f/g/h values in relevant textboxes
	public void cellInfo(ActionEvent event) {
		int x = Integer.parseInt(xbox.getText());
		int y = Integer.parseInt(ybox.getText());
		showCellInfo(currGrid.grid[x][y]);
	}
	
	//onclick for solve button
		//uses selected search algorithm and heuristic
		//displays solution path on textarea
	public void solvePuzzle(ActionEvent event) {
		solutionBox.clear();
		searchAlgo.grid = currGrid.grid;
		ArrayList<Cell> sol = searchAlgo.solve(currGrid.start, currGrid.end);
		
		if (sol == null) {
			solutionBox.appendText("no solution\n");
			return;
		}
		
		for (int i = 0; i < sol.size(); i++) {
			solutionBox.appendText(sol.get(i).toString() + "\n");
		}
	}
	
	//onclick for generate puzzles button
	public void genPuzzles(ActionEvent event) {
		grids = new Grid[50];
		AbstractHeuristic tempSolver = new AStar(Heuristic.MINDISTANCE);
		
		for (int i = 0; i < 5; i++) {
			Grid temp = new Grid();
			
			for (int j = 0; j < 10; j++) {
				Grid temp2 = new Grid(temp);
				tempSolver.grid = temp2.grid;
				
				while (tempSolver.solve(temp2.start, temp2.end) == null) {
					temp2 = new Grid(temp);
					tempSolver.grid = temp2.grid;
				}
				
				grids[i * 10 + j] = temp2;
			}
		}
		
		searchAlgo = tempSolver;
		searchAlgo.heuristic = Heuristic.MINDISTANCE;
		displayGrid(0);
	}
	
	//onclick for export puzzle
		//finds filename from associated textbox
		//calls translate to file method on current grid
	public void export(ActionEvent event) {
		String filename = exportName.getText();
		currGrid.writeToFile(filename);
	}
	
	//onclick for import puzzle
		//finds filename from associated textbox
		//calls translate from file method on current grid
	public void importFile(ActionEvent event) {
		String filename = importName.getText();
		currGrid.createFromFile(filename);
		displayGrid(Integer.parseInt(gridIndex.getText()));		
	}
	
	//onclick for get stats
		//use currently selected AbstractHeuristic with associated heuristic
		//create counters for runtime, resulting solution path length, number of nodes expanded, solved searches
		//for loop from 0 to 50
			//perform AbstractHeuristic on array[i]
			//add results to each counter
		//divide value in each counter by 50 (for average)
		//display search algorithm/heuristic and results in console
	public void getStats(ActionEvent event) {
		long runTime = 0;
		long pathLength = 0;
		long nodesExpanded = 0;
		long solved = 0;
		
		for (int i = 0; i < grids.length; i++) {
			long startTime = Calendar.getInstance().getTimeInMillis();
			searchAlgo.grid = grids[i].grid;
			List<Cell> res = searchAlgo.solve(grids[i].start, grids[i].end);
			long endTime = Calendar.getInstance().getTimeInMillis();

			if (res != null && res.size() != 1) {
				runTime += (endTime - startTime);
				nodesExpanded += searchAlgo.nodesExpanded();
				solved++;
				pathLength += res.size();
			}
		}
		
		System.out.println("Search algorithm used: " + searchAlgo.getClass().getName());
		System.out.println("Heuristic used: " + searchAlgo.heuristic);
		System.out.println("Weight1: " + searchAlgo.weight1 + ", Weight2: " + searchAlgo.weight2);
		System.out.println("Average runtime: " + runTime/solved);
		System.out.println("Average solution length: " + pathLength/solved);
		System.out.println("Average nodes expanded: " + nodesExpanded/solved);
		System.out.println("solved puzzles: " + solved);
		System.out.println("");
	}
}

package grids;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
	int x, y;
	CellType type;
	public Rectangle rect;			//create 1px black border on each rectangle
	float[] gvals;
	float[] fvals;
	Cell[] parents;
	
	//create enums to describe each square type
	public enum CellType {
		UNBLOCKED, BLOCKED, HARD, RIVER_UNBLOCKED, RIVER_HARD,
		START_UNBLOCKED, START_HARD, END_UNBLOCKED, END_HARD
	}
	
	//square constructors using either coords or enum type
	public Cell() {}
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		type = CellType.UNBLOCKED;
	}
	
	public Cell(int x, int y, CellType type) {
		this.x = x;
		this.y = y;
		this.convertTo(type);
	}
	
	public Cell(Cell other) {
		this.x = other.x;
		this.y = other.y;
		this.convertTo(other.type);
		this.rect = new Rectangle(10, 10);
		this.rect.setFill(other.rect.getFill());
		this.rect.setX(other.rect.getX());
		this.rect.setY(other.rect.getY());
	}
	
	public void convertTo(CellType other) {
		this.type = other;
	}
	
	//cell blocked or not
	public boolean isBlocked() {
		if (this.type.equals(CellType.BLOCKED)) {
			return true;
		}
		
		return false;
	}
	
	//hard traverse or not
	public boolean hardTraverse() {
		if (this.type.equals(CellType.HARD)) {
			return true;
		}
		else if (this.type.equals(CellType.RIVER_HARD)) {
			return true;
		}
		else if (this.type.equals(CellType.START_HARD)) {
			return true;
		}
		else if (this.type.equals(CellType.END_HARD)) {
			return true;
		}
		
		return false;
	}
	
	//river or not
	public boolean isRiver() {
		if (this.type.equals(CellType.RIVER_UNBLOCKED)) {
			return true;
		}
		else if (this.type.equals(CellType.RIVER_HARD)) {
			return true;
		}
		
		return false;
	}
	
	//boundary cell or not
	public boolean onBoundary() {
		if (x == 0 || y == 0 || x == 119 || y == 159) {
			return true;
		}
		
		return false;
	}
	
	//check if cell in bounds
	public boolean inBounds() {
		if (x >= 0 && x < 120 && y >= 0 && y < 160) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get the cost of traversing to another block, from this block
	 * 
	 * @param other		square to traverse to
	 * @return			cost required
	 */
	public float costTo(Cell other) {
		if (isBlocked() || other.isBlocked()) {
			return 0;
		}
		
		float res1 = 1;
		if (hardTraverse()) {
			res1 *= 2;
		}
		if (x != other.x && y != other.y) {
			res1 *= Math.sqrt(2.0);
		}
		
		float res2 = 1;
		if (other.hardTraverse()) {
			res2 *= 2;
		}
		if (x != other.x && y != other.y) {
			res2 *= Math.sqrt(2.0);
		}
		
		float res = (res1 + res2) / 2;
		if (isRiver() && other.isRiver()) {
			return res / 4;
		}
		
		return res;
	}
	
	/**
	 * Show this square onto the screen
	 */
	public void showCell() {
		switch (type) {
			case BLOCKED:
				this.rect.setFill(Color.BLACK);
				break;
			case END_HARD:
			case END_UNBLOCKED:
				this.rect.setFill(Color.ORANGERED);
				break;
			case HARD:
				this.rect.setFill(Color.GREY);
				break;
			case RIVER_HARD:
				this.rect.setFill(Color.DARKBLUE);
				break;
			case RIVER_UNBLOCKED:
				this.rect.setFill(Color.LIGHTBLUE);
				break;
			case START_HARD:
			case START_UNBLOCKED:
				this.rect.setFill(Color.LAWNGREEN);
				break;
			case UNBLOCKED:
				this.rect.setFill(Color.WHITE);
			default:
				break;
		}
		
		this.rect.setVisible(true);
	}
	
	//print this cell as a string
	@Override
	public String toString() {
		return ("x: " + this.x + ", y: " + this.y + ", type: " + this.type);
	}
	
	//add equals method to check if two cells equal
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		if (!(other instanceof Cell)) {
			return false;
		}
		
		Cell o = (Cell) other;
		
		return (o.x == this.x && o.y == this.y && o.type.equals(this.type));
	}
}

package solver;

import grids.Cell;

public class Fringe {

	//binary heap for A star and weighted A star
	//min heap using f-value as comparison
	
	//array of squares as a field
	Cell[] minHeap;
	int heapSize;
	private int num;
	
	//blank constructor
	public Fringe(int n) {
		minHeap = new Cell[120 * 160];
		heapSize = 0;
		num = n;
		
		for (int i = 0; i < minHeap.length; i++) {
			minHeap[i] = new Cell();
		}
	}
	
	public Fringe(int n, Cell start) {
		minHeap = new Cell[120 * 160];
		heapSize = 0;
		num = n;
		
		for (int i = 0; i < minHeap.length; i++) {
			minHeap[i] = new Cell(start);
		}
	}
	
	//get parent index of given index
	private int parent(int index) {
		return index / 2;
	}
	
	//get left child of given index
	private int leftChild(int index) {
		return 2 * index;
	}
	
	//get right child of given index
	private int rightChild(int index) {
		return leftChild(index) + 1;
	}
	
	//insert method
		//takes square and f-value as input
	public void insert(Cell cell) {
		minHeap[heapSize] = cell;
		heapUp(heapSize);
		heapSize++;
	}
	
	//contains method
	private int indexOf(Cell cell) {
		int max = Math.min((heapSize + 1) * 2, minHeap.length);
		for (int i = 0; i < max; i++) {
			if (minHeap[i] == cell) {
				return i;
			}
		}
		
		return -1;
	}
	
	//remove method
		//takes square and f-value as input
		//searches min heap for f-value
		//if f-value found and square at f-value == square
			//replace found square with last square in heap
			//move square up or down to fix heap as needed
	public void remove(Cell cell) {
		int index = indexOf(cell);
		
		if (index == -1) {
			return;
		}
		
		int parent = parent(index);
		minHeap[index] = minHeap[heapSize];
		heapSize--;
		
		if (index == 0 || minHeap[index].fvals[num] > minHeap[parent].fvals[num]) {
			heapDown(index);
		} 
		else if (minHeap[index].fvals[num] < minHeap[index].fvals[num]) {
			heapUp(index);
		}
	}
	
	//peek method
	public Cell peek() {
		return minHeap[0];
	}
	
	private void heapDown(int index) {
		int leftChild = leftChild(index);
		int rightChild = rightChild(index);
		int min;
		Cell temp;
		
		if (rightChild > heapSize) {
			if (leftChild > heapSize) {
				return;
			}
			else {
				min = leftChild;
			}
		}
		else {
			if (minHeap[leftChild].fvals[num] < minHeap[rightChild].fvals[num]) {
				min = leftChild;
			}
			else {
				min = rightChild;
			}
		}
		
		if (minHeap[index].fvals[num] > minHeap[min].fvals[num]) {
			temp = minHeap[min];
			minHeap[min] = minHeap[index];
			minHeap[index] = temp;
			heapDown(min);
		}
	}
	
	private void heapUp(int index) {
		int parent = parent(index);
		Cell temp = minHeap[parent];
		
		if (index == 0) {
			return;
		}
		
		if (minHeap[index].fvals[num] < minHeap[parent].fvals[num]) {
			minHeap[parent] = minHeap[index];
			minHeap[index] = temp;
			heapUp(parent);
		}
	}
}

package solver;

public class Fringe {

	//binary heap for A star and weighted A star
	//min heap using f-value as comparison
	
	//array of squares as a field
	
	//insert method
		//takes square and f-value as input
	
	//contains method
		//takes square and f-value as input
		//searches min heap for f-value
		//if f-value not found, return false
			//if f-value found and square at f-value == square
			//return true
	
	//remove method
		//takes square and f-value as input
		//searches min heap for f-value
		//if f-value found and square at f-value == square
			//replace found square with last square in heap
			//move square up or down to fix heap as needed
	
	//pop method
		//stores current min in heap
		//replaces min with max
		//fix heap by moving square down as needed
	
	
	//peek method
		//returns current minimum, without removing it
}

package mvc;
import java.util.ArrayList;

public class Node extends Observable {
	public static final int WHITE = -1, GREY = 0, BLACK = 1, WALL = 2, PATH = 3, START = 4, FINISH = 5, NOWALL = 10;
	public int row, col, color, distance, timeDiscover, timeFinish;
	public ArrayList<Node> neighbors;
	
	public Node(int row, int col) {
		this.row = row;
		this.col = col;
		
		this.color = Node.WHITE;
		this.distance = Integer.MAX_VALUE;
		
		this.neighbors = new ArrayList<Node>();
		
		this.timeDiscover = -1;
		this.timeFinish = -1;
	}
	
	@Override
	public String toString() {
		if(color == Node.PATH) {
			return "P";
			
		} else if(color == Node.WALL) {
			return "W";
			
		} else if(color == Node.START) {
			return "S";
			
		} else if(color == Node.FINISH) {
			return "F";
			
		} else { 
			return Integer.toString(this.color);
		}
	}
}

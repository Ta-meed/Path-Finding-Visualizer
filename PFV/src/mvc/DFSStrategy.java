package mvc;

import java.util.ArrayList;
import java.util.Collections;

public class DFSStrategy extends PathStrategy {
	
	public DFSStrategy(PathBoard board) {
		super(board);
	}
	
	public void calculate(Node start, Node end) {
		int[] time = new int[1];
		DFSVisit(time, start);
		findPath(start, end);
		
	}
	
	private void DFSVisit(int[] time, Node parent) {
		time[0]++;
		parent.color = Node.grey;
		parent.timeDiscover = time[0];
		this.notifyObservers();
		
		for(Node child: parent.neighbors) {
			if(child.color == Node.white && !child.isWall) {
				DFSVisit(time, child);
			}
		}
		
		time[0]++;
		parent.color = Node.black;
		parent.timeFinish = time[0];
	}
	
	private void findPath(Node start, Node end) {
		/*
		 * Starting from the end backtrack to the start by following the first discovered neighbors
		 * print the board following the path
		 */
		ArrayList<Node> path = new ArrayList<Node>();
		Node curr = end;
		path.add(end);
		
		// End point is unreachable
		if(end.timeDiscover == -1) {
			return;
		}
				
		while(curr != start) {
			Node least = curr;
			for(Node n: curr.neighbors) {
				if(n.timeDiscover < least.timeDiscover && n.timeDiscover > -1) {
					least = n;
				}
			}
			path.add(least);
			curr = least;
		}
		
		Collections.reverse(path);
		
		for(Node n: path) {
			n.isPath = true;
			this.notifyObservers();
			board.printBoard();
			System.out.println();
		}
	}
	
	@Override
	public String toString() {
		return "DFS";
	}

}

package mvc;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;


public class BFSStrategy extends PathStrategy {
	
	public BFSStrategy(PathBoard board, Queue<Step> steps) {
		super(board, steps);
	}
	
	public void calculate(Node start, Node finish) {
		Queue<Node> q = new LinkedList<Node>();
		
		start.distance = 0;
		q.add(start);
		
		// Calculate all paths from starting point
		while(q.size() > 0) {
			
			Node parent = q.poll();
			for(Node child: parent.neighbors) {
				
				if(child.color == Node.WHITE || child.color == Node.START || child.color == Node.FINISH) {
					
					child.color = Node.GREY;		
					steps.add(new Step(child.row, child.col, Node.GREY));
			
					child.distance = parent.distance + 1;
					q.add(child);
					
				}
			}
			
			if(parent == start) {
				steps.add(new Step(parent.row, parent.col, Node.START));
			} else if(parent == finish) {
				steps.add(new Step(parent.row, parent.col, Node.FINISH));
			} else {
				steps.add(new Step(parent.row, parent.col, Node.BLACK));
			}
			parent.color = Node.BLACK;
			
		}
		start.color = Node.START;
		finish.color = Node.FINISH;
		
	}
	
	
	public void findPath(Node start, Node finish) {
		/*
		 * Starting from the end backtrack to the start by following the least distance neighbors
		 * print the board following the path
		 */
		ArrayList<Node> path = new ArrayList<Node>();
		Node curr = finish;
		path.add(finish);
		
		// End is unreachable, no shortest path to find
		if(finish.distance == Integer.MAX_VALUE) {
			return;
		}
				
		while(curr.distance != 0) {
			Node least = curr;
			for(Node n: curr.neighbors) {
				if(n.distance < least.distance) {
					least = n;
				}
			}
			path.add(least);
			curr = least;

		}
		
		Collections.reverse(path);
		
		for(Node n: path) {
				if(n != start && n != finish) {
					n.color = Node.PATH;
					steps.add(new Step(n.row, n.col, Node.PATH));
					//board.printBoard();
					//System.out.println();	
				}
		}
	}
	
	@Override
	public String toString() {
		return "BFS";
	}
}

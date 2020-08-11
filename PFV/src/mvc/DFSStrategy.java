package mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class DFSStrategy extends PathStrategy {
	
	public DFSStrategy(PathBoard board, Queue<Step> steps) {
		super(board, steps);
	}
	
	public void calculate(Node start, Node finish) {
		int[] time = new int[1];
		DFSVisit(time, start, start, finish);
		start.color = Node.START;
		finish.color = Node.FINISH;
	}
	
	private void DFSVisit(int[] time, Node parent, Node start, Node finish) {
		time[0]++;
		parent.color = Node.GREY;
		parent.timeDiscover = time[0];
		
		steps.add(new Step(parent.row, parent.col, parent.color));
		
		// Avoid the start and end points being overwritten with grey
		if(parent == start) {
			steps.add(new Step(parent.row, parent.col, Node.START));
		} else if(parent == finish) {
			steps.add(new Step(parent.row, parent.col, Node.FINISH));
		} 
		
		
		for(Node child: parent.neighbors) {
			if(child.color == Node.WHITE || child.color == Node.FINISH) {
				DFSVisit(time, child, start, finish);
			}
		}
		
		steps.add(new Step(parent.row, parent.col, Node.BLACK));
		
		// Add the final black, avoid start and finish
		if(parent == start) {
			steps.add(new Step(parent.row, parent.col, Node.START));
		} else if(parent == finish) {
			steps.add(new Step(parent.row, parent.col, Node.FINISH));
		} else {
			steps.add(new Step(parent.row, parent.col, Node.BLACK));
		}
		parent.color = Node.BLACK;
		time[0]++;
		parent.timeFinish = time[0];
	}
	
	public void findPath(Node start, Node finish) {
		
		ArrayList<Node> path = new ArrayList<Node>();
		Node curr = finish;
		path.add(finish);
		
		// End point is unreachable
		if(finish.timeDiscover == -1) return;
		
		// Find a short path by going to the neighbor with the earliest discovery time
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
			if(n != start && n != finish) {
				n.color = Node.PATH;
				steps.add(new Step(n.row, n.col, n.color));
			}
		}
	}
	
	@Override
	public String toString() {
		return "DFS";
	}

}

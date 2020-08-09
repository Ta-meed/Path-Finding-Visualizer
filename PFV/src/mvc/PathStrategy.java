package mvc;
import java.util.LinkedList;
import java.util.Queue;

public abstract class PathStrategy {
	
	protected PathBoard board;
	public Queue<Step> steps;
	
	public PathStrategy(PathBoard board, Queue<Step> steps) {
		this.steps = steps;
		this.board = board;
	}
	
	public abstract void calculate(Node start, Node finish);
	
	public abstract void findPath(Node start, Node finish);
	
	@Override
	public abstract String toString();
}

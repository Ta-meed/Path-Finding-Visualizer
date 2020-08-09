package mvc;

import java.util.LinkedList;
import java.util.Queue;

public class PathMain extends Observable {
	
	// THIS CLASS HOLDS ALL THE CONTROL TO THE MODEL OF PATH FINDING
	PathBoard board;
	PathStrategy strat;
	Node start, finish;
	Queue<Step> steps;
	
	public PathMain() {
		this(10, 10);
	}
	
	public PathMain(int row, int col) {
		this.steps = new LinkedList<Step>();
		this.board = new PathBoard(row, col);
		this.strat = PathFactory.set("BFS", board, this.steps);
		this.start = this.board.get(0, 0);
		this.finish = this.board.get(4, 4);
		this.start.color = Node.START;
		this.finish.color = Node.FINISH;
	}
	

	
	//TODO: a bug where the wall disappear
	public int getRowDim() {
		return this.board.row;
	}
	
	public int getColDim() {
		return this.board.col;
	}
	
	public Node get(int row, int col) {
		return this.board.get(row, col);
	}
		
	public Node placeWall(int row, int col) {
		this.board.placeWall(row, col);
		
		// In the off chance that remove wall removes the start or finish re-add them
		this.setStart(this.start.row, this.start.col);
		this.setFinish(this.finish.row, this.finish.col);
		
		this.notifyObservers(this.get(row, col));
		return this.get(row, col);
	}
	
	public Node removeWall(int row, int col) {
		this.board.removeWall(row, col);
		
		// In the off chance that remove wall removes the start or finish re-add them
		this.setStart(this.start.row, this.start.col);
		this.setFinish(this.finish.row, this.finish.col);
		
		this.notifyObservers(this.get(row, col));
		return this.get(row, col);
	}
	
	public void clearWalls() {
		this.board.clearWalls();
		this.notifyObservers();
	}
	
	public void clearBoard() {
		this.board.clearBoard();
		this.notifyObservers();
	}
	
	public Node setStart(int row, int col) {
		if(this.get(row, col).color != Node.FINISH) {
			this.steps.clear();
			this.start.color = Node.WHITE;
			this.start = this.get(row, col);
			this.start.color = Node.START;
			this.notifyObservers();
		}
		
		return this.start;
	}
	
	public Node setFinish(int row, int col) {
		if(this.get(row, col).color != Node.START) {
			this.finish.color = Node.WHITE;
			this.finish = this.get(row, col);
			this.finish.color = Node.FINISH;
			this.notifyObservers();
		}
		return this.finish;
	}
	
	public void setStrategy(String strategyName) {
		this.board.clearBoard();
		this.steps.clear();
		this.strat = PathFactory.set(strategyName, this.board, this.steps);	
	}

	public void setDimensions(int row, int col) {
		this.steps.clear();
		this.board = new PathBoard(row, col);
		this.strat = PathFactory.set(strat.toString(), this.board, this.steps);
		this.notifyObservers();
	}
	
	public void findPath() {
		this.clearBoard();
		this.steps.clear();
		this.strat.calculate(this.start, this.finish);
		this.strat.findPath(this.start, this.finish);
		this.steps.add(new Step(this.start.row, this.start.col, Node.START));
		this.steps.add(new Step(this.finish.row, this.finish.col, Node.FINISH));
		this.notifyObservers();
	}
	
	public static void main(String[] args) {
		PathMain obj = new PathMain();
		obj.placeWall(0, 2);
		obj.placeWall(1, 0);
		obj.findPath();
		
		System.out.println("======================");
		
		obj.setStrategy("BFS");
		obj.findPath();
	}

}

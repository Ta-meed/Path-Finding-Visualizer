package mvc;
import java.util.Queue;

/**
 * Class returns a PathStrategy type given the strategy name
 * @author Tahmid Alam
 */
public class PathFactory {
	
	public static final String[] ALGORITHMLIST = {"BFS", "DFS", "Dijkstra", "A*"};
	
	/**
	 * @param strategyName: A path finding algorithm name such as BFS, DFS, ASTAR, etc
	 * @param board: the main PathBoard
	 * @param steps: The main steps queue
	 * @return the specified PathStrategy
	 */
	public static PathStrategy set(String strategyName, PathBoard board, Queue<Step> steps) {
		
		PathStrategy strategy = new BFSStrategy(board, steps);
		
		switch(strategyName) {
		case "BFS":
			strategy = new BFSStrategy(board, steps);
			break;
			
		case "DFS":
			strategy = new DFSStrategy(board, steps);
			break;
			
		case "Dijkstra":
			//strategy = new DIKStrategy(board, steps);
			break;
			
		case "A*":
			//strategy = new ASTARStrategy(board, steps);
			break;
			
		}

		return strategy;
	}
}

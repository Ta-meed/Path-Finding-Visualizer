/* Class returns a PathStrategy type given the strategy name
 * */

package mvc;

import java.util.Queue;

public class PathFactory {
	
	public static PathStrategy set(String strategyName, PathBoard board, Queue<Step> steps) { // TODO: potentially need to add main thing as parameter
		
		PathStrategy strategy = null;
		if(strategyName.contentEquals("BFS")) {
			strategy = new BFSStrategy(board, steps);
		} else if(strategyName.contentEquals("DFS")) {
			//strategy = new DFSStrategy(board, steps);
		} else if(strategyName.contentEquals("DIK")) {
			//TODO
		} else if(strategyName.contentEquals("ASTAR")) {
			//TODO
		}
		return strategy;
	}
}

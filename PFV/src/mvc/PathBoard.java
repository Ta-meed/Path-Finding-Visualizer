package mvc;

public class PathBoard {
	protected int row = 10, col = 10;
	public Node[][] board;
	
	public PathBoard(int row, int col) {
		this.row = row;
		this.col = col;
		this.board = new Node[this.row][this.col];
		
		// Setup nodes
		for(int y = 0; y < this.row; y++) {
			for(int x = 0; x < this.col; x++) {
				this.board[y][x] = new Node(y, x);
			}
		}
		
		// Setup node neighbors
		for(int y = 0; y < this.row; y++) {
			for(int x = 0; x < this.col; x++) {
				
				if(0 <= x - 1) { // left
					this.board[y][x].neighbors.add(board[y][x - 1]);
				
				} if(x + 1 < this.col) { // right
					this.board[y][x].neighbors.add(board[y][x + 1]);
				
				} if(0 <= y - 1) { // top
					this.board[y][x].neighbors.add(board[y - 1][x]);
				
				} if(y + 1 < this.row) { // bottom
					this.board[y][x].neighbors.add(board[y + 1][x]);
				}
				
			}
		}
	}
	
	public Node get(int row, int col) {
		return this.board[row][col];
	}
	
	public void placeWall(int row, int col) {
		if(this.board[row][col].color != Node.START && this.board[row][col].color != Node.FINISH) {
			this.board[row][col].color = Node.WALL;
		}
	}
	
	public void removeWall(int row, int col) {
		if(this.board[row][col].color == Node.WALL) {
			this.board[row][col].color = Node.WHITE;
		}
	}
	
	public void clearBoard() {
		for(int row = 0; row < this.row; row++) {
			for(int col = 0; col < this.col; col++) {
				
				if(this.board[row][col].color != Node.WALL &&
						this.board[row][col].color != Node.START && 
						this.board[row][col].color != Node.FINISH) {
					this.board[row][col].color = Node.WHITE;//TODO FIX THIS SO START, FINISH, AND WALL DONT DISSAPEAR
				}
				
				this.board[row][col].distance = Integer.MAX_VALUE;
				this.board[row][col].timeDiscover = -1;
				this.board[row][col].timeFinish = -1;
			}
		}
	}
	
	public void clearWalls() {
		for(int row = 0; row < this.row; row++) {
			for(int col = 0; col < this.col; col++) {
				this.removeWall(row, col);
			}
		}
	}
	
	public void printBoard() {
		for(int row = 0; row < this.row; row++) {
			for(int col = 0; col < this.col; col++) {
				System.out.print(this.board[row][col].toString() + "|");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		PathBoard board = new PathBoard(10, 10);
		board.printBoard();
	}
}

package mvc;

public class Position {
	int row;
	int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public void set(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Position)) {
			return false;
		}
		
		if(this.row == ((Position)other).getRow() && this.col == ((Position)other).getCol()) {
			return true;
		} else {
			return false;
		}
	}
	
}

package mvc;

public class Step {
	int row, col, color;
	
	public Step(int row, int col, int color) {
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "(" + row + ", " + col + ", " + color + ")";
	}
}

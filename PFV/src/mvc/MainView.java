package mvc;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.util.Duration;
/**
 * Class controls all GUI components
 * @author Tahmid Alam
 *
 */
public class MainView extends VBox implements Observer {
	
	//TODO: instead of dragging along the board and destroying it when placing start and finish we should
	// 		place on release
	
	
	private int windowSize = 700;
	private double tileSize = 0.02; // Size of tile from 0.02 to 0.99
	
	private Button btnPathFind;
	private Button btnClear;
	private Button btnClearWalls;
	
	private Slider slider;
	//TODO: drop down to select algorithm
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Affine transformer;
	private PathMain model;
	private int drawMode = Node.WALL; // Used to identify what tile to place on drag
	
	private HBox hbox;
	
	public MainView(PathMain model) {
		
		this.model = model;
		this.model.attach(this);
		
		this.transformer = new Affine();
		this.transformer.appendScale(
				this.windowSize/(float)(this.model.getRowDim()),
				this.windowSize/(float)(this.model.getColDim()));
		
		this.slider = new Slider(2, 100, 50);
		
		// Set the button handlers
		this.btnPathFind = new Button("Visualize");
		this.btnPathFind.setOnAction(this::btnHandlerVisualize);
		
		this.btnClear = new Button("Clear");
		this.btnClear.setOnAction(this::btnHandlerClear);
		
		this.btnClearWalls = new Button("Clear Walls");
		this.btnClearWalls.setOnAction(this::btnHandlerClearWalls);
		
		// Set the canvas handlers
		this.canvas = new Canvas(this.windowSize, this.windowSize);
		this.canvas.setOnMousePressed(this::canvasHandlerPressed);
		this.canvas.setOnMouseDragged(this::canvasHandlerDragged);
		
		// Set the transformation
		this.gc = this.canvas.getGraphicsContext2D();
		this.gc.setTransform(this.transformer);
		
		// Draw the background
		this.gc.setFill(Color.GHOSTWHITE);
		this.gc.fillRect(0, 0, this.model.getRowDim(), this.model.getRowDim());
		this.drawLines();
		
		// Set the button style
		btnStyle(btnPathFind);
		btnStyle(btnClear);
		btnStyle(btnClearWalls);
		
		this.hbox = new HBox();
		this.hbox.getChildren().addAll(this.btnPathFind, this.btnClear, this.btnClearWalls, this.slider);
		
		this.getChildren().addAll(hbox, canvas);
		

		
	}
	
	
	private void btnStyle(Button btn) {
		btn.setStyle("-fx-background-radius: 0;"// Remove the rounded borders
				+ " -fx-border-color: #000000;"// Black
				+ " -fx-border-width: 0.5px;"
				+ " -fx-background-color: #e0e0e0;");
	}
	
	private void btnHandlerClear(ActionEvent event) {
		model.clearBoard();
	}
	
	
	private void btnHandlerClearWalls(ActionEvent event) {
		model.clearWalls();
	}
	
	
	private void btnHandlerVisualize(ActionEvent event) {
		model.findPath();
	}
	
	/**
	 * Update the drawMode based on the tile pressed 
	 * update the tile on the tile that was pressed
	 * 
	 * @param event MouseEvent
	 */
	private void canvasHandlerPressed(MouseEvent event) {
		
		try {
			Point2D point = this.transformer.inverseTransform(event.getX(), event.getY());
			
			int row = (int)point.getY();
			int col = (int)point.getX();
			
			Node tile = this.model.get(row, col);
			
			if(tile.color != Node.WALL && tile.color != Node.START && tile.color != Node.FINISH) {
				drawMode = Node.WALL;
				this.model.placeWall(row, col);
				
			} else if(tile.color == Node.WALL) {
				drawMode = Node.WHITE;
				this.model.removeWall(row, col);
				
			} else if(tile.color == Node.START) {
				drawMode = Node.START;
				
			} else if(tile.color == Node.FINISH) {
				drawMode = Node.FINISH;
			}
			
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Move, add, or remove tiles when the mouse is being dragged to the mouse's current position
	 * based on the current drawMode variable
	 * 
	 * @param event mouseEvent
	 */
	private void canvasHandlerDragged(MouseEvent event) {
		
		try {
			Point2D point = this.transformer.inverseTransform(event.getX(), event.getY());
			
			int row = (int)point.getY();
			int col = (int)point.getX();
			
			// Round the coordinates to the nearest tile if the mouse is off the board
			if(row >= this.model.getRowDim()) {
				row = this.model.getRowDim() - 1;
				
			} else if(row < 0) {
				row = 0;
			}
			
			if(col >= this.model.getColDim()) {
				col = this.model.getColDim() - 1;
				
			} else if(col < 0) {
				col = 0;
			}
			
			// Draw the specified tile
			if(drawMode == Node.WALL && this.model.get(row, col).color != Node.WALL) {
				this.model.placeWall(row, col);
				
			} else if(drawMode == Node.WHITE && this.model.get(row, col).color != Node.WHITE) {
				this.model.removeWall(row, col);
				
			} else if(drawMode == Node.START && this.model.get(row, col).color != Node.START) {
				this.model.setStart(row, col);
				
			} else if(drawMode == Node.FINISH && this.model.get(row, col).color != Node.FINISH) {
				this.model.setFinish(row, col);
			}
			
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void update(Observable o) {
		
		if(model.steps.size() > 0) {
			this.stepByStep();
			
		} else {
			// Bypass updating the whole board by only updating a single cell
			if(o instanceof Node) {
				Node cell = (Node)o;
				this.tileColorSet(cell.row, cell.col, cell.color);
				
			// Update the whole board
			} else {
				for(int row = 0; row < this.model.getRowDim(); row++) {
					for(int col = 0; col < this.model.getColDim(); col++) {
						this.tileColorSet(row, col, this.model.get(row, col).color);
					}
				}
			}
			
		}
	}
	
	/**
	 * Draws the separating lines for the tiles on the board
	 */
	private void drawLines() {

		this.gc.closePath();
		this.gc.beginPath();
		this.gc.setStroke(Color.LIGHTBLUE);
		this.gc.setLineWidth(0.02f);
		
		for(int col = 0; col <= this.model.getColDim(); col++) {
			this.gc.strokeLine(col, 0, col, this.model.getRowDim());
			this.gc.strokeLine(0, col, this.model.getColDim(), col);
		}
	}
	
	/**
	 * Updates the board to show each step the algorithm took
	 */
	private void stepByStep() {
			
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis((int)this.slider.getValue()), e -> {
			
			Step curr = model.steps.poll();
			if(curr == null) return;
			this.tileColorSet(curr.row, curr.col, curr.color);
		}));
		
		timeline.setCycleCount(model.steps.size());
		timeline.play();
	}
	
	/**
	 * Changes the tile to the specified color at (row, col)
	 * 
	 * @param row: row in board from {0, model.col}
	 * @param col: column in board from {0, model.col}
	 * @param color: Integer representing the color in {-1, 0, 1, 2, 3, 4, 5, 10}
	 */
	private void tileColorSet(int row, int col, int color) {
		
		switch(color) {
		case Node.START:
			this.gc.setFill(Color.GREENYELLOW);
			break;
			
		case Node.FINISH:
			this.gc.setFill(Color.GOLD);
			break;
		
		case Node.WALL:
			this.gc.setFill(Color.DARKSLATEGREY);
			break;
			
		case Node.PATH:
			this.gc.setFill(Color.YELLOW);
			break;
			
		case Node.WHITE:
			this.gc.setFill(Color.GHOSTWHITE);
			break;
			
		case Node.GREY:
			this.gc.setFill(Color.DEEPPINK);
			break;
			
		case Node.BLACK:
			this.gc.setFill(Color.DARKTURQUOISE);
			break;
		}
		
		// Remove the previous tile, add the new tile
		this.gc.clearRect(col + this.tileSize, row + this.tileSize, 1 - this.tileSize*2, 1 - this.tileSize*2);
		this.gc.fillRect(col + this.tileSize, row + this.tileSize, 1 - this.tileSize*2, 1 - this.tileSize*2);
		
	}



}

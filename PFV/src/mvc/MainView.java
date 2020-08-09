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

public class MainView extends VBox implements Observer {
	
	//TODO: instead of dragging along the board and destroying it when placing start and finish we should
	// 		place on release
	
	
	private int size = 700;
	
	private Button btnPathFind;
	private Button btnClear;
	private Button btnClearWalls;
	
	private Canvas canvas;
	//TODO: drop down to select algorithm
	private Slider slider;
	
	private Affine transformer;
	private PathMain model;
	private int drawMode = Node.WALL;
	
	private HBox hbox;
	
	
	public MainView(PathMain model) {
		
		
		this.model = model;
		this.model.attach(this);
		
		this.transformer = new Affine();
		this.transformer.appendScale(this.size/(float)(this.model.getRowDim()) , this.size/(float)(this.model.getColDim()));
		
		this.btnPathFind = new Button("Visualize");
		this.btnPathFind.setOnAction(this::btnHandlerVisualize);
		
		this.btnClear = new Button("Clear");
		this.btnClear.setOnAction(this::btnHandlerClear);
		
		this.btnClearWalls = new Button("Clear Walls");
		this.btnClearWalls.setOnAction(this::btnHandlerClearWalls);
		
		this.slider = new Slider(2, 100, 50);
		
		this.canvas = new Canvas(size, size);
		this.canvas.setOnMousePressed(this::canvasHandlerPressed);
		this.canvas.setOnMouseDragged(this::canvasHandlerDragged);
		
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
	
	private void canvasHandlerPressed(MouseEvent event) {
		
		try {
			Point2D point = this.transformer.inverseTransform(event.getX(), event.getY());
			
			int row = (int)point.getY();
			int col = (int)point.getX();
			
			Node cell = this.model.get(row, col);
			
			if(cell.color != Node.WALL && cell.color != Node.START && cell.color != Node.FINISH) {
				drawMode = Node.WALL;
				this.model.placeWall(row, col);
				
			} else if(cell.color == Node.WALL) {
				drawMode = Node.WHITE;
				this.model.removeWall(row, col);
				
			} else if(cell.color == Node.START) {
				drawMode = Node.START;
				
			} else if(cell.color == Node.FINISH) {
				drawMode = Node.FINISH;
			}
			
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
	}
	
	
	private void canvasHandlerDragged(MouseEvent event) {
		
		try {
			Point2D point = this.transformer.inverseTransform(event.getX(), event.getY());
			
			int row = (int)point.getY();
			int col = (int)point.getX();
			
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
		GraphicsContext g = this.canvas.getGraphicsContext2D();
		g.setTransform(this.transformer);
		
		if(model.steps.size() > 0) {
			this.stepByStep(g);
			
		} else {
			
			// Bypass updating the whole board by only updating a single cell
			if(o instanceof Node) {
				Node cell = (Node)o;
				this.tileColorSet(g, cell.row, cell.col, cell.color);
				
				if(cell.color != Node.WALL) {
					this.drawLines(g);
				}
				
			// update whole board
			} else {
				for(int row = 0; row < this.model.getRowDim(); row++) {
					for(int col = 0; col < this.model.getColDim(); col++) {
						this.tileColorSet(g, row, col, this.model.get(row, col).color);
						this.drawLines(g);
					}
				}
			}
			
		}
	}
	
	
	private void drawLines(GraphicsContext g) {

		g.closePath();
		g.beginPath();
		g.setStroke(Color.LIGHTBLUE);
		g.setLineWidth(0.02f);
		
		for(int col = 0; col <= this.model.getColDim(); col++) {
			g.strokeLine(col, 0, col, this.model.getRowDim());
			g.strokeLine(0, col, this.model.getColDim(), col);
		}
		
	}
	

	private void stepByStep(GraphicsContext g) {
			
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis((int)slider.getValue()), e -> {
			Step curr = model.steps.poll();
			
			if(curr == null) return;
			
			this.tileColorSet(g, curr.row, curr.col, curr.color);
			this.drawLines(g);
			
		}));
		
		timeline.setCycleCount(model.steps.size());
		timeline.play();
		return;
	}
	
	/**
	 * Changes the tile to the specified color at (row, col)
	 * 
	 * @param g: GraphicsContext from Canvas
	 * @param row: row in board from {0, model.col}
	 * @param col: column in board from {0, model.col}
	 * @param color: Integer representing the color in {-1, 0, 1, 2, 3, 4, 5, 10}
	 */
	private void tileColorSet(GraphicsContext g, int row, int col, int color) {
		
		switch(color) {
		case Node.START:
			g.setFill(Color.GREENYELLOW);
			break;
			
		case Node.FINISH:
			g.setFill(Color.GOLD);
			break;
		
		case Node.WALL:
			g.setFill(Color.DARKSLATEGREY);
			break;
			
		case Node.PATH:
			g.setFill(Color.YELLOW);
			break;
			
		case Node.WHITE:
			g.setFill(Color.GHOSTWHITE);
			break;
			
		case Node.GREY:
			g.setFill(Color.DEEPPINK);
			break;
			
		case Node.BLACK:
			g.setFill(Color.DARKTURQUOISE);
			break;
		}
		
		g.clearRect(col, row, 1, 1);
		g.fillRect(col, row, 1, 1);
		
	}



}

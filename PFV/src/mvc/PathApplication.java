package mvc;

import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.util.Duration;

public class PathApplication extends Application { 

	@Override 
	public void start(Stage stage) throws Exception {
		
		PathMain model = new PathMain(15, 15); // Main model
		VBox view = new MainView(model); // Main View
		
		Scene scene = new Scene(view);
		stage.setTitle("Path Visualizer");
		stage.setScene(scene);

		model.notifyObservers(); // Update GUI on launch
		
		// LAUNCH GUI 
		stage.show();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		PathApplication view = new PathApplication();
		launch(args);   
	}
}

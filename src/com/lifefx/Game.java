package com.lifefx;

import java.util.Random;

import com.lifefx.utils.Cells;
import com.lifefx.utils.GameUI;
import com.lifefx.utils.Ruleset;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Game {
	private final int canvasWidth = 1024, canvasHeight = 576;
	private final double updateTime = 0.016667;
	
	public static boolean paused = true;
	
	public static Ruleset rules = new Ruleset("23/3");
	
	private Stage mainStage;
	private Scene mainScene;
	
	private Canvas canvas = new Canvas(canvasWidth, canvasHeight);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	public static int cellsWide = 40, cellsHigh = 40;
	
	public enum Schema {
		WIREWORLD, CYCLIC, RAINBOW, CUSTOM;
	}
	
	private GameUI ui;
	public static Schema schema;
	
	public static final Random rand = new Random();
	
	private Environment<Color> env = new Environment<Color>();
	
	private double getScaleFactor() {
		double wScale = (canvas.getWidth() / Game.cellsWide);
		double hScale = (canvas.getHeight() / Game.cellsHigh);
		return ((hScale < wScale) ? hScale : wScale);
	}
	
	public Game(Stage stage) {
		mainStage = stage;
		mainStage.setResizable(false);
		mainStage.centerOnScreen();
		
		BorderPane pane = new BorderPane();
		mainScene = new Scene(pane, mainStage.getWidth(), mainStage.getHeight());
		
		canvas.setOnMousePressed(mouseHandler);
		canvas.setOnMouseDragged(mouseHandler);
		
		mainStage.setScene(mainScene);
		
		Game.schema = Game.Schema.WIREWORLD;
		
		ui = new GameUI(env);
		
		pane.setTop(ui.getToolbar());
		pane.setLeft(ui.getPropbar());
		pane.setCenter(canvas);
		pane.setBottom(ui.getColorbar());
		
		Environment.forAllCells(0, Game.cellsWide, 0, Game.cellsHigh, (x, y) -> {
			env.setCell(x, y, Cells.getSchemaEmptyColor());
		});
		
		new AnimationTimer() {
			private long lastUpdate = 0;
			
			public void handle(long currentNanoTime) {
				if((((double)currentNanoTime - (double)lastUpdate) / 1000000000.0) >= (updateTime / ui.getSpeedSlider().getValue())) {
					gc.setFill(Color.WHITE);
					gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					
					double scale = getScaleFactor();
					
					Environment.forAllCells(0, Game.cellsWide, 0, Game.cellsHigh, (x, y) -> {
						Color c = env.getSpecies(x, y);
						if(c != null) {
							double sX = (scale * x), sY = (scale * y);
							
							gc.setFill(c);
							gc.fillRect(sX, sY, scale, scale);
						}
					});
					
					if(paused) {
						gc.setFill(Color.YELLOW);
						gc.setStroke(Color.BLACK);
						gc.setLineWidth(2);
						gc.setTextAlign(TextAlignment.RIGHT);
	                    gc.setFont(Font.font(48));
	                    gc.fillText("PAUSED", (canvas.getWidth() - 5), 48);
	                    gc.strokeText("PAUSED", (canvas.getWidth() - 5), 48);
					} else {
						env.simulate(0, Game.cellsWide, 0, Game.cellsHigh);
					}
					
					lastUpdate = currentNanoTime;
				}
			}
		}.start();
		
		mainStage.show();
	}
	
	EventHandler<MouseEvent> mouseHandler = event -> {
		double scale = getScaleFactor();
		int sX = (int)(event.getX() / scale),
	        sY = (int)(event.getY() / scale);
		
		Environment.forAllCells(sX, sX, sY, sY, (x, y) -> {
			env.setCell(x, y, GameUI.drawColor);
		});
	};
}

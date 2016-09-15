package com.lifefx.utils;

import java.util.Optional;

import com.lifefx.Environment;
import com.lifefx.Game;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameUI {
	private Environment<Color> env;
	public static Color drawColor = Cells.Wireworld.EMPTY.getColor();
	public static int nColors = 0;
	
	private HBox toolbar;
	public HBox getToolbar() { return toolbar; }
	private HBox colorbar;
	public HBox getColorbar() { return colorbar; }
	private VBox propbar;
	public VBox getPropbar() { return propbar; }
	
	private final Button pauseButton = new Button("Pause");
	private final Slider cellsWideSlider = new Slider(10, 500, Game.cellsWide);
	private final Slider cellsHighSlider = new Slider(10, 500, Game.cellsHigh);
	private final Slider speedSlider = new Slider(0.05, 2.0, 1.0);
	public Slider getSpeedSlider() { return speedSlider; }
	private final Slider nColorsSlider = new Slider(4, 16, 4);
	private final CheckBox lockToggle = new CheckBox("Lock Dimensions");
	
	public GameUI(Environment<Color> e) {
		env = e;
		
		toolbar = new HBox(5);
		colorbar = new HBox(5);
		propbar = new VBox(5);
		
		{ // Setup the main toolbar.
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ChoiceBox simChoice = new ChoiceBox(FXCollections.observableArrayList(
					"Wireworld", "Cyclic Automata", "Rainbow Game", "Custom")
			);
			
			HBox.setMargin(simChoice, new Insets(5, 0, 5, 0));
			simChoice.setTooltip(new Tooltip("Choose the simulation mode."));
			simChoice.getSelectionModel().selectFirst();
			simChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					if(old_val == new_val) return;
					else if(new_val.intValue() == 0) Game.schema = Game.Schema.WIREWORLD;
					else if(new_val.intValue() == 1) Game.schema = Game.Schema.CYCLIC;
					else if(new_val.intValue() == 2) Game.schema = Game.Schema.RAINBOW;
					else if(new_val.intValue() == 3) Game.schema = Game.Schema.CUSTOM;
					
					env.reset();
					resetPropbar();
					resetColorbar();
					clearCanvas();
				}
			});
			toolbar.getChildren().add(simChoice);
				
			HBox.setMargin(pauseButton, new Insets(5, 0, 5, 0));
			pauseButton.setOnMouseClicked(event -> {
				togglePause();
			});
			toolbar.getChildren().add(pauseButton);
			
			Button clearButton = new Button("Clear");
			HBox.setMargin(clearButton, new Insets(5, 5, 5, 0));
			clearButton.setOnMouseClicked(clearHandler);
			toolbar.getChildren().add(clearButton);
			
			toolbar.setAlignment(Pos.CENTER_RIGHT);
		} // End toolbar setup.
		
		resetPropbar();
		resetColorbar();
	}
	
	private void togglePause() {
		togglePause(!Game.paused);
	}
	
	private void togglePause(boolean state) {
		Game.paused = state;
		pauseButton.setText((!Game.paused)?"Pause":"Resume");
	}
	
	public void resetPropbar() {
		propbar.getChildren().clear();
		
		Label cwl = new Label("Cells Wide: " + Integer.toString(Game.cellsWide));
		Label chl = new Label("Cells High: " + Integer.toString(Game.cellsHigh));
		
		VBox.setMargin(cwl, new Insets(10, 10, 0, 15));
		propbar.getChildren().add(cwl);
		VBox.setMargin(cellsWideSlider, new Insets(0, 10, 0, 10));
		cellsWideSlider.setShowTickMarks(true);
		cellsWideSlider.setMajorTickUnit(50);
		cellsWideSlider.setMinorTickCount(4);
		cellsWideSlider.setSnapToTicks(true);
		cellsWideSlider.setBlockIncrement(10);
		cellsWideSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	cwl.setText("Cells Wide: " + (int)new_val.doubleValue());
            	if(lockToggle.isSelected()) {
            		cellsHighSlider.setValue(new_val.doubleValue());
            		chl.setText("Cells High: " + (int)new_val.doubleValue());
            	}
            	resizeGrid((int)cellsWideSlider.getValue(), (int)cellsHighSlider.getValue());
            }
        });
		propbar.getChildren().add(cellsWideSlider);
		
		VBox.setMargin(chl, new Insets(0, 10, 0, 15));
		propbar.getChildren().add(chl);
		VBox.setMargin(cellsHighSlider, new Insets(0, 10, 0, 10));
		cellsHighSlider.setShowTickMarks(true);
		cellsHighSlider.setMajorTickUnit(50);
		cellsHighSlider.setMinorTickCount(4);
		cellsHighSlider.setSnapToTicks(true);
		cellsHighSlider.setBlockIncrement(10);
		cellsHighSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	chl.setText("Cells High: " + (int)new_val.doubleValue());
            	if(lockToggle.isSelected()) {
            		cellsWideSlider.setValue(new_val.doubleValue());
            		cwl.setText("Cells Wide: " + (int)new_val.doubleValue());
            	}
            	resizeGrid((int)cellsWideSlider.getValue(), (int)cellsHighSlider.getValue());
            }
        });
		propbar.getChildren().add(cellsHighSlider);
		
		VBox.setMargin(lockToggle, new Insets(10, 10, 15, 15));
		propbar.getChildren().add(lockToggle);
		
		Label ssl = new Label("Speed: 1.0");
		VBox.setMargin(ssl, new Insets(0, 10, 0, 15));
		propbar.getChildren().add(ssl);
		VBox.setMargin(speedSlider, new Insets(0, 10, 0, 10));
		speedSlider.setShowTickMarks(true);
		speedSlider.setMajorTickUnit(0.1);
		speedSlider.setMinorTickCount(1);
		speedSlider.setSnapToTicks(true);
		speedSlider.setBlockIncrement(0.05);
		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				ssl.setText("Speed: " + String.format("%1$.2f", new_val.doubleValue()));
			}
		});
		propbar.getChildren().add(speedSlider);
		
		if(Game.schema == Game.Schema.CYCLIC) {
			Label ncl = new Label("Colors: 4");
			VBox.setMargin(ncl, new Insets(0, 10, 0, 10));
			propbar.getChildren().add(ncl);
			VBox.setMargin(nColorsSlider, new Insets(0, 10, 0, 10));
			nColorsSlider.setShowTickMarks(true);
			nColorsSlider.setMajorTickUnit(1);
			nColorsSlider.setSnapToTicks(true);
			nColorsSlider.setBlockIncrement(1);
			nColorsSlider.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					ncl.setText("Colors: " + new_val.intValue());
					clearCanvas();
				}
			});
			propbar.getChildren().add(nColorsSlider);
		} else if(Game.schema == Game.Schema.CUSTOM) {
			Button rb = new Button("Change Ruleset");
			VBox.setMargin(rb, new Insets(0, 10, 0, 10));
			rb.setOnMouseClicked(event -> {
				TextInputDialog dlg = new TextInputDialog();
				dlg.setTitle("Enter New Ruleset");
				dlg.setHeaderText("Enter the new ruleset string.");
				dlg.setContentText("Ex: 23/3 (default Conway's Life)");
				
				Optional<String> s = dlg.showAndWait();
				if(s.isPresent()) Game.rules.parseRules(s.get());
			});
			propbar.getChildren().add(rb);
		}
	}
	
	public void resetColorbar() {
		colorbar.getChildren().clear();
		
		if(Game.schema == Game.Schema.WIREWORLD) {
			Rectangle empty = createColorBox(Cells.getSchemaEmptyColor(), 30);
			HBox.setMargin(empty, new Insets(10, 0, 10, 10));
			colorbar.getChildren().add(empty);
			
			Rectangle head = createColorBox(Cells.Wireworld.HEAD.getColor(), 30);
			HBox.setMargin(head, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(head);
			
			Rectangle tail = createColorBox(Cells.Wireworld.TAIL.getColor(), 30);
			HBox.setMargin(tail, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(tail);
			
			Rectangle conductor = createColorBox(Cells.Wireworld.CONDUCTOR.getColor(), 30);
			HBox.setMargin(conductor, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(conductor);
		} else if(Game.schema == Game.Schema.RAINBOW) {
			Rectangle empty = createColorBox(Cells.getSchemaEmptyColor(), 30);
			HBox.setMargin(empty, new Insets(10, 0, 10, 10));
			colorbar.getChildren().add(empty);
			
			Rectangle light = createColorBox(Cells.Rainbow.LIGHT.getColor(), 30);
			HBox.setMargin(light, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(light);
			
			Rectangle medium = createColorBox(Cells.Rainbow.MEDIUM.getColor(), 30);
			HBox.setMargin(medium, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(medium);
			
			Rectangle dark = createColorBox(Cells.Rainbow.DARK.getColor(), 30);
			HBox.setMargin(dark, new Insets(10, 0, 10, 0));
			colorbar.getChildren().add(dark);
		} else if(Game.schema == Game.Schema.CUSTOM) {
			Rectangle empty = createColorBox(Cells.getSchemaEmptyColor(), 30);
			HBox.setMargin(empty, new Insets(10, 0, 10, 10));
			colorbar.getChildren().add(empty);
			
			Rectangle cells = createColorBox(Cells.Custom.CELLS.getColor(), 30);
			HBox.setMargin(cells, new Insets(10, 0, 10, 10));
			colorbar.getChildren().add(cells);
		}
	}
	
	private Rectangle createColorBox(Color c, double size) {
		Rectangle box = new Rectangle(size, size);
		
		box.setFill(c);
		box.setStroke(Color.BLACK);
		box.setOnMouseClicked(event -> {
			GameUI.drawColor = c;
		});
		
		return box;
	}
	
	private void resizeGrid(int cw, int ch) {
		Game.cellsWide = cw;
		Game.cellsHigh = ch;
	}
	
	EventHandler<Event> clearHandler = event -> {
		clearCanvas();
	};
	
	private void clearCanvas() {
		env.reset();
		
		if(Game.schema != Game.Schema.CYCLIC) {
			Color emptyColor = Cells.getSchemaEmptyColor();
			
			Environment.forAllCells(0, Game.cellsWide, 0, Game.cellsHigh, (x, y) -> {
				env.setCell(x, y, emptyColor);
			});
		} else {
			int n = (int)nColorsSlider.getValue();
			Environment.forAllCells(0, Game.cellsWide, 0, Game.cellsHigh, (x, y) -> {
				int state = Game.rand.nextInt(n);
				env.setCell(x, y, Cells.getCyclicColor(state));
			});
		}
		
		togglePause(true);
	}
}

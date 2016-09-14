package com.lifefx;

import javafx.application.Application;
import javafx.stage.Stage;

public class LifeFX extends Application{
	public static void main(String[] args) {
		try {
			launch(args);
		} catch(Exception e) {
			System.err.println("ERROR: Problem launching application.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("LifeFX");
		new Game(stage);
	}
}

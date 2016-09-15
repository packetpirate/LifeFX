package com.lifefx.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lifefx.Game;

import javafx.scene.paint.Color;

public class Cells {
	public static enum Wireworld {
		EMPTY(Color.BLACK),
		HEAD(Color.BLUE),
		TAIL(Color.RED),
		CONDUCTOR(Color.ORANGE);
		
		private Color cellColor;
		public Color getColor() { return cellColor; }
		
		Wireworld(Color c) {
			cellColor = c;
		}
	}
	
	public static enum Rainbow {
		EMPTY(Color.RED),
		LIGHT(Color.WHITE),
		MEDIUM(Color.GRAY),
		DARK(Color.BLACK);
		
		private Color cellColor;
		public Color getColor() { return cellColor; }
		
		Rainbow(Color c) {
			cellColor = c;
		}
	}
	
	public static enum Custom {
		EMPTY(Color.GRAY),
		CELLS(Color.BLACK);
		
		private Color cellColor;
		public Color getColor() { return cellColor; }
		
		Custom(Color c) {
			cellColor = c;
		}
	}
	
	@SuppressWarnings("serial")
	public static Map<Integer, Color> Cyclic = new HashMap<Integer, Color>() {{
		put(0, Color.rgb(0x00, 0x55, 0x00));
		put(1, Color.rgb(0x00, 0x55, 0xAA));
		put(2, Color.rgb(0x00, 0xFF, 0x00));
		put(3, Color.rgb(0x00, 0xFF, 0xAA));
		
		put(4, Color.rgb(0xAA, 0x55, 0x00));
		put(5, Color.rgb(0xAA, 0x55, 0xAA));
		put(6, Color.rgb(0xAA, 0xFF, 0x00));
		put(7, Color.rgb(0xAA, 0xFF, 0xAA));
		
		put(8, Color.rgb(0x00, 0x55, 0x55));
		put(9, Color.rgb(0x00, 0x55, 0xFF));
		put(10, Color.rgb(0x00, 0xFF, 0x55));
		put(11, Color.rgb(0x00, 0xFF, 0xFF));
		
		put(12, Color.rgb(0xAA, 0x55, 0x55));
		put(13, Color.rgb(0xAA, 0x55, 0xFF));
		put(14, Color.rgb(0xAA, 0xFF, 0x55));
		put(15, Color.rgb(0xAA, 0xFF, 0xFF));
	}};
	
	public static Color getCyclicColor(int state) {
		Color c = Cells.Cyclic.get(state);
		if(c != null) return c;
		else return Color.BLACK;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getCyclicState(Color c) {
		int state = 0;
		Iterator it = Cells.Cyclic.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Integer, Color> pair = (Map.Entry<Integer, Color>)it.next();
			if(pair.getValue().equals(c)) state = pair.getKey();
		}
		
		return state;
	}
	
	public static Color getSchemaEmptyColor() {
		Color c = Color.WHITE;
		
		if(Game.schema == Game.Schema.WIREWORLD) c = Cells.Wireworld.EMPTY.getColor();
		else if(Game.schema == Game.Schema.CYCLIC) c = Color.WHITE;
		else if(Game.schema == Game.Schema.RAINBOW) c = Cells.Rainbow.EMPTY.getColor();
		else if(Game.schema == Game.Schema.CUSTOM) c = Cells.Custom.EMPTY.getColor();
		
		return c;
	}
}

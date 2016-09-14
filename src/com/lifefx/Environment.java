package com.lifefx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

import com.lifefx.utils.Cells;
import com.lifefx.utils.Pair;

import javafx.scene.paint.Color;

public class Environment<T> {
	private Map<Pair<Integer>, T> cells = new HashMap<>();
	private final Neighbors neighbors = new Neighbors();
	
	public void reset() {
		cells.clear();
	}
	
	public T getSpecies(int x, int y) {
		return cells.get(new Pair<Integer>(x, y));
	}
	
	public Map<Pair<Integer>, T> getCells() {
		return cells;
	}
	
	public void setCell(int x, int y, T s) {
		cells.put(new Pair<Integer>(x, y), s);
	}
	
	public Neighbors getNeighbors(int x, int y) {
		neighbors.reset();
		
		for(int cy = (y - 1); cy <= (y + 1); cy++) {
			for(int cx = (x - 1); cx <= (x + 1); cx++) {
				Pair<Integer> pos = new Pair<>(cx, cy);
				T species = cells.get(pos);
				
				if((species != null) && (species != Cells.getSchemaEmptyColor()) && !((cx == x) && (cy == y))) {
					neighbors.add(species);
				}
			}
		}
		
		return neighbors;
	}
	
	@SuppressWarnings("unchecked")
	private T getCellState(int x, int y) {
		Neighbors neighborSpecies = getNeighbors(x, y);
		T species = getSpecies(x, y);
		
		if(Game.schema == Game.Schema.WIREWORLD) {
			boolean alive = (species != null) && (species != Cells.getSchemaEmptyColor());
			
			if(alive) {
				if(species == Cells.Wireworld.HEAD.getColor()) {
					return (T)Cells.Wireworld.TAIL.getColor();
				} else if(species == Cells.Wireworld.TAIL.getColor()) {
					return (T)Cells.Wireworld.CONDUCTOR.getColor();
				} else if(species == Cells.Wireworld.CONDUCTOR.getColor()) {
					int headCount = neighborSpecies.speciesCount((T)Cells.Wireworld.HEAD.getColor());
					
					if((headCount == 1) || (headCount == 2)) {
						return (T)Cells.Wireworld.HEAD.getColor();
					} else return (T)Cells.Wireworld.CONDUCTOR.getColor();
				} else {
					return (T)Cells.getSchemaEmptyColor();
				}
			}
		} else if(Game.schema == Game.Schema.CYCLIC) {
			int state = Cells.getCyclicState((Color)species);
			int nextState = (state + 1) % Cells.Cyclic.size();
			if(neighborSpecies.speciesCount((T)Cells.Cyclic.get(nextState)) > 0) {
				return (T)Cells.Cyclic.get(nextState);
			}
			
			return species;
		} else if(Game.schema == Game.Schema.RAINBOW) {
			boolean alive = (species != null) && (species != Cells.getSchemaEmptyColor());
			
			if(alive) {
				if((neighbors.getNeighbors() == 2) || (neighbors.getNeighbors() == 3)) {
					return species;
				} else if((neighbors.getNeighbors() < 2) || (neighbors.getNeighbors() > 3)) {
					return (T)Cells.getSchemaEmptyColor();
				}
			} else {
				if(neighbors.getNeighbors() == 3) {
					return neighbors.averageSpecies();
				}
			}
		}
		
		return (T)Cells.getSchemaEmptyColor();
	}
	
	public static void forAllCells(int xMin, int xMax, int yMin, int yMax, BiConsumer<Integer, Integer> f) {
		for(int y = yMin; y <= yMax; y++) {
			for(int x = xMin; x <= xMax; x++) {
				f.accept(x, y);
			}
		}
	}
	
	public void simulate(int xMin, int xMax, int yMin, int yMax) {
		Map<Pair<Integer>, T> newCells = new HashMap<>();
		
		forAllCells(xMin, xMax, yMin, yMax, (x, y) -> {
			T cellState = getCellState(x, y);
			
			if(cellState != null) {
				newCells.put(new Pair<Integer>(x, y), cellState);
			}
		});
		
		cells = newCells;
	}
	
	class MutableInt {
		private int val;
		
		public MutableInt() {
			this(0);
		}
		
		public MutableInt(int v) {
			this.val = v;
		}
		
		public void inc() {
			val++;
		}
		
		public int getVal() {
			return val;
		}
	}
	
	class Neighbors {
		private final Map<T, MutableInt> neighbors = new HashMap<>();
		private int totalNeighbors = 0;
		
		public void add(T species) {
			MutableInt count = neighbors.get(species);
			
			if(count == null) {
				neighbors.put(species, new MutableInt(1));
			} else count.inc();
			
			totalNeighbors++;
		}
		
		public void reset() {
			neighbors.clear();
			totalNeighbors = 0;
		}
		
		public int getNeighbors() {
			return totalNeighbors;
		}
		
		public int speciesCount(T species) {
			MutableInt count = neighbors.get(species);
			
			if(count == null) return 0;
			else return count.getVal();
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public T averageSpecies() {
			int avgR = 0;
			int avgG = 0;
			int avgB = 0;
			int n = 0;
			
			Iterator it = neighbors.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<T, MutableInt> pair = (Map.Entry<T, MutableInt>)it.next();
				Color c = (Color)pair.getKey();
				if(c != Cells.getSchemaEmptyColor()) {
					avgR += (int)(c.getRed() * 255);
					avgG += (int)(c.getGreen() * 255);
					avgB += (int)(c.getBlue() * 255);
					n++;
				}
			}
			
			return (T)Color.rgb((avgR / n), (avgG / n), (avgB / n));
		}
	}
}

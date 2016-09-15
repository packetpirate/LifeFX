package com.lifefx.utils;

import java.util.ArrayList;
import java.util.List;

public class Ruleset {
	private List<Integer> survive;
	private List<Integer> born;
	
	public Ruleset(String rules) {
		survive = new ArrayList<Integer>();
		born = new ArrayList<Integer>();
		parseRules(rules);
	}
	
	public boolean isSurviveCondition(int n) {
		return survive.contains(n);
	}
	
	public boolean isBornCondition(int n) {
		return born.contains(n);
	}
	
	public void parseRules(String rules) {
		survive.clear();
		born.clear();
		
		String [] sp = rules.split("/");
		
		try {
			for(int i = 0; i < sp[0].length(); i++) {
				char c = sp[0].charAt(i);
				survive.add(Character.digit(c, 10));
			}
			
			for(int i = 0; i < sp[1].length(); i++) {
				char c = sp[1].charAt(i);
				born.add(Character.digit(c, 10));
			}
		} catch(IndexOutOfBoundsException ibe) {
			System.err.println("ERROR: Problem parsing ruleset format string!");
			System.exit(0);
		}
	}
}

package com.ais.equinox.tuner.app.bean;

import java.util.ArrayList;

public class BeanStatEquinox {
	
	private String time;
	private ArrayList<String[]> statEquinox = new ArrayList<String[]>();
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public ArrayList<String[]> getStatEquinox() {
		return statEquinox;
	}
	public void setStatEquinox(ArrayList<String[]> statEquinox) {
		this.statEquinox = statEquinox;
	}
	
	

}

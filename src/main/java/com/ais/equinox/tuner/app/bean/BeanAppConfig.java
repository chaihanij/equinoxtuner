package com.ais.equinox.tuner.app.bean;

import java.util.ArrayList;

public class BeanAppConfig {
	private String pathConfig;
	private String pathStatEquinox;
	private String statInterval;
	private ArrayList<String[]> nodeConnection = new ArrayList<String[]>();
	private ArrayList<String[]> activeElement = new ArrayList<String[]>();
	
	private ArrayList<String> InternalStatus = new ArrayList<String>();
	private ArrayList<String> measurement = new ArrayList<String>();
	private ArrayList<String> accumulatingCounters = new ArrayList<String>();
	
	
	public ArrayList<String> getInternalStatus() {
		return InternalStatus;
	}

	public void setInternalStatus(ArrayList<String> internalStatus) {
		InternalStatus = internalStatus;
	}

	public ArrayList<String> getMeasurement() {
		return measurement;
	}

	public void setMeasurement(ArrayList<String> measurement) {
		this.measurement = measurement;
	}

	public ArrayList<String> getAccumulatingCounters() {
		return accumulatingCounters;
	}

	public void setAccumulatingCounters(ArrayList<String> accumulatingCounters) {
		this.accumulatingCounters = accumulatingCounters;
	}


	

	public ArrayList<String[]> getNodeConnection() {
		return nodeConnection;
	}

	public void setNodeConnection(ArrayList<String[]> nodeConnection) {
		this.nodeConnection = nodeConnection;
	}

	

	public String getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}

	public String getPathStatEquinox() {
		return pathStatEquinox;
	}

	public void setPathStatEquinox(String pathStatEquinox) {
		this.pathStatEquinox = pathStatEquinox;
	}

	public String getStatInterval() {
		return statInterval;
	}

	public void setStatInterval(String statInterval) {
		this.statInterval = statInterval;
	}

	

	public ArrayList<String[]> getActiveElement() {
		return activeElement;
	}

	public void setActiveElement(ArrayList<String[]> activeElement) {
		this.activeElement = activeElement;
	}


}

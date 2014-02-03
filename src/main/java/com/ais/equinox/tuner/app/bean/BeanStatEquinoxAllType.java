package com.ais.equinox.tuner.app.bean;

import java.util.ArrayList;

public class BeanStatEquinoxAllType {

private String time ;
private String[] timecal;
private ArrayList<String[]> systemInternalStat = new ArrayList<String[]>();
private ArrayList<String[]> systemMeasurement = new ArrayList<String[]>();
private ArrayList<String[]> systemMeasurementvalue = new ArrayList<String[]>();
private ArrayList<String[]> accumulatingCounters = new ArrayList<String[]>();
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public ArrayList<String[]> getSystemInternalStat() {
	return systemInternalStat;
}
public void setSystemInternalStat(ArrayList<String[]> systemInternalStat) {
	this.systemInternalStat = systemInternalStat;
}
public ArrayList<String[]> getSystemMeasurement() {
	return systemMeasurement;
}
public void setSystemMeasurement(ArrayList<String[]> systemMeasurement) {
	this.systemMeasurement = systemMeasurement;
}
public ArrayList<String[]> getAccumulatingCounters() {
	return accumulatingCounters;
}
public void setAccumulatingCounters(ArrayList<String[]> accumulatingCounters) {
	this.accumulatingCounters = accumulatingCounters;
}
public String[] getTimecal() {
	return timecal;
}
public void setTimecal(String[] timecal) {
	this.timecal = timecal;
}
public ArrayList<String[]> getSystemMeasurementvalue() {
	return systemMeasurementvalue;
}
public void setSystemMeasurementvalue(ArrayList<String[]> systemMeasurementvalue) {
	this.systemMeasurementvalue = systemMeasurementvalue;
}

}

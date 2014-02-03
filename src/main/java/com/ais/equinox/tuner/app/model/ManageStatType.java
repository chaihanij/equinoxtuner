package com.ais.equinox.tuner.app.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

import com.ais.equinox.tuner.app.bean.BeanConfig;
import com.ais.equinox.tuner.app.bean.BeanStatEquinoxAllType;

public class ManageStatType {
	public ManageStatType(String sessionId) {
		this.sessionId = sessionId;
		typeInternal = BeanConfig.beanConfigApp.getInternalStatus();
		typeMeasurement = BeanConfig.beanConfigApp.getMeasurement();
		typeAccumulation = BeanConfig.beanConfigApp.getAccumulatingCounters();

	}

	private String sessionId;
	private ArrayList<String> typeInternal = new ArrayList<String>();
	private ArrayList<String> typeMeasurement = new ArrayList<String>();
	private ArrayList<String> typeAccumulation = new ArrayList<String>();

	private BeanStatEquinoxAllType beanStatAll = new BeanStatEquinoxAllType();

	public BeanStatEquinoxAllType  PorcessStat( Stack<String> stackStr) {
		ArrayList<String[]> internalStat = new ArrayList<String[]>();		
		ArrayList<String[]> accumulationStat = new ArrayList<String[]>();
		HashMap<String, String[]> calStat = new HashMap<String, String[]>();
		String[] time = new String[2];

		int countStatBlock = 1;
		// Loop Keep All Stat to {beanStatAll}
		while (!stackStr.empty()) {
			String str = stackStr.pop();
			
			if (str.matches("^[a-z|A-Z].*")) {
				String[] strArray = str.split("=");
				// ProcessStatType
				// Check Match InternalStat
				if (countStatBlock == 1) {
					for (String nameStat : typeInternal) {
						if (nameStat.equals(strArray[0])) {
							System.out.println("Match Internal : "+ strArray[0]);
							String[] arrrayStr = str.split("=");
							internalStat.add(arrrayStr);
						}
					}
					//Set Internal
					beanStatAll.setSystemInternalStat(internalStat);
				}
				// Check Match AccumulatingCounters
				if (countStatBlock == 1) {
					for (String nameStat : typeAccumulation) {
						if (nameStat.equals(strArray[0])) {
							System.out.println("Match Accumulation : "+ strArray[0]);
							String[] arrrayStr = str.split("=");
							accumulationStat.add(arrrayStr);
						}
					}
					//Set Accumulating
					beanStatAll.setAccumulatingCounters(accumulationStat);
				}
				// Check Match MeansurementStat
				for (String nameStat : typeMeasurement) {
					if (nameStat.equals(strArray[0])) {
						System.out.println("Match Measurement : "+ strArray[0]);
						if (countStatBlock == 1) {
							String[] arrrayStr = str.split("=");
							String[] value = new String[2];
							value[0] = arrrayStr[1];
							calStat.put(arrrayStr[0], value);

						} else {
							String[] arrrayStr = str.split("=");
							String[] value = new String[2];
							value = calStat.get(arrrayStr[0]);
							value[1] = arrrayStr[1];
							calStat.put(arrrayStr[0], value);
						}

					}
				}

			} else if (str.matches("^[0-9].*") && countStatBlock <= 2) {
				// Set Time
				if (countStatBlock == 1) {
					beanStatAll.setTime(str);
					time[0] = str;
				}
				if (countStatBlock == 2) {
					time[1] = str;
					break;
				}
				// Set Time Array
				beanStatAll.setTimecal(time);
				countStatBlock++;

			}
		}
		System.out.println("==================== End Keep ============================");
		Calculating(time,calStat);
		return beanStatAll;
	}

	private void Calculating(String[] time,HashMap<String, String[]> calStat) {
		ArrayList<String[]> measuremnetStat = new ArrayList<String[]>();
		ArrayList<String[]> measuremnetValue = new ArrayList<String[]>();
		 //Parser Time 
	     long [] longTime = new long[2];
	     SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
	     int i=0;
	     for (String string : time) {
	    	 try {
				Date startDate = df.parse(string);
				longTime[i]=startDate.getTime();
				System.out.println(startDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			System.out.println("Parser Time ERROR");
			}
	    	 i++;
		}
	     System.out.println("+++++++++++++++++++++++ Print Result +++++++++++++++++++++++++++");
	
	     long result =  longTime[0]-longTime[1];
	   System.out.println(result);
	  
	
	 
	   //// Get msec from each, and subtract.
	   long sec = result / 1000 ; 
	   long diffSeconds = result / 1000 % 60;  
	   long diffMinutes = result / (60 * 1000) % 60;         
	   long diffHours = result / (60 * 60 * 1000);      
	   //Cal 
	  for (Object key : calStat.keySet()) {
		
		 String[] str = calStat.get(key);
		 int stat1= Integer.parseInt(str[0]);
		 int stat2= Integer.parseInt(str[1]);
		 int stat =stat1-stat2;
		 long results = stat/ sec;
		 String[] value = new String[2];
		value[0]=key.toString();
		value[1]=""+results;
		 String[] valueMeasurement = new String[2];
		 valueMeasurement[0]=key.toString();
		 valueMeasurement[1]=""+stat1;
		measuremnetValue.add(valueMeasurement);
		measuremnetStat.add(value);
		
	}
	  beanStatAll.setSystemMeasurement(measuremnetStat);
	  beanStatAll.setSystemMeasurementvalue(measuremnetValue);
	   
	}

	

}

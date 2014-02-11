package com.ais.equinox.tuner.app.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ais.equinox.tuner.Main;
import com.ais.equinox.tuner.SystemLogger;
import com.ais.equinox.tuner.app.bean.BeanAppConfig;

public class SaveConfigApp {

	public void createDocument(BeanAppConfig configuration,String pathFile) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("configuration");
		// Set System path
		Element systemPath = root.addElement("SystemPath");
		Element configPath = systemPath.addElement("ConfigPath").addAttribute(
				"value", configuration.getPathConfig());
		
		
		//Set Ative Element 
		Element activeElement = root.addElement("ActiveElement");
		Element type = activeElement.addElement("Type").addAttribute("XPath", "//configulation/warm");		 
		for (String[] item : configuration.getActiveElement()) {
			 type.addElement(item[0])
			 .addAttribute("attributeName", "value")
			 .addAttribute("max", item[1])
			 .addAttribute("min", item[2])
			 .addAttribute("unit", item[3])
			 .addAttribute("description", item[4]);		 
		}
		
		//Set Node Connection 
		Element nodeConnection = root.addElement("NodeConnection");
		for (String[] item : configuration.getNodeConnection()) {
			nodeConnection.addElement("Node")
			 .addAttribute("nodeName", item[0])
			 .addAttribute("ip", item[1])
			 .addAttribute("user", item[2])
			 .addAttribute("pass", item[3]);	
		}
		//Set StatEquinox
		//Set Path 
		Element statEquinox = root.addElement("StateEquinox");	
		statEquinox.addElement("ConfigPath").addAttribute("value", configuration.getPathStatEquinox());
		statEquinox.addElement("Interval").addAttribute("value", configuration.getStatInterval());
		Element statType = statEquinox.addElement("StateType");
		Element internalStatus = statType.addElement("SystemInternalStatus");
		//Set Type Internal Status
		for (String string : configuration.getInternalStatus()) {
			internalStatus.addElement(string);
		}
		//Set Type System Measurement 
		Element systemMeasurement = statType.addElement("SystemMeasurement");
		for (String string : configuration.getMeasurement()) {
			systemMeasurement.addElement(string);
		}
		//Set Type System Measurement 
		Element accumulatingCounters = statType.addElement("AccumulatingCounters");
		for (String string : configuration.getAccumulatingCounters()) {
			accumulatingCounters.addElement(string);
			}
		
		write(document,pathFile);
	}

	private void write(Document document,String pathFile) {

		try {
			OutputFormat outFormat = new OutputFormat();
			outFormat = OutputFormat.createPrettyPrint();
			outFormat.setEncoding("tis-620");
			XMLWriter output = new XMLWriter(
					new FileWriter(
							new File(pathFile)),
					outFormat);
			output.write(document);
			output.close();
			System.out.println("+++++ WriteXML Completed +++++");
			SystemLogger.logger.info("[SaveConfigApp]:+++++ WriteXML Completed +++++");
		} catch (IOException e) {
			System.out.println("Write File ERROR");
			System.out.println(e.getMessage());
			SystemLogger.logger.error("[SaveConfigApp]:Write File ERROR"+e);
		}

	}

}
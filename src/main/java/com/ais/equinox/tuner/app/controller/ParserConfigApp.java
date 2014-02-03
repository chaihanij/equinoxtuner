package com.ais.equinox.tuner.app.controller;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ais.equinox.tuner.app.bean.BeanAppConfig;

public class ParserConfigApp {

	public BeanAppConfig parser(String pathFile) {
		BeanAppConfig configApp = new BeanAppConfig();

		/* ++++ Read Config App ++++ */
		SAXReader reader = new SAXReader();
		Document xmlConfig = null;
	
		
		
		try {
			
			xmlConfig = reader.read(pathFile);
		} catch (DocumentException e) {
			System.out.println("Read Config ERROR");
			e.printStackTrace();
		}
		// Show Config App
		System.out.println("======= Config Application ========");
		System.out.println(xmlConfig.asXML());
		System.out.println("======= Read file suscess ========");
		// Get value in xml
		Element root = xmlConfig.getRootElement();
		// Keep ConfigPath to Bean
		Element systemPath = root.element("SystemPath");
		configApp.setPathConfig(systemPath.element("ConfigPath")
				.attributeValue("value"));	

		// Keep NodeConnection to Bean
		Element nodeConnection = root.element("NodeConnection");
		ArrayList<String[]> listNode = new ArrayList<String[]>();

		for (Iterator i = nodeConnection.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			int j = 0;
			String[] node = new String[4];
			for (Iterator k = element.attributeIterator(); k.hasNext();) {
				Attribute attribute = (Attribute) k.next();

				node[j] = attribute.getValue();

				j++;
			}
			listNode.add(node);
		}
		configApp.setNodeConnection(listNode);

		// Keep activeElement to Bean
		Element activeElement = root.element("ActiveElement").element("Type");
		ArrayList<String[]> listElement = new ArrayList<String[]>();

		for (Iterator i = activeElement.elementIterator(); i.hasNext();) {
			String[] attributes = new String[6];
			Element element = (Element) i.next();
			attributes[0] = element.getName();
			int j = 1;
			for (Iterator k = element.attributeIterator(); k.hasNext();) {
				Attribute attribute = (Attribute) k.next();
				attributes[j] = attribute.getValue();
				j++;
			}
			listElement.add(attributes);
			for (String ee : attributes) {
				// System.out.println(ee);
			}
			// System.out.println("============");
		}

		configApp.setActiveElement(listElement);
		// StatEquinox
		// ++ keep path and Interval
		Element equinox = root.element("StateEquinox");
		configApp.setPathStatEquinox(equinox.element("ConfigPath")
				.attributeValue("value"));
		configApp.setStatInterval(equinox.element("Interval").attributeValue(
				"value"));
		// ++ Keep StatEquinoxActive
		// +++ SystemInternalStatus
		Element internalStatus = root.element("StateEquinox")
				.element("StateType").element("SystemInternalStatus");
		ArrayList<String> arrayInternalStatus = new ArrayList<String>();
		for (Iterator i = internalStatus.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			arrayInternalStatus.add(element.getName());
		}
		configApp.setInternalStatus(arrayInternalStatus);
		// SystemMeasurement
		Element measurement = root.element("StateEquinox").element("StateType")
				.element("SystemMeasurement");
		ArrayList<String> arrayMeasurement = new ArrayList<String>();
		for (Iterator i = measurement.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			arrayMeasurement.add(element.getName());
		}
		configApp.setMeasurement(arrayMeasurement);
		// AccumulatingCounters
		Element accumulatingCounters = root.element("StateEquinox")
				.element("StateType").element("AccumulatingCounters");
		ArrayList<String> arrayAccumulatingCounters = new ArrayList<String>();
		for (Iterator i = accumulatingCounters.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			arrayAccumulatingCounters.add(element.getName());
		}
		configApp.setAccumulatingCounters(arrayAccumulatingCounters);

		/* ========= End Read Config ======== */
		return configApp;

	}

}

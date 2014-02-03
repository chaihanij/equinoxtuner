package com.ais.equinox.tuner.app.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ais.equinox.tuner.Main;

public class ModifyXML {

	private Element rootElement;
	private Document document;
	private ArrayList<String[]> arrayModify;

	public Element modifyDocument(Element inputXml, ArrayList<String[]> arrayModify) {
		this.rootElement = inputXml;
		this.arrayModify = arrayModify;
		Element nElement = null;
		System.out.println("============== Modify XML =================");

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(rootElement
					.asXML()));
			if (true == Main.DEBUG_CONSOLE){
			System.out.println("++++++++++ Befor Modify +++++++++");
			System.out.println(document.asXML());
			System.out.println("+++++++++++++++++++++++++++++++++");
			}
			// +++++++ Change Value in Attribute in Loop ++++++++

			Element root = document.getRootElement();

			for (Iterator i = root.elementIterator("warm"); i.hasNext();) {
				Element warm = (Element) i.next();
				for (Iterator j = warm.elementIterator(); j.hasNext();) {
					Element element = (Element) j.next();
					for (String[] string : arrayModify) {
						if (string[0].equals(element.getName())) {
							// Modify Value in Attribute
							for (Iterator k = element.attributeIterator(); k
									.hasNext();) {
								Attribute attribute = (Attribute) k.next();
								attribute.setValue(string[1]);

							}
						}
					}

				}
			}

			nElement = document.getRootElement();
			if (true == Main.DEBUG_CONSOLE){
			System.out.println("++++++++++ After Modify +++++++++");
			System.out.println(nElement.asXML());
			System.out.println("+++++++++++++++++++++++++++++++++");
			}
		}

		catch (DocumentException e) {
			System.out.println("Modify XML ERROR " + e.getMessage());
		}

		return nElement;

	}
}

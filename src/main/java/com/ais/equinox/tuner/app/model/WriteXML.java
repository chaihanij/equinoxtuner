package com.ais.equinox.tuner.app.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ais.equinox.tuner.Main;

public class WriteXML {

	private String fileName;
	private Element rootElement;

	public void writeXmlFile(String filename, Element rootElement) {
		this.fileName = filename;
		this.rootElement = rootElement;
		if (true == Main.DEBUG_CONSOLE) {
			System.out
					.println("============ Show Element at Write ============");
			System.out.println(rootElement.asXML());
		}
		// Creating document

		Document document;
		try {
			document = DocumentHelper.parseText(rootElement.asXML());
			// Write File
			write(document, filename);
		} catch (DocumentException e) {
			System.out.println("Write File ERROR");
			e.printStackTrace();
		}

	}

	private void write(Document document, String filename) {
		try {
			OutputFormat outFormat = new OutputFormat();
			outFormat = OutputFormat.createPrettyPrint();
			outFormat.setEncoding("tis-620");
			XMLWriter output = new XMLWriter(
					new FileWriter(new File(filename)), outFormat);
			output.write(document);
			output.close();
			System.out.println("+++++ WriteXML Completed +++++");
		} catch (IOException e) {
			System.out.println("Write File ERROR");
			System.out.println(e.getMessage());
		}

	}
	public void deleteFile(){
		try{
			 
    		File file = new File(fileName);
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted form temp!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
 
		
	}

}

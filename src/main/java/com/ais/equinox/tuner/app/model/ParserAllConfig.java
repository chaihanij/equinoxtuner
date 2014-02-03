package com.ais.equinox.tuner.app.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.SAXReader;



public class ParserAllConfig {

	private String stringXML;
	private String fileName;
	
public ParserAllConfig(String str,String filename){
	this.stringXML=str;
	this.fileName=filename;
	
}
	public Element Parser()   {
		
		SAXReader reader = new SAXReader();
		Document newNodeDocument = null;
		
		
		try {
			newNodeDocument = reader.read(new StringReader(stringXML));
			
			
			
		} catch (DocumentException e) { 
			System.out.println("Parser ERROR :"+fileName);
			e.printStackTrace();
		}
						
		Element root = newNodeDocument.getRootElement();
		System.out.println("+++++++++++++++ Parser suscess " +fileName+"++++++++++++ ");
		
		
  return root;
	}
	
	
		
		
}

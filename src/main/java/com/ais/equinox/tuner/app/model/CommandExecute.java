package com.ais.equinox.tuner.app.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.ais.equinox.tuner.Main;
import com.ais.equinox.tuner.app.bean.BeanConfig;


public class CommandExecute {
	
	public void createCommand(String math, String path, String host,
			String user, String password, String sessionId) throws IOException,
			DocumentException {
		/* +++ check file in folder ++ */

		String command1 = "ls " + path + "|grep \"^" + math + "\\.\"";
		System.out.println("Command Search :"+command1);
		ConnectNodeExec connection1 = new ConnectNodeExec(host, user, password);
		String str = connection1.connect(command1);
		String[] arrayStr = str.split("\n");

		HashMap<String, String> mapcommandset = new HashMap<String, String>();
		
		int j = 1;
		//Keep Key to Map Bean Element(1)
		HashMap<String, Element> mapElement = new HashMap<String, Element>();
		for (String string : arrayStr) {
			if (true == Main.DEBUG_CONSOLE)
				System.out.println(j+". >> cat "+path+ string);
			
			mapcommandset.put(string, "cat "+path+string);
			//Keep Key to Map Bean Element(2)
			mapElement.put(string, null);
			BeanConfig.beanElement.put(sessionId, mapElement);
			
			j++;
		}
		HashMap<String, String> outputs = new HashMap<String, String>();
		outputs = connection1.connect1(mapcommandset); 
	
		
		for (Entry<String, String> item : outputs.entrySet()) {
			
			if(item.getValue()==""){
				System.out.println("File : "+item.getKey()+" >> String Null ");
				
				
			}else {				
				
				xmlToBean(sessionId, item.getKey(),item.getValue() );
			}
		}

	}
	
	public String[] getListStatEquinox(String math, String path, String host,
			String user, String password, String sessionId) throws IOException,
			DocumentException {
		/* +++ check file in folder ++ */

		String command1 = "ls " + path + "|grep \"^" + math + "\\.\"";
		
		ConnectNodeExec connection1 = new ConnectNodeExec(host, user, password);
		String str = connection1.connect(command1);
		String[] arrayStr = str.split("\n");
		
		return arrayStr;		
	}



	private void xmlToBean(String sessionId, String filename, String str)
			throws DocumentException {

		ParserAllConfig parser = new ParserAllConfig(str, filename);
		Element rootElemrnt = parser.Parser();
		if (BeanConfig.beanElement.get(sessionId)!=null) {
			HashMap<String, Element> mapElement = new HashMap<String, Element>();
			mapElement=BeanConfig.beanElement.get(sessionId);
			mapElement.put(filename, rootElemrnt);
			BeanConfig.beanElement.put(sessionId, mapElement);
			
		} else {
			HashMap<String, Element> mapElement = new HashMap<String, Element>();
			mapElement.put(filename, rootElemrnt);
			BeanConfig.beanElement.put(sessionId, mapElement);
		}
	}

}

package com.ais.equinox.tuner.web.controller;

import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.equinox.tuner.SystemLogger;
import com.ais.equinox.tuner.app.bean.BeanAppConfig;
import com.ais.equinox.tuner.app.controller.ParserConfigApp;
import com.ais.equinox.tuner.app.model.SaveConfigApp;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/config")
public class ConfigApplicationController {

	@RequestMapping(value = "/layout.htm")
	public String getConfigAppPartialPage(ModelMap modelMap) {

		System.out.println("ConfigApplication/layout.htm");
		return "config";
	}

	@RequestMapping(value = "/getcongfigurationapp.htm", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getConfigurationName(HttpSession session, Principal principal,HttpServletRequest request) {
		
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();	
		String pathFile = sc.getRealPath("/WEB-INF/config.xml");
		System.out.println("getRealPath : "+pathFile);
		
		
		ParserConfigApp parserConfigApp = new ParserConfigApp();
		BeanAppConfig beanConfigApp = parserConfigApp.parser(pathFile);
		// Json
		JsonObject jsObject = new JsonObject();
		jsObject.addProperty("configPath", beanConfigApp.getPathConfig());
		jsObject.addProperty("configPatheStatEquinox",beanConfigApp.getPathStatEquinox());
		jsObject.addProperty("interval",beanConfigApp.getStatInterval());
	
		// Add Path System

		// JsonArray ActiveElement
		JsonArray arrayActiveElement = new JsonArray();
		ArrayList<String[]> activeElement = new ArrayList<String[]>();
		activeElement = beanConfigApp.getActiveElement();
		for (String[] strings : activeElement) {
			JsonObject jsObjectAttribute = new JsonObject();
			jsObjectAttribute.addProperty("name",strings[0] );
			jsObjectAttribute.addProperty("max", strings[2]);
			jsObjectAttribute.addProperty("min", strings[3]);
			jsObjectAttribute.addProperty("unit", strings[4]);
			jsObjectAttribute.addProperty("description", strings[5]);
			arrayActiveElement.add(jsObjectAttribute);
		}
		jsObject.add("activeelement", arrayActiveElement);
		// JsonArray NodeConnection
		JsonArray arrayNodeConnection = new JsonArray();
		ArrayList<String[]> nodeConnection = new ArrayList<String[]>();
		nodeConnection = beanConfigApp.getNodeConnection();
		for (String[] strings : nodeConnection) {
			JsonObject jsObjectAttribute = new JsonObject();
			jsObjectAttribute.addProperty("nodeName", strings[0]);
			jsObjectAttribute.addProperty("ip", strings[1]);
			jsObjectAttribute.addProperty("user", strings[2]);
			jsObjectAttribute.addProperty("password", strings[3]);
			arrayNodeConnection.add(jsObjectAttribute);
		}
		jsObject.add("nodeconnection", arrayNodeConnection);
		// Json StatEquinox
		
		JsonObject statEquinox = new JsonObject();
		// Json SystemInternalStat
		JsonArray activeInternalStat = new JsonArray();
		ArrayList<String> arrayInternalStat = new ArrayList<String>();
		arrayInternalStat = beanConfigApp.getInternalStatus();
		for (String string : arrayInternalStat) {
			JsonObject jsObjectAttribute = new JsonObject();
			jsObjectAttribute.addProperty("name", string);
			activeInternalStat.add(jsObjectAttribute);
		}
		statEquinox.add("internalstat", activeInternalStat);
		// Json SystemMeasurement
		JsonArray activeMeasurement = new JsonArray();
		ArrayList<String> arrayMeasurement = new ArrayList<String>();
		arrayMeasurement = beanConfigApp.getMeasurement();
		for (String string : arrayMeasurement) {
			JsonObject jsObjectAttribute = new JsonObject();
			jsObjectAttribute.addProperty("name", string);
			activeMeasurement.add(jsObjectAttribute);
		}
		statEquinox.add("measurement", activeMeasurement);
		// Json AccumulatingCounters
				JsonArray activeAccumulating = new JsonArray();
				ArrayList<String> arrayAccumulating = new ArrayList<String>();
				arrayAccumulating = beanConfigApp.getAccumulatingCounters();
				for (String string : arrayAccumulating) {
					JsonObject jsObjectAttribute = new JsonObject();
					jsObjectAttribute.addProperty("name", string);
					activeAccumulating.add(jsObjectAttribute);
				}
				statEquinox.add("accumulating", activeAccumulating);
		//Add all statEquinoxType
		jsObject.add("statequinox", statEquinox);

		System.out.println("output>>GetconfigurationApp : "
				+ jsObject.toString());
		SystemLogger.logger.info("output>>GetconfigurationApp : "
				+ jsObject.toString());	

		return jsObject.toString();
	}

	@RequestMapping(value = "/savecongfigurationapp.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String saveConfigApp(@RequestBody final String json, HttpSession session,
			Principal principal,HttpServletRequest request) {
		System.out.println("savecongfigurationapp.htm : "+json);
		SystemLogger.logger.info("savecongfigurationapp.htm : "+json);	
		BeanAppConfig beanAppConfigSave = new BeanAppConfig();
		//Json Parser 
		JsonParser jsParser = new JsonParser();
		JsonElement jsElemnet = jsParser.parse(json);
		JsonObject jsObject = jsElemnet.getAsJsonObject();
		String configPath = jsObject.get("configPath").getAsString();
		String pathStatEquinox = jsObject.get("configPatheStatEquinox").getAsString();
		String interval = jsObject.get("interval").getAsString();
		//Set to Bean
		beanAppConfigSave.setPathConfig(configPath);
		
		beanAppConfigSave.setPathStatEquinox(pathStatEquinox);
		beanAppConfigSave.setStatInterval(interval);
		
		System.out.println(beanAppConfigSave.getPathConfig());		
		System.out.println(beanAppConfigSave.getPathStatEquinox());
		
		//Array activeElement 
		JsonArray jsArray = jsObject.get("activeelement").getAsJsonArray();		
		ArrayList<String []>  activeElement = new ArrayList<String[]>();
		for (JsonElement jsonElement : jsArray) {
			String[] value = new String[5];
			JsonObject jsOb = jsonElement.getAsJsonObject();
			value[0]= jsOb.get("name").getAsString();
			value[1]= jsOb.get("max").getAsString();
			value[2]= jsOb.get("min").getAsString();
			value[3]= jsOb.get("unit").getAsString();
			value[4]= jsOb.get("description").getAsString();
			activeElement.add(value);
			
		}
		beanAppConfigSave.setActiveElement(activeElement);
		//Array StatEquinox 
		JsonObject jsObjectStatEquinox = jsObject.get("statequinox").getAsJsonObject();
		JsonArray jInternal = jsObjectStatEquinox.get("internalstat").getAsJsonArray();
		JsonArray jMeasurement = jsObjectStatEquinox.get("measurement").getAsJsonArray();
		JsonArray jAccumulating = jsObjectStatEquinox.get("accumulating").getAsJsonArray();
		//InternalStatus	
			ArrayList<String>  arrayInternal = new ArrayList<String>();
		for (JsonElement jsonElement : jInternal) {
			String value =null;
			JsonObject jsOb = jsonElement.getAsJsonObject();
			value= jsOb.get("name").getAsString();
			System.out.println("InternalStat  :  "+value.toString());
			arrayInternal.add(value);
			
		}
		//Measurement	
		ArrayList<String>  arrayMeasurement = new ArrayList<String>();
	for (JsonElement jsonElement : jMeasurement) {
		String value =null;
		JsonObject jsOb = jsonElement.getAsJsonObject();
		value= jsOb.get("name").getAsString();
		System.out.println("Measurement  :  "+value.toString());
		arrayMeasurement.add(value);
	}
	//AccumulatingCounters
	
			ArrayList<String>  arrayAccumulating = new ArrayList<String>();
			for (JsonElement jsonElement : jAccumulating) {
				String value =null;
				JsonObject jsOb = jsonElement.getAsJsonObject();
				value= jsOb.get("name").getAsString();
				System.out.println("AccumulatingCounters  :  "+value.toString());
				arrayAccumulating.add(value);
				
			}
	//Set Array to Bean 
	beanAppConfigSave.setInternalStatus(arrayInternal);
	beanAppConfigSave.setMeasurement(arrayMeasurement);
	beanAppConfigSave.setAccumulatingCounters(arrayAccumulating);
	//Set Node Conncetion 
	JsonArray jsArrayNode = jsObject.get("nodeconnection").getAsJsonArray();
	ArrayList<String[]> arrayNode = new ArrayList<String[]>();
	for (JsonElement jsonElement : jsArrayNode) {
		String[] value = new String[4];
		JsonObject jsOb = jsonElement.getAsJsonObject();
		value[0]= jsOb.get("nodeName").getAsString();
		value[1]= jsOb.get("ip").getAsString();
		value[2]= jsOb.get("user").getAsString();
		value[3]= jsOb.get("password").getAsString();
		arrayNode.add(value);
	}
		beanAppConfigSave.setNodeConnection(arrayNode);
	// Save Config
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();	
		String pathFile = sc.getRealPath("/WEB-INF/config.xml");
		SaveConfigApp saveConfig = new SaveConfigApp();
		saveConfig.createDocument(beanAppConfigSave,pathFile);
		
		
		
		return "Success";
		
	}

}

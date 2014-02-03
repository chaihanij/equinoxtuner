package com.ais.equinox.tuner.web.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.equinox.tuner.Main;
import com.ais.equinox.tuner.SystemLogger;
import com.ais.equinox.tuner.app.bean.BeanAppConfig;
import com.ais.equinox.tuner.app.bean.BeanConfig;
import com.ais.equinox.tuner.app.controller.ParserConfigApp;
import com.ais.equinox.tuner.app.model.CommandExecute;
import com.ais.equinox.tuner.app.model.CommandShell;
import com.ais.equinox.tuner.app.model.ConnectNodeExec;
import com.ais.equinox.tuner.app.model.ModifyXML;
import com.ais.equinox.tuner.app.model.ParserAllConfig;
import com.ais.equinox.tuner.app.model.SCPupload;
import com.ais.equinox.tuner.app.model.WriteXML;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/tunerconfig")
public class TunerConfigurationController {
	
	
	private String path = "/tunerconfig";
	private HashMap<String, String> NameConfigActive = new HashMap<String, String>();
	private BeanAppConfig beanConfig;

	@RequestMapping(value = "/layout.htm")
	public String getTunerConfigurationPartialPage(ModelMap modelMap) {
//		System.out.println("/Tunerconfiguration/layout.htm");
		return "tuner";
	}

	@RequestMapping(value = "/getsystemgroup.htm", produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getSystemGroup(HttpSession session,HttpServletRequest request) {

		if (Main.DEBUG_CONSOLE != false)
			System.out.println("PathView = " + path + "/getsystemgroup.json");
		
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();	
		String pathFile = sc.getRealPath("/WEB-INF/config.xml");
		System.out.println("getRealPath : "+pathFile);
		ParserConfigApp parser = new ParserConfigApp();
		beanConfig = parser.parser(pathFile);
		SystemLogger.logger.info("GetRealPath : "+pathFile);
		JsonArray jaSysGroup = new JsonArray();

		try {
			for (String[] string : beanConfig.getNodeConnection()) {
				JsonObject jsSysGroup = new JsonObject();
				jsSysGroup.addProperty("text", string[0]);
				jsSysGroup.addProperty("value", string[1]);
				jaSysGroup.add(jsSysGroup);
			}
			if (true == Main.DEBUG_CONSOLE)
				System.out.println(jaSysGroup);
			SystemLogger.logger.info(jaSysGroup);
			return jaSysGroup.toString();
		} catch (Exception e) {
			System.out.println("==============  ERROR in Get SessionGroup  ===================");
			SystemLogger.logger.error("ERROR in Get SessionGroup", e);
			return jaSysGroup.toString();
		}
	}

	@RequestMapping(value = "/getcongfigurationname.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getConfigurationName(@RequestBody final String json,
			HttpSession session, Principal principal) {

		String _strtext = null;
		String _strvalue = null;
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("pahtView = " + path
					+ "/getcongfigurationname.json" + " input =" + json);
		SystemLogger.logger.info("InputData getcongfigurationname.htm : "+json);

		JsonArray array = new JsonArray();
		try {

			/** Get Parameter array Application List **/
			if (!json.equals("") && json.length() > 0) {
				JsonParser jp = new JsonParser();
				JsonElement jelement = jp.parse(json);
				JsonObject jobject = jelement.getAsJsonObject();
				JsonElement text = jobject.get("text");
				JsonElement value = jobject.get("value");
				_strtext = text.toString().replace("\"", "");
				_strvalue = value.toString().replace("\"", "");
				/** Get Parameter array Application List **/
			}
			/** Retrun array Application List **/
			if (true == Main.DEBUG_CONSOLE)
				System.out.println("text:" + _strtext + " value:" + _strvalue);
			// Clear Map When Select New Node Connectin
			if (BeanConfig.beanElement.get(session.getId()) != null) {
				BeanConfig.beanElement.get(session.getId()).clear();
			}

			String host = null;
			String user = null;
			String pass = null;
			for (String[] str : beanConfig.getNodeConnection()) {
				if (str[0].equals(_strtext)&&str[1].equals(_strvalue)) {
					host = str[1];
					user = str[2];
					pass = str[3];
				}
			}
			//Set host user pass to session
			session.setAttribute("host", host);
			session.setAttribute("user", user);
			session.setAttribute("pass", pass);
			
			CommandExecute commandEx = new CommandExecute();
			commandEx.createCommand(_strtext, beanConfig.getPathConfig()+"/", host,
					user, pass, session.getId());

			for (Entry<String, Element> item : BeanConfig.beanElement.get(
					session.getId()).entrySet()) {
				JsonObject object = new JsonObject();
				object.addProperty("text", item.getKey());
				object.addProperty("value", item.getKey());
				array.add(object);
			}

			/** Retrun array Application List **/

			if (true == Main.DEBUG_CONSOLE)
				System.out.println("Outputgetappication" + array.toString());
			SystemLogger.logger.info("Output getcongfigurationname.htm : "+array.toString());
			return array.toString();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out
					.println("==============  ERROR in getConfigurationName  ===================");
		SystemLogger.logger.error("ERROR in getConfigurationName", e);
			return "[]";
		}
	}

	@RequestMapping(value = "/getconfigurationdetail.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getConfigutionDetail(@RequestBody final String json,
			HttpSession session, Principal principal) {
		String text = null;
		String value;

		if (true == Main.DEBUG_CONSOLE)
			System.out.println("Input getconfigutiondetail" + json);
			SystemLogger.logger.info("Input getcongfigurationname.htm : "+json);
		try {

			/** Get Parameter array Application List **/
			if (!json.equals("") && json.length() > 0) {

				JsonParser jp = new JsonParser();
				JsonElement jelement = jp.parse(json);
				JsonObject jobject = jelement.getAsJsonObject();
				value = jobject.get("value").getAsString();
				text = jobject.get("text").getAsString();

				/** Get Parameter array Application List **/

			}
			/** Retrun array Application List **/
			// JsonArray jaSysDevice = new JsonArray();
			// String string =
			// readHttpJson("http://localhost:9090/Tuner/resources/js/config.json");
			// Keep Active Name
			// try {
			NameConfigActive.put(session.getId(), text);
			// } catch (Exception e) {
			// System.out.println("ERROE in Even getdetial when keep value in MapNameConfigActive");
			// }

			JsonArray jaArray = new JsonArray();
			JsonObject object = new JsonObject();
			JsonObject sfLog = new JsonObject();
			JsonArray warm = new JsonArray();
			System.out.println(session.getId() + " : " + text);
			HashMap<String, Element> mapElement = new HashMap<String, Element>();

			if (BeanConfig.beanElement.get(session.getId()).get(text) != null) {
				mapElement = BeanConfig.beanElement.get(session.getId());
			} else {
				// Reconnection if Map null or No data in Map
				String host=(String) session.getAttribute("host");
				String user=(String) session.getAttribute("user");
				String pass=(String) session.getAttribute("pass");
				
				ConnectNodeExec connection1 = new ConnectNodeExec(host, user,pass);
				String command1 = "cat " + beanConfig.getPathConfig()+"/" + text;
				String str = connection1.connect(command1);
				ParserAllConfig parser = new ParserAllConfig(str, text);
				Element rootElemrnt = parser.Parser();
				HashMap<String, Element> saveElement = new HashMap<String, Element>();
				saveElement = BeanConfig.beanElement.get(session.getId());
				saveElement.put(text, rootElemrnt);
				BeanConfig.beanElement.put(session.getId(), saveElement);
				// Set value
				mapElement = saveElement;
			}

			Element configInElement = mapElement.get(text);
			try {
				configInElement = configInElement.element("warm");
			} catch (Exception e) {
				SystemLogger.logger.error("ERROR in get configInElement", e);
				System.out.println("ERROR in get configInElement");
			}

			for (String[] bean : beanConfig.getActiveElement()) {
				for (Iterator i = configInElement.elementIterator(); i
						.hasNext();) {
					Element element = (Element) i.next();
					if (element.getName().equals(bean[0])) {
						if (bean[0].equals("SFLOG")) {

							String str = configInElement.element(bean[0])
									.attributeValue(bean[1]);
							sfLog.addProperty(bean[0], str);

						} else {

							String str = configInElement.element(bean[0])
									.attributeValue(bean[1]);

							JsonObject obWarm = new JsonObject();
							obWarm.addProperty("text", bean[0]);
							obWarm.addProperty("value", str);
							obWarm.addProperty("min", bean[3]);
							obWarm.addProperty("max", bean[2]);
							obWarm.addProperty("unit", bean[4]);
							obWarm.addProperty("description", bean[5]);

							warm.add(obWarm);

						}
					}
				}
			}

			// name
			object.addProperty("name", text);
			// sflog
			object.add("sflog", sfLog);
			// warm
			object.add("wram", warm);
			jaArray.add(object);
			System.out.println("Output getconfigutiondetail.htm"
					+ jaArray.toString());
			SystemLogger.logger.info("Output getconfigutiondetail.htm"
					+ jaArray.toString());
			/** Retrun array Application List **/
			// if (true == Main.DEBUG_CONSOLE)
			// System.out.println("Output getconfigutiondetail.htm" + string);
			return jaArray.toString();
		} catch (Exception e) {
			System.out
					.println("==============  ERROR in Keep Value in Session  ===================");
			System.out.println("ERROR : Paramiter Missmatch");
			SystemLogger.logger.error("ERROR in Get ConfigulationDetail: Paramiter Missmatch", e);
			return "[]";
		}

	}

	@RequestMapping(value = "/onclickdeploy.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String onClickDeploy(@RequestBody final String json, HttpSession sessiont) {

		if (true == Main.DEBUG_CONSOLE)
			System.out.println("intput onclickdeploy.htm" + json);

		return "";
	}

	@RequestMapping(value = "onclickapply.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String onClickApply(@RequestBody final String json, HttpSession session,
			Principal principal,HttpServletRequest request) {
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();	
		String pathFileLocal = sc.getRealPath("/temp/");
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("intput onclickapply.htm" + json);
			SystemLogger.logger.info("intput onclickapply.htm" + json);
		
		// Parser Json
		JsonParser jsParser = new JsonParser();
		JsonElement jsElemnet = jsParser.parse(json);
		JsonObject jsObject = jsElemnet.getAsJsonObject();
		String fileName = jsObject.get("configname").getAsString();
		Element rootElement = null;

		if (BeanConfig.beanElement.get(session.getId()) != null) {
			rootElement = BeanConfig.beanElement.get(session.getId()).get(
					fileName);
			// Add value to Array Send to ModifyXML
			ArrayList<String[]> valueModify = new ArrayList<String[]>();

			JsonArray jsArray = jsObject.get("wrams").getAsJsonArray();

			for (JsonElement jsonElement : jsArray) {
				JsonObject jsOb = jsonElement.getAsJsonObject();
				String[] value = new String[2];
				value[0] = jsOb.get("text").getAsString();
				value[1] = jsOb.get("value").getAsString();
				valueModify.add(value);
			}
			// Modify XML from view
			ModifyXML modifyXML = new ModifyXML();
			Element nElement = modifyXML.modifyDocument(rootElement,
					valueModify);
			// Update Bean
			HashMap<String, Element> mapElement = new HashMap<String, Element>();
			mapElement = BeanConfig.beanElement.get(session.getId());
			mapElement.put(fileName, nElement);
			BeanConfig.beanElement.put(session.getId(), mapElement);
			// Keep Config to Bean Recovery
			SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd_hh:mm:ss");
			Date date = new Date();
			String formart = ft.format(date);
			// init
			try {
				if (session.getAttribute("Recovery:" + principal.getName()) == null) {
					HashMap<String, HashMap<String, Element>> beanRecovery = new HashMap<String, HashMap<String, Element>>();
					HashMap<String, Element> changeElement = new HashMap<String, Element>();
					changeElement.put(formart, nElement);
					beanRecovery.put(fileName, changeElement);
					for (Object item : changeElement.keySet()) {
						System.out.println("Key in Recovery : "
								+ item.toString());
						System.out.println("Element : "
								+ changeElement.get(item).asXML());
					}
					session.setAttribute("Recovery:" + principal.getName(),
							beanRecovery);
				} else {
					HashMap<String, HashMap<String, Element>> beanRecovery;
					beanRecovery = (HashMap<String, HashMap<String, Element>>) session
							.getAttribute("Recovery:" + principal.getName());
					HashMap<String, Element> changeElement = new HashMap<String, Element>();
					//Get file HashMap in Recovery 
					changeElement = beanRecovery.get(fileName);
					if(changeElement==null){
						changeElement = new HashMap<String, Element>();
					}
					changeElement.put(formart, nElement);
					
					
					for (Object item : beanRecovery.keySet()) {
						System.out.println("Key in Recovery : "
								+ item.toString());
						//System.out.println("Element : "+ beanRecovery.get(fileName).get(item).asXML());
					}
					beanRecovery.put(fileName, changeElement);
					
					session.setAttribute("Recovery:"+principal.getName(),beanRecovery);
				}
			} catch (Exception e) {
				System.out
						.println("============== ERROR in Even Apply : Add MapRecovery in Session  ===================");
				SystemLogger.logger.error("ERROR in Even Apply : Add MapRecovery in Session", e);
			}

			// Write new XML file
			WriteXML write = new WriteXML();
			write.writeXmlFile(pathFileLocal+"/"+ fileName,
					nElement);
			// Connect for upload file
		
			String host=(String) session.getAttribute("host");
			String user=(String) session.getAttribute("user");
			String pass=(String) session.getAttribute("pass");
			SCPupload upload = new SCPupload();
			String pathLocalFile = pathFileLocal+"/"+ fileName;
			String remoteFile = beanConfig.getPathConfig()+"/"+ fileName;
			System.out.println(pathLocalFile);
			System.out.println(remoteFile);
			upload.uploadFile(pathLocalFile, remoteFile, host, user, pass);
			write.deleteFile();
			//restart equinox
			String []appName = fileName.split("\\.");
			String commandStartEqx ="eqx "+appName[0]+" "+fileName+" start";
			String commandStopEqx ="eqx "+appName[0]+" "+fileName+" forcestop";
			String [] cmdSet = {commandStopEqx,"@wait 1500","@match .*y/n.*","@ends (y/n)","y",commandStartEqx,"exit"};	
			CommandShell commandShell = new CommandShell("Restart eqx", host, user, pass, cmdSet);
			commandShell.startSending();
			
			

		} else {
			System.out.println("Event Apply MisMatch value in HashMap");
			SystemLogger.logger.error("Event Apply MisMatch value in HashMap");
		}

		return "";
	}

	@RequestMapping(value = "/onclicksave.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String onClickSAVE(@RequestBody final String json, HttpSession session,
			Principal principal) {
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("input onclicksave.htm" + json);
			SystemLogger.logger.info("input onclicksave.htm" + json);
		// Parser Json
		JsonParser jsParser = new JsonParser();
		JsonElement jsElemnet = jsParser.parse(json);
		JsonObject jsObject = jsElemnet.getAsJsonObject();
		String fileName = jsObject.get("configname").getAsString();
		String date = jsObject.get("date").getAsString();
		String nickname = jsObject.get("nickname").getAsString();

		Element rootElement = null;

		if (BeanConfig.beanElement.get(session.getId()) != null) {
			rootElement = BeanConfig.beanElement.get(session.getId()).get(
					fileName);
			// Add value to Array Send to ModifyXML
			ArrayList<String[]> valueModify = new ArrayList<String[]>();

			JsonArray jsArray = jsObject.get("wrams").getAsJsonArray();

			for (JsonElement jsonElement : jsArray) {
				JsonObject jsOb = jsonElement.getAsJsonObject();
				String[] value = new String[2];
				value[0] = jsOb.get("text").getAsString();
				value[1] = jsOb.get("value").getAsString();
				valueModify.add(value);
			}
			// Modify XML from view
			ModifyXML modifyXML = new ModifyXML();
			Element nElement = modifyXML.modifyDocument(rootElement,
					valueModify);
			// Set key in Map
			String key = null;
			key = fileName + "." + date + "." + nickname;
			System.out.println("+++++++++ UserName : " + principal.getName());
			try {
				// Check Map if null create new Map and save
				if (BeanConfig.beanSave.get(principal.getName()) == null) {
					// Map Value
					HashMap<String, Element> valueSave = new HashMap<String, Element>();
					valueSave.put(key, nElement);
					// Map FileName
					HashMap<String, HashMap<String, Element>> mapFileName = new HashMap<String, HashMap<String, Element>>();
					mapFileName.put(fileName, valueSave);
					// Add to HashMap Save
					BeanConfig.beanSave.put(principal.getName(), mapFileName);
				} else {
					// Get Map and Save
					// Map FileName
					HashMap<String, HashMap<String, Element>> mapFileName = new HashMap<String, HashMap<String, Element>>();
					mapFileName = BeanConfig.beanSave.get(principal.getName());
					// Map Value
					HashMap<String, Element> valueSave = new HashMap<String, Element>();
					valueSave = mapFileName.get(fileName);
					//check map null
					if(valueSave==null){
						valueSave=new HashMap<String, Element>();
					}
					// Add value
					valueSave.put(key, nElement);
					mapFileName.put(fileName, valueSave);
					// Add to HashMap Save
					BeanConfig.beanSave.put(principal.getName(), mapFileName);

				}
			} catch (Exception e) {
				System.out.println("ERROR in Save");
				SystemLogger.logger.error("ERROR in Save", e);
				
			}

		} else {
			System.out.println("Mismatch Element in Event SAVE");
			SystemLogger.logger.error("ERROR in Save");
		}
		return "";
	}

	@RequestMapping(value = "/getloadlist.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getLoadList(@RequestBody final String json, HttpSession session,
			Principal principal) {
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("input getloadlist.htm" + json);
			SystemLogger.logger.info("input getloadlist.htm" + json);
		// input
		String inputFileName = json;
		try {
			if (BeanConfig.beanSave.get(principal.getName()) != null) {
				JsonObject jsOb = new JsonObject();
				jsOb.addProperty("filename", inputFileName);
				JsonArray jsArray = new JsonArray();

				for (Object key : BeanConfig.beanSave.get(principal.getName())
						.get(inputFileName).keySet()) {
					JsonObject jsvalue = new JsonObject();
					jsvalue.addProperty("value", key.toString());
					jsArray.add(jsvalue);

				}
				jsOb.add("list", jsArray);
				System.out.println("/getloadlist.htm :"+jsArray.toString());
				return jsOb.toString();
			} else {
				System.out.println("No List Save ");
				SystemLogger.logger.info("No List Save ");
				
				return "[]";
			}

		} catch (Exception e) {
			System.out.println("ERROR in getLoadList");
			SystemLogger.logger.error("ERROR in getLoadList", e);
			return "[]";
		}

	}

	@RequestMapping(value = "/actionload.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String actionLoad(@RequestBody final String json, HttpSession session,
			Principal principal) {

		if (true == Main.DEBUG_CONSOLE)
			System.out.println("input actionload.htm.htm" + json);
		SystemLogger.logger.info("input actionload.htm.htm" + json);
		// Parser Json
		JsonParser jsParser = new JsonParser();
		JsonElement jsElemnet = jsParser.parse(json);
		JsonObject jsObject = jsElemnet.getAsJsonObject();
		// File Name
		String fileName = jsObject.get("filename").getAsString();
		JsonObject value = jsObject.get("value").getAsJsonObject();
		// fullName
		String fullName = value.get("value").getAsString();

		if (BeanConfig.beanSave.get(principal.getName()) != null) {

			// Return Data

			JsonArray jaArray = new JsonArray();
			JsonObject object = new JsonObject();
			JsonObject sfLog = new JsonObject();
			JsonArray warm = new JsonArray();

			Element configInElement = BeanConfig.beanSave
					.get(principal.getName()).get(fileName).get(fullName);
			configInElement = configInElement.element("warm");

			for (String[] bean : beanConfig.getActiveElement()) {
				for (Iterator i = configInElement.elementIterator(); i
						.hasNext();) {
					Element element = (Element) i.next();
					if (element.getName().equals(bean[0])) {
						if (bean[0].equals("SFLOG")) {

							String str = configInElement.element(bean[0])
									.attributeValue(bean[1]);
							sfLog.addProperty(bean[0], str);

						} else {

							String str = configInElement.element(bean[0])
									.attributeValue(bean[1]);

							JsonObject obWarm = new JsonObject();
							obWarm.addProperty("text", bean[0]);
							obWarm.addProperty("value", str);
							obWarm.addProperty("min", bean[3]);
							obWarm.addProperty("max", bean[2]);
							obWarm.addProperty("unit", bean[4]);
							obWarm.addProperty("description", bean[5]);

							warm.add(obWarm);

						}
					}
				}
			}

			// name
			object.addProperty("name", fileName);
			// sflog
			object.add("sflog", sfLog);
			// warm
			object.add("wram", warm);
			jaArray.add(object);
			System.out.println("Output actionLoad.htm" + jaArray.toString());
			SystemLogger.logger.info("Output actionLoad.htm" + jaArray.toString());			
			return jaArray.toString();
		} else {
			System.out.println("No List Save ");
		
			return "[]";
		}
	}

	@RequestMapping(value = "getrecoverylist.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String onClickRecoveryshowlist(@RequestBody final String fileName,
			HttpSession session, Principal principal) {

		if (true == Main.DEBUG_CONSOLE)
			System.out
					.println("intput onclickrecoveryshowlist.htm " + fileName);
		SystemLogger.logger.info("intput onclickrecoveryshowlist.htm " + fileName);

		try {
			// Edit By Dee
			HashMap<String, HashMap<String, Element>> beanRecovery = null;
			beanRecovery = (HashMap<String, HashMap<String, Element>>) session
					.getAttribute("Recovery:" + principal.getName());
			HashMap<String, Element> recoveryFile = beanRecovery.get(fileName);
			JsonObject jsOb = new JsonObject();
			jsOb.addProperty("filename", fileName);

			JsonArray jsArray = new JsonArray();
			for (Object key : recoveryFile.keySet()) {
				// System.out.println("Key : " + key.toString() + " Value : "+
				// recoveryFile.get(key));
				JsonObject jsvalue = new JsonObject();
				jsvalue.addProperty("value", key.toString());
				System.out.println("value : " + key.toString());
				jsArray.add(jsvalue);

			}
			jsOb.add("list", jsArray);
			if (true == Main.DEBUG_CONSOLE)
				System.out.println("intput onclickrecoveryshowlist.htm "
						+ jsOb.toString());
			SystemLogger.logger.info("intput onclickrecoveryshowlist.htm "
					+ jsOb.toString());
			// ++++++++++++++++++++++++++++
			return jsOb.toString();
		} catch (Exception e) {
			System.out
					.println("==============  ERROR in getListRecovery or NoData beanRecovery null ===================");
			SystemLogger.logger.error("ERROR in getListRecovery or NoData beanRecovery null", e);
			return "[]";
		}
	}

	@RequestMapping(value = "onclickrecovery.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String onClickRecovery(@RequestBody final String json, HttpSession session,
			Principal principal,HttpServletRequest request) {
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();
		String pathFileLocal = sc.getRealPath("/temp/");
		// Parser Json
		JsonParser jsParser = new JsonParser();
		JsonElement jsElemnet = jsParser.parse(json);
		JsonObject jsObject = jsElemnet.getAsJsonObject();

		String fileName = jsObject.get("filename").getAsString();
		String dateTime = jsObject.get("date").getAsString();
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("intput onclickrecovery.htm " + json);
		SystemLogger.logger.info("intput onclickrecovery.htm " + json);		
		try {
			// Step send Config to view
			JsonArray jaArray = new JsonArray();
			JsonObject object = new JsonObject();
			JsonObject sfLog = new JsonObject();
			JsonArray warm = new JsonArray();
			HashMap<String, HashMap<String, Element>> beanRecovery = new HashMap<String, HashMap<String, Element>>();
			beanRecovery = (HashMap<String, HashMap<String, Element>>) session
					.getAttribute("Recovery:" + principal.getName());

			if (beanRecovery.get(fileName).get(dateTime) != null) {
				Element reElement = beanRecovery.get(fileName).get(dateTime);
				// Write new XML file
				System.out.println("Show Element : " + reElement.asXML());
				WriteXML write = new WriteXML();
				write.writeXmlFile(pathFileLocal+"/"+ fileName,
						reElement);
				// Connect for upload file
				String host=(String) session.getAttribute("host");
				String user=(String) session.getAttribute("user");
				String pass=(String) session.getAttribute("pass");
				SCPupload upload = new SCPupload();
				String pathLocalFile = pathFileLocal+"/"+ fileName;
				String remoteFile = beanConfig.getPathConfig()+"/"+ fileName;
				System.out.println(pathLocalFile);
				System.out.println(remoteFile);
				upload.uploadFile(pathLocalFile, remoteFile, host, user, pass);
				write.deleteFile();
				//restart equinox
				String []appName = fileName.split("\\.");
				String commandStartEqx ="eqx "+appName[0]+" "+fileName+" start";
				String commandStopEqx ="eqx "+appName[0]+" "+fileName+" forcestop";
				String [] cmdSet = {commandStopEqx,"@wait 1500","@match .*y/n.*","@ends (y/n)","y",commandStartEqx,"exit"};	
				CommandShell commandShell = new CommandShell("Restart eqx", host, user, pass, cmdSet);
				commandShell.startSending();
				// Return Data
				Element recoveryElement = beanRecovery.get(fileName).get(
						dateTime);
				recoveryElement = recoveryElement.element("warm");

				for (String[] bean : beanConfig.getActiveElement()) {
					for (Iterator i = recoveryElement.elementIterator(); i
							.hasNext();) {
						Element element = (Element) i.next();
						if (element.getName().equals(bean[0])) {
							if (bean[0].equals("SFLOG")) {

								String strSFLOG = recoveryElement.element(
										bean[0]).attributeValue(bean[1]);
								sfLog.addProperty(bean[0], strSFLOG);

							} else {

								String strelement = recoveryElement.element(
										bean[0]).attributeValue(bean[1]);

								JsonObject obWarm = new JsonObject();
								obWarm.addProperty("text", bean[0]);
								obWarm.addProperty("value", strelement);
								obWarm.addProperty("min", bean[3]);
								obWarm.addProperty("max", bean[2]);
								obWarm.addProperty("unit", bean[4]);
								obWarm.addProperty("description", bean[5]);

								warm.add(obWarm);

							}
						}
					}
				}

				// name
				object.addProperty("name", fileName);
				// sflog
				object.add("sflog", sfLog);
				// warm
				object.add("wram", warm);
				jaArray.add(object);

			} else {
				System.out.println("No data in Recovery");
				SystemLogger.logger.error("No data in Recovery");	
			}
			if (true == Main.DEBUG_CONSOLE)
				System.out.println("output onclickrecovery.htm = "
						+ jaArray.toString());
			SystemLogger.logger.info("output onclickrecovery.htm = "
					+ jaArray.toString());	
			return jaArray.toString();
		} catch (Exception e) {
			System.out.println("==============  ERROR in onClick Recovery  ===================");
			SystemLogger.logger.error("ERROR in onClick Recovery", e);
			return "[]";
		}
	}

}
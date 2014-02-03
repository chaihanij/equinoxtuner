package com.ais.equinox.tuner.web.controller;

import java.security.Principal;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import com.ais.equinox.tuner.app.bean.BeanStatEquinoxAllType;
import com.ais.equinox.tuner.app.controller.ParserConfigApp;
import com.ais.equinox.tuner.app.model.CommandExecute;
import com.ais.equinox.tuner.app.model.ConnectGetStat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/monitor")
public class MonitorContrller {
	private BeanAppConfig beanConfig;
	private HashMap<String, String> NameStatActive = new HashMap<String, String>();
	
	@RequestMapping(value = "/layout.htm")
	public String getMonitorPartialPage(ModelMap modelMap) {
		System.out.println("monitor/layout.htm");
		return "monitor";
	}
	
	@RequestMapping(value = "/getsystemgroupmoniotr.htm", produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getSystemGroup(HttpServletRequest request,HttpSession session)  {
		
		/*  ++++++++++Get Path File Config +++++++++++*/
		ServletContext sc = session.getServletContext();	
		String pathFile = sc.getRealPath("/WEB-INF/config.xml");
		System.out.println("getRealPath : "+pathFile);
		ParserConfigApp parser = new ParserConfigApp();
		beanConfig = parser.parser(pathFile);
		
		//set to Static bean
		BeanConfig.beanConfigApp=beanConfig;
		
		JsonArray jaSysGroup = new JsonArray();

		try {
			for (String[] string : beanConfig.getNodeConnection()) {
				JsonObject jsSysGroup = new JsonObject();
				jsSysGroup.addProperty("text", string[0]);
				jsSysGroup.addProperty("value", string[1]);
				jaSysGroup.add(jsSysGroup);
			}
			if (true == Main.DEBUG_CONSOLE)
				System.out.println("GetSystemGroup : "+jaSysGroup);
			return jaSysGroup.toString();
		} catch (Exception e) {
			System.out
					.println("==============  ERROR in Get getsystemgroupmoniotr  ===================");
			return jaSysGroup.toString();
		}
	}
	
	@RequestMapping(value = "/selectnodemonitor.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getConfigurationName(@RequestBody final String json,
			HttpSession session, Principal principal) {
		
		String text = null;
		String value = null;
		if (true == Main.DEBUG_CONSOLE)
			System.out.println("selectnodemonitor.htm >> input = " + json);
		SystemLogger.logger.info("selectnodemonitor.htm >> input = " + json);
		JsonArray array = new JsonArray();
		try {

			/** Get Parameter array Application List **/
			if (!json.equals("") && json.length() > 0) {
				JsonParser jp = new JsonParser();
				JsonElement jelement = jp.parse(json);
				JsonObject jobject = jelement.getAsJsonObject();
				 text = jobject.get("text").getAsString();
				 value = jobject.get("value").getAsString();
				
				/** Get Parameter array Application List **/
			}
			/** Retrun array Application List **/
		
			// Clear Map When Select New Node Connectin
			if(BeanConfig.beanElement.get(session.getId())!=null){
				BeanConfig.beanElement.get(session.getId()).clear();
			}
			
		

				String host = null;
				String user = null;
				String pass = null;
				for (String[] str : beanConfig.getNodeConnection()) {
					if (str[0].equals(text)&&str[1].equals(value)) {
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
			String[] listStat = null;
			listStat =	commandEx.getListStatEquinox(text, beanConfig.getPathStatEquinox()+"/", host, user, pass, session.getId());

				for (String string : listStat) {
					JsonObject object = new JsonObject();
					object.addProperty("text", string);
					object.addProperty("value", string);
					array.add(object);
				}

				/** Retrun array Application List **/
			
			if (true == Main.DEBUG_CONSOLE)
				System.out.println("selectnodemonitor" + array.toString());
			SystemLogger.logger.info("selectnodemonitor" + array.toString());

			return array.toString();

		} catch (Exception e) {
			System.out
					.println("==============  ERROR in selectnodemonitor  ===================");
			SystemLogger.logger.error("==============  ERROR in selectnodemonitor  ===================");
			return "[]";
		}
	}

	// Get Value Stat Equinox
	
	@RequestMapping(value = "/getstatequinox.htm", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getConfigutionDetail(@RequestBody final String json,
			HttpSession session, Principal principal) {
		// input pattern seesion_ID+user+nameStat
		String  text = null;
		String value = null;
	

		if (true == Main.DEBUG_CONSOLE)
			System.out.println("Input getconfigutiondetail" + json);
			SystemLogger.logger.info("Input getconfigutiondetail");
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
			
			NameStatActive.put(session.getId(), value);
			
			// Start Connect Get Stat
			String host=(String) session.getAttribute("host");
			String user=(String) session.getAttribute("user");
			String pass=(String) session.getAttribute("pass");
			String line ="150";
			
			ConnectGetStat connection = new ConnectGetStat(host,user,pass,line,beanConfig.getPathStatEquinox(),text,beanConfig.getStatInterval(),session.getId());	
			BeanStatEquinoxAllType beanStat=connection.run();
			
			JsonObject jsStatAll = new JsonObject();
			//Add Time
			jsStatAll.addProperty("time", beanStat.getTime());
			//Add Internal Stat 
			
			JsonArray jsArInternalStat = new JsonArray();
			for (String [] arStr : beanStat.getSystemInternalStat()) {
				JsonObject jsvalue = new JsonObject();
				jsvalue.addProperty(arStr[0],arStr[1] );
				jsArInternalStat.add(jsvalue);
				
			}
			jsStatAll.add("internalstat", jsArInternalStat);
			//Add Measurement Stat
			JsonArray jsArMeasurement = new JsonArray();
			
			for (String [] arStr : beanStat.getSystemMeasurement()) {
				JsonObject jsvalue = new JsonObject();
				jsvalue.addProperty(arStr[0],arStr[1] );
				jsArMeasurement.add(jsvalue);
			}
			jsStatAll.add("measurement", jsArMeasurement);
			//Add MeasurementValue Stat
			JsonArray jsArMeasurementvalue = new JsonArray();
			
			for (String [] arStr : beanStat.getSystemMeasurementvalue()) {
				JsonObject jsvalue = new JsonObject();
				jsvalue.addProperty(arStr[0],arStr[1] );
				jsArMeasurementvalue.add(jsvalue);
			}
			jsStatAll.add("measurementvalue", jsArMeasurementvalue);
			//Add Accumulating Stat
			
			JsonArray jsArAcculating = new JsonArray();
			for (String [] arStr : beanStat.getAccumulatingCounters()) {
				JsonObject jsvalue = new JsonObject();
				jsvalue.addProperty(arStr[0],arStr[1] );
				jsArAcculating.add(jsvalue);
			}
			jsStatAll.add("acculating", jsArAcculating);
			
		
			
			System.out.println("Output getstatequinox.htm >> "+jsStatAll.toString());
			SystemLogger.logger.info("Output getstatequinox.htm >> "+jsStatAll.toString());	
			
			return jsStatAll.toString();
		} catch (Exception e) {
			System.out
					.println("==============  ERROR in getstatequinox  ===================");
			SystemLogger.logger.error("==============  ERROR in getstatequinox  ==================="+e);	
			System.err.println(e);
			return "[]";
		}

	}
	
	
}

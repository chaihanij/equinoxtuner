package com.ais.equinox.tuner.app.bean;

import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Element;

public class BeanConfig {
	public static HashMap<String, HashMap< String, Element> > beanElement = new HashMap<String, HashMap<String,Element>>();
	public static BeanAppConfig beanConfigApp = new BeanAppConfig();
	public static HashMap<String,HashMap< String, HashMap<String, Element>>> beanSave = new HashMap<String, HashMap<String,HashMap<String,Element>>>();
//	public static HashMap<String,BeanStatEquinoxAllType> StatEquinoxAllType = new HashMap<String,BeanStatEquinoxAllType>();
	

}

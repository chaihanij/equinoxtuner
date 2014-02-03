package com.ais.equinox.tuner.app.bean;

import java.util.HashMap;

import org.dom4j.Element;

public class BeanRecovery {
	private  HashMap< String, HashMap<String, Element>> beanRecovery = new HashMap<String,HashMap<String, Element>>();

	public HashMap< String, HashMap<String, Element>> getBeanRecovery() {
		return beanRecovery;
	}

	public void setBeanRecovery(HashMap< String, HashMap<String, Element>> beanRecovery) {
		this.beanRecovery = beanRecovery;
	}	
}

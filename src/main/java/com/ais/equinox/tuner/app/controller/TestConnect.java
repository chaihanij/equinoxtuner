package com.ais.equinox.tuner.app.controller;

import com.ais.equinox.tuner.app.model.CommandShell;

public class TestConnect {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "10.252.192.15";
		String user = "root";
		String pass = "$And@ais;";
		
		String [] cmdSet = {"eqx TJ forcestop","@wait 1000","@match .*y/n.*","@ends (y/n)","y","eqx TJ start","exit"};		
		String [] cmdSet1 = {"ping localhost"};		
		System.out.println("TestConnect");
		CommandShell commandShell = new CommandShell("Test",host, user, pass, cmdSet);
		commandShell.startSending();
	}

}


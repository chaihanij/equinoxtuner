package com.ais.equinox.tuner.app.model;

import java.util.Stack;

import com.ais.equinox.tuner.app.bean.BeanStatEquinoxAllType;

public class ConnectGetStat {
	private String host;
	private String user;
	private String pass;
	private String line;
	private String path;
	private String fileName;
	private String sessionId;
	
	
	public ConnectGetStat(String host,String user,String pass,String line,String path,String fileName,String interval,String sessionId) {
		this.host=host;
		this.user=user;
		this.pass=pass;
		this.line=line;
		this.fileName=fileName;
		this.path=path;
		this.sessionId=sessionId;
	}

	
	public BeanStatEquinoxAllType run() {
			
		String command = "tail -n "+line+" "+path+"/"+fileName;
		System.out.println(command);
		ConnectNodeExec connect = new ConnectNodeExec(host,user,pass);
		Stack<String> stackStr = connect.connectgetStat(command,sessionId);
		ManageStatType manage = new ManageStatType("1234");
		BeanStatEquinoxAllType beanStat =manage.PorcessStat(stackStr);
		return beanStat;		
		
	}
	
}

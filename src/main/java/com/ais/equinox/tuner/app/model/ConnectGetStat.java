package com.ais.equinox.tuner.app.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import com.ais.equinox.tuner.app.bean.BeanBlockStat;
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
		ManageStatType manage = new ManageStatType(sessionId);
		BeanStatEquinoxAllType beanStat =manage.PorcessStat(stackStr);
		return beanStat;		
		
	}
	
	public ArrayList<BeanBlockStat> run2(String  time) {
		
		
		String command = "tail -n "+line+" "+path+"/"+fileName;
		System.out.println(command);
		ConnectNodeExec connect = new ConnectNodeExec(host,user,pass);
		Stack<String> stackStr = connect.connectgetStat(command,sessionId);
		GetStatRetroact  getStatRetroact =  new GetStatRetroact("1234");
		String[] nLine = getStatRetroact.checkSizeBlock(stackStr);
		
		//Time Perser 
		long [] longTime = new long[2];
		System.out.println(nLine[0]);
		System.out.println(nLine[1]);
		System.out.println(time);
		System.out.println(nLine[2]);
		
	     SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
	     try {
				Date startDate = df.parse(time);
				longTime[0]=startDate.getTime();
				
				Date endDate = df.parse(nLine[2]);
				longTime[1]=endDate.getTime();
				
				System.out.println(startDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			System.out.println("Parser Time ERROR");
			}
	     long result =  longTime[1]-longTime[0];
	     String strInterval[] = nLine[1].split("=");
	     long interval =Integer.parseInt(strInterval[1]);
	     long sec = result / 1000 ;
	     long point = sec/interval;
	     
	    
		
		int sizeBlock = Integer.parseInt(nLine[0]);
		int points = (int) (point+1);
		int size = points*sizeBlock;
		String sizeContent = ""+size;
		System.out.println("Size Content : "+sizeContent);
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++ New Connection ++++++++++++++++++++++++++++++++++++++++++++++=");
		
		// New Connect get statRetroact
		System.out.println("Point :"+point+"  SizeBlock : "+sizeBlock+"  Size : "+sizeContent);
		String nCommand =  "tail -n "+sizeContent+" "+path+"/"+fileName;
		
		System.out.println("Show Command :"+nCommand );
		ConnectNodeExec nConnect = new ConnectNodeExec(host,user,pass);
		Stack<String> nStackStr = connect.connectgetStat(nCommand,sessionId);
		//Process 
		ArrayList<BeanBlockStat> dataAllBlock = new ArrayList<BeanBlockStat>();
		dataAllBlock =getStatRetroact.processStat(nStackStr,time);
		
		
		return dataAllBlock;		
		
	}
	
}

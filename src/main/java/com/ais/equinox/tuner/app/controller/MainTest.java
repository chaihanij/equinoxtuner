package com.ais.equinox.tuner.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import com.ais.equinox.tuner.app.bean.BeanAppConfig;
import com.ais.equinox.tuner.app.bean.BeanConfig;
import com.ais.equinox.tuner.app.bean.BeanStatEquinoxAllType;
import com.ais.equinox.tuner.app.model.CommandShell;
import com.ais.equinox.tuner.app.model.ConnectNodeExec;
import com.ais.equinox.tuner.app.model.ManageStatType;
import com.ais.equinox.tuner.app.model.SCPupload;

public class MainTest {

	public static void UploadSCP(){
	String[] data = {"smf.E00.x.0",
    "smf.E03.0.0",
    "smf.E04.0.0",
    "smf.E06.0.0",
    "smf.E11.0.0",
    "smf.E11.0.1",
    "smf.ES00.0",
    "smf.ES03.DS2A.0",
    "smf.ES05.OFF.0",
    "smf.ES06.ocf_G3.0",
    "smf.ES06.ocf_G3.1",
    "smf.ES06.ocf_G3.2",
    "smf.ES06.ocf_G3.3",
    "smf.ES06.ocf_cwdc.0",
    "smf.ES06.ocf_cwdc.1",
    "smf.ES06.ocf_cwdc.2",
    "smf.ES06.ocf_cwdc.3",
    "smf.ES06.ocf_suk.0",
    "smf.ES06.ocf_suk.1",
    "smf.ES06.ocf_suk.2",
    "smf.ES06.ocf_suk.3"
	/*"OCF5.ES00.x.0_20130924.stat",
	"TJ.E00.x.0_20120917.stat",
	"TJ.E03.0.0_20120917.stat",
	"TJ.E04.x.0_20120917.stat",
	"TJ.E11.0.0_20120917.stat",
	"TJ.E11.0.1_20120917.stat",
	"TJ.ES03.ds2afe.0_20120917.stat",
	"TJ.ES03.ds2afe.1_20120917.stat",
	"TJ.ES04.httpclient.0_20120917.stat",
	"TJ.ES05.httpserver.0_20120917.stat"
	*/
	
	
	};
for (String string : data) {
String pathLocalFile = "C:\\Users\\Dee\\Desktop\\configTest\\"+string;
String remoteFile = "/opt/equinox/conf/"+string;
String host = "10.252.192.15";
String user = "toro";
String pass = "toro";
SCPupload scp = new SCPupload();
scp.uploadFile(pathLocalFile, remoteFile, host, user, pass);
}
	}
	public static void main(String[] args) {
		UploadSCP();
		
//	}
//		String host = "10.252.192.15";
//		String user = "root";
//		String pass = "$And@ais;";
		
//		String [] cmdSet = {"eqx TJ forcestop","@matches .*(y/n).*","@end y"};		
//		CommandShell commandShell = new CommandShell("Test",host, user, pass, cmdSet);
		
//		//Set Config
//		ParserConfigApp parser = new ParserConfigApp();
//		BeanAppConfig beanconfigapp =parser.parser();
//		BeanConfig.beanConfigApp=beanconfigapp;
//		
//		String testCommand = " tail -n 150 /home/dee/statEquinox/OCF5.ES00.x.0_20130924.stat";
//		ConnectNodeExec connect = new ConnectNodeExec(host, user, pass);
//		Stack<String> stackStr = connect.connectgetStat(testCommand,"1234");
//		ManageStatType manage = new ManageStatType("1234");
//		BeanStatEquinoxAllType beanStat =manage.PorcessStat(stackStr);
//		System.out.println("==============================================================");
//		System.out.println("++++++++++++++++++++++ Internal ++++++++++++++++++++++++++++");
//		for (String[] strArray : beanStat.getSystemInternalStat()) {
//			System.out.println("StatName : "+strArray[0]+" Value : "+strArray[1]);
//		}
//		System.out.println("+++++++++++++++++++++AccumulatingCounters+++++++++++++++++++++++++++++");
//		for (String[] strArray : beanStat.getAccumulatingCounters()) {
//			System.out.println("StatName : "+strArray[0]+" Value : "+strArray[1]);
//		}
//		System.out.println("++++++++++++++++++++++SystemMeasurement+++++++++++++++++++++++++++++");
//		for (String[] strArray : beanStat.getSystemMeasurement()) {
//			System.out.println("StatName : "+strArray[0]+" Value : "+strArray[1]);
//		}
//		System.out.println("++++++++++++++++++++++END+++++++++++++++++++++++++++++");
		
		
		
		
	}
	
	
	
	public static void calculator(){
		String host="10.252.192.15";
		String user="root";
		String password="$And@ais;";
		
	       ConnectNodeExec concentNode = new ConnectNodeExec(host, user, password);
	       String testCommand = " tail -n 100 /opt/equinox/conf/configTest/OCF5.ES00.x.0_20130924.stat";
	     Stack<String> stackStr = new Stack<String>();
	     stackStr=  concentNode.connectgetStat(testCommand,"1234");
	       
	     System.out.println("+++++++++++++++++++++++ Print Stack +++++++++++++++++++++++++++");
	     ArrayList<String> arraySystemInternal = new ArrayList<String>();
	     ArrayList<String> arrayMeasurement = new ArrayList<String>();
	     ArrayList<String> arrayAccumulating = new ArrayList<String>();
	     ArrayList<String> time = new ArrayList<String>();
	     ArrayList<String> allStat = new ArrayList<String>();
	     
	     
	     int count =1;
	     while (!stackStr.empty()) {
	    String str = stackStr.pop();
		System.out.println(str);
		if (str.matches("^[a-z|A-Z].*")) {
			allStat.add(str);
		}
		else if (str.matches("^[0-9].*")&&count<=2) {
			time.add(str);
			if(count==2){break;}
			count++;
		}
		
		
		}
		
	     System.out.println("+++++++++++++++++++++++ Print Array +++++++++++++++++++++++++++");
	     System.out.println(time.toString());
	     //Parser Time 
	     long [] longTime = new long[2];
	     SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
	     int i=0;
	     for (String string : time) {
	    	 try {
				Date startDate = df.parse(string);
				longTime[i]=startDate.getTime();
				System.out.println(startDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			System.out.println("Parser Time ERROR");
			}
	    	 i++;
		}
	     System.out.println("+++++++++++++++++++++++ Print Result +++++++++++++++++++++++++++");
	
	     long result =  longTime[0]-longTime[1];
	   System.out.println(result);
	   System.out.println("+++++++++++++++++++++++ Print Show SumTime +++++++++++++++++++++++++++");
	
	 
	   //// Get msec from each, and subtract.
	   long ses = result / 1000 ; 
	   long diffSeconds = result / 1000 % 60;  
	   long diffMinutes = result / (60 * 1000) % 60;         
	   long diffHours = result / (60 * 60 * 1000);                      
	   System.out.println("Time in seconds: " + ses + " seconds.");  
	   System.out.println("Time in seconds: " + diffSeconds + " seconds.");         
	   System.out.println("Time in minutes: " + diffMinutes + " minutes.");         
	   System.out.println("Time in hours: " + diffHours + " hours."); 
	   
		
		
	}

}

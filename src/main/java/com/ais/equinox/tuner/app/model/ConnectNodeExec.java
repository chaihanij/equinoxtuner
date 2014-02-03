package com.ais.equinox.tuner.app.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ConnectNodeExec {
	private String host;
	private String user;
	private String password;
	private String command;
	private HashMap<String, String> commandset;
	// status code
		
		public static final int RUNNING = 1;
		public static final int ERROR = 0;
		public static final int FINISHED = 2;
		private int status;
	

	public ConnectNodeExec(String host, String user, String password) {
		this.host = host;
		this.user = user;
		this.password = password;

	}

	public String connect(String command) {
		this.command = command;
		String str = "";
		try {
			this.setStatus(RUNNING);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];

			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					str += new String(tmp, 0, i);
					System.out.println(str);

				}

				if (channel.isClosed()) {

					break;
				}
				try {

				} catch (Exception ee) {
				}
			}
			channel.disconnect();
			session.disconnect();
			this.setStatus(FINISHED);
			System.out.println("DONE");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Connection ERROE!!");
			this.setStatus(ERROR);
		}
		return str;

	}

	public HashMap<String, String> connect1(HashMap<String, String> commands) {
		this.commandset = commands;

		HashMap<String, String> outputs = new HashMap<String, String>();

		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");

			for (Entry<String, String> item : commandset.entrySet()) {
				this.setStatus(RUNNING);
				Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(item.getValue());
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[1024];
				String str = "";
				InputStream inNoBOM =checkForUtf8BOMAndDiscardIfAny(in);
				while (true) {
					while (inNoBOM.available() > 0) {
						int i = inNoBOM.read(tmp, 0, 1024);
						if (i < 0)
							break;
						str += new String(tmp, 0, i);

					}

					if (channel.isClosed()) {

						break;
					}
					try {

					} catch (Exception ee) {
					}
				}
				
				outputs.put(item.getKey(), str);
				channel.disconnect();
			}

			session.disconnect();
			this.setStatus(FINISHED);
			System.out.println("DONE");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Connection ERROE!!");
			this.setStatus(ERROR);
		}
		return outputs;

	}
	
	public Stack<String> connectgetStat(String command,String sessionId){
		this.command = command;
		String str = "";
		Stack<String> stackStr = new Stack<String>();
		try {
			this.setStatus(RUNNING);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");
			//Loop recusive get StatEquinox
//			while(true){
				
			str = "";
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					str += new String(tmp, 0, i);
					System.out.println(str);

				}

				if (channel.isClosed()) {

					break;
				}
				try {

				} catch (Exception ee) {
					System.err.println("Connection ERROE!!");
					this.setStatus(ERROR);
				}
			}
			// Split String for Add to Array Keep in Stack<String>
			String[] array = str.split("\n");
			ArrayList<String[]> valueStat = new ArrayList<String[]>();
			String time=null;
			for (String item : array) {
				stackStr.push(item);
						
			}
			//Add value to Map
//			BeanStatEquinox beanStat = new BeanStatEquinox();
//			beanStat.setTime(time);
//			beanStat.setStatEquinox(valueStat);
//			BeanConfig.beanStatEquinox.put(sessionId, beanStat);
//			System.out.println("SetValue To Bean Sucess");
			
//			if(BeanConfig.statusReadStat.get(sessionId))
//			{
//				break;
//			}
//				channel.disconnect();
//				int cooldown = Integer.parseInt(sleep);
//				Thread.sleep(cooldown);
//				
//			}
			
			
			session.disconnect();
			this.setStatus(FINISHED);
			System.out.println("DONE");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Connection ERROE!!");
			this.setStatus(ERROR);
		}
		return stackStr;
		

	}
	private static InputStream checkForUtf8BOMAndDiscardIfAny(InputStream inputStream) throws IOException {
	    PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
	    byte[] bom = new byte[3];
	    if (pushbackInputStream.read(bom) != -1) {
	        if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
	            pushbackInputStream.unread(bom);
	        }
	    }
	    return pushbackInputStream; }
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
		
	
}
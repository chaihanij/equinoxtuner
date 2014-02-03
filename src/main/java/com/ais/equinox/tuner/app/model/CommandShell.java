package com.ais.equinox.tuner.app.model;



import java.util.concurrent.Callable;

import com.ais.equinox.tuner.SystemLogger;


public class CommandShell implements Callable<ConnectNodeShell> {
	protected String[] cmd;
	protected String[] cmdSet;
	protected long[] time;
	protected String[] end;
	protected String[] match;
	protected int nCmd;

	private String workType;
	protected String ip;
	protected String uname;
	protected String pass;
	protected ConnectNodeShell connectNodeShell;

	

	public CommandShell(String workType, String ip, String uname, String pass,
			String[] cmdSet) {
		this.workType = workType;
		this.ip = ip;
		this.uname = uname;
		this.pass = pass;
		this.cmdSet = cmdSet;
		this.nCmd = 0;
		this.time = new long[cmdSet.length];
		this.end = new String[cmdSet.length];
		this.match = new String[cmdSet.length];
		this.cmd = new String[cmdSet.length];
	}



	private void processCommand() {
		System.out.println("Connecting");
		try {			
				while (true) {
					System.out.println(connectNodeShell.mess);
					SystemLogger.logger.info(connectNodeShell.mess);
					if (connectNodeShell.getStatus() == 2 || connectNodeShell.getStatus() == -1) {						
						break;
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return this.workType;
	}

	/* +++++++++++ Send Command1 +++++++++++ */
	public void startSending() {
		int i = 0;

		for (i = 0; i < this.cmdSet.length; i++) {

			String[] tmp = new String[2];
			tmp = this.cmdSet[i].split(" ");

			if (this.cmdSet[i].startsWith("@wait")) {
				this.time[this.nCmd] = Long.parseLong(tmp[1]);
				if ((this.cmdSet[(i + 1)] != null)
						&& (this.cmdSet[(i + 1)].startsWith("@match"))) {

					if ((this.cmd[(i + 2)] != null)
							&& (!this.cmdSet[(i + 2)].startsWith("@end")))
						System.err.println("INPUT ERROR cannot find end");
				} else
					System.err.println("INPUT ERROR cannot find match");

			} else if (this.cmdSet[i].startsWith("@match")) {

				this.match[this.nCmd] = tmp[1];

			} else if (this.cmdSet[i].startsWith("@end")) {

				this.end[this.nCmd] = tmp[1];

			} else {

				this.cmd[this.nCmd] = (this.cmdSet[i] + "\n");
				this.nCmd += 1;
			}
		}

		System.out.println(this.nCmd);
		connectNodeShell = new ConnectNodeShell(ip, uname, pass, cmd, time, end, match, nCmd);
		Thread c = new Thread(connectNodeShell);
		c.start();
	}



	/* +++++Worked on Thread call+++++ */
	public ConnectNodeShell call() throws Exception {

		System.out.println(Thread.currentThread().getName()
				+ " Start. Send Command = " + workType);

		processCommand();
		System.out.println(Thread.currentThread().getName() + " End.");

		return connectNodeShell;
	}
}
package com.ais.equinox.tuner.app.model;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ConnectNodeShell implements Runnable {

	// -------variable
	protected String host;
	protected String uname;
	protected String pass;
	protected String key;
	protected String[] cmd;
	protected long[] time;
	protected String[] end;
	protected int i;
	protected String[] match;
	protected int nCmd;
	protected int progress;
	protected String filename;
	protected String mess;

	// status code
	public static final int NOT_START = 0;
	public static final int RUNNING = 1;
	public static final int ERROR = -1;
	public static final int FINISHED = 2;
	int status;

	public String getHost() {
		return this.host;
	}

	public String getProgress() {
		return "" + this.progress + "/" + this.nCmd;
	}

	public int getCurrentCmd() {
		return this.progress;
	}

	public int getTotalCmd() {
		return this.nCmd;
	}

	public int getStatus() {
		return this.status;
	}

	public String getFile() {
		return this.filename;
	}

	public String getMess() {
		return mess;
	}

	public ConnectNodeShell(String ip, String uname, String pass,
			String[] command, long[] time, String[] end, String[] match, int idx) {

		this.host = ip;
		this.uname = uname;
		this.pass = pass;
		this.cmd = command;
		this.match = match;
		this.progress = 0;
		this.time = time;
		this.end = end;
		this.nCmd = idx;
		this.status = 0;
		this.mess = "";
	}

	public void run() {
		try {
			JSch jsch = new JSch();

			String password1 = this.pass;

			Session session = jsch.getSession(this.uname, this.host, 22);
			session.setConfig("StrictHostKeyChecking", "no");

			session.setPassword(password1);
			session.connect();

			Channel channel = session.openChannel("shell");

			PipedOutputStream commandIO = new PipedOutputStream();
			InputStream sessionInput = new PipedInputStream(commandIO);

			channel.setInputStream(sessionInput);

			InputStream sessionOutput = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			int n = 0;
			System.out.println("length=" + this.cmd.length);

			this.status = 1;
			for (n = 0; n <= this.nCmd; n++) {
				if (n < this.nCmd) {
					if (this.time[n] != 0L) {
						String a = "";

						Thread.sleep(this.time[n]);
						while ((this.i = sessionOutput.read(tmp, 0, tmp.length)) != -1) {
							if ((new String(tmp, 0, this.i)
									.endsWith(this.end[n]))
									|| (new String(tmp, 0, this.i).trim()
											.endsWith(this.end[n]))) {
								a = a + new String(tmp, 0, this.i);
								System.out.print(new String(tmp, 0, this.i));

								break;
							}
							a = a + new String(tmp, 0, this.i);
							System.out.print(new String(tmp, 0, this.i));

						}
						String reg = this.match[n];

						String input = a;
						Pattern p = Pattern.compile(reg, 32);
						Matcher m = p.matcher(input);
						boolean isMatch = m.matches();
						System.out.println("ismatch: " + isMatch);
						if (!isMatch) {
							this.status = -1;

							break;
						}
					} else {
						if (this.end[n] != null) {
							while ((this.i = sessionOutput.read(tmp, 0,
									tmp.length)) != -1) {
								if ((new String(tmp, 0, this.i)
										.endsWith(this.end[n]))
										|| (new String(tmp, 0, this.i).trim()
												.endsWith(this.end[n]))) {
									System.out
											.print(new String(tmp, 0, this.i));

									break;
								}
								System.out.print(new String(tmp, 0, this.i));

							}
						}
						while ((this.i = sessionOutput.read(tmp, 0, tmp.length)) != -1) {
							if ((new String(tmp, 0, this.i).trim()
									.endsWith("#"))
									|| (new String(tmp, 0, this.i).trim()
											.endsWith("$"))) {
								System.out.print("**"
										+ new String(tmp, 0, this.i));

								break;
							}
							System.out.print(new String(tmp, 0, this.i));

						}
					}
				}
				if (n == this.nCmd) {
					while ((this.i = sessionOutput.read(tmp, 0, tmp.length)) != -1) {
						if ((new String(tmp, 0, this.i).trim().endsWith("#"))
								|| (new String(tmp, 0, this.i).trim()
										.endsWith("]$"))) {
							System.out.print("**" + new String(tmp, 0, this.i));

							break;
						}
						System.out.print(new String(tmp, 0, this.i));

					}
				}
				if (n < this.nCmd) {
					commandIO.write(this.cmd[n].getBytes());
					commandIO.flush();
					this.progress += 1;
				}
			}
			System.out.println("\nStatus : " + this.progress + "/" + this.nCmd);

			commandIO.close();
			sessionInput.close();

			channel.disconnect();
			session.disconnect();
			if (this.progress == this.nCmd) {
				this.status = 2;
			}
		} catch (Exception e) {
			this.status = -1;
			e.printStackTrace();
		}

	}
}
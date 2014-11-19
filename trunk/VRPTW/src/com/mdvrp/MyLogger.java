package com.mdvrp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class MyFormatter extends Formatter {
	//
	// Create a DateFormat to format the logger timestamp.
	//
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append("[").append(df.format(new Date(record.getMillis()))).append("] - ");
		builder.append(record.getLevel()).append(" --> ");
		builder.append(record.getSourceClassName()).append(".");
		builder.append(record.getSourceMethodName()).append(": ");
		builder.append(formatMessage(record));
		builder.append("\n");
		return builder.toString();
	}

	public String getHead(Handler h) {
		return super.getHead(h);
	}

	public String getTail(Handler h) {
		return super.getTail(h);
	}
}

public class MyLogger {

	private static final DateFormat df = new SimpleDateFormat("ddMMyyyy");
	private static final String LOG_FILE = "VRPTW_";
	private Logger log = null;
	private static FileHandler handler = null;

	public MyLogger(String name) {

		log = Logger.getLogger(name);
		// Construct a default FileHandler.
		Handler fh = null;
		fh = getFileHandler();
		// append is true
		fh.setFormatter(new MyFormatter()); // Set the log format
		// Add the FileHander to the logger.
		log.addHandler(fh);
		// Remove the ConsoleHandler from the logger
		log.setUseParentHandlers(false);
		// Set the logger level to produce logs at this level and above.
		log.setLevel(Level.FINE);

	}

	private static FileHandler getFileHandler() {
		if (handler == null) {
			try {
				String date = df.format(new Date());
				handler = new FileHandler(LOG_FILE + date + ".log", true);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return handler;
	}

	public void err(String sender, String fun, String msg) {
		if (msg != null) {
			int len = msg.length();
			StringBuffer s = new StringBuffer("x");
			for (int i = 0; i < len + 9; i++)
				s.append("x");
			log.logp(Level.SEVERE, sender, fun, "\n" + s.toString() + "\n" + "x    " + msg + "    x\n" + s.toString());
		}
	}

	public void warning(String sender, String fun, String msg) {
		if (msg != null) {
			int len = msg.length();
			StringBuffer s = new StringBuffer("w");
			for (int i = 0; i < len + 9; i++)
				s.append("w");
			log.logp(Level.SEVERE, sender, fun, "\n" + s.toString() + "\n" + "w    " + msg + "    w\n" + s.toString());
		}
	}

	public void info(String sender, String fun, String msg) {
		if (msg != null) {
			log.logp(Level.INFO, sender, fun, msg);
		}
	}

}

package com.irobuddy.webcontrol.utils;

import android.util.Log;

public class Logger {
	private static Logger logger = new Logger();

	private boolean isDebug = true;
	
	public static Logger getDefault() {
		return logger;
	}

	public boolean isDebug() {
		return isDebug;
	}
	
	public void error(String tag, String message, Throwable e) {
		Log.e(tag, message, e);
	}

	public void debug(String tag, String message) {
		if (isDebug) {
			Log.d(tag, Thread.currentThread().getId()+": "+message);
		}
	}

	public void info(String tag, String message) {
		Log.i(tag, message);
	}

	public void trace(String tag, String message) {
		if (isDebug) {
			Log.d(tag, message);
		}
	}

}

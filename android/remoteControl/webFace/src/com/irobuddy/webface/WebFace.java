package com.irobuddy.webface;

import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;
import org.webbitserver.netty.NettyWebServer;


import com.irobuddy.webface.WebSocketsHandler;
import com.irobuddy.webface.utils.Logger;
import com.irobuddy.webface.utils.Utils;

import android.content.Context;
import android.os.Environment;

public class WebFace {
	final static String TAG = "WebFace";
	
	//Singleton mode
	private static WebFace mInstance;
	
	//Instances variables
	private int mWebServerListenPort;
	private String mWebServerRootDir;
	private boolean mIsRunning;
	private Thread mControlServerThread;
	private static String mWebSocketURL = "/chat";
	public static Context mContext;
	/*
	 * irobuddy remoteWEBControl public API
	 */
	public static WebFace getInstance(Context context){
		return mInstance == null ?
				(mInstance = new WebFace(context)) :
					mInstance;
	}
	
	public static Context getGlobalContext(){
		return mContext;
	}
	
	public void setRunningPort(int port){
		mWebServerListenPort = port;
	}
	
	public boolean isRunning(){
		return mIsRunning;
	}
	
	public void run(){
		mControlServerThread.start();
		mIsRunning = true;
	}
	
	public void stop(){
		mControlServerThread.interrupt();
		mIsRunning = false;
	}
	
	
	/*
	 * irobuddy remoteWEBControl Internal methods
	 */
	
	private WebFace(Context context){
		//set default configuration of local WEB server
		mWebServerListenPort = 8080;
		mWebServerRootDir = Environment.getExternalStorageDirectory() + "/irobuddy_web";
		mIsRunning = false;
		mContext = context;
		
		//Copy all files under assents/web to /sdcard/irobuddy_web.
		//Utils.CopyAssets(mContext, mWebServerRootDir);
		
		//Copy all files under res/raw to /sdcard/irobuddy_web
		Utils.clearResource(mWebServerRootDir);
		Utils.buildResource(mContext, mWebServerRootDir);
		
		//Create and start WebFaceAN
		WebFaceAN.getInstance().start();
		  
		//Start WEB control server.
		mControlServerThread = new WebControlServerThread();
	}

	private class WebControlServerThread extends Thread{
		
		  @Override
		  public void run() {
			
			  startWebControlServer();
		  }
		  
		 /*
		  * Start a HTTP and Websocket Server
		  */
		  public void startWebControlServer (){
		    	WebServer server = new NettyWebServer(mWebServerListenPort);
		    	
		        server.add(new LoggingHandler(new SimpleLogSink()));
		        
		        server.add(new StaticFileHandler(mWebServerRootDir));
		        
		        server.add(mWebSocketURL, new WebSocketsHandler());
		        
		        server.start();
		        
		        Logger.getDefault().debug(TAG, "WEBControlServer start.");
		    }
	  }
}

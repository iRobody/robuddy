package com.irobuddy.webcontrol;

import android.content.Intent;

public class RobotsMsg {

	private String mCmdMsg;
	private static String packageName = "com.irobuddy";
	private static String className = "Supervisor";
	private static String action="com.irobuddy.robot.msg";
	
	public RobotsMsg(String msg){
		mCmdMsg = msg;
	}
	
	public void Dispatch(){
		Intent cmdIntent = new Intent(action);
		cmdIntent.setClassName(packageName, className);
		cmdIntent.putExtra("MSG_TYPE", mCmdMsg);
		WebControlServer.getGlobalContext().startActivity(cmdIntent);	
	}
}

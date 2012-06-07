package com.irobuddy.webface;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;
import com.irobuddy.move.MoveEvent;
import com.irobuddy.move.MoveSignal;

public class RobotsCmdMsg {

	private String mCmdMsg;
	private boolean mIsValid;
	
	public RobotsCmdMsg(String jason){
		mCmdMsg = jason;
		mIsValid = true;
	}
	
	/*
	 * Temporary solution, TODO: To build MoveEvent by calling MoveEvent.build($JASON-string)
	 */
	public MxEvent createMxEvent()
	{
		//TODO: call EventBuilder.build($jason-string)
		
		MoveEvent e = new MoveEvent();
		e.type = MxEvent.EVENT_TYPE_RELAY;
		e.channel = RobodyChannel.EVENT_CH_MOVE_C;
		e.sig = MoveSignal.MV_SIG_RAW;
		String[] speeds = mCmdMsg.split(" ");
		
		int leftWheelSpeed = Integer.parseInt(speeds[0]);
		int rightWheelSpeed = Integer.parseInt(speeds[1]);
		e.length = 4;
		
		
		e.lF = (leftWheelSpeed > 0) ? true:false;
		e.lS = (byte) Math.abs(leftWheelSpeed);
		e.rF = (rightWheelSpeed > 0) ? true:false;
		e.rS = (byte) Math.abs(rightWheelSpeed);
		
		return e;
	}
	
	public boolean isValid(){
		//TODO: Check whether the input jason string is valid.
		
		return mIsValid;
	}
}

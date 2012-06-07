package com.irobuddy.webface;

import com.irobuddy.matrix.ActiveNode;
import com.irobuddy.matrix.MxEvent;
import com.irobuddy.matrix.MxState;

public class WebFaceAN extends ActiveNode {
	final static String TAG = "WebFaceAN";
	private static WebFaceAN mInstance;
	
	public static WebFaceAN getInstance(){
		return mInstance == null ?
				(mInstance = new WebFaceAN()) :
					mInstance;
	}
	
	public void start( ) {
		//subscribe();
		start( TAG, false, initial, null);
	}
	
	public void stop( ) {
		super.stop();
	}
	
	
	//State machine ---------------------------------------------
	public final MxState initial = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			return 	run;
		}
	};
		
	public MxState run = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			//TODO : handle subscriber msg
			return ActiveNode.QSTATE_HANDLED;
		}
	};
}

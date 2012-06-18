package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveRawEvent extends MxEvent {
	public final static int LENGTH = 4;
	
	public final static String MOVE_EVENT_LEFT_FORWARD_NAME = "leftForward";
	public final static String MOVE_EVENT_LEFT_SPEED_NAME = "leftSpeed";
	public final static String MOVE_EVENT_RIGHT_FORWARD_NAME = "rightForward";
	public final static String MOVE_EVENT_RIGHT_SPEED_NAME = "rightSpeed";
	
	public boolean lF;
	public byte lS;
	public boolean rF;
	public byte rS;
	
	public static MoveRawEvent build( byte[] rawEvent) {
		if( rawEvent.length != LENGTH + MxEvent.EVENT_HEADER_SIZE)
			return null;
		MoveRawEvent e = new MoveRawEvent();
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		e.channel = RobodyChannel.EVENT_CH_MOVE_C;
		e.signal = MoveSignal.MV_SIG_RAW;
		e.length = LENGTH;
		e.lF = (rawEvent[4]==1);
		e.lS = rawEvent[5];
		e.rF = (rawEvent[6]==1);
		e.rS = rawEvent[7];
		return e;
	}
	
	public static MoveRawEvent build( JSONObject jsonEvent) {
		MoveRawEvent e = new MoveRawEvent();
		try {
			e.type = (byte)(jsonEvent.getInt( MxEvent.EVENT_TYPE_NAME));
			e.channel = RobodyChannel.EVENT_CH_MOVE_C;
			e.signal = MoveSignal.MV_SIG_RAW;
			e.length = LENGTH;
			e.lF = jsonEvent.getBoolean( MOVE_EVENT_LEFT_FORWARD_NAME);
			e.lS = (byte)(jsonEvent.getInt( MOVE_EVENT_LEFT_SPEED_NAME));
			e.rF = jsonEvent.getBoolean( MOVE_EVENT_RIGHT_FORWARD_NAME);
			e.rS = (byte)(jsonEvent.getInt( MOVE_EVENT_RIGHT_SPEED_NAME));
		} catch (JSONException error) {
			return null;
		}
		return e;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();

		rawEvent[4] = lF?(byte)1:0;
		rawEvent[5] = lS;
		rawEvent[6] = rF?(byte)1:0;
		rawEvent[7] = rS;
		return rawEvent;
	}
}

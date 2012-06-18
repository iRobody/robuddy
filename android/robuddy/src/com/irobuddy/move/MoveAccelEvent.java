package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveAccelEvent extends MxEvent {
	public final static String MOVEACCEL_EVENT_ACCEL_NAME = "accel";
	public final static String MOVEACCEL_EVENT_SPEED_NAME = "speed";
	
	public final static int LENGTH = 2;
	
	public boolean accel;
	public byte speed;
	
	public static MoveAccelEvent build( byte[] rawEvent) {
		MoveAccelEvent e = new MoveAccelEvent((rawEvent[4]==1), rawEvent[5]);
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		return e;
	}
	
	public static MoveAccelEvent build( JSONObject jsonEvent) {
		MoveAccelEvent e = new MoveAccelEvent(true, (byte)0);
		try {
			e.type = (byte)(jsonEvent.getInt( MxEvent.EVENT_TYPE_NAME));
			e.accel = jsonEvent.getBoolean( MOVEACCEL_EVENT_ACCEL_NAME);
			e.speed = (byte)(jsonEvent.getInt( MOVEACCEL_EVENT_SPEED_NAME));
		} catch (JSONException error) {
			return null;
		}
		return e;
	}
	
	public MoveAccelEvent( boolean accel, byte speed) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_MOVE_C;
		signal = MoveSignal.MV_SIG_ACCEL;
		length = LENGTH;
		this.accel = accel;
		this.speed = speed;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();

		rawEvent[4] = accel?(byte)1:0;
		rawEvent[5] = speed;
		return rawEvent;
	}
}

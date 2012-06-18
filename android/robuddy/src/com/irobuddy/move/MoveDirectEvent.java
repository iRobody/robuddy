package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveDirectEvent extends MxEvent {
	public final static String MOVEDIRECT_EVENT_FORWARD_NAME = "forward";
	public final static String MOVEDIRECT_EVENT_SPEED_NAME = "speed";
	
	public final static int LENGTH = 2;
	
	public boolean forward;
	public byte speed;
	
	public static MoveDirectEvent build( byte[] rawEvent) {
		MoveDirectEvent e = new MoveDirectEvent((rawEvent[4]==1), rawEvent[5]);
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		return e;
	}
	
	public static MoveDirectEvent build( JSONObject jsonEvent) {
		MoveDirectEvent e = new MoveDirectEvent(true, (byte)0);
		try {
			e.type = (byte)(jsonEvent.getInt( MxEvent.EVENT_TYPE_NAME));
			e.forward = jsonEvent.getBoolean( MOVEDIRECT_EVENT_FORWARD_NAME);
			e.speed = (byte)(jsonEvent.getInt( MOVEDIRECT_EVENT_SPEED_NAME));
		} catch (JSONException error) {
			return null;
		}
		return e;
	}
	
	public MoveDirectEvent( boolean accel, byte speed) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_MOVE_C;
		signal = MoveSignal.MV_SIG_ACCEL;
		length = LENGTH;
		this.forward = accel;
		this.speed = speed;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();

		rawEvent[4] = forward?(byte)1:0;
		rawEvent[5] = speed;
		return rawEvent;
	}
}

package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveAccelEvent extends MxEvent {
	public final static String MOVEACCEL_EVENT_SPEED_NAME = "speed";
	
	public final static int LENGTH = 2;
	public byte speed;
	
	public MoveAccelEvent( byte speed) {
		super( RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_ACCEL);
		length = LENGTH;
		this.speed = speed;
	}
	
	public MoveAccelEvent( byte[] byteEvent) {
		super( byteEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_ACCEL);
		length = LENGTH;
		speed = (byte)(byteEvent[MxEvent.EVENT_HEADER_SIZE]);
	}
	
	public MoveAccelEvent( JSONObject jsonEvent) throws JSONException {
		super( jsonEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_ACCEL);
		length = LENGTH;
		speed = (byte)(jsonEvent.getInt( MOVEACCEL_EVENT_SPEED_NAME));
	}
	
	public static MoveAccelEvent build( byte[] byteEvent) {
		MoveAccelEvent e = new MoveAccelEvent(byteEvent);
		return e;
	}
	
	public static MoveAccelEvent build( JSONObject jsonEvent) {
		try {
			MoveAccelEvent e = new MoveAccelEvent( jsonEvent);
			return e;
		} catch (JSONException error) {
			return null;
		}
	}
	
	public byte[] dump() {
		byte[] byteEvent = super.initDump();
		byteEvent[MxEvent.EVENT_HEADER_SIZE] = speed;
		return byteEvent;
	}
}

package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveBrakeEvent extends MxEvent {
	public final static String MOVEBRAKE_EVENT_LEVEL_NAME = "level"; //0-1
	
	public final static int LENGTH = 1;
	public byte level;

	public MoveBrakeEvent( byte level) {
		super( RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_BRAKE);
		length = LENGTH;
		this.level = level;
	}
	
	public MoveBrakeEvent( byte[] byteEvent) {
		super(byteEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_BRAKE);
		length = LENGTH;
		level = byteEvent[MxEvent.EVENT_HEADER_SIZE];
	}
	
	public MoveBrakeEvent( JSONObject jsonEvent) throws JSONException {
		super( jsonEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_BRAKE);
		length = LENGTH;
		level = (byte)jsonEvent.getInt( MOVEBRAKE_EVENT_LEVEL_NAME);
	}
	
	public static MoveBrakeEvent build( byte[] byteEvent) {
		MoveBrakeEvent e = new MoveBrakeEvent(byteEvent);
		e.level = byteEvent[MxEvent.EVENT_HEADER_SIZE];
		return e;
	}
	
	public static MoveBrakeEvent build( JSONObject jsonEvent) {
		try {
			MoveBrakeEvent e = new MoveBrakeEvent( jsonEvent);
			return e;
		} catch (JSONException error) {
			return null;
		}
	}
	
	public byte[] dump() {
		byte[] byteEvent = super.initDump();
		byteEvent[MxEvent.EVENT_HEADER_SIZE] = level;
		return byteEvent;
	}
}

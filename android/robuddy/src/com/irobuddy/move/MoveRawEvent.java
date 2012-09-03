package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveRawEvent extends MxEvent {
	public final static String MOVE_EVENT_LEFT_SPEED_NAME = "leftSpeed";
	public final static String MOVE_EVENT_RIGHT_SPEED_NAME = "rightSpeed";
	
	public final static int LENGTH = 4;
	/**
	 * value: -128 ~ 128
	 */
	public byte leftSpeed;
	public byte rightSpeed;
	
	public MoveRawEvent( byte leftSpeed, byte rightSpeed) {
		super( RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_RAW);
		length = LENGTH;
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
	}
	
	public MoveRawEvent( byte[] byteEvent) {
		super( byteEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_RAW);
		length = LENGTH;
		int dataOffset = MxEvent.EVENT_HEADER_SIZE;
		leftSpeed = (byte)((byteEvent[dataOffset++]==1?1:-1)*byteEvent[dataOffset++]);
		rightSpeed = (byte)((byteEvent[dataOffset++]==1?1:-1)*byteEvent[dataOffset]);
	}
	
	public MoveRawEvent(JSONObject jsonEvent) throws JSONException {
		super( jsonEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_RAW);
		length = LENGTH;
		leftSpeed = (byte)(jsonEvent.getInt( MOVE_EVENT_LEFT_SPEED_NAME));
		rightSpeed = (byte)(jsonEvent.getInt( MOVE_EVENT_RIGHT_SPEED_NAME));
	}


	public static MoveRawEvent build( byte[] byteEvent) {
		MoveRawEvent e = new MoveRawEvent( byteEvent);
		return e;
	}
	
	public static MoveRawEvent build( JSONObject jsonEvent) {
		try {
			MoveRawEvent e = new MoveRawEvent( jsonEvent);
			return e;
		} catch (JSONException error) {
			return null;
		}
	}
	
	public byte[] dump() {
		byte[] byteEvent = super.initDump();
		int dataOffset = MxEvent.EVENT_HEADER_SIZE;
		byteEvent[dataOffset++] = leftSpeed;
		byteEvent[dataOffset] = rightSpeed;
		return byteEvent;
	}
}

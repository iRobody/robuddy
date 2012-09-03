package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveSteerEvent extends MxEvent {
	public final static String MOVESTEER_EVENT_DIR_NAME = "direction";
	
	public final static int LENGTH = 1;
	/**
	 * -2/-1/0/1/2 = hard_left/left/0/right/hard_right
	 */
	public byte direction;
	
	public MoveSteerEvent( byte direction) {
		super( RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_STEER);
		signal = MoveSignal.MV_SIG_STEER;
		length = LENGTH;
		this.direction = direction;
	}
	
	public MoveSteerEvent( byte[] byteEvent) {
		super( byteEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_STEER);
		length = LENGTH;
		this.direction = byteEvent[MxEvent.EVENT_HEADER_SIZE];
	}
	
	public MoveSteerEvent( JSONObject jsonEvent) throws JSONException {
		super( jsonEvent, RobodyChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_STEER);
		length = LENGTH;
		this.direction = (byte)(jsonEvent.getInt( MOVESTEER_EVENT_DIR_NAME));
	}
	
	public static MoveSteerEvent build( byte[] byteEvent) {
		MoveSteerEvent e = new MoveSteerEvent(byteEvent);
		return e;
	}
	
	public static MoveSteerEvent build( JSONObject jsonEvent) {
		try {
			MoveSteerEvent e = new MoveSteerEvent( jsonEvent);
			return e;
		} catch (JSONException error) {
			return null;
		}
	}
	
	public byte[] dump() {
		byte[] byteEvent = super.initDump();

		byteEvent[MxEvent.EVENT_HEADER_SIZE] = direction;
		return byteEvent;
	}
}

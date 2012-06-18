package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveBrakeEvent extends MxEvent {
	public final static String MOVEBRAKE_EVENT_HARD_NAME = "hard";
	
	public final static int LENGTH = 1;
	
	public boolean hard;

	public static MoveBrakeEvent build( byte[] rawEvent) {
		MoveBrakeEvent e = new MoveBrakeEvent((rawEvent[4]==1));
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		return e;
	}
	
	public static MoveBrakeEvent build( JSONObject jsonEvent) {
		MoveBrakeEvent e = new MoveBrakeEvent(false);
		try {
			e.type = (byte)(jsonEvent.getInt( MxEvent.EVENT_TYPE_NAME));
			e.hard = jsonEvent.getBoolean( MOVEBRAKE_EVENT_HARD_NAME);
		} catch (JSONException error) {
			return null;
		}
		return e;
	}
	
	public MoveBrakeEvent( boolean hard) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_MOVE_C;
		signal = MoveSignal.MV_SIG_BRAKE;
		length = LENGTH;
		this.hard = hard;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();

		rawEvent[4] = hard?(byte)1:0;
		return rawEvent;
	}
}

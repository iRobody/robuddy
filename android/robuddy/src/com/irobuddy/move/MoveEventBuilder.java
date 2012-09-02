package com.irobuddy.move;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.matrix.*;

public class MoveEventBuilder {

	public static MxEvent build( byte[] rawEvent) {
		MxSignal sig = MoveSignal.MAX_MOVE_SIG.fromByte( rawEvent[MxEvent.EVENT_SIG_OFFSET]);
		
		switch( (MoveSignal)sig) {
		case MV_SIG_RAW:
			return MoveRawEvent.build( rawEvent);
		case MV_SIG_STEER:
			return MoveSteerEvent.build(rawEvent);
		case MV_SIG_ACCEL:
			return MoveAccelEvent.build(rawEvent);
		case MV_SIG_BRAKE:
			return MoveBrakeEvent.build(rawEvent);
		default:
			break;
		}
		return null;
	}
	
	public static MxEvent build( JSONObject jsonEvent) {
		try {
			MxSignal sig = MoveSignal.MAX_MOVE_SIG.fromByte( (byte)(jsonEvent.getInt(MxEvent.EVENT_SIG_NAME)));
		
			switch( (MoveSignal)sig) {
			case MV_SIG_RAW:
				return MoveRawEvent.build( jsonEvent);
			case MV_SIG_STEER:
				return MoveSteerEvent.build(jsonEvent);
			case MV_SIG_ACCEL:
				return MoveAccelEvent.build(jsonEvent);
			case MV_SIG_BRAKE:
				return MoveBrakeEvent.build(jsonEvent);
			default:
				break;
			}
		} catch (JSONException error) {
			// TODO Auto-generated catch block
		}
		return null;		
	}
	
}

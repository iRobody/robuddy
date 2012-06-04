package com.irobuddy.move;

import com.irobuddy.matrix.*;

public class MoveEventBuilder {

	public static MxEvent build( byte[] rawEvent) {
		MxSignal sig = MoveSignal.MAX_MOVE_SIG.fromByte( rawEvent[MxEvent.EVENT_SIG_OFFSET]);
		
		switch( (MoveSignal)sig) {
		case MV_SIG_DIRECT:
			return MoveDirectEvent.build(rawEvent);
		case MV_SIG_STEER:
			return MoveSteerEvent.build(rawEvent);
		case MV_SIG_ACCEL:
			return MoveAccelEvent.build(rawEvent);
		case MV_SIG_BRAKE:
			return MoveBrakeEvent.build(rawEvent);
		case MV_SIG_RAW:
			return MoveEvent.build( rawEvent);
		}
		return null;
	}
	
}

package com.irobuddy.move;

import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveEvent extends MxEvent {
	public boolean lF;
	public byte lS;
	public boolean rF;
	public byte rS;
	
	public static MoveEvent build( byte[] rawEvent) {
		MoveEvent e = new MoveEvent();
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		e.channel = RobodyChannel.MAX_ROBODY_PUB_EVENT_CH.fromByte(rawEvent[MxEvent.EVENT_CHANNEL_OFFSET]);
		e.sig = MoveSignal.MAX_MOVE_SIG.fromByte( rawEvent[MxEvent.EVENT_SIG_OFFSET]);
		e.length = rawEvent[MxEvent.EVENT_LENGTH_OFFSET];
		e.lF = (rawEvent[4]==1);
		e.lS = rawEvent[5];
		e.rF = (rawEvent[6]==1);
		e.rS = rawEvent[7];
		return e;
	}
	
	public static MoveEvent build( JSONObject jsonEvent) {
		MoveEvent e = new MoveEvent();

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

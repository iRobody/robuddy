package com.irobuddy.move;

import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;

public class MoveSteerEvent extends MxEvent {
	public boolean left;
	public byte speed;
	
	public static MoveSteerEvent build( byte[] rawEvent) {
		MoveSteerEvent e = new MoveSteerEvent((rawEvent[4]==1), rawEvent[5]);
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		return e;
	}
	
	public static MoveSteerEvent build( JSONObject jsonEvent) {
		MoveSteerEvent e = new MoveSteerEvent(false, (byte)0);

		return e;
	}
	
	public MoveSteerEvent( boolean left, byte speed) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_MOVE_C;
		sig = MoveSignal.MV_SIG_STEER;
		length = 2;
		this.left = left;
		this.speed = speed;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();

		rawEvent[4] = left?(byte)1:0;
		rawEvent[5] = speed;
		return rawEvent;
	}
}

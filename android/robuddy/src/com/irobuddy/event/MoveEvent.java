package com.irobuddy.event;

import com.irobuddy.qp.QEvent;
import com.irobuddy.qp.QSignal;

public class MoveEvent extends QEvent {

	private int leftWheelSpeed;
	private int rightWheelSpeed;
	
	public static MoveEvent build( byte[] rawEvent) {
		MoveEvent e = new MoveEvent();
		e.type = rawEvent[QEvent.EVENT_TYPE_OFFSET];
		e.channel = GlobalChannel.MAX_PUB_EVENT_CH.fromByte(rawEvent[QEvent.EVENT_CHANNEL_OFFSET]);
		e.sig = MoveSignal.MV_SIG_BACKWARD.fromByte( rawEvent[QEvent.EVENT_SIG_OFFSET]);
		e.length = rawEvent[QEvent.EVENT_LENGTH_OFFSET];
		return e;
	}
	
	public static MoveEvent build( String jsonEvent) {
		MoveEvent e = new MoveEvent();
		e.type = QEvent.EVENT_TYPE_RELAY;
		e.channel = GlobalChannel.EVENT_CH_MOVE_C;
		e.sig = MoveSignal.MV_SIG_SPEED;
		String[] speeds = jsonEvent.split(" ");
		e.leftWheelSpeed = 50;//Integer.getInteger(speeds[0]);
		e.rightWheelSpeed = 50;//Integer.getInteger(speeds[1]);
		
		return e;
	}
	
	public byte[] dump() {
		byte[] raw = new byte[8];
		raw[0] = this.type;
		raw[1] = this.channel.toByte();
		raw[2] = this.sig.toByte();
		raw[3] = 4;
		raw[4] = (byte) ((this.leftWheelSpeed > 0) ? 1:0);
		raw[5] = (byte) Math.abs(this.leftWheelSpeed);
		raw[6] = (byte) ((this.rightWheelSpeed > 0) ? 1:0);
		raw[7] = (byte) Math.abs(this.rightWheelSpeed);
		return raw;
	}
	
}

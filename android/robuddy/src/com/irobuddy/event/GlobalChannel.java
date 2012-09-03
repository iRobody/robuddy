package com.irobuddy.event;

import com.irobuddy.matrix.BaseChannel;
import com.irobuddy.matrix.MxChannel;

public enum GlobalChannel implements MxChannel{
	MAX_GLOBAL_PUB_EVENT_CH;
	
	final static int offset = RobodyChannel.MAX_ROBODY_PUB_EVENT_CH;
	
	public byte toByte( ) {
		byte ch;
		int ordin = this.ordinal();
		ch = (byte)(ordin+offset);
		return ch;
	}
	
	public MxChannel fromByte( byte ch) {
		if( ch == 0)
			return BaseChannel.EVENT_CH_PRIVATE;
		if( ch < offset)
			return RobodyChannel.VALID_ROBODY_PUB_EVENT_CH.fromByte(ch);
		return RobodyChannel.values()[ch-offset];
	}
}

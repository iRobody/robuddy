package com.irobuddy.event;

import com.irobuddy.matrix.BaseChannel;
import com.irobuddy.matrix.MxChannel;


public enum RobodyChannel implements MxChannel{
	//channels for robody is the first 1024
	EVENT_CH_ACC_S,
	EVENT_CH_ACC_C,

	EVENT_CH_MOVE_S,
	EVENT_CH_MOVE_C,
	
	MAX_ROBODY_PUB_EVENT_CH;
	
	final static int offsetRobuddy = 1024;
	
	final static int offset = BaseChannel.MAX_BASIC_EVENT_CH.toByte();
	
	public byte toByte( ) {
		byte ch;
		int ordin = this.ordinal();
		ch = (byte)(ordin+offset);
		return ch;
	}
	
	public MxChannel fromByte( byte ch) {
		if( ch == 0)
			return BaseChannel.EVENT_CH_PRIVATE;
		return RobodyChannel.values()[ch-offset];
	}
}
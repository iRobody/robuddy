package com.irobuddy.event;

import com.irobuddy.qp.QBasicChannel;
import com.irobuddy.qp.QChannel;

public enum GlobalChannel implements QChannel{

	EVENT_CH_ROBUDDY_S,
	EVENT_CH_ROBUDDY_C,

	EVENT_CH_ROBODY_S,
	EVENT_CH_ROBODY_C,

	EVENT_CH_ACC_S,
	EVENT_CH_ACC_C,

	EVENT_CH_MOVE_S,
	EVENT_CH_MOVE_C,
	//
	MAX_PUB_EVENT_CH;
	
	final static int offset = QBasicChannel.MAX_BASIC_EVENT_CH.toByte();
	
	public byte toByte( ) {
		byte ch;
		int ordin = this.ordinal();
		ch = (byte)(ordin+offset);
		return ch;
	}
	
	public QChannel fromByte( byte ch) {
		if( ch == 0)
			return QBasicChannel.EVENT_CH_PRIVATE;
		return GlobalChannel.values()[ch-offset];
	}
}

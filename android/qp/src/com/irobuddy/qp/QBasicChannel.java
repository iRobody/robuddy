package com.irobuddy.qp;

public enum QBasicChannel implements QChannel{
	EVENT_CH_PRIVATE,
	MAX_BASIC_EVENT_CH;

	public byte toByte( ) {
		byte ch;
		int ordin = this.ordinal();
		ch = (byte)ordin;
		return ch;
	}
	
	public QChannel fromByte( byte ch) {
		return QBasicChannel.values()[ch];
	}
}

package com.irobuddy.matrix;

public enum BaseChannel implements MxChannel{
	EVENT_CH_PRIVATE,
	MAX_BASIC_EVENT_CH;

	public byte toByte( ) {
		byte ch;
		int ordin = this.ordinal();
		ch = (byte)ordin;
		return ch;
	}
	
	public MxChannel fromByte( byte ch) {
		return BaseChannel.values()[ch];
	}
}

package com.irobuddy.matrix;

public enum BaseChannel implements MxChannel{
	EVENT_CH_PRIVATE,
	EVENT_CH_RESERVED;
	
	public final static int MAX_BASIC_EVENT_CH = 2;

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

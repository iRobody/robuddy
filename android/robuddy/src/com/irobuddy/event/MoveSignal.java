package com.irobuddy.event;

import com.irobuddy.matrix.*;

public enum MoveSignal implements MxSignal{
	MV_SIG_FORWARD /* = MAX_BASIC_EVENT_SIG*/,
	MV_SIG_BACKWARD,
	MV_SIG_STOP,
	MV_SIG_RIGH,
	MV_SIG_LEFT,
	MV_SIG_SPEED,
	MV_SIG_RAW,

	MAX_MOVE_SIG;
	
	final static int offset = BaseSignal.MAX_BASIC_EVENT_SIG.toByte();
	
	public byte toByte() {
		byte sig;
		int ordin = this.ordinal();
		sig = (byte)(ordin + offset);
		return sig;
	}
	
	public MxSignal fromByte( byte sig) {
		if( sig < offset) 
			return BaseSignal.EVENT_SIG_RESET.fromByte(sig);
		return MoveSignal.values()[sig-offset];
	}
}

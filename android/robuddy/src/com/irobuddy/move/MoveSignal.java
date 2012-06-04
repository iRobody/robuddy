package com.irobuddy.move;

import com.irobuddy.matrix.*;

public enum MoveSignal implements MxSignal{
	/* offset = MAX_BASIC_EVENT_SIG*/
	MV_SIG_DIRECT,
	MV_SIG_STEER,
	MV_SIG_ACCEL,
	MV_SIG_BRAKE,
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

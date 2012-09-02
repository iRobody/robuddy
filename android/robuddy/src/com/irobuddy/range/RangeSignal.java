package com.irobuddy.range;

import com.irobuddy.matrix.BaseSignal;
import com.irobuddy.matrix.MxSignal;
import com.irobuddy.move.MoveSignal;

public enum RangeSignal implements MxSignal {

	/* offset = MAX_BASIC_EVENT_SIG*/
	RNG_SIG_EDGE,
	RNG_SIG_WALL,
	MAX_RANGE_SIG;
	
	final static int offset = BaseSignal.MAX_BASIC_EVENT_SIG;
	
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

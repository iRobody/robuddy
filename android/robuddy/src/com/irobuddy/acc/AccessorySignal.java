package com.irobuddy.acc;

import com.irobuddy.matrix.*;

public enum AccessorySignal implements MxSignal{
	/*offset = MAX_BASIC_EVENT_SIG*/
	//private for acc on robody side 
	ACC_SIG_DETACH,
	ACC_SIG_ATTACH,
	ACC_SIG_BUSEVENT, 
	
	MAX_ACC_SIG;
	
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
		return AccessorySignal.values()[sig-offset];
	}
}

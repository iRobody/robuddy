package com.irobuddy.event;

import com.irobuddy.qp.QSignal;
import com.irobuddy.qp.QBasicSignal;

public enum AccessorySignal implements QSignal{
	ACC_SIG_ATTACH,
	ACC_SIG_DETACH;
	
	final static int offset = QBasicSignal.MAX_BASIC_EVENT_SIG.toByte();
	
	public byte toByte() {
		byte sig;
		int ordin = this.ordinal();
		sig = (byte)(ordin + offset);
		return sig;
	}
	
	public QSignal fromByte( byte sig) {
		if( sig < offset) 
			return QBasicSignal.EVENT_SIG_RESET.fromByte(sig);
		return AccessorySignal.values()[sig-offset];
	}
}

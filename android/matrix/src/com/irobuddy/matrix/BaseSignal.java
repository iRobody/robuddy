package com.irobuddy.matrix;

public enum BaseSignal implements MxSignal {
	MX_SIG_EMPTY,		///< signal for trace superstate	
	MX_SIG_ENTRY,		///< signal for entry actions
    MX_SIG_EXIT,			///< signal for exit actions
    MX_SIG_INIT,			///< signal for nested initial transitions
    MX_SIG_INFO,
    MX_SIG_OOB,
    
	EVENT_SIG_RESET /*= Q_USER_SIG*/,
	EVENT_SIG_TIMEOUT,
	//
	MAX_BASIC_EVENT_SIG;
	
	public byte toByte( ) {
		byte sig;
		int ordin = this.ordinal();
		sig = (byte)ordin;
		return sig;
	}
	
	public MxSignal fromByte( byte sig) {
		return BaseSignal.values()[sig];
	}
}

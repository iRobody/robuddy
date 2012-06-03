package com.irobuddy.qp;

public enum QBasicSignal implements QSignal {
	QEP_EMPTY_SIG,		///< signal for trace superstate	
	Q_ENTRY_SIG,		///< signal for entry actions
    Q_EXIT_SIG,			///< signal for exit actions
    Q_INIT_SIG,			///< signal for nested initial transitions
    Q_INFO_SIG,
    Q_OOB_SIG,
    
	EVENT_SIG_RESET /*= Q_USER_SIG*/,
	EVENT_SIG_START,
	EVENT_SIG_STOP,
	EVENT_SIG_TIMEOUT,
	//
	MAX_BASIC_EVENT_SIG;
	
	public byte toByte( ) {
		byte sig;
		int ordin = this.ordinal();
		sig = (byte)ordin;
		return sig;
	}
	
	public QSignal fromByte( byte sig) {
		return QBasicSignal.values()[sig];
	}
}

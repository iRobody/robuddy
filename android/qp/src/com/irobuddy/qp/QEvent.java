package com.irobuddy.qp;

import java.nio.ByteBuffer;


public class QEvent {
	
	public final static int EVENT_SERDES_SIZE = 64;
	public final static int EVENT_HEADER_SIZE = 4;
	public final static int EVENT_DATA_SIZE = 60;
	
	public final static int EVENT_TYPE_OFFSET = 0;
	public final static int EVENT_CHANNEL_OFFSET = 1;
	public final static int EVENT_SIG_OFFSET = 2;
	public final static int EVENT_LENGTH_OFFSET = 3;
	
	public final static short EVENT_TYPE_SUB = 0x01;
	public final static short EVENT_TYPE_RELAY = 0x02;
	
	public QEvent(){}
	
	public QEvent( byte type, QChannel channel, QSignal signal) {
		this.type = type;
		this.channel = channel;
		this.sig = signal;
	}
	
	/* build instance from block */
	public QEvent( byte[] rawEvent, QChannel chBuilder, QSignal sigBuilder) {
		type = rawEvent[EVENT_TYPE_OFFSET];
		channel = chBuilder.fromByte( rawEvent[EVENT_CHANNEL_OFFSET]);
		sig = sigBuilder.fromByte(rawEvent[EVENT_SIG_OFFSET]);
		length =  rawEvent[EVENT_LENGTH_OFFSET];
		
		raw = rawEvent;
		rawBuf = ByteBuffer.wrap(rawEvent);
		dataBuf = ByteBuffer.wrap(rawEvent, EVENT_HEADER_SIZE, length);
	}
	
	public static QEvent build( byte[] rawEvent) {
		QEvent e = null;
		try {
			e = new QEvent( rawEvent, QBasicChannel.EVENT_CH_PRIVATE, QBasicSignal.EVENT_SIG_RESET);
		} finally {
		}
		return e;
	}
	
	public byte[] dump() {
		return raw;
	}
	

	
	//event data structure, total 64byte size---------------------------------------
	public byte type = EVENT_TYPE_RELAY;
	public QChannel channel;
	public QSignal sig;			//size = 1byte
	public byte length;
	public ByteBuffer dataBuf;	// only wrap after 4 byte header
	
	ByteBuffer rawBuf;
	byte[] raw;	// byte[64]
}

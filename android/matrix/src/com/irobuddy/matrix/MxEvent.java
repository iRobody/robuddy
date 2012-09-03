package com.irobuddy.matrix;

import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;


public class MxEvent {
	
	public final static int EVENT_SERDES_SIZE = 64;
	public final static int EVENT_HEADER_SIZE = 4;
	public final static int EVENT_DATA_SIZE = 60;
	
	public final static int EVENT_TYPE_OFFSET = 0;
	public final static int EVENT_CHANNEL_OFFSET = 1;
	public final static int EVENT_SIG_OFFSET = 2;
	public final static int EVENT_LENGTH_OFFSET = 3;
	
	public final static byte EVENT_TYPE_SUB = 0x01;
	public final static byte EVENT_TYPE_RELAY = (byte)0x80;
	
	public final static String EVENT_TYPE_NAME = "type";
	public final static String EVENT_CHANNEL_NAME = "channel";
	public final static String EVENT_SIG_NAME = "signal";
	public final static String EVENT_LENGTH_NAME = "length";
	public final static String EVENT_DATA_NAME = "data";
	
	public MxEvent() {
		this.type = 0;
		this.channel = BaseChannel.EVENT_CH_PRIVATE;
		this.signal = BaseSignal.EVENT_SIG_RESET;
	}
	
	public MxEvent( MxChannel channel, MxSignal signal) {
		this.type = MxEvent.EVENT_TYPE_RELAY;
		this.channel = channel;
		this.signal = signal;
	}
	
	public MxEvent( byte type, MxChannel channel, MxSignal signal) {
		this.type = type;
		this.channel = channel;
		this.signal = signal;
	}
	
	/* build instance from block */
	public MxEvent( byte[] byteEvent, MxChannel chBuilder, MxSignal sigBuilder) {
		type = byteEvent[EVENT_TYPE_OFFSET];
		channel = chBuilder.fromByte( byteEvent[EVENT_CHANNEL_OFFSET]);
		signal = sigBuilder.fromByte(byteEvent[EVENT_SIG_OFFSET]);
		length =  byteEvent[EVENT_LENGTH_OFFSET];
		if( length != 0) {
			raw = byteEvent;
			dataBuf = ByteBuffer.wrap(raw, EVENT_HEADER_SIZE, length);
		}
	}
	
	public MxEvent( JSONObject jsonEvent, MxChannel chBuilder, MxSignal sigBuilder)
			throws JSONException {
		if( jsonEvent.has(EVENT_TYPE_NAME)) 
			type = (byte)jsonEvent.getInt(EVENT_TYPE_NAME);
		else
			type = MxEvent.EVENT_TYPE_RELAY;
		channel = chBuilder.fromByte( (byte)jsonEvent.getInt(EVENT_CHANNEL_NAME));
		signal = sigBuilder.fromByte( (byte)jsonEvent.getInt(EVENT_SIG_NAME));
		//length =  (byte)jsonEvent.getInt(EVENT_LENGTH_NAME);
		// general data must in string
		if( jsonEvent.has(EVENT_DATA_NAME)) {
			raw = jsonEvent.getString(EVENT_DATA_NAME).getBytes();
			length = (byte)raw.length;
			dataBuf = ByteBuffer.wrap(raw, EVENT_HEADER_SIZE, length);
		}
	}
	
	public static MxEvent build( byte[] byteEvent) {
		MxEvent e = null;
		try {
			e = new MxEvent( byteEvent, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.EVENT_SIG_RESET);
		} finally {
		}
		return e;
	}
	
	public static MxEvent build( JSONObject jsonEvent) {
		try {
			MxEvent e = new MxEvent( jsonEvent, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.EVENT_SIG_RESET);
			return e;
		} catch (JSONException e1) {
			return null;
		}
	}
	
	public byte[] initDump( ) {
		byte[] byteEvent= new byte[EVENT_HEADER_SIZE+ length];
		byteEvent[EVENT_TYPE_OFFSET] = type;
		byteEvent[EVENT_CHANNEL_OFFSET] = channel.toByte();
		byteEvent[EVENT_SIG_OFFSET] = signal.toByte();
		return byteEvent;
	}
	
	public byte[] dump() {
		byte[] byteEvent= new byte[EVENT_HEADER_SIZE+ length];
		byteEvent[EVENT_TYPE_OFFSET] = type;
		byteEvent[EVENT_CHANNEL_OFFSET] = channel.toByte();
		byteEvent[EVENT_SIG_OFFSET] = signal.toByte();
		byteEvent[EVENT_LENGTH_OFFSET] = length;
		if( length != 0) {
			ByteBuffer dBuf = ByteBuffer.wrap(byteEvent, EVENT_HEADER_SIZE, length);
			dBuf.put(dataBuf);
		}
		return byteEvent;
	}
	
	//event data structure, total 64byte size---------------------------------------
	public byte type = (byte)EVENT_TYPE_RELAY;
	public MxChannel channel;
	public MxSignal signal;			//size = 1byte
	public byte length;
	public ByteBuffer dataBuf;	// only wrap after 4 byte header

	byte[] raw;
}

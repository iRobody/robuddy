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
	
	public MxEvent(){}
	
	public MxEvent( byte type, MxChannel channel, MxSignal signal) {
		this.type = type;
		this.channel = channel;
		this.signal = signal;
	}
	
	/* build instance from block */
	public MxEvent( byte[] rawEvent, MxChannel chBuilder, MxSignal sigBuilder) {
		type = rawEvent[EVENT_TYPE_OFFSET];
		channel = chBuilder.fromByte( rawEvent[EVENT_CHANNEL_OFFSET]);
		signal = sigBuilder.fromByte(rawEvent[EVENT_SIG_OFFSET]);
		length =  rawEvent[EVENT_LENGTH_OFFSET];
		
		raw = rawEvent;
		rawBuf = ByteBuffer.wrap(raw);
		dataBuf = ByteBuffer.wrap(raw, EVENT_HEADER_SIZE, length);
	}
	
	public MxEvent( JSONObject jsonEvent, MxChannel chBuilder, MxSignal sigBuilder)
			throws JSONException {
		type = (byte)jsonEvent.getInt(EVENT_TYPE_NAME);
		channel = chBuilder.fromByte( (byte)jsonEvent.getInt(EVENT_CHANNEL_NAME));
		signal = sigBuilder.fromByte( (byte)jsonEvent.getInt(EVENT_SIG_NAME));
		length =  (byte)jsonEvent.getInt(EVENT_LENGTH_NAME);
		// general data must in string
		raw = jsonEvent.getString(EVENT_DATA_NAME).getBytes();
		rawBuf = ByteBuffer.wrap(raw);
		dataBuf = ByteBuffer.wrap(raw, EVENT_HEADER_SIZE, length);
	}
	
	public static MxEvent build( byte[] rawEvent) {
		MxEvent e = null;
		try {
			e = new MxEvent( rawEvent, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.EVENT_SIG_RESET);
		} finally {
		}
		return e;
	}
	
	public static MxEvent build( JSONObject jsonEvent) {
		MxEvent e = null;
		try {
			e = new MxEvent( jsonEvent, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.EVENT_SIG_RESET);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
		}
		return e;
	}
	
	public byte[] dump() {
		if( null == raw) {
			raw = new byte[EVENT_HEADER_SIZE+ length];
			rawBuf = ByteBuffer.wrap(raw);
			dataBuf = ByteBuffer.wrap(raw, EVENT_HEADER_SIZE, length);
			raw[EVENT_TYPE_OFFSET] = type;
			raw[EVENT_CHANNEL_OFFSET] = channel.toByte();
			raw[EVENT_SIG_OFFSET] = signal.toByte();
			raw[EVENT_LENGTH_OFFSET] = length;
		}
		return raw;
	}
	
	//event data structure, total 64byte size---------------------------------------
	public byte type = (byte)EVENT_TYPE_RELAY;
	public MxChannel channel;
	public MxSignal signal;			//size = 1byte
	public byte length;
	public ByteBuffer dataBuf;	// only wrap after 4 byte header
	
	ByteBuffer rawBuf;
	byte[] raw;	// byte[64]
}

package com.irobuddy.range;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.MxEvent;


public class RangeEdgeEvent extends MxEvent {
	public final static String EDGE_EVENT_FRONTLEFT_NAME = "frontLeft";
	public final static String EDGE_EVENT_FRONTRIGHT_NAME = "frontRight";
	public final static String EDGE_EVENT_REARLEFT_NAME = "rearLeft";
	public final static String EDGE_EVENT_REARRIGHT_NAME = "rearRight";
	
	public final static int LENGTH = 4;

	public byte frontLeft;
	public byte frontRight;
	public byte rearLeft;
	public byte rearRight;
	
	public RangeEdgeEvent( ) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_RANGE_S;
		signal = RangeSignal.RNG_SIG_EDGE;
		length = LENGTH;
	}
	
	public RangeEdgeEvent( byte frontLeft, byte frontRight, byte rearLeft, byte rearRight) {
		super();
		type = MxEvent.EVENT_TYPE_RELAY;
		channel = RobodyChannel.EVENT_CH_RANGE_S;
		signal = RangeSignal.RNG_SIG_EDGE;
		length = LENGTH;
		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
		this.rearLeft = rearLeft;
		this.rearRight = rearRight;
	}
	
	public static RangeEdgeEvent build( byte[] rawEvent) {
		int dataOffset = MxEvent.EVENT_HEADER_SIZE;
		RangeEdgeEvent e = new RangeEdgeEvent( rawEvent[dataOffset++], rawEvent[dataOffset++]
				, rawEvent[dataOffset++], rawEvent[dataOffset]);
		e.type = rawEvent[MxEvent.EVENT_TYPE_OFFSET];
		return e;
	}
	
	public static RangeEdgeEvent build( JSONObject jsonEvent) {
		RangeEdgeEvent e = new RangeEdgeEvent();
		try {
			e.type = (byte)(jsonEvent.getInt( MxEvent.EVENT_TYPE_NAME));
			e.frontLeft = (byte)(jsonEvent.getInt( EDGE_EVENT_FRONTLEFT_NAME));
			e.frontRight = (byte)(jsonEvent.getInt( EDGE_EVENT_FRONTRIGHT_NAME));
			e.rearLeft = (byte)(jsonEvent.getInt( EDGE_EVENT_REARLEFT_NAME));
			e.rearRight = (byte)(jsonEvent.getInt( EDGE_EVENT_REARRIGHT_NAME));
		} catch (JSONException error) {
			return null;
		}
		return e;
	}
	
	public byte[] dump() {
		byte[] rawEvent = super.dump();
		int dataOffset = MxEvent.EVENT_HEADER_SIZE;
		rawEvent[ dataOffset++ ] = frontLeft;
		rawEvent[ dataOffset++ ] = frontRight;
		rawEvent[ dataOffset++ ] = rearLeft;
		rawEvent[ dataOffset++ ] = rearRight;
		return rawEvent;
	}
	
}

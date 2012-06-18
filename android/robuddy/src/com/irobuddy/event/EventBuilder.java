package com.irobuddy.event;

import org.json.JSONException;
import org.json.JSONObject;

import com.irobuddy.matrix.BaseChannel;
import com.irobuddy.matrix.MxChannel;
import com.irobuddy.matrix.MxEvent;
import com.irobuddy.matrix.MxSignal;
import com.irobuddy.move.MoveEventBuilder;

/* decide the builder by channel*/
public class EventBuilder {
	public static MxEvent build( byte type, MxChannel ch, MxSignal sig) {
		MxEvent e = new MxEvent( type, ch, sig);
		return e;
	}
	
	public static MxEvent build( byte[] rawEvent) {
		MxEvent e = null;
		MxChannel ch = null;
		try {
			ch = GlobalChannel.MAX_GLOBAL_PUB_EVENT_CH.fromByte( rawEvent[MxEvent.EVENT_CHANNEL_OFFSET]);
		} finally {
			if( null == ch)
				return null;
		}
		//for private Event
		if( BaseChannel.EVENT_CH_PRIVATE == ch) {
			return MxEvent.build( rawEvent);
		}
		
		switch( (RobodyChannel)ch) {
		case EVENT_CH_MOVE_S:
		case EVENT_CH_MOVE_C:
			e = MoveEventBuilder.build(rawEvent);
			break;
		}
		return e;
	}
	
	public static MxEvent build( JSONObject jsonEvent) {
		MxEvent e = null;
		MxChannel ch = null;
		try {
			ch = GlobalChannel.MAX_GLOBAL_PUB_EVENT_CH.fromByte( (byte)jsonEvent.getInt( MxEvent.EVENT_CHANNEL_NAME));
		} catch (JSONException e1) {
				// TODO Auto-generated catch block
		} finally {
			if( null == ch)
				return null;
		}
		//for private Event
		if( BaseChannel.EVENT_CH_PRIVATE == ch) {
			return MxEvent.build( jsonEvent);
		}
		
		switch( (RobodyChannel)ch) {
		case EVENT_CH_MOVE_S:
		case EVENT_CH_MOVE_C:
			e = MoveEventBuilder.build(jsonEvent);
			break;
		}
		return e;
	}
	
	public static MxEvent build( String jsonStr) {
		try {
			return build( new JSONObject(jsonStr));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}

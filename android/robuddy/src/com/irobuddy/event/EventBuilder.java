package com.irobuddy.event;

import java.util.Map;

import com.irobuddy.matrix.*;
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
	
	public static MxEvent build( Map<String, String> jsonEvent) {
		return null;
	}
	
	public static MxEvent build( String jsonStr) {
		
		return null;
	}
}

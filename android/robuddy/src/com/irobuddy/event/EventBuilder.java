package com.irobuddy.event;

import com.irobuddy.matrix.*;

public class EventBuilder {
	public static MxEvent build( byte type, MxChannel ch, MxSignal sig) {
		MxEvent e = new MxEvent( type, ch, sig);
		return e;
	}
	
	public static MxEvent build( byte[] rawEvent) {
		MxEvent e = null;
		MxChannel ch = null;
		try {
			ch = GlobalChannel.MAX_PUB_EVENT_CH.fromByte( rawEvent[MxEvent.EVENT_CHANNEL_OFFSET]);
		} finally {
			if( null == ch)
				return null;
		}
		//for private Event, only build the basic part
		if( BaseChannel.EVENT_CH_ROBUDDY == ch) {
			return MxEvent.build( rawEvent);
		}
		
		switch( (GlobalChannel)ch) {
		case EVENT_CH_MOVE_S:
		case EVENT_CH_MOVE_C:
			e = MoveEvent.build(rawEvent);
			break;
		}
		return e;
	}
	
	public static MxEvent build( String strEvent) {
		MxEvent e = null;

		return e;
	}
}

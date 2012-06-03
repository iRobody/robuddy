package com.irobuddy.event;

import com.irobuddy.qp.QBasicChannel;
import com.irobuddy.qp.QChannel;
import com.irobuddy.qp.QEvent;
import com.irobuddy.qp.QSignal;

public class EventBuilder {
	public static QEvent build( byte type, QChannel ch, QSignal sig) {
		QEvent e = new QEvent( type, ch, sig);
		return e;
	}
	
	public static QEvent build( byte[] rawEvent) {
		QEvent e = null;
		QChannel ch = null;
		try {
			ch = GlobalChannel.MAX_PUB_EVENT_CH.fromByte( rawEvent[QEvent.EVENT_CHANNEL_OFFSET]);
		} finally {
			if( null == ch)
				return null;
		}
		//for private Event, only build the basic part
		if( QBasicChannel.EVENT_CH_PRIVATE == ch) {
			return QEvent.build( rawEvent);
		}
		
		switch( (GlobalChannel)ch) {
		case EVENT_CH_MOVE_S:
		case EVENT_CH_MOVE_C:
			e = MoveEvent.build(rawEvent);
			break;
		}
		return e;
	}
	
	public static QEvent build( String strEvent) {
		QEvent e = null;

		return e;
	}
}

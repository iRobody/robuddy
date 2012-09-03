package com.irobuddy.cruise;

import android.util.Log;

import com.irobuddy.event.RobodyChannel;
import com.irobuddy.matrix.ActiveNode;
import com.irobuddy.matrix.MxEvent;
import com.irobuddy.matrix.MxState;
import com.irobuddy.move.MoveBrakeEvent;
import com.irobuddy.move.MoveRawEvent;
import com.irobuddy.range.RangeEdgeEvent;
import com.irobuddy.range.RangeSignal;

public class CruiseAN extends ActiveNode {
	final static String TAG = "CRUISE";
	
	public CruiseAN() {
		super();
	}
	
	public void start( ) {
		subscribe( RobodyChannel.EVENT_CH_RANGE_S);
		start( TAG, false, initial, null);
	}
	
	public void stop() {
		super.stop();
	}
	
	//State machine ---------------------------------------------
	public final MxState initial = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			return 	idle;
		}
	};
	
	public final MxState idle = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			if( event.channel== RobodyChannel.EVENT_CH_RANGE_S) {
				switch((RangeSignal)event.signal) {
				case RNG_SIG_EDGE:
				{
					RangeEdgeEvent edgeE = (RangeEdgeEvent)event;
					String msg = "Edge detected:";
					if( edgeE.frontLeft == 1) msg+="fl,";
					if( edgeE.frontRight == 1) msg+="fr,";
					if( edgeE.rearLeft == 1) msg+="rl,";
					if( edgeE.rearRight == 1) msg+="rr";
					Log.d( TAG, msg);
					/*if( (edgeE.frontLeft==1) | (edgeE.frontRight==1) | (edgeE.rearLeft==1) | (edgeE.rearRight==1)) {
						//MoveBrakeEvent e = new MoveBrakeEvent(true);
						MoveRawEvent e = new MoveRawEvent((byte)-100, (byte)-100);
						publish(e);
					} else {
						MoveRawEvent e = new MoveRawEvent((byte)100,(byte)100);
						publish(e);
					}*/
				}
					break;
				default:
					break;
				}
			}
			return ActiveNode.MX_STATE_HANDLED;
		}
	};
}

package com.irobuddy.matrix;

import java.util.ArrayList;
import java.util.List;

public class HsmActiveNode extends ActiveNode {
	/**
	 * event should pass to parent
	 */
	public final static MxState MX_STATE_SUPER = new MxState(){
		@Override
		public MxState kick(MxEvent event) {
		return null;
	}};
	/**
	 * event should pass to the top handler	
	 */
	public final static MxState MX_STATE_TOP = null;
	
	public final static MxEvent MX_EVENT_EMPTY = new MxEvent( (byte)0
			, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.MX_SIG_EMPTY);
	
	public final static MxEvent MX_EVENT_ENTRY = new MxEvent( (byte)0
			, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.MX_SIG_ENTRY);
	
	public final static MxEvent MX_EVENT_EXIT = new MxEvent( (byte)0
			, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.MX_SIG_EXIT);
	
	public final static MxEvent MX_EVENT_INIT = new MxEvent( (byte)0
			, BaseChannel.EVENT_CH_PRIVATE, BaseSignal.MX_SIG_INIT);
	
	/* hierarchy state machine switcher*/
	@Override
	void _kick( final MxEvent event) {
		MxState target = state.kick(event);
		//bypass event to super
		MxState cursor = state;
		while( MX_STATE_SUPER == target) {
			cursor = cursor.kick( MX_EVENT_EMPTY);
			if( MX_STATE_TOP == cursor)
				return;
			target = cursor.kick(event);
		}
		//handled without status change
		if( MX_STATE_HANDLED == target || MX_STATE_TOP == target)
			return;
		//status transfer
		//look for nearest common parent node
		List<MxState> sPath = new ArrayList<MxState>();
		cursor = state;
		while( MX_STATE_TOP != cursor) {
			sPath.add(cursor);
			cursor = cursor.kick( MX_EVENT_EMPTY);
		}
		List<MxState> tPath = new ArrayList<MxState>();
		cursor = target;
		while( MX_STATE_TOP != cursor) {
			tPath.add(cursor);
			cursor = cursor.kick( MX_EVENT_EMPTY);
		}
		MxState parent = MX_STATE_TOP;
		int sPathDeep = sPath.size()-1;
		int tPathDeep = tPath.size()-1;
		int pos = tPathDeep;
		for( int i = 0; i <= sPathDeep && i <= tPathDeep; i++, pos--) {
			if( sPath.get(sPathDeep-i) != tPath.get(tPathDeep-i)) {
				break;
			}
			parent = sPath.get(sPathDeep-i);
		}
		//quit to parent node
		cursor = state;
		while( parent != cursor) {
			cursor.kick(MX_EVENT_EXIT);
			cursor = cursor.kick( MX_EVENT_EMPTY);
		}
		//enter the target
		while( pos >= 0 ) {
			tPath.get(pos).kick(MX_EVENT_ENTRY);
		}
		target.kick(MX_EVENT_INIT);
	}
}

package com.irobuddy.qp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QF {
	
	public static QF getInstance() {
		return THIS;
	}
	
/* QF standard API */
	/* QF flow control */
	public void run() {
	}
	
	public void stop() {
	}
	
	/* publish /subscribe events */
	/**
	 * publish events
	 * @param e
	 */
	public void publish( final QEvent e, QActive sender) {		
		synchronized( chDispatchTable) {
			List<QActive> aol = chDispatchTable.get( e.channel);
			if( null != aol) {
				for( int size = aol.size(); size > 0; ) {
					final QActive ao = aol.get( --size);
					ao.kick( e, sender);
				}
			}
		}
		
		synchronized( greedyAoList) {
			for( int size = greedyAoList.size(); size > 0;) {
				final QActive ao = greedyAoList.get( --size);
				ao.kick( e, sender);
			}
		}
	}
	/**
	 * subscribe channels
	 * @param so	subscriber
	 * @param ch	null is for greedy, that may get all chs 
	 */
	public void subscribe( final QActive ao, QChannel[] chs) {
		if( null == chs) {
			synchronized( greedyAoList) {
				greedyAoList.add( ao);
			}
			return;
		}
		synchronized( chDispatchTable) {
			List<List<QActive>> all = ao2alTable.get( ao);
			if( null == all) {
				all = new ArrayList<List<QActive>>(10);
				ao2alTable.put( ao, all);
			}
			for( int i = 0; i < chs.length; i++) {
				List<QActive> aol = chDispatchTable.get( chs[i]);
				if( null == aol) {
					aol = new ArrayList<QActive>(5);
					chDispatchTable.put( chs[i], aol);
				}
				if(!aol.contains(ao)) {
					aol.add( ao);
					all.add( aol);
				}
			}
		}
	}
	
	public void unsubscribe( final QActive ao) {
		synchronized( chDispatchTable) {
			List<List<QActive>> all = ao2alTable.get( ao);
			if( null != all) {
				for( int size = all.size(); size > 0;) {
					List<QActive> aol = all.get( --size);
					aol.remove( ao);
				}
			}
			ao2alTable.remove( ao);
		}
		synchronized( greedyAoList) {
			greedyAoList.remove( ao);
		}
	}

	
/* framework internal implementation */
	final static QF THIS = new QF( "super");
	QF(String name) {
	}

	/* one AO list to each channel */
	final Map<QChannel, List<QActive>> chDispatchTable = new HashMap<QChannel, List<QActive>>(10);
	/* AOs that receive all channels */
	final ArrayList<QActive> greedyAoList = new ArrayList<QActive>(5);
	/* AO to chAoList back-trace */
	final Map<QActive, List<List<QActive>>> ao2alTable = new HashMap< QActive, List<List<QActive>>>(10);

/* android specials */	
}

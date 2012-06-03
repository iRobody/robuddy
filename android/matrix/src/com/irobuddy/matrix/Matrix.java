package com.irobuddy.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Matrix {
	
	public static Matrix getInstance() {
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
	public void publish( final MxEvent e, ActiveNode sender) {		
		synchronized( chDispatchTable) {
			List<ActiveNode> aol = chDispatchTable.get( e.channel);
			if( null != aol) {
				for( int size = aol.size(); size > 0; ) {
					final ActiveNode ao = aol.get( --size);
					ao.kick( e, sender);
				}
			}
		}
		
		synchronized( greedyAoList) {
			for( int size = greedyAoList.size(); size > 0;) {
				final ActiveNode ao = greedyAoList.get( --size);
				ao.kick( e, sender);
			}
		}
	}
	/**
	 * subscribe channels
	 * @param so	subscriber
	 * @param ch	null is for greedy, that may get all chs 
	 */
	public void subscribe( final ActiveNode ao, MxChannel[] chs) {
		if( null == chs) {
			synchronized( greedyAoList) {
				greedyAoList.add( ao);
			}
			return;
		}
		synchronized( chDispatchTable) {
			List<List<ActiveNode>> all = ao2alTable.get( ao);
			if( null == all) {
				all = new ArrayList<List<ActiveNode>>(10);
				ao2alTable.put( ao, all);
			}
			for( int i = 0; i < chs.length; i++) {
				List<ActiveNode> aol = chDispatchTable.get( chs[i]);
				if( null == aol) {
					aol = new ArrayList<ActiveNode>(5);
					chDispatchTable.put( chs[i], aol);
				}
				if(!aol.contains(ao)) {
					aol.add( ao);
					all.add( aol);
				}
			}
		}
	}
	
	public void unsubscribe( final ActiveNode ao) {
		synchronized( chDispatchTable) {
			List<List<ActiveNode>> all = ao2alTable.get( ao);
			if( null != all) {
				for( int size = all.size(); size > 0;) {
					List<ActiveNode> aol = all.get( --size);
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
	final static Matrix THIS = new Matrix( "super");
	Matrix(String name) {
	}

	/* one AO list to each channel */
	final Map<MxChannel, List<ActiveNode>> chDispatchTable = new HashMap<MxChannel, List<ActiveNode>>(10);
	/* AOs that receive all channels */
	final ArrayList<ActiveNode> greedyAoList = new ArrayList<ActiveNode>(5);
	/* AO to chAoList back-trace */
	final Map<ActiveNode, List<List<ActiveNode>>> ao2alTable = new HashMap< ActiveNode, List<List<ActiveNode>>>(10);

/* android specials */	
}

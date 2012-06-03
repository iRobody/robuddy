package com.irobuddy.matrix;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class ActiveNode {

	/**
	 * event has been consumed
	 */
	public final static MxState QSTATE_HANDLED = new MxState(){
		@Override
		public MxState kick(MxEvent event) {
			return null;
		}};
	
	public ActiveNode( MxState initState) {
		state = initState;
	}
	
	public ActiveNode( ) {
	}
	
	// start the active, normally subscribe channels here
	public void start( final String name, boolean threadMode, MxState state, MxEvent event) {
		this.threadMode = threadMode;
		this.name = name;
		this.state = state;
		
		if( !threadMode)
			initHandler();
		else
			/*FIXME: crash here*/
			createWorker();

		Message.obtain( localHandler, _IS_INIT, event).sendToTarget();
	}
	
	//stop the active, and unsubscribe this AO automatically
	public void stop() {
		Message.obtain( localHandler, _IS_EXIT).sendToTarget();
		qf.unsubscribe( this);
	}

	/* publish chs */
	public void publish( MxEvent e ) {
		qf.publish(e, this);
	}
	/*subscribe interest topics for this AO*/
	public void subscribe( final MxChannel[] chs) {
		qf.subscribe( this, chs);
	}
	public void subscribe( MxChannel sig) {
		MxChannel[] chs = new MxChannel[1];
		qf.subscribe( this, chs);
	}
	public void subscribe( ) {
		qf.subscribe( this, null);
	}
	
	public void unsubscribe( ) {
		qf.unsubscribe( this);
	}
	/* post event to this active privately*/
	public void postFIFO( MxEvent e) {
		kick( e, null);
	}
/* internal implementation */
	/* default use the QF singleton */
	final Matrix qf = Matrix.getInstance();
	String name;
	/* start a separated thread */
	boolean threadMode = false;
	
	MxState state;
	
	final static int _IS_KICK = 0;
	final static int _IS_INIT = 1;
	final static int _IS_EXIT = 2;
	
	/* queEvent called only by QF internally */
	final void kick( MxEvent e, ActiveNode sender) {
		Message.obtain( localHandler, _IS_KICK, e).sendToTarget();
	}
	
	/* real place to handler state*/
	void _kick( final MxEvent event) {
		MxState s = state.kick(event);
		if( QSTATE_HANDLED != s && null != s)
			state = s;
	}
	
/* android specials */
	Handler localHandler;
	HandlerThread worker;
	void initHandler() {
		localHandler = new Handler() {
			@Override
			public void handleMessage( Message msg) {
				switch (msg.what) {
				case _IS_INIT:
				case _IS_KICK:
					final MxEvent e = (MxEvent)msg.obj;
					_kick( e);
					break;
				case _IS_EXIT:
					if( threadMode) {
						worker.quit();
					}
				}
			}
		};
	}
	/* create worker thread*/
	void createWorker() {
		worker = new HandlerThread( name) {
			@Override
			protected
			void onLooperPrepared () {
				initHandler();
			}
		};
	}
}

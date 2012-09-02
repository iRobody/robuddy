package com.irobuddy.webface;

import java.util.HashSet;
import java.util.Set;

import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

import com.irobuddy.event.EventBuilder;
import com.irobuddy.matrix.Matrix;
import com.irobuddy.matrix.MxEvent;

public class WebSocketsHandler implements WebSocketHandler{

	private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();
	
	@Override
	public void onOpen(WebSocketConnection connection) throws Throwable {
		// TODO Auto-generated method stub
		connections.add(connection);
	}
	
	@Override
	public void onClose(WebSocketConnection connection) throws Throwable {
		// TODO Auto-generated method stub
		connections.remove(connection);
	}

	@Override
	public void onMessage(WebSocketConnection connection, String msg)
			throws Throwable {
		
		if( msg.startsWith("p:")) {
			String json = msg.substring(2);
			MxEvent event = EventBuilder.build( json);
			if( null != event)
				Matrix.getInstance().publish(event, null);
		}
	}

	@Override
	public void onMessage(WebSocketConnection connection, byte[] msg)
			throws Throwable {
		throw new UnsupportedOperationException();
	}

	

	@Override
	public void onPing(WebSocketConnection connection, byte[] msg) throws Throwable {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void onPong(WebSocketConnection connection, byte[] msg) throws Throwable {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}

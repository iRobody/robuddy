package com.irobuddy.webface;

import java.util.HashSet;
import java.util.Set;

import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

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
		
		RobotsCmdMsg cmdMsg = new RobotsCmdMsg(msg);
		
		if (cmdMsg.isValid()) {
			WebFaceAN.getInstance().publish(cmdMsg.createMxEvent());
		}
		
		/*
		for (WebSocketConnection ws : connections) {
            ws.send(msg);
        }
		*/
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

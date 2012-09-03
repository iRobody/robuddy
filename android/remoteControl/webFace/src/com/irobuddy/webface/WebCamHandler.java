package com.irobuddy.webface;

import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

import android.os.HandlerThread;

import com.irobuddy.webface.utils.Logger;

public class WebCamHandler implements WebSocketHandler{

	final static String TAG = "WebCamHandler";

	@Override
	public void onOpen(WebSocketConnection connection) throws Throwable {
		// TODO Auto-generated method stub
		Logger.getDefault().debug(TAG, "Start webcam streaming.\n");
		
		WebCam.getInstance().mWebCamStreamingList.add(connection);
		
		HandlerThread webCamThread = new HandlerThread("webCamthread") {
			@Override
		    protected void onLooperPrepared()
		    {
		        super.onLooperPrepared();
		        
		        WebCam.getInstance().Start();
		    }
		};
		webCamThread.start();
	}
	
	@Override
	public void onClose(WebSocketConnection connection) throws Throwable {
		// TODO Auto-generated method stub
		WebCam.getInstance().mWebCamStreamingList.remove(connection);

		//connections.remove(connection);
	}

	@Override
	public void onMessage(WebSocketConnection connection, String msg)
			throws Throwable {
		Logger.getDefault().debug(TAG, msg);

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

/*
public class WebCamHandler implements HttpHandler {

	//private final Executor videoExecutor = Executors.newCachedThreadPool();
	final static String TAG = "WebCamHandler";
	
	@Override
	public void handleHttpRequest(HttpRequest arg0, final HttpResponse res,
			final HttpControl ctl) throws Exception {
		
		Logger.getDefault().debug(TAG, "start handling webcam http-request\n");
		
		//HandlerThread videoStreamingThread = new HandlerThread("vidostreaming");
		//videoStreamingThread.start();

		HandlerThread webCamThread = new HandlerThread("webCamthread") {
			@Override
		    protected void onLooperPrepared()
		    {
		        super.onLooperPrepared();
		        
		        WebCam.getInstance().Start();
		    }
		};
		webCamThread.start();
		
		Handler streamingHandler = new Handler(webCamThread.getLooper()) {
			private boolean mFirstPkg = true;

			private void writeData(final byte[] data) {
				ctl.execute(new Runnable() {
		            public void run() {
		            	Logger.getDefault().debug(TAG, "write data length="+data.length+"\n");
		            	res.write(new String(data));
		            }
		        });
			}
			
			private void writeData(final String data) {
				ctl.execute(new Runnable() {
		            public void run() {
		            	Logger.getDefault().debug(TAG, "write data length="+data.length()+"\n");
		            	res.write(data);
		            }
		        });
			}
			
			@Override
        	public void handleMessage (Message msg){
				switch(msg.what){
        		case 1:
        			
        			if (mFirstPkg){
        				
        				writeData(
        		                "HTTP/1.0 200 OK\r\n" +
        		                        "Server: SL4A\r\n" +
        		                        "Connection: close\r\n" +
        		                        "Max-Age: 0\r\n" +
        		                        "Expires: 0\r\n" +
        		                        "Cache-Control: no-cache, private\r\n" + 
        		                        "Pragma: no-cache\r\n" + 
        		                        "Content-Type: multipart/x-mixed-replace; boundary=--BoundaryString\r\n\r\n");//.getBytes());
        				mFirstPkg = false;
        			}
        			
        			byte[] data = (byte[])msg.obj;
        			byte[] header = ("--BoundaryString\r\n" +
            				"Content-type: image/jpg\r\n" + 
            				"Content-Length: " + data.length + "\r\n\r\n").getBytes();
        			byte[] tailer = "\r\n\r\n".getBytes();
        			byte[] content = new byte[header.length+data.length+tailer.length];
        			System.arraycopy(header, 0, content, 0, header.length);
        			System.arraycopy(data, 0, content, header.length, data.length);
        			System.arraycopy(tailer, 0, content, header.length+data.length, tailer.length);
            
        			writeData(content);
        			break;
        		}
			}
		};
		
		WebCam.getInstance().mWebCamStreamingList.add(streamingHandler);
		
		
	}
	
	
}
*/



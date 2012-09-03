package com.irobuddy.webface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.webbitserver.WebSocketConnection;

import com.irobuddy.webface.utils.Logger;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;



public class WebCam {
	
	final static String TAG = "WebCam";
	//Singleton mode
	private static WebCam mInstance;
		
	private ByteArrayOutputStream mJpegCompressionBuffer = null;
	
	private byte[] mJpegData;

	private boolean mStreaming;
	private boolean mJpegSupported = false;
	private int mPreviewHeight;
	private int mPreviewWidth;
	private int mResolutionLevel;
	private int mJpegQuality;
	private int mCameraDataBufferSize;
	
	private Camera mCamera;
	private Parameters mParameters;
	
	public Set<WebSocketConnection> mWebCamStreamingList = new HashSet<WebSocketConnection>();

	public static LinkedBlockingQueue<Frame> freeFrames;
    public static LinkedBlockingQueue<Frame> filledFrames = new LinkedBlockingQueue<Frame>();
    private Thread videoStreamingThread;
    private static final int NUMBER_OF_BUFFERS = 10;
    byte[][] buffers;
    private int dropped;
    private int frames;
    
    private class videoStreamingThreadRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				Frame frame;
	            try {
	                frame = filledFrames.take();
	            } catch (InterruptedException e) {
	                Log.e(TAG, "Error getting filled frame", e);
	                continue;
	            }
	            try {
	            	mJpegData = frame.data;
	            
	            	if (!mJpegSupported){
	    	    		mJpegData = compressYuvToJpeg(frame.data);
	    	    	}
	            	
	    	        //byte[] b64image = Base64.encodeToByte(mJpegData, false);
	            	//String b64image = Base64.encode(mJpegData);
	            	String b64image = Base64.encodeToString(mJpegData, Base64.NO_WRAP);

	    	    	for (final WebSocketConnection ws : mWebCamStreamingList) {
	    	    		ws.send(b64image);
	    	    		//System.gc();
	    	        }
	            } finally {
	                freeFrames.add(frame);
	            }
			}
		}
    }
    
    private class Frame {
        public Frame() {
            uncompressedSize = 0;
            compressedSize = 0;
        }
        byte[] data;
        int uncompressedSize;
        int compressedSize;
    }
    
    
    /*
     * WebCam implementations
     */
	public static WebCam getInstance(){
		return mInstance == null ?
				(mInstance = new WebCam()):
					mInstance;
	}
	
	public WebCam() {
	    mStreaming = false;
	    mCamera = null;
	    mParameters = null;
	    mJpegQuality = 75;
	    mResolutionLevel = 1;
	    
	    frames = 0;
	    dropped = 0;
	    
	    if(freeFrames == null) {
            freeFrames = new LinkedBlockingQueue<Frame>();
            for (int i = 0; i < NUMBER_OF_BUFFERS; i++) {
                Frame frame = new Frame();
                frame.data = null;
                //frame.data = new byte[COMPRESSED_FRAME_SIZE];
                freeFrames.add(frame);
            }
        }
        if(videoStreamingThread == null) {
        	videoStreamingThread = new Thread(new videoStreamingThreadRunnable());
        	videoStreamingThread.start();
        }
        
	}
	
	/* 
	 * public interfaces
	 */
	public void Start(){
		if (!mStreaming){
			WebFace.addCamView(new WebCamSurfaceView(WebFace.getGlobalContext()));
		}
	}
	
	public void Start(SurfaceHolder holder) {
		try {
			openCamera(holder);
			startStream();
			
		} catch (Exception e) {
			Logger.getDefault().error(TAG, "error start", e);
			Stop();
		}
	}
	
	public void Stop() {
		stopStream();
		releaseCamera();
		Logger.getDefault().debug(TAG, "Frame dropping rate:"+dropped*100/(dropped+frames)+"%.\n");
	}
	
	public void SetQuality(Integer resolutionLevel, Integer jpegQuality) {
		mJpegQuality = jpegQuality;
	    mResolutionLevel = resolutionLevel;
	}
	
	public void AdjustQuality(Integer resolutionLevel, Integer jpegQuality) throws IOException, InterruptedException{
		mJpegQuality = jpegQuality;
	    mResolutionLevel = resolutionLevel;
		Stop();
		//Start();
	}
	
	
	private void openCamera(SurfaceHolder holder) throws IOException,
	    InterruptedException {
	  Logger.getDefault().debug(TAG, "OpenCamera\n");
	  mCamera = Camera.open();
	  mParameters = mCamera.getParameters();
	  
	  List<Integer> supportedFormats = mParameters.getSupportedPreviewFormats();
	  for (Integer supportedFormat : supportedFormats) {
		  if (supportedFormat == ImageFormat.JPEG){
			  mJpegSupported = true;
			  break;
		  }
	  }
	  
	  if (mJpegSupported) {
		  mParameters.setPictureFormat(ImageFormat.JPEG);
		  mParameters.setPreviewFormat(ImageFormat.JPEG);
	  }
	  else{
		  mParameters.setPictureFormat(ImageFormat.NV21);
		  mParameters.setPreviewFormat(ImageFormat.NV21);
	  }
	  
	  List<Size> supportedPreviewSizes = mParameters.getSupportedPreviewSizes();
	  Collections.sort(supportedPreviewSizes, new Comparator<Size>() {
		    @Override
		    public int compare(Size o1, Size o2) {
		      return o1.width - o2.width;
		    }
	  });
	  
	  Size previewSize = supportedPreviewSizes.get(Math.min(mResolutionLevel, 
			  										supportedPreviewSizes.size() - 1));
	  
	  mPreviewHeight = previewSize.height;
	  mPreviewWidth = previewSize.width;
	  Logger.getDefault().debug(TAG, "width:"+mPreviewWidth+" height:"+mPreviewHeight+"\n");
	  
	  mParameters.setPreviewSize(mPreviewWidth, mPreviewHeight);
	  mCamera.setParameters(mParameters);
	  mJpegQuality = Math.min(Math.max(mJpegQuality, 0), 100);
	  
	  mCameraDataBufferSize = (int)(mPreviewHeight*mPreviewWidth*
              (ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat())/8.0));
	  // TODO(damonkohler): Rotate image based on orientation.
	  mCamera.setPreviewDisplay(holder);
	}
	
	private byte[] compressYuvToJpeg(final byte[] yuvData) {
		
		if (mJpegCompressionBuffer == null){
			mJpegCompressionBuffer = new ByteArrayOutputStream(mCameraDataBufferSize);
			Logger.getDefault().debug(TAG, "JPEG Compress Buffersize="+mCameraDataBufferSize+"\n");
		}
		
	    mJpegCompressionBuffer.reset();
	    YuvImage yuvImage =
	        new YuvImage(yuvData, ImageFormat.NV21, mPreviewWidth, mPreviewHeight, null);
	    yuvImage.compressToJpeg(new Rect(0, 0, mPreviewWidth, mPreviewHeight), mJpegQuality,
	        mJpegCompressionBuffer);
	    return mJpegCompressionBuffer.toByteArray();
	}
	
	private final PreviewCallback mPreviewCallback = new PreviewCallback() {
	    @Override
	    public synchronized void onPreviewFrame(final byte[] data, final Camera camera) {
	    	
	    	try {
		    	if (mCamera == null){
		    		return;
		    	}
		    	
		    	if(!freeFrames.isEmpty()) {
	                Frame frame;
	                
					frame = freeFrames.take();
					if (frame.data == null){
		                	frame.data = new byte[mCameraDataBufferSize];
		            }
		            System.arraycopy(data, 0, frame.data, 0, mCameraDataBufferSize);
		                
		            filledFrames.put(frame);
		            frames++;
	            } else {
	            	Logger.getDefault().debug(TAG, "Frame dropped.\n");
	                dropped++;
	            }
	    	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	
	    	/*
	    	mJpegData = data;
	    	
	    	if (!mJpegSupported){
	    		mJpegData = compressYuvToJpeg(data);
	    	}
	    	
	        String b64image = Base64.encode(mJpegData);
	        
	    	for (WebSocketConnection ws : mWebCamStreamingList) {
	    		
	            ws.send(b64image);
	        }
	    	
	    	*/
	    	
	        mCamera.addCallbackBuffer(data);
	    }
	};
	  
	public void startStream() {
	  mStreaming = true;
	  
	  mCamera.addCallbackBuffer(new byte[mCameraDataBufferSize]);
	  mCamera.setPreviewCallbackWithBuffer(mPreviewCallback);
	  
	  //mCamera.setPreviewCallback(mPreviewCallback);
	  mCamera.startPreview();
	}

	private void stopStream() {
	  mStreaming = false;

	  mCamera.stopPreview();
	  mCamera.setPreviewCallback(null);

	}
	
	private void releaseCamera() {
	    if (mCamera != null) {
	      mCamera.release();
	      mCamera = null;
	    }
	    mParameters = null;
	}

}

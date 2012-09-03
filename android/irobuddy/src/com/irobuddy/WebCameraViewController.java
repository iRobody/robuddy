package com.irobuddy;

import android.os.Handler;
import android.view.SurfaceView;


import com.irobuddy.webface.interfaces.*;
import com.irobuddy.webface.utils.Logger;

public class WebCameraViewController implements IWebCameraSurfaceView{

	private Handler mHandler;
	
	public WebCameraViewController(Handler handler) {
		mHandler = handler;
	}
	
	public void Show(SurfaceView view) {
		// TODO Auto-generated method stub
		mHandler.obtainMessage(Supervisor.MSG_ADD_SURFACEVIEW,view).sendToTarget();
	}

	public void Hide(SurfaceView view) {
		// TODO Auto-generated method stub
		
	}
	

}

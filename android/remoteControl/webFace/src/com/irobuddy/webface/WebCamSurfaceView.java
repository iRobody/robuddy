package com.irobuddy.webface;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WebCamSurfaceView extends SurfaceView implements SurfaceHolder.Callback{    
    SurfaceHolder holder;    
    
    public WebCamSurfaceView(Context context)    
    {    
        super(context);    
        holder = getHolder();//获得surfaceHolder引用    
        holder.addCallback(this);    
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型     
    }    
    
    @Override    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
    	// TODO Auto-generated method stub            
    }    
    @Override    
    public void surfaceCreated(SurfaceHolder holder) {    
        // TODO Auto-generated method stub   
    	WebCam.getInstance().Start(holder);
    }    
    
    @Override    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
        WebCam.getInstance().Stop();
    }    
}    
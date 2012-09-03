package com.irobuddy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.irobuddy.acc.AccessoryAN;
import com.irobuddy.webface.WebFace;
import com.irobuddy.webface.utils.Logger;

public class Supervisor extends Activity
{
	final static String TAG = "Supervisor";
	static WebFace webFace = null;
	PowerManager.WakeLock wakeLock;
	
	public static final int MSG_ADD_SURFACEVIEW = 0; 
    public static final int MSG_DELETE_SURFACEVIEW = 1; 
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        
        handleIntent( getIntent());

        Handler webFaceHandler = new Handler() {
        	@Override
        	public void handleMessage (Message msg){
        		switch(msg.what){
        		case MSG_ADD_SURFACEVIEW:
        			SurfaceView view = (SurfaceView)msg.obj;
        			Logger.getDefault().debug(TAG, "ADD Surface.\n");
        			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(320,480,Gravity.BOTTOM);
        			addContentView(view, lp);
        			break;
        		case MSG_DELETE_SURFACEVIEW:
        			break;
        		}
        	}
        };
        
        if( null==webFace) {
            webFace = WebFace.getInstance(this, new WebCameraViewController(webFaceHandler));
        	webFace.run();
        }
    }
    
    @Override
   	public void onNewIntent( Intent intent) {
    	handleIntent( intent);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if( null!= accAN && accAN.isAttached())
    		wakeLock.acquire();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
<<<<<<< HEAD
    	if (wakeLock.isHeld())
=======
    	if( wakeLock.isHeld())
>>>>>>> 286979f7b0f72c40b497307dd81bf6916ec035bc
    		wakeLock.release();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if( null != accAN) {
    		accAN.stop();
    		this.unregisterReceiver( mUsbReceiver);
    	}
    	if( null!=webFace) {
    		webFace.stop();
    		webFace = null;
    	}
    }
    
    void handleIntent( Intent intent) {
    	if( intent.getAction() == "android.intent.action.MAIN") {
    		//try to open accessory
    		openAccessory( null);
    	}
    	/* actived by USB docked */
        if( intent.getAction() == "android.hardware.usb.action.USB_ACCESSORY_ATTACHED" ) {
        	/* android sdk */
        	//UsbManager usbMng =	(UsbManager) getSystemService(Context.USB_SERVICE);
        	/* google api */
        	UsbManager usbMng = UsbManager.getInstance(this);
    		/* android sdk */
			//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
			/* google api */
			UsbAccessory accessory = UsbManager.getAccessory(intent);
    		//open accessory
			openAccessory( accessory);
        }

    }
    
    void openAccessory( UsbAccessory accessory) {
    	UsbManager usbMng = UsbManager.getInstance(this);
    	if( null == accessory) {
    	UsbAccessory[] accessories = usbMng.getAccessoryList();
    	accessory = (accessories == null ? null : accessories[0]);
    	}
    	
    	if( null != accessory) {
    		
    		if( null == accAN) {
        		accAN = new AccessoryAN( usbMng);
        	}

    		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
    		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
    		registerReceiver(mUsbReceiver, filter);
    		
			if (usbMng.hasPermission(accessory)) {
				accAN.start(accessory);
				wakeLock.acquire();
			} else {
				synchronized (mUsbReceiver) {
						usbMng.requestPermission(accessory,
								PendingIntent.getBroadcast(this, 0, new Intent(
										ACTION_USB_PERMISSION), 0));
				}
			}
		}
    }
    
    AccessoryAN accAN;
    static final String ACTION_USB_PERMISSION = "com.irobuddy.action.USB_PERMISSION";
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					/* android sdk */
					//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					/* google api */
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						accAN.start(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				/* android sdk */
				//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				/* google api */
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				accAN.stop( accessory);
				wakeLock.release();
			}
		}
	};
    
}

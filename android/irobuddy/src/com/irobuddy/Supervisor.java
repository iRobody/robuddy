package com.irobuddy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.irobuddy.acc.AccessoryAO;

public class Supervisor extends Activity
{
	final static String TAG = "Supervisor";
    /** Called when the activity is first created. */
	static TextView textV;
	static String logText= "Hello World, supervisor";
	final static void LOG( String t) {
		logText += ("\r\n" + t);
		textV.setText( logText);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textV = (TextView)findViewById( R.id.hello_txt);
        handleIntent( getIntent());
    }
    
    @Override
   	public void onNewIntent( Intent intent) {
    	handleIntent( intent);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if( null != accAO) {
    		accAO.stop();
    		this.unregisterReceiver( mUsbReceiver);
    	}
    }
    
    void handleIntent( Intent intent) {
    	LOG("on new intent");
    	/* actived by USB docked */
        if( intent.getAction() == "android.hardware.usb.action.USB_ACCESSORY_ATTACHED" ) {
        	/* android sdk */
        	//UsbManager usbMng =	(UsbManager) getSystemService(Context.USB_SERVICE);
        	/* google api */
        	UsbManager usbMng = UsbManager.getInstance(this);
        	if( null == accAO) {
        		accAO = new AccessoryAO( usbMng);
        	}
    		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
    		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
    		//filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
    		registerReceiver(mUsbReceiver, filter);
    		/* android sdk */
			//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
			/* google api */
			UsbAccessory accessory = UsbManager.getAccessory(intent);
    		if (accessory != null) {
    			if (usbMng.hasPermission(accessory)) {
    				accAO.start(accessory);
    				LOG("adk attached");
    			} else {
    				synchronized (mUsbReceiver) {
    						usbMng.requestPermission(accessory,
    								PendingIntent.getBroadcast(this, 0, new Intent(
    										ACTION_USB_PERMISSION), 0));
    				}
    				LOG("adk request permission");
    			}
    		}
			
        }
        /* handle remote cmd */
        /*
        if( intent.getAction() == "com.irobuddy.robot.msg") {
        	String cmd = intent.getStringExtra("MSG_TYPE");
        	MoveEvent e = MoveEvent.build(cmd);
        	QF.getInstance().publish(e, null);
        }*/
    }
    
    AccessoryAO accAO;
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
						accAO.start(accessory);
						LOG("adk opened by permission");
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
						LOG("adk permission failed");
					}
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
	        	UsbManager usbMng = UsbManager.getInstance(Supervisor.this);
	        	if( null == accAO) {
	        		accAO = new AccessoryAO( usbMng);
	        	}
	    		/* android sdk */
				//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				/* google api */
				UsbAccessory accessory = UsbManager.getAccessory(intent);
	    		if (accessory != null) {
	    			if (usbMng.hasPermission(accessory)) {
	    				accAO.start(accessory);
	    				LOG("adk reattached");
	    			} else {
	    				synchronized (mUsbReceiver) {
	    						usbMng.requestPermission(accessory,
	    								PendingIntent.getBroadcast(Supervisor.this, 0, new Intent(
	    										ACTION_USB_PERMISSION), 0));
	    				}
	    				LOG("adk request permission");
	    			}
	    		}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				/* android sdk */
				//UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				/* google api */
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				accAO.stop( accessory);
				LOG("adk closed");
			}
		}
	};
    
}

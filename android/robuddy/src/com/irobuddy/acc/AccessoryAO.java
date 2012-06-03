package com.irobuddy.acc;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.irobuddy.event.AccessorySignal;
import com.irobuddy.event.EventBuilder;
import com.irobuddy.event.GlobalChannel;
import com.irobuddy.matrix.*;

public class AccessoryAO extends ActiveNode implements Runnable{
	final static String TAG = "ACCESSORY";
	
	UsbManager usbMng;
	public AccessoryAO( UsbManager usbManager) {
		super();
		usbMng = usbManager;
	}
	
	final static MxEvent attEvent = EventBuilder.build((byte)0, 
			GlobalChannel.EVENT_CH_ACC_C, AccessorySignal.ACC_SIG_ATTACH);
	
	final static MxEvent detEvent = EventBuilder.build((byte)0, 
			GlobalChannel.EVENT_CH_ACC_C, AccessorySignal.ACC_SIG_DETACH);
	
	public void start( UsbAccessory acc) {
		attached( acc);
		subscribe();
		start( TAG, false, initial, null);
	}
	
	public void stop( UsbAccessory accessory) {
		if( accessory != mAccessory || null == mAccessory)
			return;
		super.stop();
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}
	
	public void attached( UsbAccessory acc) {
		mFileDescriptor = usbMng.openAccessory(acc);
		if (mFileDescriptor != null) {
			mAccessory = acc;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "DemoKit");
			thread.start();
			Log.d(TAG, "accessory opened");
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	public void run() {
		int ret = 0;
		byte[] eRaw = new byte[MxEvent.EVENT_SERDES_SIZE];

		while (ret >= 0) {
			try {
				ret = mInputStream.read(eRaw);
			} catch (IOException error) {
				break;
			}
			if( ret < MxEvent.EVENT_HEADER_SIZE
				|| ret != (eRaw[MxEvent.EVENT_LENGTH_OFFSET] + MxEvent.EVENT_HEADER_SIZE) )
				continue;
			

			MxEvent e = EventBuilder.build( eRaw);
			if( null == e) 
				continue;
			
			if( e.channel == BaseChannel.EVENT_CH_ROBUDDY) {
				this.postFIFO(e);
			}
			else {
				e.type &= ~MxEvent.EVENT_TYPE_RELAY;
				publish(e);
				//as eBlock be consumed, new it again
				eRaw = new byte[MxEvent.EVENT_SERDES_SIZE];
			}
		}
	}
	
	public boolean relayEvent( MxEvent event) {
		if( event.channel == GlobalChannel.EVENT_CH_ACC_C
				|| BaseChannel.EVENT_CH_ROBUDDY == event.channel)
			return false;

		if((event.type & MxEvent.EVENT_TYPE_RELAY) == MxEvent.EVENT_TYPE_RELAY) {
			if (mOutputStream != null) {
				try {
					mOutputStream.write(event.dump());
				} catch (IOException error) {
					Log.e(TAG, "write failed", error);
				}
			}
		}
		return true;
	}
	
	//State machine ---------------------------------------------
	public final MxState initial = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			return 	connected;
		}
	};
	
	public MxState connected = new MxState() {
		@Override
		public MxState kick(MxEvent event){
			if( null == event)
				return null;
			if( relayEvent( event)) {
				return ActiveNode.QSTATE_HANDLED;
			}
			
			if( BaseChannel.EVENT_CH_ROBUDDY == event.channel) {
			}
			return ActiveNode.QSTATE_HANDLED;
		}
	};
	
}

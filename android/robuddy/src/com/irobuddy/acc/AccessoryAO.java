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
import com.irobuddy.qp.QActive;
import com.irobuddy.qp.QBasicChannel;
import com.irobuddy.qp.QEvent;
import com.irobuddy.qp.QState;

public class AccessoryAO extends QActive implements Runnable{
	final static String TAG = "ACCESSORY";
	
	UsbManager usbMng;
	public AccessoryAO( UsbManager usbManager) {
		super();
		usbMng = usbManager;
	}
	
	final static QEvent attEvent = EventBuilder.build((byte)0, 
			GlobalChannel.EVENT_CH_ACC_C, AccessorySignal.ACC_SIG_ATTACH);
	
	final static QEvent detEvent = EventBuilder.build((byte)0, 
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
		byte[] eRaw = new byte[QEvent.EVENT_SERDES_SIZE];

		while (ret >= 0) {
			try {
				ret = mInputStream.read(eRaw);
			} catch (IOException error) {
				break;
			}
			if( ret < QEvent.EVENT_HEADER_SIZE
				|| ret != (eRaw[QEvent.EVENT_LENGTH_OFFSET] + QEvent.EVENT_HEADER_SIZE) )
				continue;
			

			QEvent e = EventBuilder.build( eRaw);
			if( null == e) 
				continue;
			
			if( e.channel == QBasicChannel.EVENT_CH_PRIVATE) {
				this.postFIFO(e);
			}
			else {
				e.type &= ~QEvent.EVENT_TYPE_RELAY;
				publish(e);
				//as eBlock be consumed, new it again
				eRaw = new byte[QEvent.EVENT_SERDES_SIZE];
			}
		}
	}
	
	public boolean relayEvent( QEvent event) {
		if( event.channel == GlobalChannel.EVENT_CH_ACC_C
				|| QBasicChannel.EVENT_CH_PRIVATE == event.channel)
			return false;

		if((event.type & QEvent.EVENT_TYPE_RELAY) == QEvent.EVENT_TYPE_RELAY) {
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
	public final QState initial = new QState() {
		@Override
		public QState kick(QEvent event){
			return 	connected;
		}
	};
	
	public QState connected = new QState() {
		@Override
		public QState kick(QEvent event){
			if( null == event)
				return null;
			if( relayEvent( event)) {
				return QActive.QSTATE_HANDLED;
			}
			
			if( QBasicChannel.EVENT_CH_PRIVATE == event.channel) {
			}
			return QActive.QSTATE_HANDLED;
		}
	};
	
}

package com.orbbec.obColor2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.orbbec.NativeNI.OpenNIEx;
import com.orbbec.utils.GlobalDef;
import com.orbbec.utils.OpenNIHelper;
import com.orbbec.view.OpenGLView;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MainActivity extends Activity implements OpenNIHelper.DeviceOpenListener {

	String TAG = "obColor";

	boolean mInit_Ok = false;
	private Thread m_thread;
	private OpenNIHelper mOpenNIHelper;
	private HomeKeyListener mHomeListener;
	private OpenGLView m_glView;

	private boolean mExit = false;

	private int mWidth = GlobalDef.RESOLUTION_X;
	private int mHeight = GlobalDef.RESOLUTION_Y;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		m_glView = (OpenGLView)findViewById(R.id.glView);

		mOpenNIHelper = new OpenNIHelper(this);
		mOpenNIHelper.requestDeviceOpen(this);
	}


	@Override
	protected void onResume() {
		super.onResume();


	}

	@Override
	public void onDeviceOpened(UsbDevice device) {


		int VID = device.getVendorId();
		int PID = device.getProductId();
		int rc = OpenNIEx.open(VID, PID);
		if(rc < 0){
			Toast.makeText(this, "OpenNI Open failed", Toast.LENGTH_SHORT).show();
			return;
		}else {
			Log.d(TAG, "open device success "+ Integer.toHexString(VID) +"0x" +Integer.toHexString(PID));
		}
		startThread();
	}

	@Override
	public void onDeviceOpenFailed(UsbDevice uri) {

	}

	void startThread(){
		mInit_Ok = true;
		Log.v(TAG, "start thread");
		m_thread = new Thread(){

			@Override
			public void run() {

				while (!mExit) {
					if(OpenNIEx.WaitAndUpdate() < 0){
					  continue;
					}
					m_glView.update(mWidth, mHeight);
				}
			}
		};

		m_thread.start();
	}

	@Override
	protected void onStop() {


		if(mInit_Ok) {
			mExit = true;
			if (m_thread != null) {
				try {
					m_thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			OpenNIEx.closedevice();

			Log.i(TAG, "Activity exit!");
			System.exit(0);
		}
		super.onStop();
	}

	private void showAlertAndExit(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.show();
	}


	/**
	 * 注册Home键的监听
	 */
	private void registerHomeListener() {
		mHomeListener = new HomeKeyListener(this);
		mHomeListener
				.setOnHomePressedListener(new HomeKeyListener.OnHomePressedListener() {

					@Override
					public void onHomePressed() {
						// TODO 进行点击Home键的处理
						finish();
					}

					@Override
					public void onHomeLongPressed() {
						// TODO 进行长按Home键的处理
					}
				});
		mHomeListener.startWatch();
	}

	private void unRegisterHomeListener() {
		if (mHomeListener != null) {
			mHomeListener.stopWatch();
		}
	}

}
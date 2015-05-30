package com.example.sdfsd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private SurfaceView mPreview;
	private SurfaceHolder mSurfaceHolder;
	private CameraModule mCameraModule;
	private Button mShutter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreview = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mPreview.getHolder();
        mCameraModule = new CameraModule(this);
        mSurfaceHolder.addCallback(mCameraModule);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
        mShutter = (Button) findViewById(R.id.capture_button);
        mShutter.setOnClickListener(this);
    }
    public SurfaceHolder getSurfaceHolder() {
    	return mSurfaceHolder;
    }
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState,
			PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState, persistentState);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCameraModule.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mCameraModule.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCameraModule.onDestroy();
	}
	private boolean record = false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mShutter) {
			//mCameraModule.capture();
			if(!record) {
				try {
					mCameraModule.beginRecording(mSurfaceHolder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				record = true;
			} else {
				mCameraModule.stopRecording();
				record = false;
			}
		}
	}
}

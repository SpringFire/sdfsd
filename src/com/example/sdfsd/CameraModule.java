package com.example.sdfsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.sdfsd.ui.FocusManager;

import android.R.integer;
import android.content.Context;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraDevice;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
public class CameraModule implements SurfaceHolder.Callback{

	private final String TAG = "CameraModule";
	private Camera mCameraDevice;
    private MainActivity mMainContext;
    private int mCameraId = 0;
    private FocusManager mFocusManager;
    private static int mCameraState = CameraState.CAMERA_IDLE;
    
	public CameraModule(MainActivity mContext) {
		mMainContext = mContext;
		mFocusManager = new FocusManager(mContext);
		init();
	}
	public void init() {
		//this here should change the cameraid
		mCameraId = 0;
		openCamera(mCameraId);
		
	}
    public void openCamera(int CameraId) {
    	try{
    		mCameraDevice = android.hardware.Camera.open(0);
    	}catch(Exception e) {
    		Log.e(TAG, "there is something wrong when opencamera e:"+e.fillInStackTrace());
    	}
    	if(mCameraDevice == null) {
    		Log.e(TAG, "open camera failed");
    	}
    }
    

	protected void onResume() {
		if(mCameraDevice == null) {
			openCamera(mCameraId);
			startPreview();
		}
	}

	protected void onPause() {
		if (mCameraDevice != null) {
			mCameraDevice.release();
			mCameraDevice = null;
		}
	}

	protected void onDestroy() {
	}
	
	public void startPreview() {
        if (mCameraDevice != null) {
        	try {
				mCameraDevice.setPreviewDisplay(mMainContext.getSurfaceHolder());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	mCameraDevice.setDisplayOrientation(90);
        	updateCameraParameters();
        	mCameraDevice.startPreview();
        }
	}
	/* this method for us to update the parameters*/
	private void updateCameraParameters() {
		Camera.Parameters parameter = mCameraDevice.getParameters();
		
		parameter.setFocusMode(mFocusManager.getFocusMode());
        parameter.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
        Size size = parameter.getSupportedPictureSizes().get(0);
        Log.i(TAG, "LMCH0513 h:"+size.height+",w:"+size.width);
        parameter.setPreviewSize(1280, 720);
        parameter.set("zsl","on");
        parameter.setPictureSize(size.width, size.height);
		mCameraDevice.setParameters(parameter);
	}
	private ShutterCallback mShutterCallback = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(TAG, "LMCH0511 shutterCallback");
		}
	};
	private PictureCallback mPictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "LMCH0511 picturecallback");
			setCameraState(CameraState.CAMERA_IDLE);
			savepic(data,camera);
			mCameraDevice.startPreview();
		}
	};
	private void savepic(byte[] data,Camera camera) {
        Date date = new Date();  
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间  
        String filename = format.format(date) + ".jpg";  
        File fileFolder = new File(Environment.getExternalStorageDirectory() 
                + "/limingchun/");  
        Log.i(TAG, "LMCH0511 filename:"+Environment.getExternalStorageDirectory() 
                + "/limingchun/");
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录  
            fileFolder.mkdir();  
        }  
        File jpgFile = new File(fileFolder, filename);  
        FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(jpgFile);
	        outputStream.write(data); // 写入sd卡中  
	        outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void capture() {
		//mCameraDevice.stopPreview();
		if(mCameraState != CameraState.CAMERA_IDLE) return;
		setCameraState(CameraState.CAMERA_CAPTURE);
		updateCameraParameters();
		mCameraDevice.takePicture(mShutterCallback, null, mPictureCallback);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "LMCH0508 holder:"+holder);
		Log.i(TAG, "LMCH0508 mMainContext.getSurfaceHolder():"+mMainContext.getSurfaceHolder());
		// TODO Auto-generated method stub
        startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

    private MediaRecorder record = null;
    private static final String OUTPUT_FILE = "/sdcard/videooutput.mp4";
    public void stopRecording() {
        // TODO Auto-generated method stub
        if(record != null){
            record.stop();
        }
    }
    public void beginRecording(SurfaceHolder holder) throws Exception{
        // TODO Auto-generated method stub
        if(record != null){
            record.stop();
            record.release();
        }
        mCameraDevice.stopPreview();
        File outFile = new File(OUTPUT_FILE);
        if(outFile.exists()){
            outFile.delete();
        }
        
        try{
            record = new MediaRecorder();
            record.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            record.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            record.setVideoSize(320, 240);
            record.setVideoFrameRate(15);
            record.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            record.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            record.setMaxDuration(30000);
            record.setPreviewDisplay(holder.getSurface());
            record.setOutputFile(OUTPUT_FILE);
            record.prepare();
            record.start();
        }catch(Exception e){
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }
    public static void setCameraState(int s) {
    	mCameraState = s;
    }
    public static int getCameraState() {
    	return mCameraState;
    }
}

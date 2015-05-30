package com.example.sdfsd.ui;

import com.example.sdfsd.MainActivity;

import android.hardware.Camera;

public class FocusManager {

	private final static String TAG = "FocusManager";
	private boolean setFocus = false;
	private MainActivity mContext;
	public FocusManager(MainActivity context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
    public String getFocusMode() {
    	String focusmode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
    	if(setFocus) {
    		//TODO: when focus is set,set the focus to the mode
    	} else {
    		//TODO£º
    	}
    	return focusmode;
    }
}

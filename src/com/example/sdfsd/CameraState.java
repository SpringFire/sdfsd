package com.example.sdfsd;

import android.R.integer;

public class CameraState {
	
public final static int CAMERA_IDLE = 0;
public final static int CAMERA_CAPTURE = 1;

public CameraState() {
	// TODO Auto-generated constructor stub
}


class State {
    int mStateid;
    int mPreStateid;
    State(int stateid,int preStateid){
    	mStateid = stateid;
    	mPreStateid = preStateid;
    }
}
}

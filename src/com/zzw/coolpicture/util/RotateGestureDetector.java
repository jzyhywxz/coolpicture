package com.zzw.coolpicture.util;

import android.content.Context;
import android.view.MotionEvent;

public class RotateGestureDetector 
extends TwoFingerGestureDetector {
    private OnRotateGestureListener mListener;
    private boolean mSloppyGesture;
	
	public interface OnRotateGestureListener {
		public boolean onRotate(
				RotateGestureDetector detector);
		public boolean onRotateBegin(
				RotateGestureDetector detector);
		public void onRotateEnd(
				RotateGestureDetector detector);
	}
	
	public static class SimpleOnRotateGestureListener 
	implements OnRotateGestureListener {
		@Override
	    public boolean onRotate(
	    		RotateGestureDetector detector) {
	        return false;
	    }
		@Override
	    public boolean onRotateBegin(
	    		RotateGestureDetector detector) {
	        return true;
	    }
		@Override
	    public void onRotateEnd(
	    		RotateGestureDetector detector) {}
	}
    
    public RotateGestureDetector(Context context, 
    		OnRotateGestureListener listener) {
    	super(context);
        mListener=listener;
    }

    @Override
    protected void handleStartProgressEvent(
    		int actionCode, MotionEvent event){
        switch(actionCode){
            case MotionEvent.ACTION_POINTER_DOWN:
                resetState();
                mPrevEvent=MotionEvent.obtain(event);
                updateStateByEvent(event);
                
                mSloppyGesture=isSloppyGesture(event);
                if(!mSloppyGesture){
                    mGestureInProgress=
                    		mListener.onRotateBegin(this);
                }
            	break;
            case MotionEvent.ACTION_MOVE:
                if(!mSloppyGesture){
                	break;
                }
                
                mSloppyGesture=isSloppyGesture(event);
                if(!mSloppyGesture){
                    mGestureInProgress=
                    		mListener.onRotateBegin(this);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(!mSloppyGesture){
                	break;
                }
                break; 
        }
    }
    
    @Override
    protected void handleInProgressEvent(
    		int actionCode, MotionEvent event){
        switch(actionCode){
            case MotionEvent.ACTION_POINTER_UP:
                updateStateByEvent(event);
                if(!mSloppyGesture){
                    mListener.onRotateEnd(this);
                }
                resetState();
                break;
            case MotionEvent.ACTION_CANCEL:
                if(!mSloppyGesture){
                    mListener.onRotateEnd(this);
                }
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                if(mCurrPressure/mPrevPressure > 
                PRESSURE_THRESHOLD){
                    if (mListener.onRotate(this)){
                        mPrevEvent.recycle();
                        mPrevEvent=MotionEvent.obtain(event);
                    }
                }
                break;
        }
    }

    @Override
    protected void resetState() {
        super.resetState();
        mSloppyGesture=false;
    }


    public float getRotationDegreesDelta(){
		double diffRadians=
				Math.atan2(mPrevFingerDiffY, mPrevFingerDiffX)-
				Math.atan2(mCurrFingerDiffY, mCurrFingerDiffX);
		return (float)(diffRadians*180/Math.PI);
	}
}

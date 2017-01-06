package com.zzw.coolpicture.util;

import android.content.Context;
import android.view.MotionEvent;

public class ClickMoveGestureDetector extends MoveGestureDetector{
	private long mBegTime;	// 事件开始时间
	private long mEndTime;	// 事件终止时间
	private static final long MAX_CLICK_TIME_DELTA=100;
	private static final float MAX_CLICK_FOCUS_DELTA=10;
	
	public interface OnClickMoveGestureListener 
	extends MoveGestureDetector.OnMoveGestureListener {
		public void onClick(MotionEvent event);
	}
	
	public static class SimpleOnClickMoveGestureListener 
	implements OnClickMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			return false;
		}
		@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			return true;
		}
		@Override
		public void onMoveEnd(MoveGestureDetector detector) {}
		@Override
		public void onClick(MotionEvent event) {}
	}
	
	public ClickMoveGestureDetector(Context context, 
			OnClickMoveGestureListener listener) {
		super(context, listener);
		mListener=listener;
	}
	
	@Override
	protected void handleStartProgressEvent(
			int actionCode, MotionEvent event) {
		switch(actionCode){ 
        case MotionEvent.ACTION_DOWN: 
            resetState();
            mBegTime=System.currentTimeMillis();
            mPrevEvent=MotionEvent.obtain(event);
            updateStateByEvent(event);
            break;
        case MotionEvent.ACTION_MOVE:
            mGestureInProgress=mListener.onMoveBegin(this);
            break;
        }
	}
	
	@Override
	protected void handleInProgressEvent(
			int actionCode, MotionEvent event) {
		switch(actionCode){
    	case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
        	mEndTime=System.currentTimeMillis();
            if(mEndTime-mBegTime<MAX_CLICK_TIME_DELTA || 
            		getFocusTotalDelta()<MAX_CLICK_FOCUS_DELTA){
            	((OnClickMoveGestureListener)mListener).
            	onClick(event);
            }
            else{
            	mListener.onMoveEnd(this);
            }
            resetState();
            break;
        case MotionEvent.ACTION_MOVE:
            updateStateByEvent(event);
            if(mCurrPressure/mPrevPressure>PRESSURE_THRESHOLD){
                if(mListener.onMove(this)){
                    mPrevEvent.recycle();
                    mPrevEvent=MotionEvent.obtain(event);
                }
            }
            break;
		}
	}
	
	public float getFocusTotalDelta() {
		return (float)Math.sqrt(
				mFocusExternal.x*mFocusExternal.x+
				mFocusExternal.y*mFocusExternal.y);
	}
	
}

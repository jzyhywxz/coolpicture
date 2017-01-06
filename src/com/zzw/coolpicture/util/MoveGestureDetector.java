package com.zzw.coolpicture.util;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

public class MoveGestureDetector extends BaseGestureDetector {
    private PointF mCurrFocusInternal;		// 当前事件发生点
    private PointF mPrevFocusInternal;		// 上次事件发生点
    // 总位移
    protected PointF mFocusExternal=new PointF();
    // 每次位移差
    private PointF mFocusDeltaExternal=new PointF();
    
    protected OnMoveGestureListener mListener;
    
	public interface OnMoveGestureListener {
		public boolean onMove(MoveGestureDetector detector);
		public boolean onMoveBegin(MoveGestureDetector detector);
		public void onMoveEnd(MoveGestureDetector detector);
	}
	
	public static class SimpleOnMoveGestureListener 
	implements OnMoveGestureListener {
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
	}

	public MoveGestureDetector(Context context, 
			OnMoveGestureListener listener) {
		super(context);
		mListener=listener;
	}

	@Override
	protected void handleStartProgressEvent(
			int actionCode, MotionEvent event) {
		switch(actionCode){ 
        case MotionEvent.ACTION_DOWN: 
            resetState();
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
            mListener.onMoveEnd(this);
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
	
	@Override
	protected void updateStateByEvent(MotionEvent curr) {
    	super.updateStateByEvent(curr);

    	final MotionEvent prev=mPrevEvent;
    	// Focus internal
        mCurrFocusInternal=determineFocalPoint(curr);
        mPrevFocusInternal=determineFocalPoint(prev);
        // Focus external
        boolean mSkipNextMoveEvent=
        		prev.getPointerCount()!=curr.getPointerCount();
        mFocusDeltaExternal=mSkipNextMoveEvent?new PointF():
        	new PointF(mCurrFocusInternal.x-mPrevFocusInternal.x, 
        			mCurrFocusInternal.y-mPrevFocusInternal.y);
        
        mFocusExternal.x+=mFocusDeltaExternal.x;
        mFocusExternal.y+=mFocusDeltaExternal.y;        
    }

    private PointF determineFocalPoint(MotionEvent e){
        final int pCount=e.getPointerCount(); 
        float x=0f;
        float y=0f;
        for(int i=0;i<pCount;i++){
        	x+=e.getX(i);
        	y+=e.getY(i);
        }
        
        return new PointF(x/pCount, y/pCount);
    }

    public PointF getCurrFocusInternal(){
    	return mCurrFocusInternal;
    }
    
    public float getFocusX() {
        return mFocusExternal.x;
    }

    public float getFocusY() {
        return mFocusExternal.y;
    }
    
    public PointF getFocusDelta() {
		return mFocusDeltaExternal;
    }

}

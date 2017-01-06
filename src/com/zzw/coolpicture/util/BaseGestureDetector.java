package com.zzw.coolpicture.util;

import android.content.Context;
import android.view.MotionEvent;

public abstract class BaseGestureDetector {
	protected final Context mContext;
	protected boolean mGestureInProgress;	// 是否正在处理事件
	
	protected MotionEvent mPrevEvent;		// 上次事件
	protected MotionEvent mCurrEvent;		// 当前事件
	
	protected float mCurrPressure;			// 上次压力
	protected float mPrevPressure;			// 当前压力
	protected static final float PRESSURE_THRESHOLD=0.67f;
	
	protected long mTimeDelta;				// 事件时差
	
	public BaseGestureDetector(Context context) {
    	mContext=context;
    }
    
	public boolean onTouchEvent(MotionEvent event){
    	final int actionCode=event.getAction()
    			&MotionEvent.ACTION_MASK;
    	if(!mGestureInProgress){	// 还未处理事件
    		handleStartProgressEvent(actionCode, event);
    	}
    	else{						// 正在处理事件
    		handleInProgressEvent(actionCode, event);
    	}
    	return true;
    }
    
	// 开始处理事件
    protected abstract void 
    handleStartProgressEvent(int actionCode, MotionEvent event);
    // 继续处理事件
	protected abstract void 
	handleInProgressEvent(int actionCode, MotionEvent event);
    // 更新事件信息
    protected void updateStateByEvent(MotionEvent curr){
    	final MotionEvent prev=mPrevEvent;
        if(mCurrEvent!=null){
        	mCurrEvent.recycle();
            mCurrEvent=null;
        }
        mCurrEvent=MotionEvent.obtain(curr);
        
        mTimeDelta=curr.getEventTime()-prev.getEventTime();

        mCurrPressure=curr.getPressure(curr.getActionIndex());
        mPrevPressure=prev.getPressure(prev.getActionIndex());
    }
    
    // 重置事件
    protected void resetState() {
        if(mPrevEvent!=null){
            mPrevEvent.recycle();
            mPrevEvent=null;
        }
        if(mCurrEvent!=null){
            mCurrEvent.recycle();
            mCurrEvent=null;
        }
        mTimeDelta=0;
        mGestureInProgress=false;
    }

    public boolean isInProgress() {
        return mGestureInProgress;
    }

	public long getTimeDelta() {
		return mTimeDelta;
	}

	public long getEventTime() {
		return mCurrEvent.getEventTime();
	}
}

package com.zzw.coolpicture.util;

import android.content.Context;
import android.view.MotionEvent;

public abstract class BaseGestureDetector {
	protected final Context mContext;
	protected boolean mGestureInProgress;	// �Ƿ����ڴ����¼�
	
	protected MotionEvent mPrevEvent;		// �ϴ��¼�
	protected MotionEvent mCurrEvent;		// ��ǰ�¼�
	
	protected float mCurrPressure;			// �ϴ�ѹ��
	protected float mPrevPressure;			// ��ǰѹ��
	protected static final float PRESSURE_THRESHOLD=0.67f;
	
	protected long mTimeDelta;				// �¼�ʱ��
	
	public BaseGestureDetector(Context context) {
    	mContext=context;
    }
    
	public boolean onTouchEvent(MotionEvent event){
    	final int actionCode=event.getAction()
    			&MotionEvent.ACTION_MASK;
    	if(!mGestureInProgress){	// ��δ�����¼�
    		handleStartProgressEvent(actionCode, event);
    	}
    	else{						// ���ڴ����¼�
    		handleInProgressEvent(actionCode, event);
    	}
    	return true;
    }
    
	// ��ʼ�����¼�
    protected abstract void 
    handleStartProgressEvent(int actionCode, MotionEvent event);
    // ���������¼�
	protected abstract void 
	handleInProgressEvent(int actionCode, MotionEvent event);
    // �����¼���Ϣ
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
    
    // �����¼�
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

package com.zzw.coolpicture.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public abstract class TwoFingerGestureDetector 
extends BaseGestureDetector {
	// Inset in pixels to look for touchable content 
	//when the user touches the edge of the screen
	private final float mEdgeSlop;
    private float mRightSlopEdge;
    private float mBottomSlopEdge;
    // 上次事件两指间距离
	protected float mPrevFingerDiffX;
	protected float mPrevFingerDiffY;
	// 当前事件两指间坐标
	protected float mCurrFingerDiffX;
	protected float mCurrFingerDiffY;
    // 两指间距离
    private float mCurrLen;
    private float mPrevLen;
	
    public TwoFingerGestureDetector(Context context) {
    	super(context);
    	
        ViewConfiguration config=ViewConfiguration.get(context);
        mEdgeSlop=config.getScaledEdgeSlop();
    }
    
    @Override
	protected void updateStateByEvent(MotionEvent curr){
		super.updateStateByEvent(curr);
		
		final MotionEvent prev=mPrevEvent;
		// 更新两指间距离
        mCurrLen=-1;
        mPrevLen=-1;
        // Previous
        final float px0=prev.getX(0);
        final float py0=prev.getY(0);
        final float px1=prev.getX(1);
        final float py1=prev.getY(1);
        mPrevFingerDiffX=px1-px0;
        mPrevFingerDiffY=py1-py0;
        // Current
        final float cx0=curr.getX(0);
        final float cy0=curr.getY(0);
        final float cx1=curr.getX(1);
        final float cy1=curr.getY(1);
        mCurrFingerDiffX=cx1-cx0;
        mCurrFingerDiffY=cy1-cy0;
	}
	
    public float getCurrentSpan() {
        if(mCurrLen==-1)
            mCurrLen=(float)Math.sqrt(
            		mCurrFingerDiffX*mCurrFingerDiffX+
            		mCurrFingerDiffY*mCurrFingerDiffY);
        return mCurrLen;
    }

    public float getPreviousSpan() {
        if(mPrevLen==-1)
            mPrevLen=(float)Math.sqrt(
            		mPrevFingerDiffX*mPrevFingerDiffX+
            		mPrevFingerDiffY*mPrevFingerDiffY);
        return mPrevLen;
    }
    
    protected static float getRawX(MotionEvent event, 
    		int pointerIndex) {
        if(pointerIndex<event.getPointerCount()){
        	return event.getX(pointerIndex)+
        			event.getX()-event.getRawX();
        }
        return 0f;
    }

    protected static float getRawY(MotionEvent event, 
    		int pointerIndex) {
        if(pointerIndex<event.getPointerCount()){
        	return event.getY(pointerIndex)+
        			event.getY()-event.getRawY();
        } 
        return 0f;
    }

	protected boolean isSloppyGesture(MotionEvent event){
        DisplayMetrics metrics=mContext.getResources().
        		getDisplayMetrics();
        mRightSlopEdge=metrics.widthPixels-mEdgeSlop;
        mBottomSlopEdge=metrics.heightPixels-mEdgeSlop;
        
        final float x0=event.getRawX();
        final float y0=event.getRawY();
        final float x1=getRawX(event, 1);
        final float y1=getRawY(event, 1);

        boolean p0sloppy=
        		x0<mEdgeSlop || x0>mRightSlopEdge || 
        		y0<mEdgeSlop || y0>mBottomSlopEdge;
        boolean p1sloppy=
        		x1<mEdgeSlop || x1>mRightSlopEdge || 
        		y1<mEdgeSlop || y1>mBottomSlopEdge;

        if(p0sloppy || p1sloppy)
            return true;
        return false;
    }
}

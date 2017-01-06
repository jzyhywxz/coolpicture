package com.zzw.coolpicture.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public abstract class WrapPopupWindow extends PopupWindow {
	protected Context context;		// ������
	protected View contentView;		// ������ͼ
	
	public WrapPopupWindow(Context context, View contentView, 
			int width, int height, boolean focusable){
		super(contentView, width, height, focusable);
		
		this.context=context;
		this.contentView=contentView;
		
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setTouchable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
					dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	public abstract void initView();
	public abstract void initEvent();
	public abstract void init();
	
	public View findViewById(int id){
		return contentView.findViewById(id);
	}
	
	public void showAtBottom(View parent){
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	public void showAtCenter(View parent){
		showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
}

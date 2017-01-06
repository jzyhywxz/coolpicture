package com.zzw.coolpicture.activity;

import java.util.ArrayList;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.graphics.MyRenderer;
import com.zzw.coolpicture.util.RotateGestureDetector;
import com.zzw.coolpicture.util.RotateGestureDetector.SimpleOnRotateGestureListener;
import com.zzw.coolpicture.view.SimpleFuncPopupWindow;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;

public class TDImageActivity extends Activity 
implements OnTouchListener {
	private GLSurfaceView mImgView;
	private ImageButton mTDRotate;
	private MyRenderer mRenderer;
	//  ÷ ∆º‡Ã˝∆˜
	private RotateGestureDetector mRGDetector;
	// π¶ƒ‹µØ¥∞
	private SimpleFuncPopupWindow rotateFuncWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_td_img);
		
		initView();
		initEvent();
		initWindow();
	}
	
	private void initView(){
		mImgView=(GLSurfaceView)findViewById(R.id.td_img_view);
		mRenderer=new MyRenderer();
		mImgView.setRenderer(mRenderer);
		
		mTDRotate=(ImageButton)findViewById(R.id.td_rotate);
	}
	
	private void initEvent(){
		mTDRotate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				rotateFuncWindow.showAtCenter(mImgView);
			}
		});
		mRGDetector=new RotateGestureDetector(this, new MyRGListener());
		mImgView.setOnTouchListener(this);
	}
	
	private void initWindow(){
		LayoutInflater inflater=LayoutInflater.from(this);
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int sw=metrics.widthPixels;
		// Rotate Function PopupWindow
		ArrayList<String> lrotate=new ArrayList<String>();
		lrotate.add("X÷·"); lrotate.add("Y÷·");
		lrotate.add("Z÷·"); lrotate.add("πÿ±’");
		rotateFuncWindow=new SimpleFuncPopupWindow(this, 
				inflater.inflate(R.layout.list_func, null), 
				sw-100, LayoutParams.WRAP_CONTENT, lrotate, 2);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mRGDetector.onTouchEvent(event);
		return true;
	}
	
	class MyRGListener extends SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			float d=detector.getRotationDegreesDelta();
			switch(rotateFuncWindow.getSelected()){
			case 0: mRenderer.addRotate(d, 0, 0); break;
			case 1:	mRenderer.addRotate(0, d, 0); break;
			case 2:	mRenderer.addRotate(0, 0, d); break;
			default:break;
			}
			return true;
		}
	}

}

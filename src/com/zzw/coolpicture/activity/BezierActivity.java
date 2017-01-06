package com.zzw.coolpicture.activity;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.ClickMoveGestureDetector;
import com.zzw.coolpicture.util.ImageRecorder;
import com.zzw.coolpicture.util.MoveGestureDetector;
import com.zzw.coolpicture.view.BezierView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class BezierActivity extends Activity 
implements OnTouchListener {
	private BezierView mBezView;
	private View mBezBtmBar;
	private ImageButton mBezAnimator;
	private ImageButton mBezContPoint;
	private ImageButton mBezContLine;
	private ImageButton mBezAssiLine;
	private ImageButton mBezClear;
	private ImageButton mBezSave;
	private ClickMoveGestureDetector mCMGDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bezier);
		
		initView();
		initEvent();

		Intent intent=getIntent();
		String path=intent.getStringExtra("IMG_PATH");
		Bitmap bm=BitmapFactory.decodeFile(path);
		mBezView.setImageBitmap(bm);
	}

	private void initView(){
		mBezView=(BezierView)findViewById(R.id.bezier_view);
		mBezBtmBar=findViewById(R.id.bezier_btmbar);
		mBezAnimator=(ImageButton)findViewById(R.id.bezier_animator);
		mBezContPoint=(ImageButton)findViewById(R.id.bezier_contpoint);
		mBezContLine=(ImageButton)findViewById(R.id.bezier_contline);
		mBezAssiLine=(ImageButton)findViewById(R.id.bezier_assiline);
		mBezClear=(ImageButton)findViewById(R.id.bezier_clear);
		mBezSave=(ImageButton)findViewById(R.id.bezier_save);
	}
	
	private void initEvent(){
		mCMGDetector=new ClickMoveGestureDetector(this, new MyCMGListener());
		mBezView.setOnTouchListener(this);
		mBezAnimator.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBezView.startAnimator();
			}
		});
		mBezContPoint.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBezView.reverseDrawConPoint();
			}
		});
		mBezContLine.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBezView.reverseDrawContLine();
			}
		});
		mBezAssiLine.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBezView.reverseDrawAssiLine();
			}
		});
		mBezClear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBezView.clearControlPoint();
			}
		});
		mBezSave.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String path=ImageRecorder.restoreScreen(
						BezierActivity.this, mBezBtmBar.getHeight(), 
						ImageRecorder.STORAGE, null);
				if(path!=null)
					Toast.makeText(BezierActivity.this, "Õº∆¨±£¥Ê÷¡"+path, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(BezierActivity.this, "Õº∆¨±£¥Ê ß∞‹", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mCMGDetector.onTouchEvent(event);
		return true;
	}
	
	class MyCMGListener implements 
	ClickMoveGestureDetector.OnClickMoveGestureListener {
		@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			return true;
		}
		@Override
		public void onMoveEnd(MoveGestureDetector detector) {}
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			return true;
		}
		@Override
		public void onClick(MotionEvent event) {
			float x=event.getX();
			float y=event.getY();
			mBezView.addControlPoint(new PointF(x, y));
		}
	}

}

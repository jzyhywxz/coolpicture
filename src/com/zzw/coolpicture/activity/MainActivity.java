package com.zzw.coolpicture.activity;

import java.util.ArrayList;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.graphics.Circle;
import com.zzw.coolpicture.graphics.Graphic2DBean;
import com.zzw.coolpicture.graphics.ImageBean;
import com.zzw.coolpicture.graphics.Line;
import com.zzw.coolpicture.graphics.Oval;
import com.zzw.coolpicture.graphics.Polygon;
import com.zzw.coolpicture.graphics.Rectangle;
import com.zzw.coolpicture.util.ClickMoveGestureDetector;
import com.zzw.coolpicture.util.FuncBean;
import com.zzw.coolpicture.util.ImageRecorder;
import com.zzw.coolpicture.util.MoveGestureDetector;
import com.zzw.coolpicture.util.RotateGestureDetector;
import com.zzw.coolpicture.util.RotateGestureDetector.SimpleOnRotateGestureListener;
import com.zzw.coolpicture.view.DividerItemDecoration;
import com.zzw.coolpicture.view.FuncAdapter;
import com.zzw.coolpicture.view.Graphic2DView;
import com.zzw.coolpicture.view.SimpleFuncPopupWindow;
import com.zzw.coolpicture.view.SimpleFuncPopupWindow.OnClickWindowListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
implements OnTouchListener, OnClickWindowListener {
	private View mTopBar;					// 添加图元时显示的顶部条
	private ImageButton mTopBack;			// 顶部返回
	private TextView mTopTitle;				// 顶部标题
	private Button mTopOK;					// 顶部完成
	private Graphic2DView g2dView;			// 主显区
	private RecyclerView mBtmBar;			// 底部条
	// 手势监听器
	private ClickMoveGestureDetector mCMGDetector;
	private ScaleGestureDetector mSGDetector;
	private RotateGestureDetector mRGDetector;
	// 功能弹窗
	private SimpleFuncPopupWindow dragFuncWindow;
	private SimpleFuncPopupWindow scaleFuncWindow;
	private SimpleFuncPopupWindow rotateFuncWindow;
	private SimpleFuncPopupWindow shapeFuncWindow;
	// 功能列表
	private ArrayList<FuncBean> mFuncs;
	private boolean enShape=false;		// 正在添加图形
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initFunc();
		initView();
		initEvent();
		initGesture();
		initWindow();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode!=RESULT_OK)
			return;
		switch(requestCode){
		case 0:
			ArrayList<String> images=data.getStringArrayListExtra("select_image");
			for(String image:images)
				g2dView.addGraphic(new ImageBean(image, g2dView.getWidth(), g2dView.getHeight()));
			break;
		default:
			break;
		}
	}
	
	private void initFunc(){
		mFuncs=new ArrayList<FuncBean>();
		
		mFuncs.add(new FuncBean("drag",R.drawable.drag){
			@Override
			public void function(View v){
				dragFuncWindow.showAtCenter(g2dView);
			}
		});
		mFuncs.add(new FuncBean("scale",R.drawable.scale){
			@Override
			public void function(View v){
				scaleFuncWindow.showAtCenter(g2dView);
			}
		});
		mFuncs.add(new FuncBean("rotate",R.drawable.rotate){
			@Override
			public void function(View v){
				rotateFuncWindow.showAtCenter(g2dView);
			}
		});
		mFuncs.add(new FuncBean("image",R.drawable.image){
			@Override
			public void function(View v){
				Intent intent=new Intent(MainActivity.this,SelectImageActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		mFuncs.add(new FuncBean("shape",R.drawable.shape){
			@Override
			public void function(View v){
				shapeFuncWindow.showAtCenter(g2dView);
			}
		});
		mFuncs.add(new FuncBean("remove",R.drawable.remove){
			@Override
			public void function(View v){
				if(g2dView.getSelectedGraphic()!=null){
					g2dView.removeGraphic();
					g2dView.postInvalidate();
				}
			}
		});
		mFuncs.add(new FuncBean("curve",R.drawable.curve){
			@Override
			public void function(View v){
				String path=ImageRecorder.restoreScreen(MainActivity.this, 
						mBtmBar.getHeight(), ImageRecorder.CACHE, null);
				if(path!=null){
					Intent intent=new Intent(MainActivity.this, BezierActivity.class);
					intent.putExtra("IMG_PATH", path);
					startActivity(intent);
				}
			}
		});
		mFuncs.add(new FuncBean("fill",R.drawable.fill){
			@Override
			public void function(View v){
				String path=ImageRecorder.restoreScreen(MainActivity.this, 
						mBtmBar.getHeight(), ImageRecorder.CACHE, null);
				// 开启活动
				if(path!=null){
					Intent intent=new Intent(MainActivity.this, FillImageActivity.class);
					intent.putExtra("IMG_PATH", path);
					startActivity(intent);
				}
			}
		});
		mFuncs.add(new FuncBean("cut",R.drawable.cut){
			@Override
			public void function(View view){
				String path=ImageRecorder.restoreScreen(MainActivity.this, 
						mBtmBar.getHeight(), ImageRecorder.CACHE, null);
				if(path!=null){
					Intent intent=new Intent(MainActivity.this, CutImageActivity.class);
					intent.putExtra("IMG_PATH", path);
					startActivity(intent);
				}
			}
		});
		mFuncs.add(new FuncBean("3D",R.drawable.td){
			@Override
			public void function(View view){
				Intent intent=new Intent(MainActivity.this, TDImageActivity.class);
				startActivity(intent);
			}
		});
		mFuncs.add(new FuncBean("save",R.drawable.save){
			@Override
			public void function(View view){
				String path=ImageRecorder.restoreScreen(
						MainActivity.this, mBtmBar.getHeight(), 
						ImageRecorder.STORAGE, null);
				if(path!=null)
					Toast.makeText(MainActivity.this, "图片保存至"+path, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MainActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initView(){
		mTopBar=findViewById(R.id.main_topbar);
		mTopBack=(ImageButton)mTopBar.findViewById(R.id.top_back);
		mTopTitle=(TextView)mTopBar.findViewById(R.id.top_title);
		mTopTitle.setText("添加图形");
		mTopOK=(Button)mTopBar.findViewById(R.id.top_ok);
		
		g2dView=(Graphic2DView)findViewById(R.id.g2d_view);
		
		mBtmBar=(RecyclerView)findViewById(R.id.main_btmbar);
	}

	private void initEvent(){
		mTopBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Graphic2DBean bean=g2dView.getSelectedGraphic();
				if(bean==null || bean instanceof Polygon)
					g2dView.removeGraphic();
				enShape=false;
				mTopBar.setVisibility(View.GONE);
			}
		});
		mTopOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				enShape=false;
				mTopBar.setVisibility(View.GONE);
			}
		});
		
		g2dView.setOnTouchListener(this);
		
		mBtmBar.setAdapter(new FuncAdapter(this, mFuncs));
		LinearLayoutManager manager=new LinearLayoutManager(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mBtmBar.setLayoutManager(manager);
		mBtmBar.addItemDecoration(new DividerItemDecoration(
				this, DividerItemDecoration.HORIZONTAL));
	}

	private void initGesture(){
		mCMGDetector=new ClickMoveGestureDetector(
				getApplicationContext(), new MyCMGListener());
		mSGDetector=new ScaleGestureDetector(
				getApplicationContext(), new MySGListener());
		mRGDetector=new RotateGestureDetector(
				getApplicationContext(), new MyRGListener());
	}
	
	private void initWindow(){
		LayoutInflater inflater=LayoutInflater.from(this);
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int sw=metrics.widthPixels;

		// Drag Function PopupWindow
		ArrayList<String> ldrag=new ArrayList<String>();
		ldrag.add("双向"); ldrag.add("横向");
		ldrag.add("纵向"); ldrag.add("关闭");
		dragFuncWindow=new SimpleFuncPopupWindow(this, 
				inflater.inflate(R.layout.list_func, null), 
				sw-100, LayoutParams.WRAP_CONTENT, ldrag, 0);
		// Scale Function PopupWindow
		ArrayList<String> lscale=new ArrayList<String>();
		lscale.add("双向"); lscale.add("横向");
		lscale.add("纵向"); lscale.add("关闭");
		scaleFuncWindow=new SimpleFuncPopupWindow(this, 
				inflater.inflate(R.layout.list_func, null), 
				sw-100, LayoutParams.WRAP_CONTENT, lscale, 0);
		// Rotate Function PopupWindow
		ArrayList<String> lrotate=new ArrayList<String>();
		lrotate.add("X轴"); lrotate.add("Y轴");
		lrotate.add("Z轴"); lrotate.add("关闭");
		rotateFuncWindow=new SimpleFuncPopupWindow(this, 
				inflater.inflate(R.layout.list_func, null), 
				sw-100, LayoutParams.WRAP_CONTENT, lrotate, 2);
		// Shape Function PopupWindow
		ArrayList<String> lshape=new ArrayList<String>();
		lshape.add("直线"); lshape.add("椭圆");
		lshape.add("圆形"); lshape.add("矩形");
		lshape.add("多边形");
		shapeFuncWindow=new SimpleFuncPopupWindow(this, 
				inflater.inflate(R.layout.list_func, null), 
				sw-100, LayoutParams.WRAP_CONTENT, lshape, 0);
		shapeFuncWindow.setClicker(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mCMGDetector.onTouchEvent(event);
		mSGDetector.onTouchEvent(event);
		mRGDetector.onTouchEvent(event);
		return true;
	}
	
	class MyCMGListener implements 
	ClickMoveGestureDetector.OnClickMoveGestureListener {
		@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			if(enShape){	// 如果正在添加图形，则记录图形起点
				PointF beg=detector.getCurrFocusInternal();
				switch(shapeFuncWindow.getSelected()){
				case 0:		// line
					g2dView.addGraphic(new Line(beg, beg));
					break;
				case 1:		// oval
					g2dView.addGraphic(new Oval(beg, beg));
					break;
				case 2:		// circle
					g2dView.addGraphic(new Circle(beg, beg));
					break;
				case 3:		// rectangle
					g2dView.addGraphic(new Rectangle(beg, beg));
					break;
				default:
					break;
				}
			}
			return true;
		}
		@Override
		public void onMoveEnd(MoveGestureDetector detector) {
			if(enShape && shapeFuncWindow.getSelected()<4){
				enShape=false;
				g2dView.getSelectedGraphic().
				setEndPoint(detector.getCurrFocusInternal());
			}
		}
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			Graphic2DBean bean=g2dView.getSelectedGraphic();
			if(enShape){	// 正在添加图形
				if(shapeFuncWindow.getSelected()>=4)
					return true;
				bean.setEndPoint(detector.getCurrFocusInternal());
				g2dView.postInvalidate();
			}
			else if(bean!=null){
				PointF d=detector.getFocusDelta();
				switch(dragFuncWindow.getSelected()){
				case 0:	bean.addTrans(d.x, d.y); break;
				case 1: bean.addTrans(d.x, 0);   break;
				case 2:	bean.addTrans(0, d.y);   break;
				default:return true;
				}
				g2dView.postInvalidate();
			}
			return true;
		}
		@Override
		public void onClick(MotionEvent event) {
			if(enShape){
				PointF p=new PointF(event.getX(), event.getY());
				Graphic2DBean bean=g2dView.getSelectedGraphic();
				switch(shapeFuncWindow.getSelected()){
				case 4:
					bean.addPoint(p);
					break;
				default:
					return;
				}
				g2dView.postInvalidate();
			}
			else{
				// SELECTED BEAN
				Graphic2DBean bean1=g2dView.getSelectedGraphic();
				// SELECTING BEAN
				Graphic2DBean bean2=g2dView.
						selectGraphic(event.getX(), event.getY());
				if(bean1==null && bean2==null)
					return;
				if(bean1!=null && bean2!=null && bean1.equals(bean2))
					return;
				g2dView.postInvalidate();
			}
		}
	}
	
	class MySGListener extends SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(enShape)
				return false;
			Graphic2DBean bean=g2dView.getSelectedGraphic();
			if(bean!=null){
				float d=detector.getScaleFactor();
				switch(scaleFuncWindow.getSelected()){
				case 0:	bean.addScale(d, d); break;
				case 1: bean.addScale(d, 1); break;
				case 2: bean.addScale(1, d); break;
				default:return true;
				}
				g2dView.postInvalidate();
			}
			return true;
		}
	}
	
	class MyRGListener extends SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			if(enShape)
				return false;
			Graphic2DBean bean=g2dView.getSelectedGraphic();
			if(bean!=null){
				float d=detector.getRotationDegreesDelta();
				switch(rotateFuncWindow.getSelected()){
				case 0: bean.addRotate(d, 0, 0); break;
				case 1:	bean.addRotate(0, d, 0); break;
				case 2:	bean.addRotate(0, 0, d); break;
				default:return true;
				}
				g2dView.postInvalidate();
			}
			return true;
		}
	}

	@Override
	public void onClick(int position) {
		enShape=true;
		if(position>=4){
			mTopBar.setVisibility(View.VISIBLE);
			g2dView.addGraphic(new Polygon());
		}
	}
	
}

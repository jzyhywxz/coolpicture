package com.zzw.coolpicture.activity;

import java.util.ArrayList;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.graphics.Circle;
import com.zzw.coolpicture.graphics.Graphic2DBean;
import com.zzw.coolpicture.graphics.Hexagon;
import com.zzw.coolpicture.graphics.Oval;
import com.zzw.coolpicture.graphics.Pentagon;
import com.zzw.coolpicture.graphics.Rectangle;
import com.zzw.coolpicture.graphics.RoundRect;
import com.zzw.coolpicture.graphics.Square;
import com.zzw.coolpicture.graphics.Triangle;
import com.zzw.coolpicture.util.FuncBean;
import com.zzw.coolpicture.util.ImageRecorder;
import com.zzw.coolpicture.util.MoveGestureDetector;
import com.zzw.coolpicture.view.CutImageView;
import com.zzw.coolpicture.view.DividerItemDecoration;
import com.zzw.coolpicture.view.FuncAdapter;
import com.zzw.coolpicture.view.SimpleFuncPopupWindow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CutImageActivity extends Activity 
implements OnTouchListener {
	private CutImageView mImgView;
	private RecyclerView mBtmBar;
	private ArrayList<FuncBean> mFuncs;	// 功能列表
	// 手势监听器
	private MoveGestureDetector mMGDetector;
	private ScaleGestureDetector mSGDetector;
	// 功能弹窗
	private SimpleFuncPopupWindow dragFuncWindow;
	private SimpleFuncPopupWindow scaleFuncWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cut_img);
		
		initFunc();
		initView();
		initDetector();
		initWindow();
		
		Intent intent=getIntent();
		String path=intent.getStringExtra("IMG_PATH");
		Bitmap bm=BitmapFactory.decodeFile(path);
		mImgView.setImageBitmap(bm);
	}
	
	private void initFunc(){
		mFuncs=new ArrayList<FuncBean>();
		
		mFuncs.add(new FuncBean("drag", R.drawable.drag){
			@Override
			public void function(View view){
				dragFuncWindow.showAtCenter(mImgView);
			}
		});
		mFuncs.add(new FuncBean("scale", R.drawable.scale){
			@Override
			public void function(View view){
				scaleFuncWindow.showAtCenter(mImgView);
			}
		});
		mFuncs.add(new FuncBean("rectangle", R.drawable.rectangle){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				Rectangle rect=new Rectangle(vw-100, vh-100){
					@Override
					public void setPaint(Paint paint){}
				};
				rect.setTransX(50);
				rect.setTransY(50);
				mImgView.setBorder(rect);
			}
		});
		mFuncs.add(new FuncBean("roundrect", R.drawable.roundrect){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				RoundRect rr=new RoundRect(vw-100, vh-100, 50, 50){
					@Override
					public void setPaint(Paint paint){}
				};
				rr.setTransX(50);
				rr.setTransY(50);
				mImgView.setBorder(rr);
			}
		});
		mFuncs.add(new FuncBean("circle", R.drawable.circle){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				Circle circle=new Circle(vw/2-50){
					@Override
					public void setPaint(Paint paint){}
				};
				circle.setTransX(50);
				circle.setTransY(vh/2-vw/2+50);
				mImgView.setBorder(circle);
			}
		});
		mFuncs.add(new FuncBean("oval", R.drawable.oval){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				Oval oval=new Oval(vw-100, vh-100){
					@Override
					public void setPaint(Paint paint){}
				};
				oval.setTransX(50);
				oval.setTransY(50);
				mImgView.setBorder(oval);
			}
		});
		mFuncs.add(new FuncBean("triangle", R.drawable.triangle){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				mImgView.setBorder(new Triangle(vw/2, vh/2, vw-100){
					@Override
					public void setPaint(Paint paint){}
				});
			}
		});
		mFuncs.add(new FuncBean("square", R.drawable.square){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				mImgView.setBorder(new Square(vw/2, vh/2, vw-100){
					@Override
					public void setPaint(Paint paint){}
				});
			}
		});
		mFuncs.add(new FuncBean("pentagon", R.drawable.pentagon){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				mImgView.setBorder(new Pentagon(vw/2, vh/2, vw/2){
					@Override
					public void setPaint(Paint paint){}
				});
			}
		});
		mFuncs.add(new FuncBean("hexagon", R.drawable.hexagon){
			@Override
			public void function(View view){
				int vw=mImgView.getWidth();
				int vh=mImgView.getHeight();
				mImgView.setBorder(new Hexagon(vw/2, vh/2, vw/2-50){
					@Override
					public void setPaint(Paint paint){}
				});
			}
		});
		mFuncs.add(new FuncBean("ok", R.drawable.ok){
			@Override
			public void function(View view){
				// 画笔
				Paint paint=new Paint();
				paint.setAntiAlias(true);
				paint.setFilterBitmap(true);
				// DEST BITMAP
				Bitmap dest=Bitmap.createBitmap(mImgView.getWidth(), 
						mImgView.getHeight(), Config.ARGB_8888);
				// SOUR BITMAP
				Bitmap sour=((BitmapDrawable)mImgView.getDrawable()).
						getBitmap();
				// 画布
				Canvas canvas=new Canvas(dest);
				Graphic2DBean border=mImgView.getBorder();
				Path path=border.getPath();
				canvas.drawPath(path, paint);
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
				canvas.drawBitmap(sour, 0, 0, paint);
				mImgView.setImageBitmap(dest);
			}
		});
		mFuncs.add(new FuncBean("cancel", R.drawable.cancel){
			@Override
			public void function(View view){
				finish();
			}
		});
		mFuncs.add(new FuncBean("save", R.drawable.save){
			@Override
			public void function(View view){
				Bitmap bm=((BitmapDrawable)mImgView.getDrawable()).
						getBitmap();
				Bitmap im=ImageRecorder.trim(bm);
				if(im!=null)
					bm=im;
				String path=ImageRecorder.restoreImage(CutImageActivity.this, 
						bm, ImageRecorder.STORAGE, null);
				if(path!=null)
					Toast.makeText(CutImageActivity.this, 
							"图片保存至"+path, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initView(){
		mImgView=(CutImageView)findViewById(R.id.cut_img_view);
		
		mBtmBar=(RecyclerView)findViewById(R.id.cut_img_btmbar);
		mBtmBar.setAdapter(new FuncAdapter(this, mFuncs));
		LinearLayoutManager manager=new LinearLayoutManager(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mBtmBar.setLayoutManager(manager);
		mBtmBar.addItemDecoration(new DividerItemDecoration(
				this, DividerItemDecoration.HORIZONTAL));
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
	}
	
	private void initDetector(){
		mMGDetector=new MoveGestureDetector(this, new MyMGListener());
		mSGDetector=new ScaleGestureDetector(this, new MySGListener());
		mImgView.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mMGDetector.onTouchEvent(event);
		mSGDetector.onTouchEvent(event);
		return true;
	}
	
	class MyMGListener implements 
	MoveGestureDetector.OnMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			Graphic2DBean bean=mImgView.getBorder();
			PointF d=detector.getFocusDelta();
			switch(dragFuncWindow.getSelected()){
			case 0:	bean.addTrans(d.x, d.y); break;
			case 1: bean.addTrans(d.x, 0);   break;
			case 2:	bean.addTrans(0, d.y);   break;
			default:return true;
			}
			mImgView.postInvalidate();
			return true;
		}
		@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			return true;
		}
		@Override
		public void onMoveEnd(MoveGestureDetector detector) {}
	}
	class MySGListener extends SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			Graphic2DBean bean=mImgView.getBorder();
			float d=detector.getScaleFactor();
			switch(scaleFuncWindow.getSelected()){
			case 0:	bean.addScale(d, d); break;
			case 1: bean.addScale(d, 1); break;
			case 2: bean.addScale(1, d); break;
			default:return true;
			}
			mImgView.postInvalidate();
			return true;
		}
	}

}

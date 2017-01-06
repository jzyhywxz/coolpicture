package com.zzw.coolpicture.view;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class BezierView extends ImageView {
	private ArrayList<ArrayList<PointF>> mPoints;
	private Paint mPaint;
	private int[] mColor;
	private Path mContPath;					// 控制路径
	private Path mCurvPath;					// 曲线路径
	private float u=0.0f;					// 比例系数
	private boolean isDrawConPoint=true;	// 是否绘制控制顶点
	private boolean isDrawContLine=true;	// 是否绘制控制路径
	private boolean isDrawAssiLine=true;	// 是否绘制辅助路径
	private boolean isRunAnimation=false;	// 是否绘制曲线动画
	private boolean isLineComplete=false;	// 是否绘制完成曲线

	public void reverseDrawConPoint(){
		isDrawConPoint=!isDrawConPoint;
		invalidate();
	}
	public void reverseDrawContLine(){
		isDrawContLine=!isDrawContLine;
		invalidate();
	}
	public void reverseDrawAssiLine(){
		isDrawAssiLine=!isDrawAssiLine;
		invalidate();
	}
	
	public BezierView(Context context) {
		this(context, null);
	}
	public BezierView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPoints=new ArrayList<ArrayList<PointF>>();
		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3);
		mColor=new int[]{
				0xffff0000, 0xffff7900, 0xffffff00, 0xff00ff00, 
				0xff00ffff, 0xff0000ff, 0xffff00ff, 
		};
		mContPath=new Path();
		mCurvPath=new Path();
	}
	
	public void addControlPoint(PointF p){
		if(mPoints.size()<=0)
			mPoints.add(new ArrayList<PointF>());
		ArrayList<PointF> zeroOrder=mPoints.get(0);
		zeroOrder.add(p);
		invalidate();
	}
	public void clearControlPoint(){
		mPoints.clear();
		mContPath.reset();
		mCurvPath.reset();
		isLineComplete=false;
		invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(mPoints.size()<=0)
			return;
		// 绘制控制线
		if(isDrawContLine){
			mContPath.reset();
			mPaint.setColor(mColor[0]);
			ArrayList<PointF> zeroOrder=mPoints.get(0);
			PointF p=zeroOrder.get(0);
			mContPath.moveTo(p.x, p.y);
			for(int i=1;i<zeroOrder.size();i++){
				p=zeroOrder.get(i);
				mContPath.lineTo(p.x, p.y);
			}
			canvas.drawPath(mContPath, mPaint);
		}
		// 绘制控制点
		if(isDrawConPoint){
			mPaint.setStyle(Style.FILL);
			mPaint.setColor(0xff790000);
			ArrayList<PointF> zeroOrder=mPoints.get(0);
			for(int i=0;i<zeroOrder.size();i++){
				PointF p=zeroOrder.get(i);
				canvas.drawCircle(p.x, p.y, 5, mPaint);
			}
			mPaint.setStyle(Style.STROKE);
		}
		if(isRunAnimation){
			PointF p;
			if(mCurvPath.isEmpty()){
				p=mPoints.get(0).get(0);
				mCurvPath.moveTo(p.x, p.y);
			}
			else{
				p=getCurvePoint(u);
				mCurvPath.lineTo(p.x, p.y);
			}
			
			if(isDrawAssiLine){
				for(int i=1;i<mPoints.size()-1;i++)
					drawPath(canvas, mPoints.get(i), i);
			}

			mPaint.setColor(0xff000000);
			canvas.drawPath(mCurvPath, mPaint);
		}
		if(isLineComplete){
			mPaint.setColor(0xff000000);
			canvas.drawPath(mCurvPath, mPaint);
		}
	}

	protected PointF getCurvePoint(float u){
		if(mPoints.size()<=0)
			return null;
		
		while(mPoints.size()>1)
			mPoints.remove(mPoints.size()-1);
		
		ArrayList<PointF> targOrder=mPoints.get(0);	// i阶
		while(targOrder.size()>1){
			ArrayList<PointF> highOrder=new ArrayList<PointF>();
			for(int i=0;i<targOrder.size()-1;i++)
				highOrder.add(getUPoint(targOrder.get(i), targOrder.get(i+1), u));
			mPoints.add(highOrder);					// i+1阶
			targOrder=highOrder;
		}
		return targOrder.get(0);
	}
	
	protected PointF getUPoint(PointF beg, PointF end, float u){
		float x=(1-u)*beg.x+u*end.x;
		float y=(1-u)*beg.y+u*end.y;
		return new PointF(x, y);
	}
	
	protected void drawPath(Canvas canvas, ArrayList<PointF> points, int order){
		mContPath.reset();
		PointF p=points.get(0);
		mContPath.moveTo(p.x, p.y);
		for(int i=1;i<points.size();i++){
			p=points.get(i);
			mContPath.lineTo(p.x, p.y);
		}
		
		mPaint.setColor(mColor[order%mColor.length]);
		canvas.drawPath(mContPath, mPaint);
	}
	
	public void startAnimator(){
		getAnimator(3000).start();
	}
	
	public ObjectAnimator getAnimator(int duration){
		ObjectAnimator animator=ObjectAnimator.ofFloat(
				BezierView.this, "u", 0.0f, 1.0f);
		animator.setDuration(duration);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				u=(Float)animation.getAnimatedValue();
				postInvalidate();
			}
		});
		animator.addListener(new AnimatorListenerAdapter(){
			@Override
			public void onAnimationStart(Animator animation) {
				isLineComplete=false;
				isRunAnimation=true;
				u=0.0f;
				mCurvPath.reset();
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				u=1.0f;
				isRunAnimation=false;
				isLineComplete=true;
			}
		});
		return animator;
	}
}

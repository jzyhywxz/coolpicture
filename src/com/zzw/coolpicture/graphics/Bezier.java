package com.zzw.coolpicture.graphics;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class Bezier {
	private ArrayList<ArrayList<PointF>> mPoints;
	private Path mContPath;					// 控制路径
	private Path mCurvPath;					// 曲线路径
	private float u=0.0f;					// 比例系数
	private boolean isRunAnimation=false;	// 是否播放绘制动画
	private boolean isLineComplete=false;	// 是否绘制完成曲线
	
	public Bezier() {
		mPoints=new ArrayList<ArrayList<PointF>>();
		mContPath=new Path();
		mCurvPath=new Path();
	}
	
	public boolean isEmpty(){
		return mPoints.size()<=0;
	}
	
	public void addControlPoint(PointF p){
		if(mPoints.size()<=0)
			mPoints.add(new ArrayList<PointF>());
		ArrayList<PointF> zeroOrder=mPoints.get(0);
		zeroOrder.add(p);
	}
	public void clearControlPoint(){
		mPoints.clear();
	}
	
	public void adjustCurvePath(){
		ArrayList<PointF> zeroOrder=mPoints.get(0);
		PointF p=zeroOrder.get(zeroOrder.size()-1);
		mCurvPath.lineTo(p.x, p.y);
	}
	
	public void draw(Canvas canvas, Paint paint){
		if(mPoints.size()<=0)
			return;
		
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
			paint.setColor(0xff000000);
			canvas.drawPath(mCurvPath, paint);
		}
		
		if(isLineComplete){
			paint.setColor(0xff000000);
			canvas.drawPath(mCurvPath, paint);
		}
	}
	
	// 绘制控制线
	public void drawControlLine(Canvas canvas, Paint paint){
		mContPath.reset();
		paint.setColor(0xffff0000);
		ArrayList<PointF> zeroOrder=mPoints.get(0);
		PointF p=zeroOrder.get(0);
		mContPath.moveTo(p.x, p.y);
		for(int i=1;i<zeroOrder.size();i++){
			p=zeroOrder.get(i);
			mContPath.lineTo(p.x, p.y);
		}
		canvas.drawPath(mContPath, paint);
	}
	// 绘制控制点
	public void drawControlPoint(Canvas canvas, Paint paint){
		paint.setStyle(Style.FILL);
		paint.setColor(0xff790000);
		ArrayList<PointF> zeroOrder=mPoints.get(0);
		for(int i=0;i<zeroOrder.size();i++){
			PointF p=zeroOrder.get(i);
			canvas.drawCircle(p.x, p.y, 5, paint);
		}
		paint.setStyle(Style.STROKE);
	}
	
	// 绘制辅助线
	public void drawAssistantLine(Canvas canvas, Paint paint, int[] colors){
		for(int i=1;i<mPoints.size()-1;i++){
			ArrayList<PointF> points=mPoints.get(i);
			mContPath.reset();
			PointF p=points.get(0);
			mContPath.moveTo(p.x, p.y);
			for(int j=1;j<points.size();j++){
				p=points.get(j);
				mContPath.lineTo(p.x, p.y);
			}
			paint.setColor(colors[i%colors.length]);
			canvas.drawPath(mContPath, paint);
		}
	}
	
	// 计算曲线点
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
	
	// 计算比例点
	protected PointF getUPoint(PointF beg, PointF end, float u){
		float x=(1-u)*beg.x+u*end.x;
		float y=(1-u)*beg.y+u*end.y;
		return new PointF(x, y);
	}
	
	// 绘制动画
	public ObjectAnimator getAnimator(final View view, int duration){
		ObjectAnimator animator=ObjectAnimator.ofFloat(
				Bezier.this, "u", 0.0f, 1.0f);
		animator.setDuration(duration);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				u=(Float)animation.getAnimatedValue();
				view.postInvalidate();
			}
		});
		animator.addListener(new AnimatorListenerAdapter(){
			@Override
			public void onAnimationStart(Animator animation) {
				u=0.0f;
				mCurvPath.reset();
				isLineComplete=false;
				isRunAnimation=true;
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				isRunAnimation=false;
				isLineComplete=true;
				adjustCurvePath();
				u=1.0f;
			}
		});
		return animator;
	}
}

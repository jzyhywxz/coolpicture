package com.zzw.coolpicture.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;

public class Circle extends Oval {
	public Circle() {
		super();
	}
	public Circle(float radius){
		super(radius*2, radius*2);
	}
	public Circle(PointF b, PointF e){
		super(b, e);
	}
	
	@Override
	public void setEndPoint(PointF p){
		mEnd=p;
		mWidth=Math.abs(mEnd.x-mBeg.x);
		mHeight=Math.abs(mEnd.y-mBeg.y);
		mWidth=mHeight=Math.min(mWidth, mHeight);
		if(mEnd.x<mBeg.x)
			mTransX=mBeg.x-mWidth;
		else
			mTransX=mBeg.x;
		if(mEnd.y<mBeg.y)
			mTransY=mBeg.y-mHeight;
		else
			mTransY=mBeg.y;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		int cx=Math.round(mTransX+mWidth/2);
		int cy=Math.round(mTransY+mHeight/2);
		float radius=mWidth/2;
		
		float[] points=Graphic2DPainter.getCirclePoints(
				cx, cy, radius);
		if(points!=null)
			canvas.drawPoints(points, paint);
		
		//canvas.drawCircle(mTransX+mWidth/2, mTransY+mHeight/2, 
		//		mWidth/2, mPaint);
	}

	@Override
	public Path getPath(){
		Path path=new Path();
		path.addCircle(mTransX+mWidth/2, mTransY+mHeight/2, 
				mWidth/2, Direction.CW);
		return path;
	}
}

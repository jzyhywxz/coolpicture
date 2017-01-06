package com.zzw.coolpicture.graphics;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;

public class Rectangle extends Graphic2DBean {
	public Rectangle() {
		super();
	}
	public Rectangle(float w, float h){
		super(w, h);
	}
	public Rectangle(PointF b, PointF e){
		super(b, e);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		ArrayList<PointF> verties=new ArrayList<PointF>();
		verties.add(new PointF(mTransX, mTransY));
		verties.add(new PointF(mTransX+mWidth, mTransY));
		verties.add(new PointF(mTransX+mWidth, mTransY+mHeight));
		verties.add(new PointF(mTransX, mTransY+mHeight));
		float[] points=Graphic2DPainter.getPolygonPoints(verties);
		if(points!=null)
			canvas.drawPoints(points, paint);
		//canvas.drawRect(mTransX, mTransY, mTransX+mWidth, 
		//		mTransY+mHeight, mPaint);
	}
	@Override
	public Path getPath() {
		Path path=new Path();
		path.addRect(getRegion(), Direction.CW);
		return path;
	}

}

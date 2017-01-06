package com.zzw.coolpicture.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;

public class Oval extends Graphic2DBean {
	public Oval() {
		super();
	}
	public Oval(float w, float h){
		super(w, h);
	}
	public Oval(PointF b, PointF e){
		super(b, e);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		int cx=Math.round(mTransX+mWidth/2);
		int cy=Math.round(mTransY+mHeight/2);
		float rx=mWidth/2;
		float ry=mHeight/2;
		
		float[] points=Graphic2DPainter.getOvalPoints(
				cx, cy, rx, ry);
		if(points!=null)
			canvas.drawPoints(points, paint);
		
		//canvas.drawOval(getRegion(), mPaint);
	}
	@Override
	public Path getPath() {
		Path path=new Path();
		path.addOval(getRegion(), Direction.CW);
		return path;
	}

}

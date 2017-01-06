package com.zzw.coolpicture.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Line extends Graphic2DBean {
	public Line(){
		super();
	}
	public Line(PointF b, PointF e) {
		super(b, e);
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		float[] points=Graphic2DPainter.getLinePoints(mBeg, mEnd);
		if(points!=null)
			canvas.drawPoints(points, paint);
		
		//canvas.drawLine(begin.x, begin.y, end.x, end.y, mPaint);
	}
	@Override
	public Path getPath() {
		Path path=new Path();
		path.moveTo(mBeg.x, mBeg.y);
		path.lineTo(mEnd.x, mEnd.y);
		return path;
	}

}

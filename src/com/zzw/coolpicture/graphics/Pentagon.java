package com.zzw.coolpicture.graphics;

import android.graphics.PointF;

public class Pentagon extends Polygon {
	private float cx, cy;
	
	public Pentagon(){
		super();
	}
	public Pentagon(float cx, float cy, float len){
		this.cx=cx;
		this.cy=cy;
		float edge0=(float)(len/(2*Math.sin(Math.PI/5)));
		float edge1=(float)(len*Math.sin(0.3*Math.PI));
		float edge2=(float)(edge1/Math.tan(0.4*Math.PI));
		float edge3=(float)(len/(2*Math.tan(Math.PI/5)));
		verties.add(new PointF(cx, cy-edge0));
		verties.add(new PointF(cx+edge1, cy-edge2));
		verties.add(new PointF(cx+len/2, cy+edge3));
		verties.add(new PointF(cx-len/2, cy+edge3));
		verties.add(new PointF(cx-edge1, cy-edge2));
		calculateRegion();
	}
	
	@Override
	public void addTrans(float dx, float dy){
		super.addTrans(dx, dy);
		cx+=dx;
		cy+=dy;
	}

	@Override
	public void addScale(float dx, float dy){
		// 平均缩放度
		float d=(dx+dy)/2;
		// 缩放前边长
		PointF p2=verties.get(2);
		PointF p3=verties.get(3);
		float len=Math.abs(p2.x-p3.x);
		// 计算新顶点
		len*=d;
		float edge0=(float)(len/(2*Math.sin(Math.PI/5)));
		float edge1=(float)(len*Math.sin(0.3*Math.PI));
		float edge2=(float)(edge1/Math.tan(0.4*Math.PI));
		float edge3=(float)(len/(2*Math.tan(Math.PI/5)));
		verties.clear();
		verties.add(new PointF(cx, cy-edge0));
		verties.add(new PointF(cx+edge1, cy-edge2));
		verties.add(new PointF(cx+len/2, cy+edge3));
		verties.add(new PointF(cx-len/2, cy+edge3));
		verties.add(new PointF(cx-edge1, cy-edge2));
		calculateRegion();
	}
}

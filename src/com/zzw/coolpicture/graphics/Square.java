package com.zzw.coolpicture.graphics;

import android.graphics.PointF;

public class Square extends Polygon {
	private float cx, cy;
	
	public Square(){
		super();
	}
	public Square(float cx, float cy, float len){
		this.cx=cx;
		this.cy=cy;
		verties.add(new PointF(cx-len/2, cy-len/2));
		verties.add(new PointF(cx+len/2, cy-len/2));
		verties.add(new PointF(cx+len/2, cy+len/2));
		verties.add(new PointF(cx-len/2, cy+len/2));
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
		PointF p0=verties.get(0);
		PointF p1=verties.get(1);
		float len=Math.abs(p1.x-p0.x);
		// 计算新顶点
		len*=d;
		verties.clear();
		verties.add(new PointF(cx-len/2, cy-len/2));
		verties.add(new PointF(cx+len/2, cy-len/2));
		verties.add(new PointF(cx+len/2, cy+len/2));
		verties.add(new PointF(cx-len/2, cy+len/2));
		calculateRegion();
	}
}

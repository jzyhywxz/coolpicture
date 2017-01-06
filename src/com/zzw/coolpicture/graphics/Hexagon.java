package com.zzw.coolpicture.graphics;

import android.graphics.PointF;

public class Hexagon extends Polygon {
	private float cx, cy;
	
	public Hexagon(){
		super();
	}
	public Hexagon(float cx, float cy, float len){
		this.cx=cx;
		this.cy=cy;
		float edge=(float)(len/(2*Math.tan(Math.PI/6)));
		verties.add(new PointF(cx-len/2, cy-edge));
		verties.add(new PointF(cx+len/2, cy-edge));
		verties.add(new PointF(cx+len, cy));
		verties.add(new PointF(cx+len/2, cy+edge));
		verties.add(new PointF(cx-len/2, cy+edge));
		verties.add(new PointF(cx-len, cy));
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
		float edge=(float)(len/(2*Math.tan(Math.PI/6)));
		verties.clear();
		verties.add(new PointF(cx-len/2, cy-edge));
		verties.add(new PointF(cx+len/2, cy-edge));
		verties.add(new PointF(cx+len, cy));
		verties.add(new PointF(cx+len/2, cy+edge));
		verties.add(new PointF(cx-len/2, cy+edge));
		verties.add(new PointF(cx-len, cy));
		calculateRegion();
	}
}

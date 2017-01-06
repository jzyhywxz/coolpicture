package com.zzw.coolpicture.graphics;

import android.graphics.PointF;

public class Triangle extends Polygon {
	private float cx, cy;
	
	public Triangle(){
		super();
	}
	public Triangle(float cx, float cy, float len){
		this.cx=cx;
		this.cy=cy;
		float edgel=(float)(len/(2*Math.cos(Math.PI/6)));
		float edges=(float)(len*Math.tan(Math.PI/6)/2);
		verties.add(new PointF(cx, cy-edgel));
		verties.add(new PointF(cx-len/2, cy+edges));
		verties.add(new PointF(cx+len/2, cy+edges));
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
		// ƽ�����Ŷ�
		float d=(dx+dy)/2;
		// ����ǰ�߳�
		PointF p1=verties.get(1);
		PointF p2=verties.get(2);
		float len=Math.abs(p2.x-p1.x);
		// �����¶���
		len*=d;
		float edgel=(float)(len/(2*Math.cos(Math.PI/6)));
		float edges=(float)(len*Math.tan(Math.PI/6)/2);
		verties.clear();
		verties.add(new PointF(cx, cy-edgel));
		verties.add(new PointF(cx-len/2, cy+edges));
		verties.add(new PointF(cx+len/2, cy+edges));
		calculateRegion();
	}
}

package com.zzw.coolpicture.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Polygon extends Graphic2DBean {
	protected List<PointF> verties=new ArrayList<PointF>();

	public Polygon(){
		super();
	}
	public Polygon(List<PointF> vert){
		verties=vert;
		calculateRegion();
	}
	
	@Override
	public void addPoint(PointF p){
		verties.add(p);
		calculateRegion();
	}
	
	protected void calculateRegion(){
		if(verties==null)
			return;
		float left=verties.get(0).x, right=left;
		float top=verties.get(0).y, buttom=top;
		for(int i=1;i<verties.size();i++){
			PointF p=verties.get(i);
			if(p.x<left)		left=p.x;
			else if(p.x>right)	right=p.x;
			if(p.y<top)			top=p.y;
			else if(p.y>buttom)	buttom=p.y;
		}
		mTransX=left;
		mTransY=top;
		mWidth=right-left;
		mHeight=buttom-top;
	}
	
	@Override
	public void addTrans(float dx, float dy){
		mTransX+=dx;
		mTransY+=dy;
		for(int i=0;i<verties.size();i++){
			PointF p=verties.get(i);
			p.x+=dx;
			p.y+=dy;
		}
	}

	@Override
	public void addScale(float dx, float dy){
		mScaleX*=dx;
		mScaleY*=dy;
		float ddx=(dx-1)*mWidth/2;
		float ddy=(dy-1)*mHeight/2;
		mTransX-=ddx;
		mTransY-=ddy;
		mWidth*=dx;
		mHeight*=dy;
		// 获取中心点
		float cx=mTransX+mWidth/2;
		float cy=mTransY+mHeight/2;
		for(int i=0;i<verties.size();i++){
			PointF p=verties.get(i);
			if(p.x<cx){
				p.x-=ddx;
				if(p.x>cx)	p.x=cx;
			}
			else if(p.x>cx){
				p.x+=ddx;
				if(p.x<cx)	p.x=cx;
			}
			if(p.y<cy){
				p.y-=ddy;
				if(p.y>cy)	p.y=cy;
			}
			else if(p.y>cy){
				p.y+=ddy;
				if(p.y<cy)	p.y=cy;
			}
		}
	}
	
	@Override
	public Path getPath(){
		if(verties==null)
			return null;
		Path path=new Path();
		path.moveTo(verties.get(0).x, verties.get(0).y);
		for(int i=1;i<verties.size();i++){
			PointF p=verties.get(i);
			path.lineTo(p.x, p.y);
		}
		path.close();
		return path;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		if(verties==null || verties.size()<=0)
			return;
		//float[] points=Graphic2DPainter.getPolygonPoints(verties);
		//if(points!=null)
		//	canvas.drawPoints(points, paint);
		Path path=new Path();
		path.moveTo(verties.get(0).x, verties.get(0).y);
		for(int i=1;i<verties.size();i++){
			PointF p=verties.get(i);
			path.lineTo(p.x, p.y);
		}
		path.close();
		canvas.drawPath(path, paint);
	}

}

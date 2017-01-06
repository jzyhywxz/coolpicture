package com.zzw.coolpicture.graphics;

import java.util.LinkedList;
import java.util.List;

import android.graphics.PointF;

public class Graphic2DPainter {
	private Graphic2DPainter(){}
	
	public static float[] getLinePoints(PointF begin, PointF end) {
		if(begin==null || end==null)
			return null;
		int startX, startY;			// 起点
		float dx, dy;				// 变量
		float p;					// 决定因子
		int pCount;					// 点数
		float[] points=null;		// 点集
		
		if(end.x==begin.x){			// "|"轴
			startX=Math.round(begin.x);
			startY=Math.round(Math.min(begin.y, end.y));
			pCount=Math.round(Math.abs(end.y-begin.y));
			points=new float[pCount+pCount];
			for(int i=0;i<pCount;i++){
				points[i+i]=startX;
				points[i+i+1]=startY+i;
			}
		}
		else if(end.y==begin.y){	// "——"轴
			startX=Math.round(Math.min(begin.x, end.x));
			startY=Math.round(begin.y);
			pCount=Math.round(Math.abs(end.x-begin.x));
			points=new float[pCount+pCount];
			for(int i=0;i<pCount;i++){
				points[i+i]=startX+i;
				points[i+i+1]=startY;
			}
		}
		else{
			// 斜率
			float k=(begin.y-end.y)/(end.x-begin.x);
			// 是否为对角线
			if(k==1.0f){			// "/"轴
				startX=Math.round(Math.min(begin.x, end.x));
				startY=Math.round(Math.max(begin.y, end.y));
				pCount=Math.round(Math.abs(end.x-begin.x));
				points=new float[pCount+pCount];
				for(int i=0;i<pCount;i++){
					points[i+i]=startX+i;
					points[i+i+1]=startY-i;
				}
			}
			else if(k==-1.0f){		// "\"轴
				startX=Math.round(Math.min(begin.x, end.x));
				startY=Math.round(Math.min(begin.y, end.y));
				pCount=Math.round(Math.abs(end.x-begin.x));
				points=new float[pCount+pCount];
				for(int i=0;i<pCount;i++){
					points[i+i]=startX+i;
					points[i+i+1]=startY+i;
				}
			}
			if(k>0 && k<1){
				startX=Math.round(Math.min(begin.x, end.x));
				startY=Math.round(Math.max(begin.y, end.y));
				pCount=Math.round(Math.abs(end.x-begin.x));
				points=new float[pCount+pCount];
				dx=1;
				dy=k;
				p=dy+dy-dx;
				points[0]=startX;
				points[1]=startY;
				for(int i=1;i<pCount;i++){
					points[i+i]=startX+i;
					if(p>=0){
						points[i+i+1]=points[i+i-1]-1;
						p=p+dy+dy-dx-dx;
					}
					else{
						points[i+i+1]=points[i+i-1];
						p=p+dy+dy;
					}
				}
			}
			else if(k >-1 && k<0){
				startX=Math.round(Math.min(begin.x, end.x));
				startY=Math.round(Math.min(begin.y, end.y));
				pCount=Math.round(Math.abs(end.x-begin.x));
				points=new float[pCount+pCount];
				dx=1;
				dy=k;
				p=-dx-dy-dy;
				points[0]=startX;
				points[1]=startY;
				for(int i=1;i<pCount;i++){
					points[i+i]=startX+i;
					if(p>=0){
						points[i+i+1]=points[i+i-1]+1;
						p=p-dy-dy-dx-dx;
					}
					else{
						points[i+i+1]=points[i+i-1];
						p=p-dy-dy;
					}
				}
			}
			else if(k>1){
				startX=Math.round(Math.min(begin.x, end.x));
				startY=Math.round(Math.max(begin.y, end.y));
				pCount=Math.round(Math.abs(end.y-begin.y));
				points=new float[pCount+pCount];
				dx=1/k;
				dy=1;
				p=dx+dx-dy;
				points[0]=startX;
				points[1]=startY;
				for(int i=1;i<pCount;i++){
					if(p>=0){
						points[i+i]=points[i+i-2]+1;
						p=p+dx+dx-dy-dy;
					}
					else{
						points[i+i]=points[i+i-2];
						p=p+dx+dx;
					}
					points[i+i+1]=startY-i;
				}
			}
			else if(k<-1){
				startX=Math.round(Math.max(begin.x, end.x));
				startY=Math.round(Math.max(begin.y, end.y));
				pCount=Math.round(Math.abs(end.y-begin.y));
				points=new float[pCount+pCount];
				dx=1/k;
				dy=1;
				p=-dx-dx-dy;
				points[0]=startX;
				points[1]=startY;
				for(int i=1;i<pCount;i++){
					if(p>=0){
						points[i+i]=points[i+i-2]-1;
						p=p-dx-dx-dy-dy;
					}
					else{
						points[i+i]=points[i+i-2];
						p=p-dx-dx;
					}
					points[i+i+1]=startY-i;
				}
			}
		}
		return points;
	}
	
	public static float[] getCirclePoints(int cx, int cy, float radius){
		if(radius>0.0f){
			LinkedList<Integer> points=new LinkedList<Integer>();
			int x=0,y=Math.round(radius);
			float p=5.0f/4.0f-radius;
			
			do{
				points.addLast(cx+x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy-y);
				points.addLast(cx+x); points.addLast(cy-y);
				points.addLast(cx+y); points.addLast(cy+x);
				points.addLast(cx-y); points.addLast(cy+x);
				points.addLast(cx-y); points.addLast(cy-x);
				points.addLast(cx+y); points.addLast(cy-x);

				if(p>=0) p=p+x+x-y-y+5;
				else     p=p+x+x+3;
				
				++x;
				if(p>=0) --y;
			}while(x<=y);
			
			float[] arr=new float[points.size()];
			for(int i=0;i<points.size();i++)
				arr[i]=points.get(i);
			return arr;
		}
		else{
			return new float[]{cx, cy};
		}
	}
	
	public static float[] getOvalPoints(
			int cx, int cy, float rx, float ry){
		if(rx==ry && rx>0.0f)
			return getCirclePoints(cx, cy, rx);
		else if(rx>0.0f && ry>0.0f){
			int x=0;
			int y=Math.round(ry);
			float p=rx*rx/4+ry*ry-rx*rx*ry;
			LinkedList<Integer> points=new LinkedList<Integer>();
			
			do{
				points.addLast(cx+x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy-y);
				points.addLast(cx+x); points.addLast(cy-y);
				
				if((ry*ry*x)>(rx*rx*y)) break;
				
				if(p>=0) p=p+ry*ry*(x+x+3)-rx*rx*(y+y-2);
				else     p=p+ry*ry*(x+x+3);
				
				++x;
				if(p>=0) --y;
			}while(true);
			
			p=ry*ry*(x+0.5f)*(x+0.5f)+rx*rx*(y-1)*(y-1)-rx*rx*ry*ry;
			do{
				if(p<0)  ++x;
				--y;
				
				points.addLast(cx+x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy+y);
				points.addLast(cx-x); points.addLast(cy-y);
				points.addLast(cx+x); points.addLast(cy-y);
				
				if(y<=0) break;
				
				if(p>=0) p=p-rx*rx*(y+y-3);
				else     p=p-rx*rx*(y+y-3)+ry*ry*(x+x+2);
			}while(true);
			
			float[] arr=new float[points.size()];
			for(int i=0;i<points.size();i++)
				arr[i]=points.get(i);
			return arr;
		}
		else
			return new float[]{cx, cy};
	}
	
	public static float[] getPolygonPoints(List<PointF> verties){
		if(verties==null || verties.size()<=0)
			return null;
		if(verties.size()==1)
			return new float[]{verties.get(0).x, verties.get(0).y};
		
		LinkedList<Float> points=new LinkedList<Float>();
		for(int i=0;i<verties.size();i++){
			PointF p1=verties.get(i);
			PointF p2=verties.get((i+1)%verties.size());
			p1.x=(int)p1.x; p1.y=(int)p1.y;
			p2.x=(int)p2.x; p2.y=(int)p2.y;
			float[] p=getLinePoints(p1, p2);
			if(p!=null)
				for(int j=0;j<p.length;j++)
					points.addLast(p[j]);
		}
		float[] arr=new float[points.size()];
		for(int i=0;i<points.size();i++)
			arr[i]=points.get(i);
		return arr;
	}
}

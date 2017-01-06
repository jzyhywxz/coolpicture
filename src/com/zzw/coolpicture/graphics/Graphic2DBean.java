package com.zzw.coolpicture.graphics;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Graphic2DBean {
	protected float mWidth=0.0f;			// 宽度
	protected float mHeight=0.0f;			// 高度
	protected float mTransX=0.0f;			// 位移 X
	protected float mTransY=0.0f;			// 位移 Y
	protected float mScaleX=1.0f;			// 缩放 X
	protected float mScaleY=1.0f;			// 缩放 Y
	protected float mRotateX=0.0f;			// 旋转 X
	protected float mRotateY=0.0f;			// 旋转 Y
	protected float mRotateZ=0.0f;			// 旋转 Z
	protected float mSkewX=0.0f;			// 错切 X
	protected float mSkewY=0.0f;			// 错切 Y
	protected Camera mCamera=new Camera();	// 三维变换
	protected Matrix preMatrix=new Matrix();// 正向矩阵
	protected Matrix invMatrix=new Matrix();// 逆向矩阵
	protected PointF mBeg=new PointF();		// 起点
	protected PointF mEnd=new PointF();		// 终点
	
	protected Graphic2DBean(){
		this(0, 0);
	}
	protected Graphic2DBean(float w, float h){
		mWidth=w;
		mHeight=h;
	}
	protected Graphic2DBean(PointF b, PointF e){
		mBeg=b;
		mEnd=e;
		mWidth=Math.abs(b.x-e.x);
		mHeight=Math.abs(b.y-e.y);
	}
	
	// 大小
	public float getWidth(){return mWidth;}
	public void setWidth(float w){mWidth=w;}
	public float getHeight(){return mHeight;}
	public void setHeight(float h){mHeight=h;}
	// 位移
	public float getTransX(){return mTransX;}
	public void setTransX(float x){
		mTransX=x;
		float dx=mEnd.x-mBeg.x;
		mBeg.x=x;
		mEnd.x=x+dx;
	}
	public float getTransY(){return mTransY;}
	public void setTransY(float y){
		mTransY=y;
		float dy=mEnd.y-mBeg.y;
		mBeg.y=y;
		mEnd.y=y+dy;
	}
	public void addTrans(float dx, float dy){
		mTransX+=dx;
		mTransY+=dy;
		mBeg.x+=dx;
		mBeg.y+=dy;
		mEnd.x+=dx;
		mEnd.y+=dy;
	}
	// 缩放
	public float getScaleX(){return mScaleX;}
	public float getScaleY(){return mScaleY;}
	public void addScale(float dx, float dy){
		mScaleX*=dx;
		mScaleY*=dy;
		float ddx=(dx-1)*mWidth/2;
		float ddy=(dy-1)*mHeight/2;
		mTransX-=ddx;
		mTransY-=ddy;
		mWidth*=dx;
		mHeight*=dy;
		if(mBeg.x<mEnd.x){
			mBeg.x=mTransX;
			mEnd.x=mTransX+mWidth;
		}
		else{
			mEnd.x=mTransX;
			mBeg.x=mTransX+mWidth;
		}
		if(mBeg.y<mEnd.y){
			mBeg.y=mTransY;
			mEnd.y=mTransY+mHeight;
		}
		else{
			mEnd.y=mTransY;
			mBeg.y=mTransY+mHeight;
		}
	}
	// 旋转
	public float getRotateX(){return mRotateX;}
	public float getRotateY(){return mRotateY;}
	public float getRotateZ(){return mRotateZ;}
	public void addRotate(float dx, float dy, float dz){
		mRotateX+=dx;
		mRotateY+=dy;
		mRotateZ+=dz;
	}
	public Matrix getPreMatrix(){return preMatrix;}
	public Matrix getInvMatrix(){return invMatrix;}
	// 错切
	public float getSkewX(){return mSkewX;}
	public void setSkewX(float x){mSkewX=x;}
	public float getSkewY(){return mSkewY;}
	public void setSkewY(float y){mSkewY=y;}
	// 点管理
	public PointF getBegPoint(){return mBeg;}
	public PointF getEndPoint(){return mEnd;}
	public void setBegPoint(PointF p){
		mBeg=p;
		if(mEnd!=null){
			mTransX=Math.min(mBeg.x, mEnd.x);
			mTransY=Math.min(mBeg.y, mEnd.y);
			mWidth=Math.abs(mEnd.x-mBeg.x);
			mHeight=Math.abs(mEnd.y-mBeg.y);
		}
	}
	public void setEndPoint(PointF p){
		mEnd=p;
		if(mBeg!=null){
			mTransX=Math.min(mBeg.x, mEnd.x);
			mTransY=Math.min(mBeg.y, mEnd.y);
			mWidth=Math.abs(mEnd.x-mBeg.x);
			mHeight=Math.abs(mEnd.y-mBeg.y);
		}
	}
	public void setTwoPoint(PointF b, PointF e){
		mBeg=b;
		mEnd=e;
		mTransX=Math.min(mBeg.x, mEnd.x);
		mTransY=Math.min(mBeg.y, mEnd.y);
		mWidth=Math.abs(mEnd.x-mBeg.x);
		mHeight=Math.abs(mEnd.y-mBeg.y);
	}
	public void addPoint(PointF p){}
	// 其它
	public float[] getCenterPoint(){
		return new float[]{
				mTransX+mWidth/2,
				mTransY+mHeight/2
		};
	}
	public RectF getRegion(){
		return new RectF(mTransX, mTransY, 
				mTransX+mWidth, 
				mTransY+mHeight);
	}
	public boolean isInRegion(float x, float y){
		RectF region=getRegion();
		preMatrix.mapRect(region);
		return  x>=region.left && x<=region.right &&
				y>=region.top && y<=region.bottom;
	}
	
	public abstract Path getPath();
	
	// 设置画笔——设置画笔颜色、粗细、样式等
	public void setPaint(Paint p){
		p.setAntiAlias(true);
		p.setFilterBitmap(true);
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		p.setStrokeWidth(1);
		p.setXfermode(null);
	}
	// 绘制内容——绘制具体的形状、位图等
	public abstract void draw(Canvas canvas, Paint paint);
	// 整个绘制流程
	public void drawGraphic(Canvas canvas, Paint paint){
		float[] cp=getCenterPoint();

		mCamera.save();
		mCamera.rotate(mRotateX, mRotateY, mRotateZ);
		mCamera.getMatrix(preMatrix);
		preMatrix.preTranslate(-cp[0], -cp[1]);
		preMatrix.postTranslate(cp[0], cp[1]);
		mCamera.restore();
		canvas.concat(preMatrix);
		
		setPaint(paint);
		draw(canvas, paint);
		
		if(preMatrix.invert(invMatrix))
			canvas.concat(invMatrix);
	}
	
}

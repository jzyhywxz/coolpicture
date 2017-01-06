package com.zzw.coolpicture.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;

public class RoundRect extends Rectangle {
	private float rx;
	private float ry;
	
	public RoundRect(){
		super();
	}
	public RoundRect(float width, float height){
		super(width, height);
	}
	public RoundRect(float width, float height, float rx, float ry){
		super(width, height);
		this.rx=rx;
		this.ry=ry;
	}
	
	public float getRX(){return rx;}
	public float getRY(){return ry;}
	public void setRX(float rx){
		if(rx>mWidth/2)
			this.rx=mWidth/2;
		else
			this.rx=rx;
	}
	public void setRY(float ry){
		if(ry>mHeight/2)
			this.ry=mHeight/2;
		else
			this.ry=ry;
	}
	public void addRadius(float dx, float dy){
		rx+=dx;
		ry+=dy;
		if(rx<0)
			rx=0;
		else if(rx>mWidth/2)
			rx=mWidth/2;
		if(ry<0)
			ry=0;
		else if(ry>mHeight/2)
			ry=mHeight/2;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawRoundRect(getRegion(), rx, ry, paint);
	}
	
	@Override
	public Path getPath(){
		Path path=new Path();
		path.addRoundRect(getRegion(), rx, ry, Direction.CW);
		return path;
	}
}

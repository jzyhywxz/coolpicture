package com.zzw.coolpicture.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class ImageBean extends Graphic2DBean {
	private final String mPath;
	private Bitmap mImage;
	private Matrix mMatrix=new Matrix();
	
	public ImageBean(String path, int w, int h) {
		super(w, h);
		mPath=path;
		loadImage();
	}

	public void loadImage(){
		mImage=BitmapFactory.decodeFile(mPath);
		
		int iw=mImage.getWidth();
		int ih=mImage.getHeight();
		
		float scale=1.0f;
		if(iw>mWidth || ih>mHeight)
			scale=Math.min(mWidth/iw, mHeight/ih);
		
		mScaleX=mScaleY=scale;
		
		mWidth=iw*scale;
		mHeight=ih*scale;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		mMatrix.reset();
		mMatrix.postScale(mScaleX, mScaleY);
		mMatrix.postTranslate(mTransX, mTransY);
		canvas.drawBitmap(mImage, mMatrix, paint);
	}

	@Override
	public Path getPath() {
		return null;
	}

}

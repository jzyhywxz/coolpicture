package com.zzw.coolpicture.view;

import com.zzw.coolpicture.graphics.Graphic2DBean;
import com.zzw.coolpicture.graphics.Rectangle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CutImageView extends ImageView {
	private Graphic2DBean border;
	private Paint paint;

	public CutImageView(Context context){
		this(context, null);
	}
	public CutImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public CutImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(3);
		
		border=new Rectangle(){
			@Override
			public void setPaint(Paint paint){}
		};
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	public Graphic2DBean getBorder(){return border;}
	public void setBorder(Graphic2DBean bean){
		border=bean;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width=this.getMeasuredWidth();
		int height=this.getMeasuredHeight();
		
		border.setWidth(width-100);
		border.setHeight(height-100);
		border.setTransX(50);
		border.setTransY(50);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.save();
		canvas.clipPath(border.getPath(), Region.Op.XOR);
		canvas.drawColor(0xa0000000);
		canvas.restore();
		border.drawGraphic(canvas, paint);
	}
	
}

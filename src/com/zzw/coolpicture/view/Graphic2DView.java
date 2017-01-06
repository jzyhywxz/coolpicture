package com.zzw.coolpicture.view;

import java.util.LinkedList;

import com.zzw.coolpicture.graphics.Graphic2DBean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Graphic2DView extends View {
	// Õº–Œ’ª
	private LinkedList<Graphic2DBean> graphicStack=
			new LinkedList<Graphic2DBean>();
	private Graphic2DBean selected=null;
	private Paint paint=new Paint();

	public Graphic2DView(Context context){
		this(context, null);
	}
	public Graphic2DView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public Graphic2DView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void addGraphic(Graphic2DBean bean){
		if(bean!=null){
			graphicStack.addLast(bean);
			selected=bean;
			invalidate();
		}
	}
	public void removeGraphic(){
		if(graphicStack.size()>0){
			graphicStack.removeLast();
			if(graphicStack.size()>0)
				selected=graphicStack.getLast();
			else
				selected=null;
			invalidate();
		}
	}
	public Graphic2DBean selectGraphic(float x, float y){
		if(graphicStack.size()<=0)
			return null;
		int beg=graphicStack.size()-1;
		for(int i=beg;i>=0;i--){
			Graphic2DBean bean=graphicStack.get(i);
			if(bean.isInRegion(x, y)){
				if(i<beg){
					graphicStack.remove(i);
					graphicStack.addLast(bean);
				}
				selected=bean;
				return bean;
			}
		}
		selected=null;
		return null;
	}
	
	public Graphic2DBean getSelectedGraphic(){
		return selected;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		
		for(Graphic2DBean bean:graphicStack)
			bean.drawGraphic(canvas, paint);
		
		if(selected!=null){
			RectF region=selected.getRegion();
			region.left-=3;
			region.top-=3;
			region.right+=3;
			region.bottom+=3;
			paint.setColor(Color.CYAN);
			paint.setStrokeWidth(3);
			
			canvas.concat(selected.getPreMatrix());
			canvas.drawRect(region, paint);
			canvas.concat(selected.getInvMatrix());
		}
	}
}

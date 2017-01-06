package com.zzw.coolpicture.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends 
RecyclerView.ItemDecoration{
	private static final int[] ATTRS=
			new int[]{android.R.attr.listDivider};
	public static final int HORIZONTAL=
			LinearLayoutManager.HORIZONTAL;
	public static final int VERTICAL=
			LinearLayoutManager.VERTICAL;
	private Drawable mDivider;
	private int mOrientation;
	
	public DividerItemDecoration(Context context,int orientation){
		TypedArray a=context.obtainStyledAttributes(ATTRS);
		mDivider=a.getDrawable(0);
		a.recycle();
		if(orientation!=HORIZONTAL&&orientation!=VERTICAL)
			throw new IllegalArgumentException(
					"invalid orientation!");
		mOrientation=orientation;
	}

	@Override
	public void onDraw(Canvas canvas,RecyclerView parent){
		if(mOrientation==HORIZONTAL)
			drawHorizontal(canvas,parent);
		else
			drawVertical(canvas,parent);
	}
	
	private void drawHorizontal(Canvas c,RecyclerView root){
		int top=root.getPaddingTop();
		int bottom=root.getHeight()-root.getPaddingBottom();
		
		int childCount=root.getChildCount();
		for(int i=0;i<childCount;i++){
			View child=root.getChildAt(i);
			RecyclerView.LayoutParams params=
					(RecyclerView.LayoutParams)
					child.getLayoutParams();
			int left=child.getRight()+params.rightMargin;
			int right=left+mDivider.getIntrinsicWidth();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}
	private void drawVertical(Canvas c,RecyclerView root){
		int left=root.getPaddingLeft();
		int right=root.getWidth()-root.getPaddingRight();
		
		int childCount=root.getChildCount();
		for(int i=0;i<childCount;i++){
			View child=root.getChildAt(i);
			RecyclerView.LayoutParams params=
					(RecyclerView.LayoutParams)
					child.getLayoutParams();
			int top=child.getBottom()+params.bottomMargin;
			int bottom=top+mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}
	
	@Override
	public void getItemOffsets(Rect outRect, int itemPosition,
			RecyclerView parent) {
		if(mOrientation==HORIZONTAL)
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		else
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
	}
}

package com.zzw.coolpicture.view;

import java.util.List;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.FuncBean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FuncAdapter extends 
RecyclerView.Adapter<FuncAdapter.ViewHolder>{
	private Context mContext;
	private List<FuncBean> mDatas;
	
	public FuncAdapter(Context context,List<FuncBean> datas){
		mContext=context;
		mDatas=datas;
	}
	
	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {
		ViewHolder holder=new ViewHolder(
				LayoutInflater.from(mContext).
				inflate(R.layout.item_func_bg1, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		FuncBean func=mDatas.get(position);
		holder.image.setImageResource(func.resId);
		holder.image.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mDatas.get(position).function(holder.image);
			}
		});
	}
	
	class ViewHolder extends RecyclerView.ViewHolder{
		private ImageButton image;

		public ViewHolder(View itemView) {
			super(itemView);
			image=(ImageButton)itemView.findViewById(R.id.func_bg1);
		}
	}
}

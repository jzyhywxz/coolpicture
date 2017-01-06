package com.zzw.coolpicture.view;

import java.util.List;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.CommonAdapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SimpleFuncPopupWindow extends WrapPopupWindow {
	private List<String> datas;
	private ListView funcList;
	private int selected;

	public SimpleFuncPopupWindow(Context context, View contentView, 
			int width, int height, List<String> datas, int selected) {
		super(context, contentView, width, height, true);
		this.datas=datas;
		this.selected=selected;
		initView();
		initEvent();
		init();
	}
	
	@Override
	public void initView() {
		funcList=(ListView)contentView.findViewById(
				R.id.func_list);
		funcList.setAdapter(new CommonAdapter<String>(
				context, datas, R.layout.item_func_bg2){
			@Override
			public void convert(ViewHolder helper, int position) {
				helper.setText(R.id.func_text, datas.get(position));
				ImageButton img=(ImageButton)helper.getView(R.id.func_image);
				img.setVisibility(position==selected ? 
						View.VISIBLE : View.GONE);
			}
		});
	}

	@Override
	public void initEvent() {
		funcList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, int position, long id) {
				selected=position;
				if(listener!=null)
					listener.onClick(position);
				dismiss();
			}
		});
	}

	@Override
	public void init() {}
	
	public int getSelected(){return selected;}
	
	public interface OnClickWindowListener{
		public void onClick(int position);
	}
	
	private OnClickWindowListener listener;
	
	public void setClicker(OnClickWindowListener l){listener=l;}
}

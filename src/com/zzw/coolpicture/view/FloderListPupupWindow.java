package com.zzw.coolpicture.view;

import java.util.List;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.CommonAdapter;
import com.zzw.coolpicture.util.ImageSearcher.ImageFloder;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class FloderListPupupWindow extends WrapPopupWindow {
	private List<ImageFloder> datas;
	private ListView floderList;
	private int selected;

	public FloderListPupupWindow(Context context, View contentView, 
			int width, int height, List<ImageFloder> datas, int selected) {
		super(context, contentView, width, height, true);
		this.datas=datas;
		this.selected=selected;
		initView();
		initEvent();
		init();
	}

	@Override
	public void initView() {
		floderList=(ListView)findViewById(R.id.floder_list);
		floderList.setAdapter(new CommonAdapter<ImageFloder>(
				context, datas, R.layout.item_floder){
			@Override
			public void convert(ViewHolder helper, int position) {
				ImageFloder item=getItem(position);
				helper.setText(R.id.floder_name, 
						item.getFloderName());
				helper.setText(R.id.image_count, 
						item.getCount()+"уе");
				helper.setImageByUrl(R.id.image_floder, 
						item.getFirstImage());
				ImageView view=(ImageView)helper.getView(R.id.locate_view);
				if(position==selected)
					view.setVisibility(View.VISIBLE);
				else
					view.setVisibility(View.GONE);
			}
		});

	}

	@Override
	public void initEvent() {
		floderList.setOnItemClickListener(
				new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, int position, long id) {
				selected=position;
				if(listener!=null)
					listener.onChanged(datas.get(position));
			}
		});

	}

	@Override
	public void init() {}
	
	public interface OnSelectChangeListener {
		void onChanged(ImageFloder floder);
	}
	
	private OnSelectChangeListener listener;
	
	public void setOnSelectChangeListener(
			OnSelectChangeListener l){
		listener=l;
	}
	
	
}

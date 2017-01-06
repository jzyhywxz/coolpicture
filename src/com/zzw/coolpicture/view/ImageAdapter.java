package com.zzw.coolpicture.view;

import java.util.List;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.CommonAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageAdapter extends CommonAdapter<String> {
	private String mFloderPath;
	private List<String> mSelectedImages;
	private final int mSelectedCapacity;
	private Button mCompleteBt;
	
	public ImageAdapter(Context context, String path, 
			List<String> images, List<String> selectedImages, 
			int capacity, int itemLayoutId, Button bt) {
		super(context, images, itemLayoutId);
		mFloderPath=path;
		mSelectedImages=selectedImages;
		mSelectedCapacity=capacity;
		mCompleteBt=bt;
	}

	@Override
	public void convert(ViewHolder helper, int position) {
		final String item=getItem(position);
		helper.setImageResource(R.id.image_view, R.drawable.pictures_no);
		helper.setImageResource(R.id.image_selector, 
				R.drawable.picture_unselected);
		helper.setImageByUrl(R.id.image_view, mFloderPath+"/"+item);
		
		final ImageView imageView=(ImageView)helper.getView(R.id.image_view);
		final ImageButton imageSelector=(ImageButton)helper.getView(R.id.image_selector);
		
		imageView.setColorFilter(null);
		imageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mSelectedImages.contains(mFloderPath+"/"+item)){
					mSelectedImages.remove(mFloderPath+"/"+item);
					imageSelector.setImageResource(
							R.drawable.picture_unselected);
					imageView.setColorFilter(null);
				}
				else if(mSelectedImages.size()<mSelectedCapacity){
					mSelectedImages.add(mFloderPath+"/"+item);
					imageSelector.setImageResource(
							R.drawable.pictures_selected);
					imageView.setColorFilter(
							Color.parseColor("#77000000"));
				}
				// update complete button
				if(!mSelectedImages.isEmpty()){
					mCompleteBt.setText("完成("+mSelectedImages.size()+
							"/"+mSelectedCapacity+")");
					mCompleteBt.setBackgroundColor(0xa000ff00);
				}
				else{
					mCompleteBt.setText("完成");
					mCompleteBt.setBackgroundColor(0x5000ff00);
				}
			}
		});
		
		if(mSelectedImages.contains(mFloderPath+"/"+item)){
			imageSelector.setImageResource(
					R.drawable.pictures_selected);
			imageView.setColorFilter(
					Color.parseColor("#77000000"));
		}
	}
}

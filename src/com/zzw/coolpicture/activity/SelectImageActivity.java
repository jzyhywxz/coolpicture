package com.zzw.coolpicture.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.ImageSearcher;
import com.zzw.coolpicture.util.ImageSearcher.ImageFloder;
import com.zzw.coolpicture.view.FloderListPupupWindow;
import com.zzw.coolpicture.view.FloderListPupupWindow.OnSelectChangeListener;
import com.zzw.coolpicture.view.ImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class SelectImageActivity extends Activity 
implements OnSelectChangeListener {
	private LinkedList<ImageFloder> mImageFloders;	// 图片文件夹列表
	private ImageFloder mFloder;					// 当前图片文件夹
	private List<String> mImages;					// 当前图片
	private ArrayList<String> mSelectedImages=
			new ArrayList<String>();				// 选中图片
	private static final int MAXSELECTEDCOUNT=9;	// 最多可选图片数
	
	private FloderListPupupWindow mFloderWindow;	// 图片文件夹弹窗
	private Handler mHandler;
	
	private View mTopBar;							// 顶部条
	private ImageButton mTopBack;					// 顶部返回
	private TextView mTopTitle;						// 顶部标题
	private Button mTopOK;							// 底部完成
	private GridView mImgAbbr;						// 缩略图
	private View mBtmBar;							// 底部条
	private Button mBtmBt;							// 底部按钮
	private TextView mBtmDesc;						// 底部描述
	
	private static final int SEARCH_COMPLETED=0;	// 搜索完成
	private static final int SEARCH_FAILED=0xF;		// 搜索失败

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sel_img);
		
		initView();
		initEvent();
		
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case SEARCH_COMPLETED:
					//隐藏进度条
					//Log.d("TAG", "mFloderList: "+mImageFloders.toString());
					//Log.d("TAG", "mFloder: "+mFloder.getPath());
					//Log.d("TAG", "mImages: "+mImages.toString());
					
					ImageAdapter adapter=new ImageAdapter(
							SelectImageActivity.this, mFloder.getPath(), 
							mImages, mSelectedImages, MAXSELECTEDCOUNT, 
							R.layout.item_image, mTopOK);
					mImgAbbr.setAdapter(adapter);
					mBtmBt.setText(mFloder.getFloderName());
					mBtmDesc.setText(mFloder.getCount()+" 张");
					initImageFloderPopupWindow();
					break;
				case SEARCH_FAILED:
					String error=(String)msg.obj;
					Toast.makeText(SelectImageActivity.this, error, 
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		
		getImage();
	}
	
	private void getImageByFloder(ImageFloder floder){
		mFloder=floder;
		mImages=Arrays.asList(new File(mFloder.getPath()).
				list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg")
						||filename.endsWith(".png")
						||filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
	}
	
	private void getImage(){
		new Thread(){
			@Override
			public void run(){
				// 显示进度条
				// 搜索图片文件夹
				mImageFloders=ImageSearcher.getExternalImageFloder(
						getApplicationContext());
				if(mImageFloders!=null&&(!mImageFloders.isEmpty())){
					// 搜索第一个文件夹中的图片
					getImageByFloder(mImageFloders.getFirst());
					mHandler.sendEmptyMessage(SEARCH_COMPLETED);
				}
				else{
					Message msg=new Message();
					msg.what=SEARCH_FAILED;
					msg.obj="not find any floder that include images!";
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	private void initView(){
		mTopBar=findViewById(R.id.sel_img_topbar);
		mTopBack=(ImageButton)mTopBar.findViewById(R.id.top_back);
		mTopTitle=(TextView)mTopBar.findViewById(R.id.top_title);
		mTopTitle.setText("选择图片");
		mTopOK=(Button)mTopBar.findViewById(R.id.top_ok);
		mImgAbbr=(GridView)findViewById(R.id.sel_img_abbr);
		mBtmBar=findViewById(R.id.sel_img_btmbar);
		mBtmBt=(Button)mBtmBar.findViewById(R.id.btm_bt);
		mBtmDesc=(TextView)mBtmBar.findViewById(R.id.btm_desc);
	}
	
	private void initImageFloderPopupWindow(){
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenHeight=metrics.heightPixels;
		
		mFloderWindow=new FloderListPupupWindow(this, 
				LayoutInflater.from(getApplicationContext()).
				inflate(R.layout.list_floder, null), 
				LayoutParams.MATCH_PARENT, (int)(screenHeight*0.7), 
				mImageFloders, 0);
		
		mFloderWindow.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp=getWindow().getAttributes();
				lp.alpha=1.0F;
				getWindow().setAttributes(lp);
			}
		});
		mFloderWindow.setOnSelectChangeListener(this);
	}
	
	private void initEvent(){
		mTopBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTopOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!mSelectedImages.isEmpty()){
					Intent intent=new Intent();
					intent.putStringArrayListExtra("select_image", mSelectedImages);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		mBtmBt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mFloderWindow.setAnimationStyle(R.style.anim_pop);
				mFloderWindow.showAtBottom(mBtmBar);
				WindowManager.LayoutParams lp=getWindow().getAttributes();
				lp.alpha=.3F;
				getWindow().setAttributes(lp);
			}
		});
	}
	
	@Override
	public void onChanged(ImageFloder floder) {
		getImageByFloder(floder);
		
		mImgAbbr.setAdapter(new ImageAdapter(
							SelectImageActivity.this, mFloder.getPath(), 
							mImages, mSelectedImages, MAXSELECTEDCOUNT, 
							R.layout.item_image, mTopOK));
		mBtmBt.setText(mFloder.getFloderName());
		mBtmDesc.setText(mFloder.getCount()+" 张");
		mFloderWindow.dismiss();
	}
}

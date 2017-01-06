package com.zzw.coolpicture.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.zzw.coolpicture.R;
import com.zzw.coolpicture.util.FuncBean;
import com.zzw.coolpicture.util.ImageRecorder;
import com.zzw.coolpicture.view.DividerItemDecoration;
import com.zzw.coolpicture.view.FuncAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class FillImageActivity extends Activity {
	private ImageView mImgView;
	private RecyclerView mBtmBar;
	private int newColor=0xffffffff;
	private LinkedList<Point> stack=new LinkedList<Point>();
	private ArrayList<FuncBean> mFuncs;	// 功能列表
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fil_img);
		
		initFunc();
		initView();
		initEvent();
		
		Intent intent=getIntent();
		String path=intent.getStringExtra("IMG_PATH");
		Bitmap bm=BitmapFactory.decodeFile(path);
		mImgView.setImageBitmap(bm);
	}
	
	private void initFunc(){
		mFuncs=new ArrayList<FuncBean>();
		
		mFuncs.add(new FuncBean("#ffffffff", R.drawable.cffffff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffff79ff", R.drawable.cff79ff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffff00ff", R.drawable.cff00ff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffff0079", R.drawable.cff0079){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffff0000", R.drawable.cff0000){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff790000", R.drawable.c790000){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffffff79", R.drawable.cffff79){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffffff00", R.drawable.cffff00){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ffff7900", R.drawable.cff7900){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff79ff00", R.drawable.c79ff00){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff00ff00", R.drawable.c00ff00){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff007900", R.drawable.c007900){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff79ffff", R.drawable.c79ffff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff00ffff", R.drawable.c00ffff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff00ff79", R.drawable.c00ff79){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff0079ff", R.drawable.c0079ff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff0000ff", R.drawable.c0000ff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff000079", R.drawable.c000079){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff7900ff", R.drawable.c7900ff){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("#ff000000", R.drawable.c000000){
			@Override
			public void function(View view){
				newColor=Color.parseColor(text);
			}
		});
		mFuncs.add(new FuncBean("save", R.drawable.save){
			@Override
			public void function(View view){
				String path=ImageRecorder.restoreScreen(
						FillImageActivity.this, mBtmBar.getHeight(), 
						ImageRecorder.STORAGE, null);
				if(path!=null)
					Toast.makeText(FillImageActivity.this, "图片保存至"+path, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(FillImageActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initView(){
		mImgView=(ImageView)findViewById(R.id.fil_img_view);
		
		mBtmBar=(RecyclerView)findViewById(R.id.fil_img_btmbar);
		mBtmBar.setAdapter(new FuncAdapter(this, mFuncs));
		LinearLayoutManager manager=new LinearLayoutManager(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mBtmBar.setLayoutManager(manager);
		mBtmBar.addItemDecoration(new DividerItemDecoration(
				this, DividerItemDecoration.HORIZONTAL));
	}
	
	private void initEvent(){
		mImgView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					final int x=(int)event.getX();
					final int y=(int)event.getY();
					fillImage(x, y);
				}
				return true;
			}
		});
	}

	private void fillImage(int x, int y){
		Bitmap b1=((BitmapDrawable)mImgView.getDrawable()).getBitmap();
		int bw=b1.getWidth();
		int bh=b1.getHeight();
		int[] pixels=new int[bw*bh];
		int oldColor=b1.getPixel(x, y);
		if(oldColor==newColor)
			return;
		b1.getPixels(pixels, 0, bw, 0, 0, bw, bh);
		fillImageInSameColor(pixels, bw, bh, oldColor, newColor, x, y);
		Bitmap b2=Bitmap.createBitmap(pixels, 0, bw, bw, bh, Config.ARGB_8888);
		mImgView.setImageBitmap(b2);
	}
	
	/**
	 * 填充方法，从种子点开始，将所有与种子点连通的颜色相同的像素点设置为新的颜色
	 * @param pixels	像素数组
	 * @param w			位图宽度
	 * @param h			位图高度
	 * @param oldColor	旧颜色
	 * @param newColor	新颜色
	 * @param x			种子点x坐标
	 * @param y			种子点y坐标
	 */
	private void fillImageInSameColor(int[] pixels, int w, int h, 
			int oldColor, int newColor, int x, int y){
		stack.addLast(new Point(x, y));
		while(stack.size()>0){
			Point seed=stack.removeLast();
			int[] border=fillLineInSameColor(pixels, w, h, oldColor, newColor, seed.x, seed.y);
			if(seed.y-1>=0)
				findSeedInSameColor(pixels, w, h, oldColor, seed.y-1, border);
			if(seed.y+1<h)
				findSeedInSameColor(pixels, w, h, oldColor, seed.y+1, border);
		}
	}

	private int[] fillLineInSameColor(int[] pixels, int w, int h, 
			int oldColor, int newColor, int x, int y){
		int left=x;
		while(left>=0){
			int index=y*w+left;
			if(pixels[index]==oldColor){
				pixels[index]=newColor;
				--left;
			}
			else
				break;
		}
		int right=x+1;
		while(right<w){
			int index=y*w+right;
			if(pixels[index]==oldColor){
				pixels[index]=newColor;
				++right;
			}
			else
				break;
		}
		return new int[]{left, right};
	}
	
	private void findSeedInSameColor(int[] pixels, int w, int h, 
			int oldColor, int y, int[] border){
		int beg=y*w+border[0]+1;
		int end=y*w+border[1]-1;
		boolean isSeed=false;
		while(beg<=end){
			if(pixels[beg]==oldColor){
				if(!isSeed){
					stack.addLast(new Point(beg%w, y));
					isSeed=true;
				}
			}
			else
				isSeed=false;
			++beg;
		}
	}
}

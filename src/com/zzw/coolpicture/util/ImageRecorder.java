package com.zzw.coolpicture.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class ImageRecorder {
	public static final String SDPATH=
			Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String CACHE="/coolpicture/cache";
	public static final String STORAGE="/coolpicture/image";
	
	private ImageRecorder(){}
	
	public static String restoreScreen(Activity act, int viewHeight, 
			final String dir, final String name){
		// 检测SDcard能否访问
		if(!Environment.MEDIA_MOUNTED.equals(
				Environment.getExternalStorageState())){
			Toast.makeText(act, "无法获得本地缓存", Toast.LENGTH_SHORT).show();
			return null;
		}
		// 获取各种尺寸
		DisplayMetrics metr=new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(metr);
		int scw=metr.widthPixels, sch=metr.heightPixels;// 整个屏幕
		View deco=act.getWindow().getDecorView();
		Rect rect=new Rect();
		deco.getWindowVisibleDisplayFrame(rect);
		int sth=sch-rect.height();						// 状态栏
		// 获取整个屏幕截图
		Bitmap scb=Bitmap.createBitmap(scw, sch, Config.ARGB_8888);
		deco.setDrawingCacheEnabled(true);
		scb=deco.getDrawingCache();
		// 获取特定屏幕截图
		int bw=scb.getWidth();
		int bh=scb.getHeight()-sth-viewHeight;
		int[] pixels=new int[bw*bh];
		scb.getPixels(pixels, 0, bw, 0, sth, bw, bh);
		Bitmap cob=Bitmap.createBitmap(bw, bh, Config.ARGB_8888);
		cob.setPixels(pixels, 0, bw, 0, 0, bw, bh);
		String path=ImageRecorder.restoreImage(act, cob, dir, name);
		deco.destroyDrawingCache();
		return path;
	}
	
	public static String restoreImage(Activity act, Bitmap src, 
			String dir, String name){
		// 本地缓存
		File dire=new File(SDPATH+dir);
		if(!dire.exists())
			if(!dire.mkdirs()){
				Toast.makeText(act, "创建目录失败", Toast.LENGTH_SHORT).show();
				return null;
			}
		String path;
		if(name==null){
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
			path=SDPATH+dir+"/"+df.format(new Date())+".png";
		}
		else
			path=SDPATH+dir+"/"+name+".png";
		FileOutputStream os=null;
		try {
			File file=new File(path);
			if(!file.exists())
				file.createNewFile();
			os=new FileOutputStream(file);
			src.compress(Bitmap.CompressFormat.PNG, 100, os);
			return path;
		} catch (FileNotFoundException e) {
			Toast.makeText(act, "创建文件失败", Toast.LENGTH_SHORT).show();
			return null;
		} catch (IOException e) {
			Toast.makeText(act, "保存资源失败", Toast.LENGTH_SHORT).show();
			return null;
		} finally {
			try {
				if(os!=null){
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				Toast.makeText(act, "文件写出失败", Toast.LENGTH_SHORT).show();
				return null;
			}
		}
	}
	
	public static Bitmap trim(Bitmap src){
		int srcw=src.getWidth(), srch=src.getHeight();
		int[] pixels=new int[srcw*srch];
		src.getPixels(pixels, 0, srcw, 0, 0, srcw, srch);
		int left=0, top=0, right=srcw-1, buttom=srch-1;
		// 确定上边界
		int i;
		T:
		for(i=0;i<srch;i++)
			for(int j=0;j<srcw;j++)
				if(isTransparent(pixels[i*srcw+j]))
					break T;
		top=i;
		// 确定下边界
		B:
		for(i=srch-1;i>=0;i--)
			for(int j=0;j<srcw;j++)
				if(isTransparent(pixels[i*srcw+j]))
					break B;
		buttom=i;
		// 确定左边界
		L:
		for(i=0;i<srcw;i++)
			for(int j=0;j<srch;j++)
				if(isTransparent(pixels[i+j*srcw]))
					break L;
		left=i;
		// 确定右边界
		R:
		for(i=srcw-1;i>=0;i--)
			for(int j=0;j<srch;j++)
				if(isTransparent(pixels[i+j*srcw]))
					break R;
		right=i;
		if(right<=left || buttom<=top)
			return null;
		Bitmap dst=Bitmap.createBitmap(src, left, top, right-left+1, buttom-top+1);
		return dst;
	}
	
	public static boolean isTransparent(int pixel){
		if(pixel>0)
			return pixel>0xffffff;
		else if(pixel<0)
			return true;
		else
			return false;
	}
}

package com.zzw.coolpicture.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class ImageSearcher {
	private static final Uri INTERNAL_URI=
			MediaStore.Images.Media.INTERNAL_CONTENT_URI;
	private static final Uri EXTERNAL_URI=
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	private ImageSearcher(){}
	
	public static LinkedList<ImageFloder> 
	getInternalImageFloder(Context context){
		ContentResolver resolver=context.getContentResolver();
		Cursor cursor=resolver.query(INTERNAL_URI, null, 
				MediaStore.Images.Media.MIME_TYPE+"=? or "+
				MediaStore.Images.Media.MIME_TYPE+"=?", 
				new String[]{"image/jpeg", "image/png"}, 
				MediaStore.Images.Media.DATE_MODIFIED);
		return getImageFloderByCursor(cursor);
	}
	public static LinkedList<ImageFloder> 
	getExternalImageFloder(Context context){
		/*if(android.os.Build.VERSION.SDK_INT >= 
				android.os.Build.VERSION_CODES.KITKAT && 
				DocumentsContract.isDocumentUri(
						context, INTERNAL_URI)){
			if("com.android.externalstorage.documents".
					equals(INTERNAL_URI.getAuthority())){
				
			}
		}*/
		if(!Environment.MEDIA_MOUNTED.equals(
				Environment.getExternalStorageState())){
			return null;
		}
		ContentResolver resolver=context.getContentResolver();
		Cursor cursor=resolver.query(EXTERNAL_URI, null, 
				MediaStore.Images.Media.MIME_TYPE+"=? or "+
				MediaStore.Images.Media.MIME_TYPE+"=?", 
				new String[]{"image/jpeg", "image/png"}, 
				MediaStore.Images.Media.DATE_MODIFIED);
		return getImageFloderByCursor(cursor);
	}
	
	public static LinkedList<ImageFloder> 
	getImageFloderByCursor(Cursor cursor){
		// ��¼���������ļ���
		LinkedHashSet<String> paths=new LinkedHashSet<String>();
		// ��¼�ļ�����Ϣ
		LinkedList<ImageFloder> floders=new LinkedList<ImageFloder>();
		
		while(cursor.moveToNext()){
			//ͼƬ·������һ��ͼƬ��
			String firstImage=cursor.getString(cursor.getColumnIndex(
					MediaStore.Images.Media.DATA));
			//Log.d("ImageSearcher", firstImage);
			//ͼƬĿ¼���ļ���·����
			File floder=new File(firstImage).getParentFile();
			if(floder==null)
				continue;
			String path=floder.getAbsolutePath();
			//�Ƿ��ظ�
			if(paths.contains(path))
				continue;
			paths.add(path);
			//ͼƬ�������ļ��д�С��
			String[] images=floder.list(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String filename) {
					if(filename.endsWith(".jpg")||
							filename.endsWith(".png")||
							filename.endsWith(".jpeg"))
						return true;
					return false;
				}
			});
			if(images==null||images.length<=0)
				continue;
			int count=images.length;
			//�����¼���ļ�����Ϣ��
			ImageFloder imageFloder=new ImageFloder(
					path, firstImage, count);
			floders.add(imageFloder);
		}
		paths=null;
		return floders;
	}

	public static class ImageFloder{
		private String path;
		private String firstImage;
		private int count;
		
		public ImageFloder(String path, String firstImage, int count){
			this.path=path;
			this.firstImage=firstImage;
			this.count=count;
		}
		
		public String getPath(){return path;}
		public String getFirstImage(){return firstImage;}
		public int getCount(){return count;}
		public String getFloderName(){
			return path.substring(path.lastIndexOf("/")+1);
		}
	}
}

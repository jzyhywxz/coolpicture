package com.zzw.coolpicture.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @author zzw
 * @version 1.0
 * ͼƬ����������ͼƬ�ڻ�����ʱ��ֱ����UIHandler�и���UI��
 * ��������������м��������񣬲������߳��м�������ͼ����ͨ��UIHandler����UI
 */
public class ImageLoader {
	// �������
	private LinkedList<Runnable> mTaskQueue=new LinkedList<Runnable>();
	// ����ͼ����
	private LruCache<String, Bitmap> mLruCache;
	// ����ģʽ
	public enum Type{FIFO, LIFO}
	private Type mType=Type.LIFO;
	// ���̣߳������������ȡ����������ͼƬ����ͼ
	private Handler mTaskHandler;
	private Semaphore mTaskSemaphore=new Semaphore(0);
	private ExecutorService mTaskExecutor;
	// ���̣߳�����������з������񣬸���UI
	private Handler mUIHandler;
	// ��̬ʵ��
	private static ImageLoader mInstance;

	// ��ȡ��̬ʵ��
	public static ImageLoader getInstance(){
		if (mInstance==null){
			synchronized(ImageLoader.class){
				if(mInstance==null){
					mInstance=new ImageLoader(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}
	
	// ��ȡ��̬ʵ��
	public static ImageLoader getInstance(int threadCount, Type type){
		if(mInstance==null){
			synchronized(ImageLoader.class){
				if(mInstance==null){
					mInstance=new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}
	
	//˽�й�����
	private ImageLoader(int threadCount, Type type){
		init(threadCount, type);
	}

	//��ʼ������
	private void init(int threadCount, Type type){
		int maxMemory=(int)Runtime.getRuntime().maxMemory();
		int cacheSize=maxMemory/8;
		mLruCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value){
				return value.getRowBytes()*value.getHeight();
			};
		};
		
		new Thread(){
			@Override
			public void run(){
				Looper.prepare();
				mTaskHandler=new Handler(){
					@Override
					public void handleMessage(Message msg){
						mTaskExecutor.execute(getTask());
					}
				};
				mTaskSemaphore.release();
				Looper.loop();
			}
		}.start();
		
		mTaskExecutor=Executors.newFixedThreadPool(threadCount);
		mType=(type==null) ? Type.LIFO : type;
	}

	//��������ͼ
	public void loadImage(final String path, final ImageView imageView){
		imageView.setTag(path);
		
		if (mUIHandler==null){
			mUIHandler=new Handler(){
				@Override
				public void handleMessage(Message msg){
					ImgBeanHolder holder=(ImgBeanHolder)msg.obj;
					ImageView imageView=holder.imageView;
					Bitmap bm=holder.bitmap;
					String path=holder.path;
					if(imageView.getTag().toString().equals(path)){
						imageView.setImageBitmap(bm);
					}
				}
			};
		}

		Bitmap bm=getBitmapFromLruCache(path);
		if (bm!=null){
			ImgBeanHolder holder=new ImgBeanHolder();
			holder.bitmap=bm;
			holder.imageView=imageView;
			holder.path=path;
			Message message=Message.obtain();
			message.obj=holder;
			mUIHandler.sendMessage(message);
		}
		else{
			addTask(new Runnable(){
				@Override
				public void run(){
					int[] imageSize=getImageViewWidth(imageView);
					int reqWidth=imageSize[0];
					int reqHeight=imageSize[1];

					Bitmap bm=decodeSampledBitmapFromResource(path, 
							reqWidth, reqHeight);
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder=new ImgBeanHolder();
					holder.bitmap=getBitmapFromLruCache(path);
					holder.imageView=imageView;
					holder.path=path;
					Message message=Message.obtain();
					message.obj=holder;
					mUIHandler.sendMessage(message);
				}
			});
		}

	}
	
	//���һ������
	private synchronized void addTask(Runnable runnable){
		try{
			if(mTaskHandler==null)
				mTaskSemaphore.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		mTaskQueue.add(runnable);
		mTaskHandler.sendEmptyMessage(0);
	}

	//���һ������
	private synchronized Runnable getTask(){
		if (mType==Type.FIFO){
			return mTaskQueue.removeFirst();
		}
		else if(mType==Type.LIFO){
			return mTaskQueue.removeLast();
		}
		return null;
	}

	//���ImageViewά����Ϣ
	private int[] getImageViewWidth(ImageView imageView){
		int[] imageSize=new int[2];
		final DisplayMetrics displayMetrics=imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params=imageView.getLayoutParams();

		//get actual image width
		int width=(params.width==LayoutParams.WRAP_CONTENT) ? 0 : 
			imageView.getWidth();
		//get layout width parameter
		if(width<=0)
			width=params.width; 
		if(width<=0)
			width=getImageViewFieldValue(imageView, "mMaxWidth");
		if(width<=0)
			width=displayMetrics.widthPixels;
		
		//get actual image height
		int height=(params.height==LayoutParams.WRAP_CONTENT) ? 0 : 
			imageView.getHeight();
		//get layout height parameter
		if(height<=0)
			height=params.height;
		if(height<=0)
			height=getImageViewFieldValue(imageView, "mMaxHeight");
		if(height<=0)
			height=displayMetrics.heightPixels;
		
		imageSize[0]=width;
		imageSize[1]=height;
		return imageSize;
	}

	//�ӻ���������ͼ
	private Bitmap getBitmapFromLruCache(String key){
		return mLruCache.get(key);
	}

	//������ͼ���뻺��
	private void addBitmapToLruCache(String key, Bitmap bitmap){
		if(getBitmapFromLruCache(key)==null){
			if(bitmap!=null)
				mLruCache.put(key, bitmap);
		}
	}

	//��������ͼ��С
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight){
		int width=options.outWidth;
		int height=options.outHeight;
		int inSampleSize=1;

		if(width>reqWidth&&height>reqHeight){
			int widthRatio=Math.round((float)width/(float)reqWidth);
			int heightRatio=Math.round((float)height/(float)reqHeight);
			inSampleSize=Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	//������ͼ���б���
	public Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight){
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(pathName, options);
		
		options.inSampleSize=calculateInSampleSize(options, reqWidth,
				reqHeight);
		
		options.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeFile(pathName, options);

		return bitmap;
	}

	//ͼƬ��Ϣ
	private class ImgBeanHolder{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

	//ʹ�÷����ȡ�ض���
	private static int getImageViewFieldValue(Object object, 
			String fieldName){
		int value=0;
		try{
			Field field=ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue=(Integer)field.get(object);
			if(fieldValue>0&&fieldValue<Integer.MAX_VALUE){
				value=fieldValue;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
}

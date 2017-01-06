package com.zzw.coolpicture.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class MyRenderer implements Renderer {
	private float rotateX=30.0f;
	private float rotateY=30.0f;
	private float rotateZ=0.0f;
	private FloatBuffer cubeVertBuf;	// 顶点
	private IntBuffer cubeColoBuf;		// 颜色
	private ByteBuffer cubeFaceBuf;		// 表面
	
	public MyRenderer(){
		float[] cubeVerties=new float[]{
				0.5f, 0.5f, 0.5f, // 上右外
				0.5f, -0.5f, 0.5f, // 下右外
				-0.5f, -0.5f, 0.5f, // 下左外
				-0.5f, 0.5f, 0.5f, // 上左外
				0.5f, 0.5f, -0.5f, // 上右内
				0.5f, -0.5f, -0.5f, // 下右内
				-0.5f, -0.5f, -0.5f, // 下左内
				-0.5f, 0.5f, -0.5f, // 上左内
		};
		cubeVertBuf=bufferUtil(cubeVerties);
		int[] cubeColors=new int[]{
				65535, 0, 0, 0, // 红色
				0, 65535, 0, 0, // 绿色
				0, 0, 65535, 0, // 蓝色
				0, 65535, 65535, 0, // 青色
				65535, 0, 65535, 0, // 紫色
				65535, 65535, 0, 0, // 黄色
		};
		cubeColoBuf=bufferUtil(cubeColors);
		byte[] cubeFaces=new byte[]{
				0, 1, 2, 0, 2, 3, // 上面
				4, 5, 6, 4, 6, 7, // 下面
				1, 2, 6, 1, 5, 6, // 左面
				0, 3, 7, 0, 4, 7, // 右面
				0, 1, 4, 1, 4, 5, // 前面
				2, 3, 7, 2, 6, 7, // 后面
		};
		cubeFaceBuf=bufferUtil(cubeFaces);
	}

	public void addRotate(float dx, float dy, float dz){
		rotateX+=dx;
		rotateY+=dy;
		rotateZ+=dz;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(0, 0, 0, 0);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio=(float)width/height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(0.0f, 0.0f, -2.0f);
		gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(rotateZ, 0.0f, 0.0f, 1.0f);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVertBuf);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, cubeColoBuf);
		gl.glDrawElements(GL10.GL_TRIANGLES, cubeFaceBuf.remaining(), GL10.GL_UNSIGNED_BYTE, cubeFaceBuf);
		
		gl.glFinish();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
	
	private IntBuffer bufferUtil(int[] arr){
		IntBuffer buffer;
		ByteBuffer bb=ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		buffer=bb.asIntBuffer();
		buffer.put(arr);
		buffer.flip();
		buffer.position(0);
		return buffer;
	}
	
	private FloatBuffer bufferUtil(float[] arr){
		FloatBuffer buffer;
		ByteBuffer bb=ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		buffer=bb.asFloatBuffer();
		buffer.put(arr);
		buffer.flip();
		buffer.position(0);
		return buffer;
	}

	private ByteBuffer bufferUtil(byte[] arr){
		ByteBuffer buffer;
		ByteBuffer bb=ByteBuffer.allocateDirect(arr.length);
		bb.order(ByteOrder.nativeOrder());
		buffer=bb;
		buffer.put(arr);
		buffer.flip();
		buffer.position(0);
		return buffer;
	}
}

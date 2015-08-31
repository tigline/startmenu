/**
 * 
 */
package com.tcl.startmenu;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

/**
 * @Project StartMenu	
 * @author houxb
 * @Date 2015-8-31
 */
public class MySurfaceView extends GLSurfaceView {
	
	int textureId;
	private SceneRenderer mRenderer;
	boolean lightFlag=true;	
	/**
	 * @param context
	 */	
	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		// TODO Auto-generated constructor stub
	}

	private class SceneRenderer implements GLSurfaceView.Renderer{
		Circle circle;
		CircleL circleL;
		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
		 */
		@Override
		public void onDrawFrame(GL10 arg0) {
			// TODO Auto-generated method stub
			//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);   
            
            //保护现场
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -10);
            //circle.drawSelf(textureId);
            circleL.drawSelf();
            MatrixState.popMatrix();
		}

		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
		 */
		@Override
		public void onSurfaceChanged(GL10 arg0, int width, int height) {
			// TODO Auto-generated method stub
			//设置视窗大小及位置 
        	GLES20.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            float ratio= (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4f, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,8.0f,0f,0f,0f,0f,1.0f,0.0f); 
            
	        //初始化光源
	        MatrixState.setLightLocation(10 , 0 , -10);
	                      
	        //启动一个线程定时修改灯光的位置
	        new Thread()
	        {
				public void run()
				{
					float redAngle = 0;
					while(lightFlag)
					{	
						//根据角度计算灯光的位置
						redAngle=(redAngle+5)%360;
						float rx=(float) (15*Math.sin(Math.toRadians(redAngle)));
						float rz=(float) (15*Math.cos(Math.toRadians(redAngle)));
						MatrixState.setLightLocation(rx, 0, rz);
						
						try {
								Thread.sleep(100);
							} catch (InterruptedException e) {				  			
								e.printStackTrace();
							}
					}
				}
	        }.start();
        
		}

		/* (non-Javadoc)
		 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
		 */
		@Override
		public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
			// TODO Auto-generated method stub
			//设置屏幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f, 1.0f);  
            //启用深度测试
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    		//设置为打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //加载纹理
            textureId = initTexture(R.drawable.menu_robot);
            circle = new Circle(MySurfaceView.this, 1, 1.6f, 36);
            circleL=new CircleL(MySurfaceView.this,1, 1.6f, 36);
		}
		
	}
	public int initTexture(int drawableId)//textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================  
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        
        return textureId;
	}
}

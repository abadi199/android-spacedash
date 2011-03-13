package com.ratmonkey.spacedash.object;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class World {
	float lightAmbient[] = new float[] { 0.3f, 0.3f, 0.3f, 1.0f };
	float lightDiffuse[] = new float[] { 0.9f, 0.9f, 0.9f, 1.0f };
//	float lightAmbient[] = new float[] { 0.0f, 0.0f, 1f, 1.0f };
//	float lightDiffuse[] = new float[] { 0.0f, 0.0f, 1f, 1.0f };
	
	float matAmbient[] = new float[] { 1f, 1f, 1f, 1.0f };
	float matDiffuse[] = new float[] { 1f, 1f, 1f, 1.0f };

	float[] lightPos = new float[] {0,0,-10};
	
	// Fog
	private float[] fogColor = {0.1f, 0.1f, 0.1f, 1.0f};
	private FloatBuffer fogColorBuffer;	//The Fog Color Buffer  ( NEW )
	
	/* The buffers for our light values ( NEW ) */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;

	public World() {
//		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		lightAmbientBuffer = byteBuf.asFloatBuffer();
//		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer = FloatBuffer.wrap(lightAmbient);
		lightAmbientBuffer.position(0);
		
//		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		lightDiffuseBuffer = byteBuf.asFloatBuffer();
//		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer = FloatBuffer.wrap(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
//		byteBuf = ByteBuffer.allocateDirect(lightPos.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		lightPositionBuffer = byteBuf.asFloatBuffer();
//		lightPositionBuffer.put(lightPos);
		lightPositionBuffer = FloatBuffer.wrap(lightPos);
		lightPositionBuffer.position(0);
		
//		byteBuf = ByteBuffer.allocateDirect(fogColor.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		fogColorBuffer = byteBuf.asFloatBuffer();
//		fogColorBuffer.put(fogColor);
		fogColorBuffer = FloatBuffer.wrap(fogColor);
		fogColorBuffer.position(0);
	}
	
	public void initWorld(GL10 gl) {
		//Settings
		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
	    
//		gl.glEnable(GL10.GL_CULL_FACE);
//		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth ShadingGL
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}
	
	public void initLight(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);		//Setup The Ambient Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);		//Setup The Diffuse Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);	//Position The Light ( NEW )
		gl.glEnable(GL10.GL_LIGHT0);											//Enable Light 0 ( NEW )

		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		
		//The Fog/The Mist
		gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);	//Fog Mode ( NEW )
		gl.glFogfv(GL10.GL_FOG_COLOR, fogColorBuffer);		//Set Fog Color ( NEW )
		gl.glFogf(GL10.GL_FOG_DENSITY, 0.1f);				//How Dense Will The Fog Be ( NEW )
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST);		//Fog Hint Value ( NEW )
		gl.glFogf(GL10.GL_FOG_START, 70.0f);					//Fog Start Depth ( NEW )
		gl.glFogf(GL10.GL_FOG_END, 100.0f);					//Fog End Depth ( NEW )
		gl.glEnable(GL10.GL_FOG);							//Enables GL_FOG ( NEW )
		
	}
	
	public void resetColor() {
		lightAmbient[0] = 0.4f;
		lightAmbient[1] = 0.4f;
		lightAmbient[2] = 0.4f;
		lightAmbient[3] = 1.0f;
		
		lightDiffuse[0] = 0.9f;
		lightDiffuse[1] = 0.9f;
		lightDiffuse[2] = 0.9f;
		lightDiffuse[3] = 1.0f;		
	}
	
	public void setColor(float r, float g, float b, float a) {
		lightAmbient[0] = r;
		lightAmbient[1] = g;
		lightAmbient[2] = b;
		lightAmbient[3] = a;
		
		lightDiffuse[0] = r * 0.9f;
		lightDiffuse[1] = g * 0.9f;
		lightDiffuse[2] = b * 0.9f;
		lightDiffuse[3] = a;
	}
}

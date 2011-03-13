package com.ratmonkey.spacedash.object;

import javax.microedition.khronos.opengles.GL10;

public class Camera {
	/* Rotation speed values */
	public float xspeed;				//X Rotation Speed ( NEW )
	public float yspeed;				//Y Rotation Speed ( NEW )
	
	public float oldX;
	public float oldY;
	public final float TOUCH_SCALE = 0.2f;		//Proved to be good for normal rotation ( NEW )
	
	public float z = -20.0f;			//Depth Into The Screen ( NEW )
	
	public float xrot = 15f;
	public float yrot;

	public void setCameraLocation(GL10 gl) {
		//Drawing
		gl.glTranslatef(0.0f, 0.0f, z);			//Move z units into the screen
		gl.glScalef(0.8f, 0.8f, 0.8f); 			//Scale the Cube to 80 percent, otherwise it would be too large for the screen
		
		//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		
	}
	
	public void autoRotate() {
		xrot += xspeed;
		yrot += yspeed;		
	}
	
}

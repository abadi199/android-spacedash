package com.ratmonkey.spacedash.particle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

/**
 * This class is an object representation of a Star
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Particle {

	public float[] position = {0,0,0}; // Position
	public float[] direction = {0,0,0}; // Direction
	public float r, g, b, a; // Color
	public float deltaR, deltaG, deltaB, deltaA;
	public float timeToLive;
	public float particleSize;

	/**
	 * The Star constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Particle() {
	}
	
	public void clear() {
		position[0] = 0;
		position[1] = 0;
		position[2] = 0;
		
		direction[0] = 0;
		direction[1] = 0;
		direction[2] = 0;
		
		r = 0;
		g = 0;
		b = 0;
		a = 0;
		deltaR = 0;
		deltaG = 0;
		deltaB = 0;
		deltaA = 0;
		
		timeToLive = 0;
		particleSize = 0;
	}

	/**
	 * The object own drawing function. Called from the renderer to redraw this
	 * instance with possible changes in values.
	 * 
	 * @param gl
	 *            - The GL Context
	 */
/*	public void draw(GL10 gl) {
		// Enable the vertex, texture and normal state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		// gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		// Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public void draw(GL11 gl) {
		// Enable the vertex, texture and normal state
		gl.glPushMatrix();

		gl.glTranslatef(x, y, z);
		gl.glColor4f(r, g, b, a);
		gl.glEnable(GL11.GL_POINT_SPRITE_OES);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// Point to our buffers
		gl.glEnable(GL11.GL_POINT_SMOOTH);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glPointSize(32);

		int[] ret = new int[2];
		gl.glGetIntegerv(GL11.GL_SMOOTH_POINT_SIZE_RANGE, ret, 0);
		Log.d("Ratmonkey", "GL_SMOOTH_POINT_SIZE_RANGE:"+Arrays.toString(ret));

		gl.glGetIntegerv(GL11.GL_ALIASED_POINT_SIZE_RANGE, ret, 0);
		Log.d("Ratmonkey", "GL_ALIASED_POINT_SIZE_RANGE:"+Arrays.toString(ret));

		float[] attenuation = { 0.0f, 0.0f, 1.0f };
		gl.glPointParameterfv(GL11.GL_POINT_DISTANCE_ATTENUATION, attenuation,
				0);

		gl.glTexEnvf(GL11.GL_POINT_SPRITE_OES, GL11.GL_COORD_REPLACE_OES,
				GL11.GL_TRUE);
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_POINTS, 0, vertices.length / 3);

		// Disable the client state before leaving
		gl.glTexEnvf(GL11.GL_POINT_SPRITE_OES, GL11.GL_COORD_REPLACE_OES,
				GL11.GL_FALSE);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisable(GL11.GL_POINT_SPRITE_OES);
		gl.glPopMatrix();
	}
*/
}
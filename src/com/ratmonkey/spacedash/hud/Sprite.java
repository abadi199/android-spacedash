package com.ratmonkey.spacedash.hud;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Sprite {
	public int texU;
	public int texV;
	public int texW;
	public int texH;

	public int screenX;
	public int screenY;

	public int textureName;

	public boolean isActive = true;
	public boolean isVisible = true;

	/**
	 * (top, left) as 0,0 _______________ |0,0 | | ________ | | | | | | |0,0___|
	 * | | | |_____________|
	 * 
	 * @param x
	 *            left coordinate of texture
	 * @param y
	 *            bottom coordinate of texture
	 * @param width
	 * @param height
	 * @param textureName
	 * @param screenX
	 *            origin at left of screen
	 * @param screenY
	 *            origin at bottom of screen
	 */
	public Sprite(int x, int y, int width, int height, int screenX,
			int screenY, int textureName) {
		this.texU = x;
		this.texV = y;
		this.texW = width;
		this.texH = -height;
		this.screenX = screenX;
		this.screenY = screenY;
		this.textureName = textureName;
		// Log.d("Ratmonkey", "screenX:"+screenX+" screenY:"+screenY);
	}

	public void draw(GL10 gl) {
		if (isVisible) {
			gl.glPushMatrix();
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, new int[] { texU, texV,
							texW, texH }, 0);
			((GL11Ext) gl).glDrawTexiOES(screenX, screenY, 0, texW, -texH);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glPopMatrix();
		}
	}

	public void setTextureName(int textureName) {
		this.textureName = textureName;
	}

	public void loadTexture(Context context, GL10 gl, int drawableId) {
		int[] tex_out = new int[1];
		gl.glGenTextures(1, tex_out, 0);

		textureName = tex_out[0];

		InputStream stream = context.getResources().openRawResource(drawableId);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;

		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		// Log.d("Ratmonkey", "Bitmap width:" + bitmap.getWidth() + ", height:"
		// + bitmap.getHeight());

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_MODULATE);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		int error = gl.glGetError();
		if (error != GL10.GL_NO_ERROR) {
			Log.e("Ratmonkey", "[Sprite][" + drawableId + "]Texture Load GLError: " + error);
		}
		bitmap.recycle();
		try {
			stream.close();
		} catch (IOException e) {
		}
	}
}

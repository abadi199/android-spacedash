package com.ratmonkey.spacedash.hud;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.ratmonkey.spacedash.R;

public class HUD {
	public int textureId = R.drawable.hud;
	int textureName;
	Context mContext;
	ArrayList<Sprite> sprites;
	int screenWidth, screenHeight;
	public SpriteBar shield;
//	int[] numbersX24 = new int[] { 0, 22, 38, 56, 74, 94, 112, 130, 149, 167 };
	int[] numbersX24 = new int[] { 255, 277, 293, 311, 329, 349, 367, 385, 404, 422 };
	int[] numbersWidth24 = new int[] { 15, 6, 15, 14, 16, 14, 14, 14, 14, 14 };

//	int[] numbersX18 = new int[] { 0, 17, 29, 42, 56, 71, 84, 98, 112, 126};
	int[] numbersX18 = new int[] { 255, 272, 284, 297, 311, 326, 339, 353, 367, 381};
	int[] numbersWidth18 = new int[] { 11, 5, 11, 11, 12, 11, 11, 11, 11, 11 };
	
	SpriteNumeric targetCurrentLabel;
	SpriteNumeric targetGoalLabel;
	SpriteNumeric timeMinuteLabel;
	SpriteNumeric timeSecondLabel;
	SpriteNumeric missileCountLabel;
	SpriteSeries startlight;
	Sprite gameover;
	Sprite missionAccomplished;
	Sprite touchanywhere;

	public int shieldPercentage;
	
	public int minutes;
	public int seconds;
	
	public int goal;
	public int current;
	
	public int missileCount;
		
	public HUD(Context context) {
		mContext = context;
		sprites = new ArrayList<Sprite>();
	}
	
	public void init(int screenWidth, int screenHeight) {
		Log.d("Ratmonkey", "[HUD] init, textureName:"+textureName);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		sprites.add(new Sprite(0, 10, 41, 10, 10, screenHeight - 20, textureName)); // time
		sprites.add(new Sprite(0, 20, 68, 10, 10, 29, textureName)); // target
		sprites.add(new Sprite(90, 52, 151, 47, screenWidth - 161, 10, textureName)); // button
		
		shield = new SpriteBar(0, 512, 24, 247, screenWidth - 34, 62, 21, textureName);
		setShieldPercentage(100);
		sprites.add(shield);

		targetCurrentLabel = new SpriteNumeric(255, 19, 177, 13, 10, 10, numbersX24, numbersWidth24, 2, 17, textureName);
		targetGoalLabel = new SpriteNumeric(255, 19, 177, 13, 57, 10, numbersX24, numbersWidth24, 2, 17, textureName);
		setGoal(10);
		setCurrent(0);
		sprites.add(targetGoalLabel);
		sprites.add(new Sprite(450, 20, 7, 14, 47, 10, textureName)); //  '/'
		sprites.add(targetCurrentLabel);

		timeMinuteLabel = new SpriteNumeric(255, 19, 177, 13, 10, screenHeight - 40, numbersX24, numbersWidth24, 2, 17, textureName);
		timeSecondLabel = new SpriteNumeric(255, 19, 177, 13, 57, screenHeight - 40, numbersX24, numbersWidth24, 2, 17, textureName);
		setTime(00,00);
		sprites.add(timeSecondLabel);
		sprites.add(new Sprite(468, 20, 7, 14, 47, screenHeight - 40, textureName)); //  ':'
		sprites.add(timeMinuteLabel);
		
		missileCountLabel = new SpriteNumeric(255, 42, 137, 10, screenWidth - 146, 28, numbersX18, numbersWidth18, 2, 14, textureName);
		setMissileCount(0);
		sprites.add(missileCountLabel);
		
		startlight = new SpriteSeries(0, 138, 174, 53, screenWidth/2-87, screenHeight - 73, 
				new int[] {0, 0, 0, 0, 0}, 
				new int[] {138, 0, 192, 0, 246}, 
				new int[] {2000, -250, 2000, -250, 2000}, textureName);
		sprites.add(startlight);
		gameover = new Sprite(0, 84, 173, 31, screenWidth/2-87, screenHeight/2-15, textureName);
		missionAccomplished = new Sprite(174, 84, 311, 31, screenWidth/2-156, screenHeight/2-15, textureName);
		touchanywhere = new Sprite(0, 256, 259, 9, screenWidth/2-129, 75, textureName);
		gameover.isVisible = false;
		missionAccomplished.isVisible = false;
		touchanywhere.isVisible = false;
		sprites.add(gameover);
		sprites.add(missionAccomplished);
		sprites.add(touchanywhere);
	}
	
	public boolean tickStartlight() {
		return startlight.tick();
	}
	
	public void setShieldPercentage(int percentage) {
		if(percentage < 0) {
			percentage = 0;
		}
		else if(percentage > 100) percentage = 100;
		this.shieldPercentage = percentage;
		shield.setPercentage(percentage);
	}
	
	public void setGoal(int goal) {
		if(goal < 0) goal = 0;
		this.goal = goal;
		targetGoalLabel.setNumber(goal);
	}
	
	public void setCurrent(int current) {
		if(current < 0) current = 0;
		this.current = current;
		targetCurrentLabel.setNumber(current);
	}
	
	public void setMissileCount(int count) {
		if(count < 0) count = 0;
		this.missileCount = count;
		missileCountLabel.setNumber(count);
	}
	
	public boolean setTime(int minutes, int seconds) {
		if(seconds > 59) seconds = 59;
		else if(seconds < 0) seconds = 0;
		
		if(minutes > 99) minutes = 99;
		if(minutes < 0) { 
			minutes = 0;
		}
			
		this.minutes = minutes;
		this.seconds = seconds;
		timeSecondLabel.setNumber(seconds);
		timeMinuteLabel.setNumber(minutes);
		return (minutes != 0 || seconds != 0);
	}
	
	public boolean tick() {
		seconds--;
		if(seconds < 0) {
			minutes--;
			if(minutes >= 0) {
				seconds = 59;
			}
		}
		return setTime(minutes, seconds);
	}

	public void loadTexture(GL10 gl) throws IOException {
		Log.d("Ratmonkey", "[HUDRenderer] loadTexture");

		int[] tex_out = new int[1];
		gl.glGenTextures(1, tex_out, 0);

		textureName = tex_out[0];

		InputStream stream = mContext.getResources().openRawResource(textureId);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;

		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		Log.d("Ratmonkey", "Bitmap width:" + bitmap.getWidth() + ", height:"
				+ bitmap.getHeight());

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
				GL10.GL_REPLACE);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		int error = gl.glGetError();
		if (error != GL10.GL_NO_ERROR) {
			Log.e("Ratmonkey", "[HUD][" + textureId + "]Texture Load GLError: " + error);
		}
		bitmap.recycle();
		stream.close();

		int size = sprites.size();
		for (int i = 0; i < size; i++) {
			sprites.get(i).setTextureName(textureName);			
		}
	}

	public void draw(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, screenWidth, 0.0f, screenHeight, 0.0f, 1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		
		// Magic offsets to promote consistent rasterization.
		gl.glTranslatef(0.375f, 0.375f, 0.0f);

		gl.glDisable(GL10.GL_FOG);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		int size = sprites.size();
		for (int i = 0; i < size; i++) {
			Sprite s = sprites.get(i);
			if(s.isActive) {
				if(s.isVisible) {
					s.draw(gl);
				}
			}else {
				sprites.remove(i);
				size--;
			}
		}
		
		gl.glEnable(GL10.GL_FOG);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glDisable(GL10.GL_BLEND);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();


	}
	
	public void showGameOver() {
		gameover.isVisible = true;
		touchanywhere.isVisible = true;
	}
	
	public void hideGameOver() {
		gameover.isVisible = false;
		touchanywhere.isVisible = false;
	}
	
	public void showMissionAccomplished() {
		missionAccomplished.isVisible = true;
		touchanywhere.isVisible = true;
	}
	
	public void hideMissionAccomplished() {
		missionAccomplished.isVisible = false;
		touchanywhere.isVisible = false;
	}
	
	public void setTexture(int textureName) {
		this.textureName = textureName;
	}
}

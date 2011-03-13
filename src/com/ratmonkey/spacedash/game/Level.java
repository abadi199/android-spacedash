package com.ratmonkey.spacedash.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.ratmonkey.spacedash.R;
import com.ratmonkey.spacedash.collision.Bound;
import com.ratmonkey.spacedash.collision.BoundBox;
import com.ratmonkey.spacedash.collision.BoundComposite;
import com.ratmonkey.spacedash.math.Functions;
import com.ratmonkey.spacedash.object.Object3D;
import com.ratmonkey.spacedash.object.ObjectLoader;

public class Level {

	GameEngine mEngine;

	long lastTime;

	Object3D wall;
	Object3D bridge;
	Object3D block;
	Object3D cube;
	Object3D sphere;
	Object3D item;
	Object3D pipe;

	ArrayList<Object3D> objects;
//	int textureConcrete;
//	int textureConcrete2;
//	int textureMetal;
//	int textureMetal2;
	int textureContainerBlue;
	int textureContainerRed;
	int textureContainerGreen;
	int textureSatellite;
	int textureGoal;
	int textureShield;
	int textureBridge;
	int textureWall;
	int textureDam;
	int texturePipe;
	
	float rotationSpeed = 10;
	
	float obstacleGap = 5000;

	public Level(GameEngine engine) {
		this.mEngine = engine;

		wall = new Object3D(mEngine, Bound.BOUND_BOX, Object3D.TYPE_OBSTACLE);
		pipe = new Object3D(mEngine, Bound.BOUND_BOX, Object3D.TYPE_OBSTACLE);
		bridge = new Object3D(mEngine, Bound.BOUND_COMPOSITE, Object3D.TYPE_OBSTACLE, 2);
		block = new Object3D(mEngine, Bound.BOUND_BOX, Object3D.TYPE_OBSTACLE);
		cube = new Object3D(mEngine, Bound.BOUND_BOX, Object3D.TYPE_OBSTACLE);
		sphere = new Object3D(mEngine, Bound.BOUND_SPHERE, Object3D.TYPE_OBSTACLE);
		item = new Object3D(mEngine, Bound.BOUND_SPHERE, Object3D.TYPE_GOAL);
		
		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.wall, wall);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading wall:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.pipe, pipe);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading pipe:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.bridge, bridge);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading bridge:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.block, block);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading cylinder:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.cube, cube);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading cylinder:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.sphere, sphere);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading cylinder:"
					+ e.getMessage());
		}

		try {
			ObjectLoader.loadRaw(mEngine.mContext, R.raw.item, item);
		} catch (Exception e) {
			Log.d("Ratmonkey", "[Level] Error loading goal:"
					+ e.getMessage());
		}

		objects = new ArrayList<Object3D>();
	}

	public void load(GL10 gl) {
//		Log.d("Ratmonkey", "[Level] load");
		
		wall.loadTextureBuffer(gl);
		pipe.loadTextureBuffer(gl);
		bridge.loadTextureBuffer(gl);
		block.loadTextureBuffer(gl);
		cube.loadTextureBuffer(gl);
		sphere.loadTextureBuffer(gl);
		item.loadTextureBuffer(gl);
		
		loadAllTexture(gl);
	}

	public boolean moveForward(float speed, long pauseDuration) {
		boolean isEmit = false;
		try {
			long time = System.currentTimeMillis();
			long delta = (time - lastTime) - pauseDuration;
//			Log.d("Ratmonkey", "[Level] delta:"+delta+", time: "+time+", lastTime: "+lastTime+", pause: "+pause);
			if (delta > (obstacleGap/speed)) { // Emit new object every n second (1000 = 1 second)
				if(lastTime != 0) {
					isEmit = true;
					int rand = (int) Math.round(Math.random() * 5.0f);
					Object3D obj;
					switch (rand) {
					case 0:
						obj = newObject3D(wall);
						setRandomWallPosition(obj);
						obj.setTexture(textureWall);
						break;
					case 1:
						obj = newObject3D(bridge);
						BoundComposite composite = (BoundComposite) obj.mBound;
						composite.addBound(new BoundBox(obj,
								-50.200752f,3.431392f,-5.967850f,
								50.200794f, 9.398803f, 5.967855f));
						composite.addBound(new BoundBox(obj,
								-1.708372f, -13.912336f, -1.708372f,
								1.708372f, 3.429847f, 1.708372f));
//						composite.addBound(new BoundBox(obj,
//								-19.292418f, -13.912336f, -1.708371f,
//								-22.709160f, 3.429847f, 1.708372f));
//						composite.addBound(new BoundBox(obj,
//								19.295223f, -13.912336f, -1.708372f,
//								22.711966f, 3.429847f, 1.708372f));
						obj.setTexture(textureBridge);
						break;
					case 2:
						obj = newObject3D(item);
						obj.type = Object3D.TYPE_GOAL;
						obj.isBlend = true;
						obj.translateYWithBound(7 * Functions.randomMinus1To1()); // randomize x
						obj.translateXWithBound(12 * Functions.randomMinus1To1()); // randomize x
						obj.setTexture(textureGoal);
						break;
					case 3:
						obj = newObject3D(item);
						obj.type = Object3D.TYPE_SHIELD_UP;
						obj.isBlend = true;
						obj.translateYWithBound(7 * Functions.randomMinus1To1()); // randomize x
						obj.translateXWithBound(12 * Functions.randomMinus1To1()); // randomize x
						obj.setTexture(textureShield);
						break;
					case 4:
						obj = newObject3D(pipe);
						obj.setTexture(texturePipe);
						break;
//					case 4:
//						obj = newObject3D(block);
//						obj.translateXWithBound(12 * Functions.randomMinus1To1()); // randomize x
//						setRandomTexture(obj);
//						break;
//					case 5:
//						obj = newObject3D(cube);
//						obj.translateXWithBound(12 * Functions.randomMinus1To1()); // randomize x
//						setRandomContainerTexture(obj);
//						break;
//					case 6:
//						obj = newObject3D(sphere);
//						obj.translateYWithBound(7 * Functions.randomMinus1To1()); // randomize x
//						obj.translateXWithBound(12 * Functions.randomMinus1To1()); // randomize x
//						obj.setTexture(textureSatellite);
//						break;
//					case 7:
//						obj = newObject3D(block);
//						obj.translateYWithBound(7 * Functions.randomMinus1To1()); // randomize x
//						setRandomTexture(obj);
//						break;
					default:
						obj = newObject3D(wall);
						obj.translateYWithBound(-11);
						obj.setTexture(textureDam);
						break;							
					}
					obj.translateZWithBound(-101);
					mEngine.mCollisionDetector.addBound(obj);

					objects.add(obj);
					mEngine.mRenderer.addObject(obj);
				}
				lastTime = time;
			}
	
			int size = objects.size();
			for (int i = 0; i < size; i++) {
				Object3D obj = objects.get(i);
				obj.translateZWithBound(obj.posZ + speed);
				switch (obj.type) {
				case Object3D.TYPE_GOAL:
				case Object3D.TYPE_SHIELD_UP:
					obj.rotateY(obj.rotY + rotationSpeed);
					break;
				}
				if (obj.posZ > 10) {
	//				Log.d("Ratmonkey","[Level] remove("+i+") size:"+objects.size());
					objects.remove(obj);
					mEngine.mRenderer.removeObject(obj);
					mEngine.mCollisionDetector.removeBound(obj);
					size--;
	//				Log.d("Ratmonkey", "[Level] end remove objects");
				}
			}
		}catch(Exception e) {
			Log.d("Ratmonkey","[Level] error on moveForward:"+e.getMessage());
		}
		return isEmit;
	}
	
	private void setRandomWallPosition(Object3D obj) {
		int rand = (int) Math.round(Math.random() * 1.0);
		switch (rand) {
		case 0:
			obj.translateXWithBound(-35);
			break;
		case 1:
			obj.translateXWithBound(37);
			break;
		}
	}
	
//	private void setRandomTexture(Object3D obj) {
//		int rand = (int) Math.round(Math.random() * 3.0);
//		switch (rand) {
//		case 0:
//			obj.setTexture(textureMetal2);
//			break;
//		case 1:
//			obj.setTexture(textureConcrete);
//			break;
//		case 2:
//			obj.setTexture(textureConcrete2);
//			break;
//		default:
//			obj.setTexture(textureMetal2);
//			break;
//		}	
//	}
	
	private void setRandomContainerTexture(Object3D obj) {
		int rand = (int) Math.round(Math.random() * 3.0);
		switch (rand) {
		case 0:
			obj.setTexture(textureContainerBlue);
			break;
		case 1:
			obj.setTexture(textureContainerRed);
			break;
		case 2:
			obj.setTexture(textureContainerGreen);
			break;
		default:
			obj.setTexture(textureContainerBlue);
			break;
		}	
	}	

	public Object3D newObject3D(Object3D source) {
		Object3D obj;
		if(source.boundType == Bound.BOUND_COMPOSITE) {
			obj = new Object3D(mEngine, source.boundType, source.type, ((BoundComposite)source.mBound).numberOfBound);
			ObjectLoader.copyData(source, obj);
			obj.mBound.isActive = false;
		}else {
			obj = new Object3D(mEngine, source.boundType, source.type);
			ObjectLoader.copyData(source, obj);
			obj.calculateBound();
			obj.mBound.isActive = false;
		}
		return obj;
	}

	public void loadAllTexture(GL10 gl) {
//		try {
//			textureConcrete = loadTexture(gl, R.drawable.concrete);
//		} catch (IOException e) {
//			Log.d("Ratmonkey", "[Level] Error loading concrete texture:"
//					+ e.getMessage());
//		}
//
//		try {
//			textureConcrete2 = loadTexture(gl, R.drawable.concrete2);
//		} catch (IOException e) {
//			Log.d("Ratmonkey", "[Level] Error loading concrete2 texture:"
//					+ e.getMessage());
//		}
//
//		try {
//			textureMetal = loadTexture(gl, R.drawable.metal);
//		} catch (IOException e) {
//			Log.d("Ratmonkey", "[Level] Error loading metal texture:"
//					+ e.getMessage());
//		}
//
//		try {
//			textureMetal2 = loadTexture(gl, R.drawable.metal2);
//		} catch (IOException e) {
//			Log.d("Ratmonkey", "[Level] Error loading metal2 texture:"
//					+ e.getMessage());
//		}

		try {
			textureContainerBlue = loadTexture(gl, R.drawable.container_blue);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading container blue texture:"
					+ e.getMessage());
		}

		try {
			textureContainerRed = loadTexture(gl, R.drawable.container_red);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading container red texture:"
					+ e.getMessage());
		}

		try {
			textureContainerGreen = loadTexture(gl, R.drawable.container_green);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading container green texture:"
					+ e.getMessage());
		}

		try {
			textureSatellite = loadTexture(gl, R.drawable.satellite);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading satellite texture:"
					+ e.getMessage());
		}

		try {
			textureGoal = loadTextureBlend(gl, R.drawable.goal);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading goal texture:"
					+ e.getMessage());
		}
	
		try {
			textureShield = loadTextureBlend(gl, R.drawable.shield_up);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading shieldtexture:"
					+ e.getMessage());
		}

		try {
			textureBridge = loadTexture(gl, R.drawable.bridge);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading bridge:"
					+ e.getMessage());
		}

		try {
			textureWall = loadTextureBlend(gl, R.drawable.wall);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading wall:"
					+ e.getMessage());
		}

		try {
			textureDam = loadTextureBlend(gl, R.drawable.dam);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading dam:"
					+ e.getMessage());
		}
	
		try {
			texturePipe = loadTextureBlend(gl, R.drawable.pipe);
		} catch (IOException e) {
			Log.d("Ratmonkey", "[Level] Error loading pipe:"
					+ e.getMessage());
		}
	}

	public int loadTexture(GL10 gl, int textureId) throws IOException {

		if (textureId != 0) {
			int[] tex_out = new int[1];
			gl.glGenTextures(1, tex_out, 0);

			int textureName = tex_out[0];

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			InputStream stream = mEngine.mContext.getResources()
					.openRawResource(textureId);

			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			// Log.d("Ratmonkey", "Bitmap width:" + bitmap.getWidth()
			// + ", height:" + bitmap.getHeight());

			stream.close();

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
				Log.e("Ratmonkey", "[Level][" + textureId + "]Texture Load GLError: " + error);
			}
			return textureName;
		}
		return -1;
	}
	
	public int loadTextureBlend(GL10 gl, int textureId) throws IOException {

		if (textureId != 0) {
			int[] tex_out = new int[1];
			gl.glGenTextures(1, tex_out, 0);

			int textureName = tex_out[0];

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			InputStream stream = mEngine.mContext.getResources()
					.openRawResource(textureId);

			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			// Log.d("Ratmonkey", "Bitmap width:" + bitmap.getWidth()
			// + ", height:" + bitmap.getHeight());

			stream.close();

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
				Log.e("Ratmonkey", "[Level][" + textureId + "]Texture Load GLError: " + error);
			}
			return textureName;
		}
		return -1;
	}

	public void removeObject(Object3D obj) {
		mEngine.mRenderer.removeObject(obj);
		mEngine.mCollisionDetector.removeBound(obj);
		objects.remove(obj);
	}
}

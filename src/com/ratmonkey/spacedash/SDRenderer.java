package com.ratmonkey.spacedash;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.util.Log;

import com.ratmonkey.spacedash.game.GameEngine;
import com.ratmonkey.spacedash.game.Level;
import com.ratmonkey.spacedash.hud.HUD;
import com.ratmonkey.spacedash.object.Camera;
import com.ratmonkey.spacedash.object.Landscape3D;
import com.ratmonkey.spacedash.object.Object3D;
import com.ratmonkey.spacedash.object.Ship3D;
import com.ratmonkey.spacedash.object.World;
import com.ratmonkey.spacedash.particle.Emitter;

/**
 * This class implements OpenGL ES Renderer class.
 * All 3D objects will be rendered on OnDrawFrame method.
 * @author abadi199
 *
 */
public class SDRenderer implements Renderer {

	public ArrayList<Object3D> objects; // array of 3D objects
	public ArrayList<Landscape3D> landscapes; // array of 3D landscape
	public Ship3D mShip;
	public ArrayList<Emitter> emitters; // array of particle emitter
	public int mWidth, mHeight; // width and height of openGL screen

	HashMap<Integer, Integer> textureList; // list of loaded texture, to make sure no texture loaded twice
	World mWorld;
	Camera mCamera;
	Context mContext;
	Handler mHandler;
	Level level;
	HUD hud;
	
	boolean isTextureLoaded = false; // flag to indicate if all textures have been loaded

	/**
	 * Constructor
	 * Initialize all ArrayList and HashMap
	 * @param context
	 * @param handler
	 */
	public SDRenderer(Context context, Handler handler) {
		mContext = context;
		objects = new ArrayList<Object3D>();
		landscapes = new ArrayList<Landscape3D>();
		emitters = new ArrayList<Emitter>();
		mHandler = handler;
		textureList = new HashMap<Integer, Integer>();
	}
	
	public void setHUD(HUD hud) {
		this.hud = hud;
	}

	public void setShip(Ship3D ship) {
		mShip = ship;
	}

	public void setCamera(Camera c) {
		mCamera = c;
	}

	public void setWorld(World w) {
		mWorld = w;
	}

	public void addObject(Object3D obj) {
		objects.add(obj);
	}

	public void addLandscape(Landscape3D l) {
		for (Landscape3D land : l.buffers) {
			landscapes.add(land);
		}
	}

	public void removeLandscape(Landscape3D l) {
		for (Landscape3D land : l.buffers) {
			landscapes.remove(land);
		}
	}

	public void addEmitter(Emitter e) {
		emitters.add(e);
	}

	public void removeEmitter(Emitter e) {
		emitters.remove(e);
	}

	public void removeObject(Object3D obj) {
		objects.remove(obj);
	}
	

	public void setLevel(Level l) {
		level = l;
	}

	/**
	 * Implementation of onSurfaceCreated from GLSurfaceView.Renderer.onSurfaceCreated
	 * Executed before onSurfaceChanged
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d("Ratmonkey", "[SDRenderer] onSurfaceCreated");
		mWorld.initWorld(gl);
//		preloadTexture(gl);
//		int size = emitters.size();
//		for (int i = 0; i < size; i++) {
//			Emitter e = emitters.get(i);
//			e.init(mContext);
//			try {
//				e.loadTexture(gl, mContext);
//			} catch (IOException e1) {
//				Log.d("Ratmonkey", "[Object3D] error loading texture");
//			}
//		}
//		loadAllTexture(gl);
//		if (level != null) {
//			level.load(gl);
//		}
	}
	
//	public void preloadTexture(GL10 gl) {
//		preloadAndPutInList(R.drawable.asphalt);
//		preloadAndPutInList(R.drawable.concrete);
//		preloadAndPutInList(R.drawable.concrete2);
//		preloadAndPutInList(R.drawable.container_blue);
//		preloadAndPutInList(R.drawable.container_green);
//		preloadAndPutInList(R.drawable.container_red);
//		preloadAndPutInList(R.drawable.explosion);
//		preloadAndPutInList(R.drawable.hud);
//		preloadAndPutInList(R.drawable.landscape);
//		preloadAndPutInList(R.drawable.metal);
//		preloadAndPutInList(R.drawable.metal2);
//		preloadAndPutInList(R.drawable.satellite);
//		preloadAndPutInList(R.drawable.);
//	}
//	
//	private void preloadAndPutInList(int texId) {
//		try {
//			texId = R.drawable.asphalt;
//			int texName = loadTexture(gl, mContext, texId);
//			if (texName > 0) {
//				textureList.put(texId, texName);
//			}
//		} catch (IOException e) {
//		}
//		
//	}

	/** 
	 * Load all texture used in the game
	 * Called from GameEngine
	 */
	public void loadAllTexture(GL10 gl) {
		Log.d("Ratmonkey", "[SDRenderer] loadAllTexture");
		// Load texture for HUD
		try {
			if(hud != null) {
				hud.loadTexture(gl);
			}
		} catch (IOException e) {
			Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
		}
		
		// Load texture for particle
		int size = emitters.size();
		for (int i = 0; i < size; i++) {
			Emitter e = emitters.get(i);
			e.init(mContext);
			try {
				e.loadTexture(gl, mContext);
			} catch (IOException e1) {
				Log.d("Ratmonkey", "[SDRenderer] error loading texture");
			}
		}
		
		//Load texture for all landscape
		size = landscapes.size();
		for (int i = 0; i < size; i++) {
			Object3D obj = landscapes.get(i);
			try {
				loadTexture(obj, gl, textureList);
			} catch (IOException e) {
				Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
			}
		}
		
		
		size = objects.size();
		// Load texture for all objects and their children
		for (int i = 0; i < size; i++) {
			Object3D obj = objects.get(i);
			try {
				loadTexture(obj, gl, textureList);

			} catch (IOException e) {
				Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
			}
			int childCount = obj.children.size();
			try {
				// load texture for all object's children
				for (int j = 0; j < childCount; j++) {
					loadTexture(obj.children.get(j), gl, textureList);
				}
			} catch (IOException e) {
				Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
			}
		}
		// Load texture for ship
		if (mShip != null) {
			try {
				loadTexture(mShip, gl, textureList);
			} catch (IOException e) {
				Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
			}
			int childCount = mShip.children.size();
			try {
				// load texture for all object's children
				for (int j = 0; j < childCount; j++) {
					loadTexture(mShip.children.get(j), gl, textureList);
				}
			} catch (IOException e) {
				Log.e("Ratmonkey", "[SDRenderer] Error loading texture:" + e.getMessage());
			}
		}
	}
	
	/**
	 * Check if texture already loaded, then call loadTexture method otherwise
	 * and put the textureName into textureList
	 * @param obj 3D Object
	 * @param gl GL10
	 * @param textureList hashMap
	 * @throws IOException
	 */
	private void loadTexture(Object3D obj, GL10 gl,
			HashMap<Integer, Integer> textureList) throws IOException {
		obj.init(gl, mContext);
		if (!textureList.containsKey(obj.textureId)) {
			int texName = loadTexture(gl, mContext, obj.textureId);
			obj.setTexture(texName);
			if (texName > 0) {
				textureList.put(obj.textureId, texName);
			}
		} else {
			obj.setTexture(textureList.get(obj.textureId));
		}
		obj.loadTextureBuffer(gl);

	}
	
	/**
	 * Load texture from resource with textureId as resource name
	 * @param gl
	 * @param context
	 * @param textureId
	 * @return
	 * @throws IOException
	 */
	private int loadTexture(GL10 gl, Context context, int textureId) throws IOException {
		int textureName;
		
		if (textureId != 0) {
			int[] tex_out = new int[1];
			gl.glGenTextures(1, tex_out, 0);

			textureName = tex_out[0];
			
			// Specify bitmap format
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;

			InputStream stream = context.getResources().openRawResource(
					textureId);

			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);
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
				Log.e("Ratmonkey", "[SDRenderer] [" + textureId + "][" + textureName
						+ "]Texture Load GLError: " + error);
			}
			bitmap.recycle();
			return textureName;
		}
		return -1;
	}
	/**
	 * Implementation of onSurfaceChanged from GLSurfaceView.Renderer.onSurfaceChange 
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d("Ratmonkey", "[SDRenderer] onSurfaceChanged");
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}
		mWidth = width;
		mHeight = height;

		initView(gl);
//		if(hud != null) {
//			hud.init(width, height);
//		}
	}

	/**
	 * Initialize view port size and set perspective view
	 * @param gl GL10 object
	 */
	public void initView(GL10 gl) {
		gl.glViewport(0, 0, mWidth, mHeight); // Reset The Current View port
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) mWidth / (float) mHeight, 0.1f,
				100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The ModelView Matrix
		gl.glLoadIdentity(); // Reset The ModelView Matrix
		
	}

	/**
	 * Implementation of GLSurfaceView.Renderer.onDrawFrame 
	 */
	public void onDrawFrame(GL10 gl) {
		// load all texture, only do this ONCE!
		if(!isTextureLoaded) {
			// make sure hud and level objects have been properly instantiate
			if(hud != null && level != null) {
				loadAllTexture(gl);
				level.load(gl);
				hud.init(mWidth, mHeight);
				isTextureLoaded = true;
				GameEngine.mEngineState = GameEngine.ENGINE_STATE_PRESTART;
			}
		}
		// Check game state, if it's still initializing, then don't draw anything 
		if(GameEngine.mEngineState == GameEngine.ENGINE_STATE_INIT) {
			return;
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset The Current ModelView Matrix
		
		mCamera.setCameraLocation(gl); // set camera location
		mWorld.initLight(gl); // initialize lighting
		
		// Draw all 3D landscape, need to be drawn before objects to make sure they stay behind 3D object 
		for (int i = landscapes.size() - 1; i >= 0 ; i--) {
			landscapes.get(i).draw(gl);
		}

		// Draw all 3D objects
		for (int i = objects.size() - 1; i >= 0 ; i--) {
			objects.get(i).draw(gl);
		}

		// Draw the ship
		if (mShip != null) {
			mShip.draw(gl);
		}

		// Draw all particles
		int size = emitters.size();
		for (int i = 0; i < size; i++) {
			Emitter e = emitters.get(i);
			e.draw(gl, mCamera.z);
			if (!e.active && e.particleCount <= 0) {
				emitters.remove(i);
				size--;
			}
		}
		
		// draw Head Unit Display (HUD)
		if(hud != null) {
			hud.draw(gl);
		}
	}
}

package com.ratmonkey.spacedash.game;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ProgressBar;

import com.ratmonkey.spacedash.R;
import com.ratmonkey.spacedash.SDRenderer;
import com.ratmonkey.spacedash.collision.Bound;
import com.ratmonkey.spacedash.collision.BoundBox;
import com.ratmonkey.spacedash.collision.BoundComposite;
import com.ratmonkey.spacedash.collision.CollisionDetector;
import com.ratmonkey.spacedash.controller.GyroController;
import com.ratmonkey.spacedash.hud.HUD;
import com.ratmonkey.spacedash.object.Camera;
import com.ratmonkey.spacedash.object.Landscape3D;
import com.ratmonkey.spacedash.object.Object3D;
import com.ratmonkey.spacedash.object.ObjectLoader;
import com.ratmonkey.spacedash.object.Ship3D;
import com.ratmonkey.spacedash.object.World;
import com.ratmonkey.spacedash.particle.Emitter;

/**
 * This class contains game's main thread. It controls the state of the game. 
 */
public class GameEngine extends Thread {
	Context mContext;
	SDRenderer mRenderer;

	public World mWorld;
	public Camera mCamera;

	public Vibrator mVibrator;
	public Ship3D mShip;
	public Object3D mShield;

	public HUD hud;
	Timer timer;

	Landscape3D landscape;
	Emitter particleEmitter;
	CollisionDetector mCollisionDetector;
	Level level;

	public ProgressBar progressBar;

	/* Constants used to determine the state of the game engine */
	public final static int ENGINE_STATE_INIT = 0;
	public final static int ENGINE_STATE_PRESTART = 1;
	public final static int ENGINE_STATE_STOP = 2;
	public final static int ENGINE_STATE_RUN = 3;
	public final static int ENGINE_STATE_PAUSE = 4;
	public final static int ENGINE_STATE_GAMEOVER = 5;
	public final static int ENGINE_STATE_FINISHED = 6;

	public static int mEngineState = ENGINE_STATE_INIT;

	public float speed = 5f;

	public GyroController mController;

	long time;
	long lastTime;
	long lastCollisionTime;
	long lastPauseTime = 0;
	long pauseDuration = 0;

	boolean isSoundOn = false;
	boolean isMusicOn = false;
	boolean isVibrateOn = false;

	Handler mHandler;

	int lastGameState = ENGINE_STATE_RUN;
	GamePreference gamePref;

	/**
	 * Constructor
	 * @param context
	 * @param renderer
	 */
	public GameEngine(Context context, SDRenderer renderer) {
		Log.d("Ratmonkey", "[GameEngine] construct");
		this.mContext = context;
		this.mRenderer = renderer;

		this.mWorld = new World();
		this.mCamera = new Camera();

		mRenderer.setCamera(mCamera);
		mRenderer.setWorld(mWorld);

		gamePref = GamePreference.createInstance(context);

		Log.d("Ratmonkey", "[GameEngine] construct end");
	}

	public void setHandler(Handler h) {
		mHandler = h;
	}

	/**
	 * Initialize HUD size
	 */
	public void initHUD() {
		Log.d("Ratmonkey", "[GameEngine] initHUD");

		if (hud != null) {
			hud.init(mRenderer.mWidth, mRenderer.mHeight);
		}
		Log.d("Ratmonkey", "[GameEngine] initHUD end");
	}

//	public void loadTexture(GL10 gl) {
//		Log.d("Ratmonkey", "[GameEngine] loadTexture");
//		 mRenderer.loadAllTexture();
//		 if (level != null) {
//		 level.load(gl);
//		 }
//		Log.d("Ratmonkey", "[GameEngine] loadTexture end");
//
//	}

	/**
	 * Construct Level object
	 */
	public void setLevel() {
		Log.d("Ratmonkey", "[GameEngine] setLevel");
		level = new Level(this);
		mRenderer.setLevel(level);
		Log.d("Ratmonkey", "[GameEngine] setLevel end");
	}

	/**
	 * Register accelerometer controller
	 */
	public void registerController() {
		SensorManager sensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> list = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(mController, list.get(0),
				SensorManager.SENSOR_DELAY_GAME);
	}
	
	/** 
	 * Load all game configuration from internal database
	 */
	private void loadAllGameConfiguration() {
		// Configure HUD values
		hud.setShieldPercentage(100);
		hud.setGoal(10);
		hud.setCurrent(0);
		hud.setMissileCount(20);
		hud.setTime(15, 0);

		// load controller calibration from database
		isMusicOn = gamePref.loadMusicSetting();
		isSoundOn = gamePref.loadSoundSetting();
		isVibrateOn = gamePref.loadVibrateSetting();
		mController.pivotH = gamePref.loadPivotHSetting();
		mController.pivotV = gamePref.loadPivotVSetting();

		Message msg = mHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putBoolean("LoadProgressScreenVisibility", false);
		msg.setData(b);
		mHandler.sendMessage(msg);
	}

	@Override
	/**
	 * Thread's run method
	 */
	public void run() {
		(new LoaderThread()).start();
		while (mEngineState == ENGINE_STATE_INIT) {
			// wait till all object loaded
		}
		Log.d("Ratmonkey", "[GameEngine] game engine prestart");

		loadAllGameConfiguration();

		new StartTime().start();
		while (mEngineState == ENGINE_STATE_PRESTART) {
			// wait till star light is green
		}
		Log.d("Ratmonkey", "[GameEngine] game engine start");
		registerController();
		timer.start();

		// Do this while game is still running
		while (mEngineState != ENGINE_STATE_STOP) {
			time = System.currentTimeMillis();
			// delta = lastTime - time;
			lastTime = time;
			try {
				if (mEngineState == ENGINE_STATE_RUN
						|| mEngineState == ENGINE_STATE_GAMEOVER
						|| mEngineState == ENGINE_STATE_FINISHED) {
					landscape.moveForward(speed); // move the landscape forward
					if (level.moveForward(speed, pauseDuration)) { // move the objects forward
						pauseDuration = 0;
					}
					if (mShield.isVisible) { // show shield when collision occurred
						mShield.rotateY(mShield.rotY + 15); // rotate the shield for special FX
						if ((time - lastCollisionTime) > 500) {
							mShield.isVisible = false;
							mWorld.resetColor();
						}
					}
					float[] collision = { 0, 0, 0 };
					
					// Check for collision, use different effect for collision with different type of object
					if (mCollisionDetector != null && mShip.mBound.isActive && GameEngine.mEngineState != GameEngine.ENGINE_STATE_FINISHED) {
						Object3D obj = mCollisionDetector.checkCollision(mShip,
								collision);
						if (obj != null) {
							switch (obj.type) {
							case Object3D.TYPE_GOAL:
								collisionWithGoal(); // if the object is goal
								break;
							case Object3D.TYPE_SHIELD_UP: // if the object is extra shield power
								collisionWithShieldUp();
								break;
							case Object3D.TYPE_MISSILE_UP: 
								break;
							default:
								collisionWithObstacle(); // if the object is an obstacle
								break;
							}
							level.removeObject(obj);
						}
					}
				}
				sleep(50);
			} catch (Exception e) {
				Log.d("Ratmonkey", "[GameEngine] error on thread:"
						+ e.getMessage());
			}
		}
		Log.d("Ratmonkey", "[GameEngine] game stopped");
	}

	/**
	 * Collision with shield power up, increase shield percentage on hud object
	 */
	private void collisionWithShieldUp() {
		if (isVibrateOn) {
			mVibrator.vibrate(100);
		}
		hud.setShieldPercentage(hud.shieldPercentage + 10);
	}

	/**
	 * Collision with goal, increase number of current score on hud
	 */
	private void collisionWithGoal() {
		if (isVibrateOn) {
			mVibrator.vibrate(100);
		}
		if (hud.current < hud.goal) {
			hud.setCurrent(hud.current + 1);
		}
		if (hud.current >= hud.goal) {
			// mission accomplished!!!
			hud.showMissionAccomplished();
			mEngineState = GameEngine.ENGINE_STATE_FINISHED;
		}
	}

	/**
	 * Collision with obstacle, decrease shield power, if shield is empty, then game over
	 */
	private void collisionWithObstacle() {
		if (isVibrateOn) {
			mVibrator.vibrate(100);
		}
		// createExplosion(mShip.posX, mShip.posY, mShip.posZ);
		mShield.isVisible = true;
		mWorld.setColor(0.2f, 0.3f, 0.6f, 1.0f);
		lastCollisionTime = time;
		hud.setShieldPercentage(hud.shieldPercentage - 5);
		if (hud.shieldPercentage == 0) {
			Log.d("Ratmonkey", "[GameEngine] gameover");
			createExplosion(mShip.posX, mShip.posY, mShip.posZ);
			mShip.isVisible = false;
			mShip.mBound.isActive = false;
			hud.showGameOver();
			mEngineState = GameEngine.ENGINE_STATE_GAMEOVER;
		}
	}

	/**
	 * Create explosion using particle emitter
	 * @param x location of explosion
	 * @param y location of explosion
	 * @param z location of explosion
	 */
	public void createExplosion(float x, float y, float z) {
		Emitter explosion = new Emitter(mContext, new float[] { x, y, z }, // sourcePosition
				new float[] { 1f, 1f, 1f }, // sourcePositionVariance
				0.2f, // speed
				0.05f, // speedVariance
				1.0f, // particleLifeSpan
				0.5f, // particleLifeSpanVariance
				0.0f, // angle
				360f, // angleVariance
				0.0f, // angleZ
				360f, // angleZVariance
				new float[] { 0.0f, 0.0f, 0.0f }, // gravity
				new float[] { 1.0f, 1.0f, 0.0f, 0.1f }, // startColor
				new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // startColorVariance
				new float[] { 1.0f, 0.0f, 0.0f, 0.0f }, // finishColor
				new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // finishColorVariance
				500, // maxParticle
				1000f, // particleSize
				0f, // particleSizeVariance
				1f, // duration
				true // blendAdditive
		);
		explosion.hasTrail = true;
		explosion.init(mContext);
		mRenderer.addEmitter(explosion);
	}

	/**
	 * Pause the game
	 * set the lastPauseTime to current time to correctly calculate the next 3D object creation by Level class 
	 */
	public void pauseGame() {
		if (GameEngine.mEngineState != ENGINE_STATE_PAUSE) {
			SensorManager sensorManager = (SensorManager) mContext
					.getSystemService(Context.SENSOR_SERVICE);
			sensorManager.unregisterListener(mController);
			lastGameState = GameEngine.mEngineState;
			GameEngine.mEngineState = ENGINE_STATE_PAUSE;
			lastPauseTime = System.currentTimeMillis();
		}
	}

	public void resumeGame() {
		if (GameEngine.mEngineState != ENGINE_STATE_RUN) {
			registerController();
			GameEngine.mEngineState = lastGameState;
			pauseDuration = System.currentTimeMillis() - lastPauseTime;
		}
	}

	/**
	 * Thread used to count the game timer
	 * @author abadi199
	 *
	 */
	class Timer extends Thread {
		@Override
		public void run() {
			while (GameEngine.mEngineState != ENGINE_STATE_STOP) {
				if (GameEngine.mEngineState == ENGINE_STATE_RUN) {
					if (!hud.tick()) {
						hud.showGameOver();
						mEngineState = ENGINE_STATE_GAMEOVER;
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	/**
	 * Thread used to count the start light at the beginning of the game
	 * @author abadi199
	 *
	 */
	class StartTime extends Thread {
		@Override
		public void run() {
			while (hud.tickStartlight()) {

			}
			GameEngine.mEngineState = ENGINE_STATE_RUN;
		}
	}

	public void setMusicOn(boolean b) {
		gamePref.saveMusicSetting(b);
		isMusicOn = b;
	}

	public void setSoundOn(boolean b) {
		gamePref.saveSoundSetting(b);
		isSoundOn = b;
	}

	public void setVibrateOn(boolean b) {
		gamePref.saveVibrateSetting(b);
		isVibrateOn = b;
	}

	/**
	 * Thread used to load all 3D object.
	 * This thread will also animate the progress bar at the loading screen.
	 * @author abadi199
	 *
	 */
	class LoaderThread extends Thread {
		public void run() {
			Log.d("Ratmonkey", "[GameEngine] load");
			mShip = new Ship3D(GameEngine.this, Bound.BOUND_COMPOSITE, 3);
			mShield = new Object3D(GameEngine.this, Bound.BOUND_SPHERE,
					Object3D.TYPE_PROPS);
			landscape = new Landscape3D(GameEngine.this);

			// Creating accelerometer controller
			mController = new GyroController();
			mCollisionDetector = new CollisionDetector();

			// Creating vibrator
			mVibrator = (Vibrator) mContext
					.getSystemService(Context.VIBRATOR_SERVICE);

			if (progressBar != null)
				progressBar.setProgress(10); // update progress bar
			
			// Load landscape object
			try {
				ObjectLoader.loadRaw(mContext, R.raw.landscape, landscape);
				landscape.setTextureId(R.drawable.landscape);
				landscape.enableBuffer();
				mRenderer.addLandscape(landscape);
			} catch (Exception e) {
				Log
						.d("Ratmonkey", "Error loading lansdscape:"
								+ e.getMessage());
			}

			if (progressBar != null)
				progressBar.setProgress(40); // update progress bar
			
			// Load ship object
			try {
				ObjectLoader.loadRaw(mContext, R.raw.ship, mShip);
//				 mShip.setBoundBox(-4.5f, -0.4f, -4.5f, 4.5f, 0.8f, 4.5f);
//				 mShip.setBoundBox(-1.351751f, -0.436521f, -4.942806f, 
//							1.351752f, 1.099867f, 4.804558f);
				BoundComposite composite = (BoundComposite) mShip.mBound;
				composite.addBound(new BoundBox(mShip,
						-1.351751f, -0.436521f, -4.942806f, 
						1.351752f, 1.099867f, 4.804558f)); //body
				composite.addBound(new BoundBox(mShip,
						-0.154063f, -0.432354f, 2.391552f, 
						0.154063f, 2.508654f, 3.997866f));//tail
				composite.addBound(new BoundBox(mShip,
						-5.045198f, -0.214152f, 1.517407f,
						5.045202f, 0.286655f, 3.998800f));//wing
				mShip.mBound.isActive = true;
				mShip.setCollisionDetector(mCollisionDetector);
				mShip.setTextureId(R.drawable.ship);
				mShip.initEngineEmitter(mContext);
				mRenderer.setShip(mShip);
				mController.addControlListener(mShip);
			} catch (Exception e) {
				Log.d("Ratmonkey", "Error loading ship:" + e.getMessage());
			}

			if (progressBar != null)
				progressBar.setProgress(70); // update progress bar
			
			// Load shield object
			try {
				ObjectLoader.loadRaw(mContext, R.raw.shield, mShield);
				// mShield.mBound.isActive = true;
				// mShield.setCollisionDetector(mCollisionDetector);
				mShield.setTextureId(R.drawable.shield);
				mShield.isBlend = true;
				mShield.isVisible = false;
				mShip.addChild(mShield);
			} catch (Exception e) {
				Log.d("Ratmonkey", "Error loading shield:" + e.getMessage());
			}

			if (progressBar != null)
				progressBar.setProgress(80); // update progress bar
			
			// create particle emitter for jet propulsion 
			particleEmitter = new Emitter(mContext, new float[] { 0, 0, 10 }, // sourcePosition
					new float[] { 10f, 8f, 10f }, // sourcePositionVariance
					2f, // speed
					0f, // speedVariance
					0.5f, // particleLifeSpan
					0.1f, // particleLifeSpanVariance
					0.0f, // angle
					0f, // angleVariance
					90f, // angleZ
					0f, // angleZVariance
					new float[] { 0.0f, 0.0f, 0.0f }, // gravity
					new float[] { 1.0f, 1.0f, 1.0f, 0.3f }, // startColor
					new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // startColorVariance
					new float[] { 1.0f, 1.0f, 1.0f, 0.0f }, // finishColor
					new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // finishColorVariance
					200, // maxParticle
					10f, // particleSize
					0, // particleSizeVariance
					-1, // duration
					true // blendAdditive
			);
			mRenderer.addEmitter(particleEmitter);
			if (progressBar != null)
				progressBar.setProgress(90); // update progress bar

			hud = new HUD(mContext);
			timer = new Timer();
			mRenderer.setHUD(hud);

			setLevel();
			if (progressBar != null)
				progressBar.setProgress(100); // update progress bar

			// loadTexture(gl);
			// initHUD();

			lastTime = System.currentTimeMillis();
			Log.d("Ratmonkey", "[GameEngine] load end");
		}
	}
}

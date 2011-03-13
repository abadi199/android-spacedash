package com.ratmonkey.spacedash;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

import com.ratmonkey.spacedash.game.GameEngine;

public class SDView extends GLSurfaceView {

	public SurfaceHolder mSurfaceHolder;

	public SDRenderer mRenderer;
	public SDHandler mHandler;
	
	public GameEngine mEngine;

	public SDView(Context context, AttributeSet set) {
		super(context, set);
		mRenderer = new SDRenderer(context, mHandler);
		this.setRenderer(mRenderer);		
		
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		// register our interest in hearing about changes to our surface
		mHandler = new SDHandler();
		mEngine = new GameEngine(context, mRenderer);

		setFocusable(true); // make sure we get key events				
	}
	
	@Override 
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		Log.d("Ratmonkey", "SDView.surfaceCreated");
		mEngine.start();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		/*
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			mRenderer.mCamera.yspeed -= 0.1f;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			mRenderer.mCamera.yspeed += 0.1f;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			mRenderer.mCamera.xspeed -= 0.1f;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			mRenderer.mCamera.xspeed += 0.1f;

		}

		// We handled the event*/
		return true;
	}

/*	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//
		float x = event.getX();
		float y = event.getY();

		// If a touch is moved on the screen
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// Calculate the change
			float dx = x - mRenderer.mCamera.oldX;
			float dy = y - mRenderer.mCamera.oldY;
			// Define an upper area of 10% on the screen
			int upperArea = getHeight() / 10;

			// Zoom in/out if the touch move has been made in the upper
			if (y < upperArea) {
				mRenderer.mCamera.z -= dx * mRenderer.mCamera.TOUCH_SCALE / 2;

				// Rotate around the axis otherwise
			} else {
				mRenderer.mCamera.xrot += dy * mRenderer.mCamera.TOUCH_SCALE;
				mRenderer.mCamera.yrot += dx * mRenderer.mCamera.TOUCH_SCALE;
			}

			// A press on the screen
		} 
		
		// Remember the values
		mRenderer.mCamera.oldX = x;
		mRenderer.mCamera.oldY = y;

		// We handled the event
		return true;
	}*/
}
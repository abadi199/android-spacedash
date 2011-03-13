package com.ratmonkey.spacedash.hud;

import android.util.Log;


public class SpriteSeries extends Sprite {
	int[] seriesX;
	int[] seriesY;
	int[] timeline;

	int time;

	public SpriteSeries(int x, int y, int width, int height, int screenX,
			int screenY, int[] seriesX, int[] seriesY, int[] timeline,
			int textureName) {
		super(x, y, width, height, screenX, screenY, textureName);
		this.seriesX = seriesX;
		this.seriesY = seriesY;
		this.timeline = timeline;
	}

	public boolean tick() {
		Log.d("Ratmonkey", "[SpriteSeries] tick");
		if (time < timeline.length) {
			if (timeline[time] < 0) {
				isVisible = false;
			} else {
				isVisible = true;
			}
			super.texU = seriesX[time];
			super.texV = seriesY[time];
			try {
				Thread.sleep(Math.abs(timeline[time]));
			} catch (InterruptedException e) {
			}
			time++;
			return true;
		}else {
			isActive = false;
			return false;
		}
	}

//	public void run() {
//		for (int i = 0; i < timeline.length; i++) {
//			if (timeline[i] < 0) {
//				isVisible = false;
//			} else {
//				isVisible = true;
//			}
//			super.texU = seriesX[i];
//			super.texV = seriesY[i];
//			try {
//				Thread.sleep(Math.abs(timeline[i]));
//			} catch (InterruptedException e) {
//			}
//		}
//		isActive = false;
//	}

}

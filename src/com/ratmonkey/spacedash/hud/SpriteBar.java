package com.ratmonkey.spacedash.hud;


public class SpriteBar extends Sprite {
	int total;
	int percentage = 100;
	int originalX;
	int originalY;
	float delta;
	
	public SpriteBar(int x, int y, int width, int height, int screenX,
			int screenY, int total, int textureName) {
		super(x, y, width, height, screenX, screenY, textureName);
		this.total = total;
		this.delta = 100f/(float)(total-1);
		originalX = x;
		originalY = y;
		
	}

	public void setPercentage(int percentage) {
		if(percentage > 100) percentage = 100;
		else if(percentage < 0) percentage = 0;
		this.percentage = percentage;
		int i = Math.round((100 - percentage) / delta);
		this.texU = originalX + (i * texW); 
//		Log.d("Ratmonkey","percentage:"+percentage+", i:"+i+", texU:"+texU);
	}
}

package com.ratmonkey.spacedash.controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class GyroController extends Controller implements SensorEventListener{

	public float pivotV = 8.0f;
	public float pivotH = 0.0f;	
	
	float speed = 5;

	float oldX = Float.MAX_VALUE, oldY = Float.MAX_VALUE, oldZ = Float.MAX_VALUE;
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public void onSensorChanged(SensorEvent event) {
//		Log.d("Ratmonkey", Arrays.toString(event.values));
		float fraction = 10 / (speed==0?1000:speed);
		float deltaV = (event.values[0] - pivotV) / fraction;
		float deltaH = (event.values[1] - pivotH) / fraction;
		
		if(deltaV < 0) {
			fireEvent(EVENT_MOVE_DOWN, -deltaV);
		}else {
			fireEvent(EVENT_MOVE_UP, deltaV);			
		}
		
		if(deltaH < 0) {
			fireEvent(EVENT_MOVE_LEFT, -deltaH);
		}else {
			fireEvent(EVENT_MOVE_RIGHT, deltaH);
		}
	}

}

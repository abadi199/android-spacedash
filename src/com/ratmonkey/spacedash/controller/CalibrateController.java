package com.ratmonkey.spacedash.controller;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.ratmonkey.spacedash.Game;
import com.ratmonkey.spacedash.Menu;
import com.ratmonkey.spacedash.ui.SDDialog;

public class CalibrateController implements SensorEventListener {
	Game game;
	Menu menu;
	float tempPivotV, tempPivotH;

	public CalibrateController(Game game) {
		this.game = game;
	}

	public CalibrateController(Menu menu) {
		this.menu = menu;
	}

	public void calibrate() {
		SensorManager sensorManager;
		if (game != null) {
			game.showDialog(SDDialog.DIALOG_CALIBRATE);
			sensorManager = (SensorManager) game
					.getSystemService(Context.SENSOR_SERVICE);
		} else { //if (menu != null) {
			menu.showDialog(SDDialog.DIALOG_CALIBRATE);
			sensorManager = (SensorManager) menu
					.getSystemService(Context.SENSOR_SERVICE);
		}
		List<Sensor> list = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, list.get(0),
				SensorManager.SENSOR_DELAY_GAME);

	}

	public void calibrateCenter(SDDialog dialog) {

		dialog.setPositiveButton(new OnClickListener() {
			public void onClick(View v) {
				if (game != null) {
					game.mViewer.mEngine.mController.pivotH = tempPivotH;
					game.mViewer.mEngine.mController.pivotV = tempPivotV;
					game.gamePref.savePivotSetting(tempPivotV, tempPivotH);
					game.dismissDialog(SDDialog.DIALOG_CALIBRATE);
				} else if (menu != null) {
					menu.gamePref.savePivotSetting(tempPivotV, tempPivotH);
					menu.dismissDialog(SDDialog.DIALOG_CALIBRATE);
				}
			}
		});
		dialog.setNegativeButton(new OnClickListener() {
			public void onClick(View v) {
				if (game != null) {
					game.dismissDialog(SDDialog.DIALOG_CALIBRATE);
				} else if (menu != null) {
					menu.dismissDialog(SDDialog.DIALOG_CALIBRATE);
				}
			}
		});
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		tempPivotV = event.values[0];
		tempPivotH = event.values[1];
	}
}

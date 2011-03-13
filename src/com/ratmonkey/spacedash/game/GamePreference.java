package com.ratmonkey.spacedash.game;

import android.content.Context;
import android.content.SharedPreferences;

public class GamePreference {
	final static String PREF_NAME = "SpaceDash";
	final static String SOUND_PREF = "SoundPref";
	final static String MUSIC_PREF = "MusicPref";
	final static String VIBRATE_PREF = "VibratePref";
	final static String PIVOT_V_PREF = "PivotV";
	final static String PIVOT_H_PREF = "PivotH";

	Context mContext;
	SharedPreferences gamePref;
	
	private static GamePreference instance;
	
	private GamePreference(Context context) {
		mContext = context;
		gamePref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
	}
	
	public static GamePreference createInstance(Context context) {
		if(instance == null) {
			return new GamePreference(context);
		}else {
			return instance;
		}
	}
	
	public boolean loadSoundSetting() {
		return gamePref.getBoolean(SOUND_PREF, false);
	}
	
	public boolean loadMusicSetting() {
		return gamePref.getBoolean(MUSIC_PREF, false);
	}
	
	public boolean loadVibrateSetting() {
		return gamePref.getBoolean(VIBRATE_PREF, false);
	}
	
	public float loadPivotVSetting() {
		return gamePref.getFloat(PIVOT_V_PREF, 8f);
	}

	public float loadPivotHSetting() {
		return gamePref.getFloat(PIVOT_H_PREF, 0f);
	}

	public void saveSoundSetting(boolean value) {
		SharedPreferences.Editor editor = gamePref.edit();
		editor.putBoolean(SOUND_PREF, value);
		editor.commit();
	}

	public void saveMusicSetting(boolean value) {
		SharedPreferences.Editor editor = gamePref.edit();
		editor.putBoolean(MUSIC_PREF, value);
		editor.commit();
	}

	public void saveVibrateSetting(boolean value) {
		SharedPreferences.Editor editor = gamePref.edit();
		editor.putBoolean(VIBRATE_PREF, value);
		editor.commit();
	}
	
	public void savePivotSetting(float v, float h) {
		SharedPreferences.Editor editor = gamePref.edit();
		editor.putFloat(PIVOT_V_PREF, v);
		editor.putFloat(PIVOT_H_PREF, h);
		editor.commit();
	}
}

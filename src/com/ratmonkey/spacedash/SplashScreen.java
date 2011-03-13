package com.ratmonkey.spacedash;


import com.ratmonkey.spacedash.game.GamePreference;
import com.ratmonkey.spacedash.ui.SDDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class SplashScreen extends Activity {
	Animation fadein;
	Animation movein;
	TextView ratmonkey, studio;
	GamePreference gamePref;
	/** Called when the activity is first created. */
	
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            boolean askSound = msg.getData().getBoolean("AskSound");
            if(askSound) {
            	showDialog(SDDialog.DIALOG_ASK_SOUND);
            }
        }
    };

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        gamePref = GamePreference.createInstance(this);
        ratmonkey = (TextView) findViewById(R.id.splash_ratmonkey);
        studio = (TextView) findViewById(R.id.splash_studio);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        movein = AnimationUtils.loadAnimation(this, R.anim.movein);
        movein.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}			
			public void onAnimationEnd(Animation animation) {
				new Thread() {
					public void run() {
						try {
							sleep(2000);
						} catch (InterruptedException e) {
						}
						Message msg = handler.obtainMessage();
						Bundle b = new Bundle();
						b.putBoolean("AskSound", true);
						msg.setData(b);
						handler.sendMessage(msg);					}
				}.start();
			}
		});
//        setContentView(new SplashView(this));
    }
    
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if(hasFocus) {
    		Log.d("Ratmonkey", "[SplashScreen] onWindowFocusChanged");
    		ratmonkey.startAnimation(fadein);
    		studio.startAnimation(movein);
    	}
    }
    
	@Override
	protected Dialog onCreateDialog(int id) {
		SDDialog dialog = null;
		switch (id) {
		case SDDialog.DIALOG_ASK_SOUND:
			dialog = new SDDialog(this, id, R.string.ask_sound);
			
			dialog.setPositiveButton(new OnClickListener() {
				public void onClick(View v) {
					gamePref.saveMusicSetting(true);
					gamePref.saveSoundSetting(true);
					gamePref.saveVibrateSetting(true);
					startActivity(new Intent(SplashScreen.this, Menu.class));
				}
			});
			dialog.setNegativeButton(new OnClickListener() {
				public void onClick(View v) {
					gamePref.saveMusicSetting(false);
					gamePref.saveSoundSetting(false);
					gamePref.saveVibrateSetting(false);
					startActivity(new Intent(SplashScreen.this, Menu.class));
				}
			});
			break;
		}
		return dialog;
		
	}    
}
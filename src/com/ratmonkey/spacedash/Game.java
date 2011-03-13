package com.ratmonkey.spacedash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.nullwire.trace.ExceptionHandler;
import com.ratmonkey.spacedash.controller.CalibrateController;
import com.ratmonkey.spacedash.game.GameEngine;
import com.ratmonkey.spacedash.game.GamePreference;
import com.ratmonkey.spacedash.ui.SDDialog;

public class Game extends Activity {
    /** Called when the activity is first created. */
	public SDView mViewer;
	public View inGameMenu;
	public CalibrateController calibrator;
	public GamePreference gamePref;
	View loadProgressScreen;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            boolean loadProgressScreenVisibility = msg.getData().getBoolean("LoadProgressScreenVisibility");
            loadProgressScreen.setVisibility(loadProgressScreenVisibility?View.VISIBLE:View.INVISIBLE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ratmonkey", "[Game] onCreate");
    	ExceptionHandler.register(this);
    	gamePref = GamePreference.createInstance(this);
    	
        if(savedInstanceState == null) {
	        setContentView(R.layout.game);
	        calibrator = new CalibrateController(this);
	        mViewer = (SDView) findViewById(R.id.SDView);
	        inGameMenu = findViewById(R.id.InGameMenu);
	        mViewer.mEngine.progressBar = (ProgressBar)findViewById(R.id.LoadProgressBar);
	        loadProgressScreen = findViewById(R.id.LoadProgressScreen);
	        mViewer.mEngine.setHandler(handler);

	        findViewById(R.id.ResumeGameButton).setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					inGameMenu.setVisibility(View.INVISIBLE);
					mViewer.mEngine.resumeGame();
				}
			});
	        findViewById(R.id.SoundButton).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ToggleButton button = (ToggleButton)v;
					mViewer.mEngine.setSoundOn(button.isChecked());
				}
			});
	        findViewById(R.id.MusicButton).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ToggleButton button = (ToggleButton)v;
					mViewer.mEngine.setMusicOn(button.isChecked());
				}
			});
	        findViewById(R.id.VibrateButton).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ToggleButton button = (ToggleButton)v;
					mViewer.mEngine.setVibrateOn(button.isChecked());
				}
			});
	        findViewById(R.id.CalibrateButton).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					calibrator.calibrate();
				}
			});
	        findViewById(R.id.EndGameButton).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showDialog(SDDialog.DIALOG_CONFIRM_ENDGAME);
				}
			});
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
        	
        }
        Log.d("Ratmonkey", "[Game] 4");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.d("Ratmonkey", "onPause");
    	mViewer.mEngine.pauseGame();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		System.exit(0);
	}
	
	private void showInGameMenu() {
		if(GameEngine.mEngineState == GameEngine.ENGINE_STATE_RUN || GameEngine.mEngineState == GameEngine.ENGINE_STATE_PAUSE) {
			mViewer.mEngine.pauseGame();
			inGameMenu.setVisibility(View.VISIBLE);
			findViewById(R.id.ResumeGameButton).requestFocus();
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_MENU:
			showInGameMenu();
            return false;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			switch (GameEngine.mEngineState) {
			case GameEngine.ENGINE_STATE_FINISHED:
			case GameEngine.ENGINE_STATE_GAMEOVER:
		        startActivity(new Intent(Game.this, Menu.class));
				break;
			}
//			if(GameEngine.mEngineState == GameEngine.ENGINE_STATE_RUN) {
//				mViewer.mEngine.pauseGame();
//			}else if(GameEngine.mEngineState == GameEngine.ENGINE_STATE_PAUSE){
//				mViewer.mEngine.resumeGame();
//			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		SDDialog dialog = null;
		switch (id) {
		case SDDialog.DIALOG_CONFIRM_EXIT:
			dialog = new SDDialog(this, id, R.string.really_quit);
			
			dialog.setPositiveButton(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			dialog.setNegativeButton(new OnClickListener() {
				public void onClick(View v) {
					dismissDialog(SDDialog.DIALOG_CONFIRM_EXIT);
				}
			});
			break;
		case SDDialog.DIALOG_CONFIRM_ENDGAME:
			dialog = new SDDialog(this, id, R.string.really_endgame);
			
			dialog.setPositiveButton(new OnClickListener() {
				public void onClick(View v) {
			        startActivity(new Intent(Game.this, Menu.class));
				}
			});
			dialog.setNegativeButton(new OnClickListener() {
				public void onClick(View v) {
					dismissDialog(SDDialog.DIALOG_CONFIRM_ENDGAME);
				}
			});
			break;
		case SDDialog.DIALOG_CALIBRATE:
			dialog = new SDDialog(this, id, R.string.calibrateCenter);
			calibrator.calibrateCenter(dialog);
			break;
		}
		
		return dialog;
	}
	
	public void hideLoadProgressScreen() {
		if(loadProgressScreen != null) {
			loadProgressScreen.setVisibility(View.INVISIBLE);
			//progress = null;
		}
		
	}
}
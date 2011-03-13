package com.ratmonkey.spacedash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.ratmonkey.spacedash.controller.CalibrateController;
import com.ratmonkey.spacedash.game.GamePreference;
import com.ratmonkey.spacedash.ui.SDDialog;

public class Menu extends Activity {
	public GamePreference gamePref;

	Intent startGameIntent;
	View resume, newGame;
	ToggleButton soundButton, musicButton, vibrateButton;
	CalibrateController calibrator;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		gamePref = GamePreference.createInstance(this);
		calibrator = new CalibrateController(this);
		
		resume = findViewById(R.id.MenuResumeGame);
		resume.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});

		newGame = findViewById(R.id.MenuNewGame);
		newGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (startGameIntent == null) {
					startGameIntent = new Intent(Menu.this, Game.class);
					startGameIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}
				startActivity(startGameIntent);
			}
		});

		findViewById(R.id.MenuOptions).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.MainMenu).setVisibility(View.INVISIBLE);
				findViewById(R.id.OptionMenu).setVisibility(View.VISIBLE);
				soundButton.setChecked(gamePref.loadSoundSetting());
				musicButton.setChecked(gamePref.loadMusicSetting());
				vibrateButton.setChecked(gamePref.loadVibrateSetting());
				setFocusOptionMenu();
			}
		});
		
		findViewById(R.id.MenuHelp).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(SDDialog.DIALOG_HELP);
			}
		});
		
		findViewById(R.id.MenuCredit).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(SDDialog.DIALOG_CREDITS);
			}
		});
		
		findViewById(R.id.MenuExit).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(SDDialog.DIALOG_CONFIRM_EXIT);
			}
		});

		// Set listener for option menu
		soundButton = (ToggleButton) findViewById(R.id.SoundButton);
		soundButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gamePref.saveSoundSetting(soundButton.isChecked());
			}
		});
		musicButton = (ToggleButton) findViewById(R.id.MusicButton);
		musicButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gamePref.saveMusicSetting(musicButton.isChecked());
			}
		});
		vibrateButton = (ToggleButton) findViewById(R.id.VibrateButton);
		vibrateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gamePref.saveVibrateSetting(vibrateButton.isChecked());
			}
		});
		findViewById(R.id.CalibrateButton).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				calibrator.calibrate();
			}
		});
		findViewById(R.id.BackButton).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.MainMenu).setVisibility(View.VISIBLE);
				findViewById(R.id.OptionMenu).setVisibility(View.INVISIBLE);
				setFocusMainMenu();
			}
		});
		setFocusMainMenu();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_HOME:
			showDialog(SDDialog.DIALOG_CONFIRM_EXIT);
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
		case SDDialog.DIALOG_CREDITS:
			dialog = new SDDialog(this, id, "");
			dialog.setPositiveButton(new OnClickListener() {
				public void onClick(View v) {
					dismissDialog(SDDialog.DIALOG_CREDITS);
				}
			});
			break;
		case SDDialog.DIALOG_HELP:
			dialog = new SDDialog(this, id, "");
			dialog.setPositiveButton(new OnClickListener() {
				public void onClick(View v) {
					dismissDialog(SDDialog.DIALOG_HELP);
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
	
	private void setFocusMainMenu() {
		if(resume.getVisibility() == View.VISIBLE) {
			resume.requestFocus();
		}else {
			newGame.requestFocus();
		}

	}

	private void setFocusOptionMenu() {
		soundButton.requestFocus();
	}

}
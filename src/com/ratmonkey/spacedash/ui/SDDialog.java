package com.ratmonkey.spacedash.ui;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.ratmonkey.spacedash.R;

public class SDDialog extends android.app.Dialog {
	
	public final static int DIALOG_CONFIRM_EXIT = 1;
	public final static int DIALOG_CONFIRM_ENDGAME = 2;
	public final static int DIALOG_CALIBRATE = 3;
	public final static int DIALOG_CREDITS = 4;
	public final static int DIALOG_HELP = 5;
	public final static int DIALOG_ASK_SOUND = 6;
	
	int type;
	Button yes, no;

	public SDDialog(Context context) {
		super(context, R.style.CustomDialog);
		setContentView(R.layout.dialog);
		setDialogType(-1);
	}

	public SDDialog(Context context, int type, String message) {
		this(context);
		setDialogType(type);
		TextView text = (TextView) findViewById(R.id.DialogText);
		text.setText(message);
	}
	
	public SDDialog(Context context, int type, int message) {
		this(context);
		setDialogType(type);
		TextView text = (TextView) findViewById(R.id.DialogText);
		text.setText(message);
	}
	
	public SDDialog(Context context, int theme) {
		this(context);
	}

	public SDDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		this(context);
	}

	public void setDialogType(int type) {
		this.type = type;

		findViewById(R.id.DialogOkCancel).setVisibility(View.INVISIBLE);
		findViewById(R.id.DialogYesNo).setVisibility(View.INVISIBLE);
		findViewById(R.id.DialogOk).setVisibility(View.INVISIBLE);
		
		switch (type) {
		case DIALOG_CREDITS:
			findViewById(R.id.DialogOk).setVisibility(View.VISIBLE);
			findViewById(R.id.DialogText).setVisibility(View.INVISIBLE);
			WebView view = (WebView) findViewById(R.id.DialogCredits);
			view.setVisibility(View.VISIBLE);
			view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			view.loadUrl("file:///android_asset/credits.html");
			yes = (Button) findViewById(R.id.ButtonOkOnly);
			break;
		case DIALOG_HELP:
			findViewById(R.id.DialogOk).setVisibility(View.VISIBLE);
			findViewById(R.id.DialogText).setVisibility(View.INVISIBLE);
			view = (WebView) findViewById(R.id.DialogHelp);
			view.setVisibility(View.VISIBLE);
			view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			view.loadUrl("file:///android_asset/help.html");
			yes = (Button) findViewById(R.id.ButtonOkOnly);
			break;
		case DIALOG_ASK_SOUND:
			findViewById(R.id.DialogYesNo).setVisibility(View.VISIBLE);
			yes = (Button) findViewById(R.id.ButtonYes);
			no = (Button) findViewById(R.id.ButtonNo);
			break;
		default:
			findViewById(R.id.DialogOkCancel).setVisibility(View.VISIBLE);
			yes = (Button) findViewById(R.id.ButtonOk);
			no = (Button) findViewById(R.id.ButtonCancel);
			break;
		}
	}
	
	public void setPositiveButton(View.OnClickListener listener) {
		if(yes != null) {
			yes.setOnClickListener(listener);
		}
	}
	
	public void setPositiveButton(String text, View.OnClickListener listener) {
		if(yes != null) {
			yes.setText(text);
			yes.setOnClickListener(listener);
		}
	}
	
	public void setPositiveButton(int text, View.OnClickListener listener) {
		if(yes != null) {
			yes.setText(text);
			yes.setOnClickListener(listener);
		}
	}
	
	public void setNegativeButton(View.OnClickListener listener) {
		if(no != null) {
			no.setOnClickListener(listener);
		}
	}

	public void setNegativeButton(String text, View.OnClickListener listener) {
		if(no != null) {
			no.setText(text);
			no.setOnClickListener(listener);
		}
	}
	
	public void setNegativeButton(int text, View.OnClickListener listener) {
		if(no != null) {
			no.setText(text);
			no.setOnClickListener(listener);
		}
	}
}
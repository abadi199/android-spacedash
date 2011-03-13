package com.ratmonkey.spacedash.ui;

import com.ratmonkey.spacedash.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class SDProgressBar extends ProgressBar {

	public SDProgressBar(Context context) {
		super(context);
	}

	public SDProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs, R.style.CustomDialog);
	}

	public SDProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}

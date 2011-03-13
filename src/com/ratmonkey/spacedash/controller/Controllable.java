package com.ratmonkey.spacedash.controller;

public interface Controllable {
	public void onUpEvent(float value);
	public void onDownEvent(float value);
	public void onLeftEvent(float value);
	public void onRightEvent(float value);
	public void onActionEvent(float value);
}

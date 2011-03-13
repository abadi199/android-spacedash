package com.ratmonkey.spacedash.controller;

import java.util.ArrayList;

public abstract class Controller {
	private ArrayList<Controllable> listeners;
	public final static int EVENT_MOVE_UP = 0;
	public final static int EVENT_MOVE_DOWN = 1;
	public final static int EVENT_MOVE_LEFT = 2;
	public final static int EVENT_MOVE_RIGHT = 3;
	public final static int EVENT_ACTION = 4;
	
	public Controller() {
		listeners = new ArrayList<Controllable>();
	}

	protected final void fireEvent(int eventType, float value) {
		switch (eventType) {
		case EVENT_MOVE_UP:
			fireUp(value);
			break;
		case EVENT_MOVE_DOWN:
			fireDown(value);
			break;
		case EVENT_MOVE_LEFT:
			fireLeft(value);
			break;
		case EVENT_MOVE_RIGHT:
			fireRight(value);
			break;
		case EVENT_ACTION:
			fireAction(value);
			break;
		}
	}

	public void addControlListener(Controllable c) {
		listeners.add(c);
	}

	public void removeControlListener(Controllable c) {
		listeners.remove(c);
	}
	
	private void fireUp(float value) {
		int count = listeners.size();
		for(int i=0; i < count; i++) {
			listeners.get(i).onUpEvent(value);
		}
	}

	private void fireDown(float value) {
		int count = listeners.size();
		for(int i=0; i < count; i++) {
			listeners.get(i).onDownEvent(value);
		}
	}

	private void fireLeft(float value) {
		int count = listeners.size();
		for(int i=0; i < count; i++) {
			listeners.get(i).onLeftEvent(value);
		}
	}

	private void fireRight(float value) {
		int count = listeners.size();
		for(int i=0; i < count; i++) {
			listeners.get(i).onRightEvent(value);
		}
	}

	private void fireAction(float value) {
		int count = listeners.size();
		for(int i=0; i < count; i++) {
			listeners.get(i).onActionEvent(value);
		}
	}
}

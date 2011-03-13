package com.ratmonkey.spacedash.collision;

import java.util.ArrayList;

import android.util.Log;

import com.ratmonkey.spacedash.object.Object3D;

public class CollisionDetector {
	ArrayList<Object3D> mBoundList;
	
	public CollisionDetector() {
		mBoundList = new ArrayList<Object3D>();
	}
	
	public void addBound(Object3D b) {
		if(!mBoundList.contains(b)) {
			mBoundList.add(b);
		}
	}
	
	public void removeBound(Object3D b) {
		mBoundList.remove(b);
	}
	
	public Object3D checkCollision(Object3D src, float[] position) {
		try {
		int index = mBoundList.indexOf(src);
		if(index > -1) {
			int size = mBoundList.size();
			for(int i = 0; i < size; i++) {
				if(i != index) {
					Object3D o = mBoundList.get(i);
					if(src.mBound.collideWith(o.mBound, position)) {
						return o;
					}
				}
			}
		}
		}catch(Exception e) {
			Log.d("Ratmonkey", "[CollisionDetector] error in collision: "+e.getMessage());
		}
		return null;
	}
}

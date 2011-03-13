package com.ratmonkey.spacedash.collision;

import com.ratmonkey.spacedash.object.Object3D;

public class BoundComposite extends Bound {
	public Bound[] bounds;
	public int numberOfBound;

	public BoundComposite(Object3D parent, int numberOfBound) {
		super(parent, Bound.BOUND_COMPOSITE);
		this.numberOfBound = numberOfBound;
		bounds = new Bound[numberOfBound];
	}
	
	public BoundComposite(Object3D parent, Bound[] bounds) {
		super(parent, Bound.BOUND_COMPOSITE);
		this.bounds = bounds;
		this.numberOfBound = bounds.length;
	}

	public void addBound(Bound b) {
		for (int i = 0; i < numberOfBound; i++) {
			if(bounds[i] == null) {
				bounds[i] = b;
				break;
			}
		}
	}

	public void removeBound(Bound b) {
		for (int i = 0; i < numberOfBound; i++) {
			if(bounds[i] == b) {
				bounds[i] = null;
				break;
			}
		}
	}

	@Override
	public boolean collideWith(Bound other, float[] position) {
		for (int i = 0; i < numberOfBound; i++) {
			if (bounds[i].collideWith(other, position)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void rotate(float a, float x, float y, float z) {
		for (int i = 0; i < numberOfBound; i++) {
			bounds[i].rotate(a, x, y, z);
		}
	}

	@Override
	public void scale(float x, float y, float z) {
		for (int i = 0; i < numberOfBound; i++) {
			bounds[i].scale(x, y, z);
		}
	}

	@Override
	public void translate(float x, float y, float z) {
		for (int i = 0; i < numberOfBound; i++) {
			bounds[i].translate(x, y, z);
		}
	}
}
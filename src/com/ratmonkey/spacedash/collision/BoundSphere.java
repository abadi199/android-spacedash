package com.ratmonkey.spacedash.collision;

import android.opengl.Matrix;

import com.ratmonkey.spacedash.object.Object3D;

public class BoundSphere extends Bound {
	float centerX;
	float centerY;
	float centerZ;
	float radius;

	public BoundSphere(Object3D parent) {
		super(parent, Bound.BOUND_SPHERE);
	}

	public void rotate(float a, float x, float y, float z) {

	}

	public void translate(float x, float y, float z) {
		centerX = x;
		centerY = y;
		centerZ = z;
	}
	
	public void scale(float x, float y, float z) {
		radius = radius * (x + y + z) / 3.0f;
	}

	public void setSphere(float x, float y, float z, float r) {
		centerX = x;
		centerY = y;
		centerZ = z;
		radius = r;
	}

	public String toString() {
		return String.format("%.6f %.6f %.6f %.6f", centerX, centerY, centerZ,
				radius);
	}
	
	@Override
	public boolean collideWith(Bound other, float[] position) {
		if (other.boundType == Bound.BOUND_SPHERE) {
			BoundSphere o = (BoundSphere) other;
			float dX = o.centerX - this.centerX;
			float dY = o.centerY - this.centerY;
			float dZ = o.centerZ - this.centerZ;
			if (Matrix.length(dX, dY, dZ) < this.radius + radius) {
				// distance between two center is less than total radius of both sphere
				float dot = Bound.dotProduct(dX, dY, dZ, parent.posX
						- parent.oldX, parent.posY - parent.oldY, parent.posZ
						- parent.oldZ);
				return (dot > 0);
			}
		} else if (other.boundType == Bound.BOUND_BOX) {
			BoundBox o = (BoundBox) other;
//			float ox, oy, oz;
			if (this.centerX + this.radius < o.minX) {
				return false;
			} else if (this.centerX - this.radius > o.maxX) {
				return false;
			} else if (this.centerY + this.radius < o.minY) {
				return false;
			} else if (this.centerY - this.radius > o.maxY) {
				return false;
			} else if (this.centerZ + this.radius < o.minZ) {
				return false;
			} else if (this.centerZ - this.radius > o.maxZ) {
				return false;
			}
//			float dx, dy, dz;
			
			
			if (this.centerX < o.minX && this.centerY < o.minY && this.centerZ < o.minZ) { // 1
				// check distance with vertex #1
//				Log.d("Ratmonkey", "1");
				return calculateDistanceSphere(o.minX, o.minY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerY < o.minY
					&& this.centerZ < o.minZ) { // 2
				// check distance with vertex #2
//				Log.d("Ratmonkey", "2");
				return calculateDistanceSphere(o.maxX, o.minY, o.minZ, parent, this, position);
				
			} else if (this.centerX > o.maxX && this.centerY < o.minY
					&& this.centerZ > o.maxZ) { // 3
				// check distance with vertex #3
//				Log.d("Ratmonkey", "3");
				return calculateDistanceSphere(o.maxX, o.minY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerY < o.minY
					&& this.centerZ > o.maxZ) { // 4
				// check distance with vertex #4
//				Log.d("Ratmonkey", "4");
				return calculateDistanceSphere(o.minX, o.minY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerY > o.maxY
					&& this.centerZ < o.minZ) { // 5
				// check distance with vertex #5
//				Log.d("Ratmonkey", "5");
				return calculateDistanceSphere(o.minX, o.maxY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerY > o.maxY
					&& this.centerZ < o.minZ) { // 6
				// check distance with vertex #6
//				Log.d("Ratmonkey", "6");
				return calculateDistanceSphere(o.maxX, o.maxY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerY > o.maxY
					&& this.centerZ > o.maxZ) {// 7
				// check distance with vertex #7
//				Log.d("Ratmonkey", "7");
				return calculateDistanceSphere(o.maxX, o.maxY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerY > o.maxY
					&& this.centerZ > o.maxZ) {// 8
				// check distance with vertex #8
//				Log.d("Ratmonkey", "8");
				return calculateDistanceSphere(o.minX, o.maxY, o.maxZ, parent, this, position);
			}

			if (this.centerX >= o.minX && this.centerX <= o.maxX
					&& this.centerY < o.minY && this.centerZ < o.minZ) { // a
//				Log.d("Ratmonkey", "a");
				return calculateDistanceSphere(this.centerX, o.minY, o.minZ, parent, this, position);
			} else if (this.centerX > o.maxX && this.centerY < o.minY
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ) { // b
//				Log.d("Ratmonkey", "b");
				return calculateDistanceSphere(o.maxX, o.minY, this.centerZ, parent, this, position);

			} else if (this.centerX >= o.minX && this.centerX <= o.maxX
					&& this.centerY < o.minY && this.centerZ > o.maxZ) { // c
//				Log.d("Ratmonkey", "c");
				return calculateDistanceSphere(this.centerX, o.minY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerY < o.minY
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ) { // d
//				Log.d("Ratmonkey", "d");
				return calculateDistanceSphere(o.minX, o.minY, this.centerZ, parent, this, position);

			} else if (this.centerX >= o.minX && this.centerX <= o.maxX
					&& this.centerY > o.maxY && this.centerZ < o.minZ) { // e
//				Log.d("Ratmonkey", "e");
				return calculateDistanceSphere(this.centerX, o.maxY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerY > o.maxY
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ) { // f
//				Log.d("Ratmonkey", "f");
				return calculateDistanceSphere(o.maxX, o.maxY, this.centerZ, parent, this, position);

			} else if (this.centerX >= o.minX && this.centerX <= o.maxX
					&& this.centerY > o.maxY && this.centerZ > o.maxZ) { // g
//				Log.d("Ratmonkey", "g");
				return calculateDistanceSphere(this.centerX, o.maxY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerY > o.maxY
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ) { // h
//				Log.d("Ratmonkey", "h");
				return calculateDistanceSphere(o.minX, o.maxY, this.centerZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerZ < o.minZ
					&& this.centerY <= o.maxY && this.centerY >= o.minY) { // i
//				Log.d("Ratmonkey", "i");
				return calculateDistanceSphere(o.minX, this.centerY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerZ < o.minZ
					&& this.centerY <= o.maxY && this.centerY >= o.minY) { // l
//				Log.d("Ratmonkey", "l");
				return calculateDistanceSphere(o.maxX, this.centerY, o.minZ, parent, this, position);

			} else if (this.centerX > o.maxX && this.centerZ > o.maxZ
					&& this.centerY <= o.maxY && this.centerY >= o.minY) { // j
//				Log.d("Ratmonkey", "j");
				return calculateDistanceSphere(o.maxX, this.centerY, o.maxZ, parent, this, position);

			} else if (this.centerX < o.minX && this.centerZ > o.maxZ
					&& this.centerY <= o.maxY && this.centerY >= o.minY) { // k
//				Log.d("Ratmonkey", "k");
				return calculateDistanceSphere(o.minX, this.centerY, o.maxZ, parent, this, position);
			}
			
			if (this.centerX >= o.minX && this.centerX <= o.maxX 
					&& this.centerY >= o.minY && this.centerY <= o.maxY
					&& this.centerZ - this.radius < o.maxZ) { // I
//				Log.d("Ratmonkey", "I");
				position[0] = (o.maxX + o.minX) / 2f;
				position[1] = (o.maxY + o.minY) / 2f;
				position[2] = o.maxZ;
				return true;			
			}else if (this.centerY >= o.minY && this.centerY <= o.maxY 
						&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ
						&& this.centerX - this.radius < o.maxX) { // II
//				Log.d("Ratmonkey", "II");
				position[0] = o.maxX;
				position[1] = (o.maxY + o.minY) / 2f;
				position[2] = (o.maxZ + o.minZ) / 2f;
				return true;			
			}else if(this.centerX >= o.minX && this.centerX <= o.maxX
					&& this.centerY >= o.minY && this.centerY <= o.maxY
					&& this.centerZ + this.radius > o.minX) { // III
//				Log.d("Ratmonkey", "III");
				position[0] = (o.maxX + o.minX) / 2f;
				position[1] = (o.maxY + o.minY) / 2f;
				position[2] = o.minZ;
				return true;
			}else if (this.centerY >= o.minY && this.centerY <= o.maxY 
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ
					&& this.centerX + this.radius > o.minX) { // IV
//				Log.d("Ratmonkey", "IV");
				position[0] = o.minX;
				position[1] = (o.maxY + o.minY) / 2f;
				position[2] = (o.maxZ + o.minZ) / 2f;
				return true;
			}else if (this.centerX >= o.minX && this.centerX <= o.maxX 
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ
					&& this.centerY - this.radius < o.maxY) { // V
//				Log.d("Ratmonkey", "V");
				position[0] = (o.maxX + o.minX) / 2f;
				position[1] = o.maxY;
				position[2] = (o.maxZ + o.minZ) / 2f;
				return true;
			}else if (this.centerX >= o.minX && this.centerX <= o.maxX 
					&& this.centerZ >= o.minZ && this.centerZ <= o.maxZ 
					&& this.centerY + this.radius > o.minY) { // VI
//				Log.d("Ratmonkey", "VI");
				position[0] = (o.maxX + o.minX) / 2f;
				position[1] = o.minY;
				position[2] = (o.maxZ + o.minZ) / 2f;				
				return true;
			}
//			Log.d("Ratmonkey", "other");
			return false;
		}
		return false;
	}
}

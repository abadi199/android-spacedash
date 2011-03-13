package com.ratmonkey.spacedash.collision;

import android.opengl.Matrix;

import com.ratmonkey.spacedash.object.Object3D;

public class BoundBox extends Bound {
	float minX, minY, minZ;
	float maxX, maxY, maxZ;

	float ominX, ominY, ominZ;
	float omaxX, omaxY, omaxZ;

	public BoundBox(Object3D parent) {
		super(parent, Bound.BOUND_BOX);
	}
	
	public BoundBox(Object3D parent, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		super(parent, Bound.BOUND_BOX);
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;

		this.ominX = minX;
		this.ominY = minY;
		this.ominZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;

		this.omaxX = maxX;
		this.omaxY = maxY;
		this.omaxZ = maxZ;
	}

	public void setMinXminYminZ(float x, float y, float z) {

		minX = x;
		minY = y;
		minZ = z;

		ominX = x;
		ominY = y;
		ominZ = z;
	}

	public void setMaxXmaxYmaxZ(float x, float y, float z) {
		maxX = x;
		maxY = y;
		maxZ = z;

		omaxX = x;
		omaxY = y;
		omaxZ = z;
	}

	public void rotate(float a, float x, float y, float z) {
		if (a != 0) {
			float[] v = { maxX, maxY, maxZ, 1 };
			float[] m = new float[16];
			Matrix.setIdentityM(m, 0);
			Matrix.rotateM(m, 0, a, x, y, z);
			Matrix.multiplyMV(v, 0, m, 0, v, 0);
			maxX = v[0];
			maxY = v[1];
			maxZ = v[2];
			
			v[0] = omaxX;
			v[1] = omaxY;
			v[2] = omaxZ;
			Matrix.multiplyMV(v, 0, m, 0, v, 0);
			omaxX = v[0];
			omaxY = v[1];
			omaxZ = v[2];
			
			v[0] = minX;
			v[1] = minY;
			v[2] = minZ;
			Matrix.multiplyMV(v, 0, m, 0, v, 0);
			minX = v[0];
			minY = v[1];
			minZ = v[2];
			
			v[0] = ominX;
			v[1] = ominY;
			v[2] = ominZ;
			Matrix.multiplyMV(v, 0, m, 0, v, 0);
			ominX = v[0];
			ominY = v[1];
			ominZ = v[2];
			
			float temp;
			if(maxX < minX) {
				temp = minX;
				minX = maxX;
				maxX = temp;
				temp = ominX;
				ominX = omaxX;
				omaxX = temp;
			}
			if(maxY < minY) {
				temp = minY;
				minY = maxY;
				maxY = temp;
				temp = ominY;
				ominY = omaxY;
				omaxY = temp;
			}
			if(maxZ < minZ) {
				temp = minZ;
				minZ = maxZ;
				maxZ = temp;
				temp = ominZ;
				ominZ = omaxZ;
				omaxZ = temp;
			}
		}
	}

	public void translate(float x, float y, float z) {
		maxX = omaxX + x;
		maxY = omaxY + y;
		maxZ = omaxZ + z;

		minX = ominX + x;
		minY = ominY + y;
		minZ = ominZ + z;
	}

	public String toString() {

		return String.format("min:%.6f %.6f %.6f\nmax:%.6f %.6f %.6f\nomin:%.6f %.6f %.6f\nomax:%.6f %.6f %.6f", 
				minX,minY, minZ, maxX, maxY, maxZ,
				ominX,ominY, ominZ, omaxX, omaxY, omaxZ);
	}

	@Override
	public boolean collideWith(Bound other, float[] position) {
		switch (other.boundType) {
		case Bound.BOUND_SPHERE:
			return collideWithSphere((BoundSphere) other, position);
		case Bound.BOUND_BOX:
			return collideWithBox((BoundBox) other, position);
		case Bound.BOUND_COMPOSITE:
			BoundComposite o = (BoundComposite) other;
			for (int i = 0; i < o.bounds.length; i++) {
				if(collideWith(o.bounds[i], position)) {
					return true;
				}
			} 
		}
		return false;
	}
	
	private boolean collideWithBox(BoundBox o, float[] position) {
		if (o.maxZ < this.minZ) {
			return false;
		}
		if (o.minZ > this.maxZ) {
			return false;
		}
		if(o.maxX < this.minX) {
			return false;
		}
		if(o.minX > this.maxX) {
			return false;
		}
		if(o.maxY < this.minY) {
			return false;
		}
		if(o.minY > this.maxY) {
			return false;
		}
//		Log.d("Ratmonkey","[BoundBox] this:"+this+" o:"+o);
		
//		if (this.maxX < o.minX || this.minX > o.maxX || this.maxY < o.minY
//				|| this.minY > o.maxY || this.maxZ < o.minZ || this.minZ > o.maxZ) {
//			return false;
//		} 
		
//		float centerX = (o.maxX + o.minX) / 2.0f;
//		float centerY = (o.maxY + o.minY) / 2.0f;
//		float centerZ = (o.maxZ + o.minZ) / 2.0f;
//
//		if(this.maxX < centerX) {
//			position[0] = this.maxX;
//		} else if(this.minX > centerX) {
//			position[0] = this.minX;
//		} else {
//			position[0] = (this.maxX + this.minX) / 2f;				
//		}
//
//		if(this.maxY < centerY) {
//			position[1] = this.maxY;
//		} else if(this.minY > centerY) {
//			position[1] = this.minY;
//		} else {
//			position[1] = (this.maxY + this.minY) / 2f;				
//		}
//		
//		if(this.maxZ < centerY) {
//			position[2] = this.maxZ;
//		} else if(this.minZ > centerZ) {
//			position[2] = this.minZ;
//		} else {
//			position[2] = (this.maxZ + this.minZ) / 2f;				
//		}

		return true;		
	}
	
	private boolean collideWithSphere(BoundSphere o, float[] position) {
//		float ox, oy, oz;
		if (o.centerX + o.radius < this.minX) {
			return false;
		} else if (o.centerX - o.radius > this.maxX) {
			return false;
		} else if (o.centerY + o.radius < this.minY) {
			return false;
		} else if (o.centerY - o.radius > this.maxY) {
			return false;
		} else if (o.centerZ + o.radius < this.minZ) {
			return false;
		} else if (o.centerZ - o.radius > this.maxZ) {
			return false;
		}
//		float dx, dy, dz;

		if (o.centerX < this.minX && o.centerY < this.minY && o.centerZ < this.minZ) { // 1
			// check distance with vertex #1
			return calculateDistanceSphere(this.minX, this.minY, this.minZ, o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerY < this.minY
				&& o.centerZ < this.minZ) { // 2
			// check distance with vertex #2
			return calculateDistanceSphere(this.maxX, this.minY, this.minZ, o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerY < this.minY
				&& o.centerZ > this.maxZ) { // 3
			// check distance with vertex #3
			return calculateDistanceSphere(this.maxX, this.minY, this.maxZ, o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerY < this.minY
				&& o.centerZ > this.maxZ) { // 4
			// check distance with vertex #4
			return calculateDistanceSphere(this.minX, this.minY, this.maxZ, o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerY > this.maxY
				&& o.centerZ < this.minZ) { // 5
			// check distance with vertex #5
			return calculateDistanceSphere(this.minX, this.maxY, this.minZ, o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerY > this.maxY
				&& o.centerZ < this.minZ) { // 6
			// check distance with vertex #6
			return calculateDistanceSphere(this.maxX, this.maxY, this.minZ, o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerY > this.maxY
				&& o.centerZ > this.maxZ) {// 7
			// check distance with vertex #7
			return calculateDistanceSphere(this.maxX, this.maxY, this.maxZ, o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerY > this.maxY
				&& o.centerZ > this.maxZ) {// 8
			// check distance with vertex #8
			return calculateDistanceSphere(this.minX, this.maxY, this.maxZ, o.parent, o, position);
		}

		if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY < this.minY && o.centerZ < this.minZ) { // a
			return calculateDistanceSphere(o.centerX, this.minY, this.minZ,
					o.parent, o, position);
		} else if (o.centerX > this.maxX && o.centerY < this.minY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ) { // b
			return calculateDistanceSphere(this.maxX, this.minY, o.centerZ,
					o.parent, o, position);

		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY < this.minY && o.centerZ > this.maxZ) { // c
			return calculateDistanceSphere(o.centerX, this.minY, this.maxZ,
					o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerY < this.minY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ) { // d
			return calculateDistanceSphere(this.minX, this.minY, o.centerZ,
					o.parent, o, position);

		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY > this.maxY && o.centerZ < this.minZ) { // e
			return calculateDistanceSphere(o.centerX, this.maxY, this.minZ,
					o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerY > this.maxY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ) { // f
			return calculateDistanceSphere(this.maxX, this.maxY, o.centerZ,
					o.parent, o, position);

		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY > this.maxY && o.centerZ > this.maxZ) { // g
			return calculateDistanceSphere(o.centerX, this.maxY, this.maxZ,
					o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerY > this.maxY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ) { // h
			return calculateDistanceSphere(this.minX, this.maxY, o.centerZ,
					o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerZ < this.minZ
				&& o.centerY <= this.maxY && o.centerY >= this.minY) { // i
			return calculateDistanceSphere(this.minX, o.centerY, this.minZ,
					o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerZ < this.minZ
				&& o.centerY <= this.maxY && o.centerY >= this.minY) { // l
			return calculateDistanceSphere(this.maxX, o.centerY, this.minZ,
					o.parent, o, position);

		} else if (o.centerX > this.maxX && o.centerZ > this.maxZ
				&& o.centerY <= this.maxY && o.centerY >= this.minY) { // j
			return calculateDistanceSphere(this.maxX, o.centerY, this.maxZ,
					o.parent, o, position);

		} else if (o.centerX < this.minX && o.centerZ > this.maxZ
				&& o.centerY <= this.maxY && o.centerY >= this.minY) { // k
			return calculateDistanceSphere(this.minX, o.centerY, this.maxZ,
					o.parent, o, position);
		}

		if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY >= this.minY && o.centerY <= this.maxY
				&& o.centerZ - o.radius < this.maxZ) { // I
			position[0] = (this.maxX + this.minX) / 2f;
			position[1] = (this.maxY + this.minY) / 2f;
			position[2] = this.maxZ;
			return true;
		} else if (o.centerY >= this.minY && o.centerY <= this.maxY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ
				&& o.centerX - o.radius < this.maxX) { // II
			position[0] = this.maxX;
			position[1] = (this.maxY + this.minY) / 2f;
			position[2] = (this.maxZ + this.minZ) / 2f;
			return true;
		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerY >= this.minY && o.centerY <= this.maxY
				&& o.centerZ + o.radius > this.minZ) { // III
			position[0] = (this.maxX + this.minX) / 2f;
			position[1] = (this.maxY + this.minY) / 2f;
			position[2] = this.minZ;
			return true;
		} else if (o.centerY >= this.minY && o.centerY <= this.maxY
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ
				&& o.centerX + o.radius > this.minX) { // IV
			position[0] = this.minX;
			position[1] = (this.maxY + this.minY) / 2f;
			position[2] = (this.maxZ + this.minZ) / 2f;
			return true;
		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ
				&& o.centerY - o.radius < this.maxY) { // V
			position[0] = (this.maxX + this.minX) / 2f;
			position[1] = this.maxY;
			position[2] = (this.maxZ + this.minZ) / 2f;
			return true;
		} else if (o.centerX >= this.minX && o.centerX <= this.maxX
				&& o.centerZ >= this.minZ && o.centerZ <= this.maxZ
				&& o.centerY + o.radius > this.minY) { // VI
			position[0] = (this.maxX + this.minX) / 2f;
			position[1] = this.minY;
			position[2] = (this.maxZ + this.minZ) / 2f;
			return true;
		}		
		return false;
	}

	@Override
	public void scale(float x, float y, float z) {
		float delta = (maxX - minX) / 2.0f;
		delta = x * delta - delta;
		maxX += delta;
		minX -= delta;
		omaxX += delta;
		ominX -= delta;

		delta = (maxY - minY) / 2.0f;
		delta = y * delta - delta;
		maxY += delta;
		minY -= delta;
		omaxY += delta;
		ominY -= delta;

		delta = (maxZ - minZ) / 2.0f;
		delta = z * delta - delta;
		maxZ += delta;
		minZ -= delta;
		omaxZ += delta;
		ominZ -= delta;

	}
}

package com.ratmonkey.spacedash.collision;

import android.opengl.Matrix;

import com.ratmonkey.spacedash.object.Object3D;

public abstract class Bound {
	public static final int BOUND_NONE = 0;
	public static final int BOUND_BOX = 1;
	public static final int BOUND_SPHERE = 2;
	public static final int BOUND_COMPOSITE = 3;
	
	public boolean isActive = false;
	Object3D parent;
	
	public int boundType;
	public Bound(Object3D parent, int boundType) {
		this.boundType = boundType;
		this.parent = parent;
	}
	
	public abstract void rotate(float a, float x, float y, float z);
	public abstract void translate(float x, float y, float z);
	public abstract void scale(float x, float y, float z);
	public abstract boolean collideWith(Bound other, float[] position);
	
	public static float dotProduct(float x1, float y1, float z1, float x2, float y2, float z2) {

		float l1 = Matrix.length(x1, y1, z1);
		float l2 = Matrix.length(x2, y2, z2);
		
		float ux1 = x1/l1;
		float uy1 = y1/l1;
		float uz1 = z1/l1;
		float ux2 = x2/l2;
		float uy2 = y2/l2;
		float uz2 = z2/l2;
		
		return ux1 * ux2 + uy1 * uy2 + uz1 * uz2;
	}
	
	boolean calculateDistanceSphere(float x, float y, float z, Object3D source, BoundSphere s, float[] position) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
		float dx = x - s.centerX;
		float dy = y - s.centerY;
		float dz = z - s.centerZ;
		return Matrix.length(dx, dy, dz) < s.radius;
//		if (Matrix.length(dx, dy, dz) < s.radius) {
//			float dot = Bound.dotProduct(dx, dy, dz, source.posX
//					- source.oldX, source.posY - source.oldY, source.posZ
//					- source.oldZ);
//			return (dot > 0);
//		}
//		return false;
	}


}

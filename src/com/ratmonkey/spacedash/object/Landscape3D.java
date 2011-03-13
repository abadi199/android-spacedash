package com.ratmonkey.spacedash.object;

import com.ratmonkey.spacedash.collision.Bound;
import com.ratmonkey.spacedash.game.GameEngine;

public class Landscape3D extends Object3D {

	public float minZ, maxZ;
	public float originalX, originalY, originalZ;

	// public Object3D buffer;
	// float bufferX, bufferY, bufferZ;
	// public Object3D buffer2;
	// float buffer2X, buffer2Y, buffer2Z;

	public Landscape3D[] buffers;

	// public Landscape3D[] doubleBuffers;

	public Landscape3D(GameEngine engine) {
		super(engine, Bound.BOUND_NONE, Object3D.TYPE_PROPS);
	}

	/*
	 * Must be called after loading data and setting texture
	 */
	public void enableBuffer() {
		buffers = new Landscape3D[3];
		buffers[0] = new Landscape3D(mEngine);
		buffers[1] = new Landscape3D(mEngine);
		buffers[2] = new Landscape3D(mEngine);

		ObjectLoader.copyData(this, buffers[0]);
		ObjectLoader.copyData(this, buffers[1]);
		ObjectLoader.copyData(this, buffers[2]);

		buffers[0].setTextureId(textureId);
		buffers[1].setTextureId(textureId);
		buffers[2].setTextureId(textureId);

		// doubleBuffers = new Landscape3D[3];
		// doubleBuffers[0] = new Landscape3D(mSurfaceHolder, mSpeed);
		// doubleBuffers[1] = new Landscape3D(mSurfaceHolder, mSpeed);
		// doubleBuffers[2] = new Landscape3D(mSurfaceHolder, mSpeed);
		//
		// ObjectLoader.copyData(this, doubleBuffers[0]);
		// ObjectLoader.copyData(this, doubleBuffers[1]);
		// ObjectLoader.copyData(this, doubleBuffers[2]);
		//		
		// doubleBuffers[0].setTextureId(textureId);
		// doubleBuffers[1].setTextureId(textureId);
		// doubleBuffers[2].setTextureId(textureId);

		// get minimum z vertex of original landscape
		float minZ = Float.MAX_VALUE;
		float maxZ = Float.MIN_VALUE;
		float z;
		int size = mVertexBuffer.capacity();
		mVertexBuffer.position(0);
		for (int i = 2; i < size; i = i + 3) {
			z = mVertexBuffer.get(i);
			minZ = (minZ < z) ? minZ : z;
			maxZ = (maxZ > z) ? maxZ : z;
		}
		mVertexBuffer.position(0);
		minZ += 10.0;

		// move buffer to minimum z location
		buffers[0].originalZ = 0;
		buffers[1].originalZ = minZ - maxZ;
		buffers[2].originalZ = 2 * buffers[1].originalZ;

		buffers[0].maxZ = maxZ;
		buffers[1].maxZ = maxZ;
		buffers[2].maxZ = maxZ;

		buffers[0].minZ = minZ;
		buffers[1].minZ = minZ;
		buffers[2].minZ = minZ;

		buffers[1].translateZ(buffers[1].originalZ+40);
		buffers[2].translateZ(buffers[2].originalZ);

		// doubleBuffers[0].originalZ = 0;
		// doubleBuffers[1].originalZ = minZ - maxZ;
		// doubleBuffers[2].originalZ = 2 * doubleBuffers[1].originalZ;
		//
		// doubleBuffers[0].maxZ = maxZ;
		// doubleBuffers[1].maxZ = maxZ;
		// doubleBuffers[2].maxZ = maxZ;
		//
		// doubleBuffers[0].minZ = minZ;
		// doubleBuffers[1].minZ = minZ;
		// doubleBuffers[2].minZ = minZ;
		//		
		// doubleBuffers[0].posY = -0.5f;
		// doubleBuffers[1].posY = -0.5f;
		// doubleBuffers[2].posY = -0.5f;
		//
		// doubleBuffers[1].translateZ(doubleBuffers[1].originalZ);
		// doubleBuffers[2].translateZ(doubleBuffers[2].originalZ);
	}

	@Override
	public void translateX(float x) {
		if (buffers == null) {
			super.translateX(originalX + x);
		} else {
			synchronized (this) {
				buffers[0].translateX(x);
				buffers[1].translateX(x);
				buffers[2].translateX(x);
			}
		}
	}

	@Override
	public void translateY(float y) {
		if (buffers == null) {
			super.translateY(originalY + y);
		} else {
			synchronized (this) {
				buffers[0].translateY(y);
				buffers[1].translateY(y);
				buffers[2].translateY(y);
			}
		}
	}

	@Override
	public void translateZ(float z) {
		if (buffers == null) {
			super.translateZ(originalZ + z);
		} else {
			synchronized (this) {
				buffers[2].translateZ(z);
				buffers[1].translateZ(z);
				buffers[0].translateZ(z);
			}
		}
	}

	public void moveForward(float speed) {
		synchronized (this) {
			translateZ(buffers[0].posZ + speed);
			if ((buffers[0].posZ + buffers[0].minZ) > buffers[0].maxZ) {
				// rotate buffers
				Landscape3D temp = buffers[0];
				temp.posZ = buffers[2].posZ + buffers[2].minZ - temp.maxZ;
				buffers[0] = buffers[1];
				buffers[1] = buffers[2];
				buffers[2] = temp;

				buffers[0].originalZ = 0;
				buffers[1].originalZ = buffers[0].minZ - buffers[0].maxZ;
				buffers[2].originalZ = 2 * buffers[1].originalZ;

				buffers[0].posZ = 0;
				buffers[1].posZ = buffers[1].originalZ;
				buffers[2].posZ = buffers[2].originalZ;
				// buffers[0].translateZ(0);
				// buffers[1].translateZ(buffers[1].originalZ);
				// buffers[2].translateZ(buffers[2].originalZ);
			}
		}
	}
}
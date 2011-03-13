package com.ratmonkey.spacedash.object;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.util.Log;

import com.ratmonkey.spacedash.collision.Bound;
import com.ratmonkey.spacedash.collision.BoundBox;
import com.ratmonkey.spacedash.collision.BoundComposite;
import com.ratmonkey.spacedash.collision.BoundSphere;
import com.ratmonkey.spacedash.collision.CollisionDetector;
import com.ratmonkey.spacedash.controller.Controllable;
import com.ratmonkey.spacedash.game.GameEngine;
import com.ratmonkey.spacedash.particle.Emitter;

public class Object3D implements Controllable {
	public FloatBuffer mVertexBuffer;
	public FloatBuffer mNormalBuffer;
	public ShortBuffer mIndexBuffer;
	public FloatBuffer mTextureBuffer;
	public int vertexCount;
	public int normalCount;
	public int faceCount;
	public int indexCount;
	public int textureCount;

	public Bound mBound;

	public float rotX, rotY, rotZ;
	public float posX, posY, posZ;
	public float oldX, oldY, oldZ;
	public float scaleX, scaleY, scaleZ;

	public int textureId;
	public int textureBufferId;
	public int textureName;

	public int boundType;
	public boolean hasTexture = false;
	public boolean isVisible = true;
	public boolean isBlend = false;

	CollisionDetector mCollisionDetector;
	GameEngine mEngine;

	public ArrayList<Emitter> emitters;
	public ArrayList<Object3D> children;
	
	public int type;
	
	public final static int TYPE_OBSTACLE = 0;
	public final static int TYPE_PROPS = 1;
	public final static int TYPE_SHIELD_UP = 2;
	public final static int TYPE_GOAL = 3;
	public final static int TYPE_MISSILE_UP = 4;

	public Object3D(GameEngine engine, int boundType, int type) {
		this(engine, boundType, type, 0);
	}
	
	public Object3D(GameEngine engine, int boundType, int type, int numberOfCompositeBound) {
		this.mEngine = engine;
		this.type = type;
		emitters = new ArrayList<Emitter>();
		children = new ArrayList<Object3D>(); 
		scaleX = scaleY = scaleZ = 1.0f;
		this.boundType = boundType;
		switch (boundType) {
		case Bound.BOUND_BOX:
			mBound = new BoundBox(this);
			break;
		case Bound.BOUND_SPHERE:
			mBound = new BoundSphere(this);
			break;
		case Bound.BOUND_COMPOSITE:
			mBound = new BoundComposite(this, numberOfCompositeBound);
			break;
		default:
			break;
		}
	}	

	public void init(GL10 gl, Context context) {
		int size = emitters.size();
		for (int i = 0; i < size; i++) {
			Emitter e = emitters.get(i);
			e.init(context);
			try {
				e.loadTexture(gl, context);
			} catch (IOException e1) {
				Log.d("Ratmonkey", "[Object3D] error loading texture");
			}
		}
	}

	public void addEmitter(Emitter e) {
		emitters.add(e);
	}
	
	public void removeEmitter(Emitter e) {
		emitters.remove(e);
	}
	
	public void addChild(Object3D object) {
		children.add(object);
	}
	
	public void removeChild(Object3D object) {
		children.remove(object);
	}
	
	public void setCollisionDetector(CollisionDetector c) {
		mCollisionDetector = c;
		c.addBound(this);
	}
	
	public void rotate(float x, float y, float z) {
		rotX = x;
		rotY = y;
		rotZ = z;
	}

	public void rotateWithBound(float x, float y, float z) {
		this.rotate(x, y, z);
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_COMPOSITE:
			mBound.rotate(rotX, 1, 0, 0);
			mBound.rotate(rotY, 0, 1, 0);
			mBound.rotate(rotZ, 0, 0, 1);
			break;
		default:
			break;
		}
	}
	
	public void rotateX(float x) {
		rotX = x;
	}

	public void rotateXWithBound(float x) {
		this.rotateX(x);
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_COMPOSITE:
			mBound.rotate(rotX, 1, 0, 0);
			mBound.rotate(rotY, 0, 1, 0);
			mBound.rotate(rotZ, 0, 0, 1);
			break;
		default:
			break;
		}

	}

	public void rotateY(float y) {
		rotY = y;
	}
	
	public void rotateYWithBound(float y) {
		this.rotateY(y);
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_COMPOSITE:
			mBound.rotate(rotX, 1, 0, 0);
			mBound.rotate(rotY, 0, 1, 0);
			mBound.rotate(rotZ, 0, 0, 1);
			break;
		default:
			break;
		}

	}

	public void rotateZ(float z) {
		rotZ = z;
	}
	
	public void rotateZWithBound(float z) {
		this.rotateZ(z);
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_COMPOSITE:
			mBound.rotate(rotX, 1, 0, 0);
			mBound.rotate(rotY, 0, 1, 0);
			mBound.rotate(rotZ, 0, 0, 1);
			break;
		default:
			break;
		}

	}

	public void translate(float x, float y, float z) {
		oldX = posX;
		posX = x;

		oldY = posY;
		posY = y;

		oldZ = posZ;
		posZ = z;
	}
	
	public void translateWithBound(float x, float y, float z) {
		this.translate(x, y, z);

		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_SPHERE:
		case Bound.BOUND_COMPOSITE:
			mBound.translate(posX, posY, posZ);
			break;
		}
	}

	public void translateX(float x) {
		oldX = posX;
		posX = x;
	}
	
	public void translateXWithBound(float x) {
		this.translateX(x);
		
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_SPHERE:
		case Bound.BOUND_COMPOSITE:
			mBound.translate(posX, posY, posZ);
			break;
		}

	}

	public void translateY(float y) {
		oldY = posY;
		posY = y;
	}
	
	public void translateYWithBound(float y) {
		this.translateY(y);
		
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_SPHERE:
		case Bound.BOUND_COMPOSITE:
			mBound.translate(posX, posY, posZ);

			break;
		}
	}
	public void translateZ(float z) {
		oldZ = posZ;
		posZ = z;
	}
	
	public void translateZWithBound(float z) {
		this.translateZ(z);
		
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_SPHERE:
		case Bound.BOUND_COMPOSITE:
			mBound.translate(posX, posY, posZ);
			break;
		}
	}

	public void scale(float x, float y, float z) {
		scaleX = x;
		scaleY = y;
		scaleZ = z;
	}
	
	public void scaleWithBound(float x, float y, float z) {
		this.scale(x, y, z);
		switch (boundType) {
		case Bound.BOUND_BOX:
		case Bound.BOUND_SPHERE:
		case Bound.BOUND_COMPOSITE:
			mBound.scale(scaleX, scaleY, scaleZ);
		}
	}

//	public void transformBound() {
//		switch (boundType) {
//		case Bound.BOUND_BOX:
//		case Bound.BOUND_COMPOSITE:
//			mBound.translate(posX, posY, posZ);
//			mBound.rotate(rotX, 1, 0, 0);
//			mBound.rotate(rotY, 0, 1, 0);
//			mBound.rotate(rotZ, 0, 0, 1);
//			break;
//		case Bound.BOUND_SPHERE:
//			mBound.translate(posX, posY, posZ);
//			break;
//		}
//	}

	public void transformEmitter() {
		int size = emitters.size();
		for (int i = 0; i < size; i++) {
			Emitter e = emitters.get(i);
			if(e.hasTrail) {
				e.posX = posX;
				e.posY = posY;
				e.posZ = posZ;
			}
		}
	}

	public void draw(GL10 gl) {
		if(isVisible) {
			gl.glPushMatrix();
	
			gl.glTranslatef(posX, posY, posZ); // Move z units into the screen
			gl.glScalef(scaleX, scaleY, scaleZ); // Scale the Cube to 80 percent,
			// otherwise it would be too large
			// for the screen
	
			// Rotate around the axis based on the rotation matrix (rotation, x, y,
			// z)
			gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f); // X
			gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f); // Y
			gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f); // Z
	
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			
			if(isBlend) {
				gl.glEnable(GL10.GL_BLEND); // Enable blending
				gl.glDepthMask(false);
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Set The		
//				gl.glDisable(GL10.GL_LIGHTING);
			}
			
			if (hasTexture) {
				gl.glEnable(GL10.GL_TEXTURE_2D);
	
				GL11 gl11 = (GL11) gl;
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
	
				if (textureBufferId != 0) {
					gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
					gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureBufferId);
					gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
					gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
				}
	
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
				gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
				gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);
	
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				gl.glDisable(GL10.GL_TEXTURE_2D);
	
			} else {
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
				gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
				gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);
			}
	
			if(isBlend) {
				gl.glDisable(GL10.GL_BLEND);
				gl.glDepthMask(true);
//				gl.glEnable(GL10.GL_LIGHTING);
			}
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			
			int size = children.size();
			for (int i = 0; i < size; i++) {
				Object3D o = children.get(i);
				if(o.isVisible) {
					o.draw(gl);
				}
			}
	
			size = emitters.size();
			for (int i = 0; i < size; i++) {
				Emitter e = emitters.get(i);
				if(!e.hasTrail) {
					e.draw(gl, mEngine.mCamera.z);
					if (!e.active && e.particleCount <= 0) {
						emitters.remove(i);
						size--;
					}
				}
			}
			
			gl.glPopMatrix();
			
			for (int i = 0; i < size; i++) {
				Emitter e = emitters.get(i);
				if(e.hasTrail) {
					e.draw(gl, mEngine.mCamera.z);
					if (!e.active && e.particleCount <= 0) {
						emitters.remove(i);
						size--;
					}
				}
			}
		}
	}

	public void setTexture(int textureName) {
		hasTexture = true;
		this.textureName = textureName;
	}

	public void loadTextureBuffer(GL10 gl) {
		if (mTextureBuffer.capacity() > 0) {
			GL11 gl11 = (GL11) gl;
			int[] temp = new int[1];
			gl11.glGenBuffers(1, temp, 0);
			textureBufferId = temp[0];

			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureBufferId);
			gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexCount * 2 * 4,
					mTextureBuffer, GL11.GL_STATIC_DRAW);
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);

			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR) {
				Log.e("Ratmonkey", "[Object3D][" + textureId + "]TextureBuffer error GLError: " + error);
			}
		}

	}

	public void onActionEvent(float value) {

	}

	public void onUpEvent(float value) {
		oldY = posY;
		if (posY < 7) {
			// posY += value*2;
			translateYWithBound(posY + value * 2);
			if (posY > 7) {
				translateYWithBound(7);
				oldY = posY;
			}
//			transformBound();
			transformEmitter();
		}
	}

	public void onDownEvent(float value) {
		oldY = posY;
		if (posY > -7) {
			// posY -= value;
			translateYWithBound(posY - value);
			if (posY < -7) {
				translateYWithBound(-7);
				oldY = posY;
			}
//			transformBound();
			transformEmitter();
		}
	}

	public void onLeftEvent(float value) {
		oldX = posX;
		if (posX > -10) {
			// posX -= value;
			translateXWithBound(posX - value);
			if (posX < -10) {
				translateXWithBound(-10);
				oldX = posX;
			}
//			transformBound();
			transformEmitter();
		}
	}

	public void onRightEvent(float value) {
		oldX = posX;
		if (posX < 10) {
			// posX += value;
			translateXWithBound(posX + value);
			if (posX > 10) {
				translateXWithBound(10);
				oldX = posX;
			}
//			transformBound();
			transformEmitter();
		}
	}

	public void calculateBound() {
		switch (boundType) {
		case Bound.BOUND_BOX:
			calculateBoundBox();
			break;
		case Bound.BOUND_SPHERE:
			calculateBoundSphere();
			break;
		}
	}

	private void calculateBoundSphere() {
		BoundSphere sphere = (BoundSphere) mBound;
		mVertexBuffer.position(0);
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float minZ = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		float maxZ = Float.MIN_VALUE;
		float x, y, z;
		while (mVertexBuffer.hasRemaining()) {
			x = mVertexBuffer.get();
			y = mVertexBuffer.get();
			z = mVertexBuffer.get();
			minX = (minX < x) ? minX : x;
			minY = (minY < y) ? minY : y;
			minZ = (minZ < z) ? minZ : z;

			maxX = (maxX > x) ? maxX : x;
			maxY = (maxY > y) ? maxY : y;
			maxZ = (maxZ > z) ? maxZ : z;
		}
		mVertexBuffer.position(0);
		float avgX = (maxX - minX) / 2f;
		float avgY = (maxY - minY) / 2f;
		float avgZ = (maxZ - minZ) / 2f;
		sphere.setSphere(minX + avgX, minY + avgY, minZ + avgZ, Math.max(avgX,
				Math.max(avgY, avgZ)));
//		Log.d("Ratmonkey", "BoundSphere:" + sphere.toString());
	}

	private void calculateBoundBox() {
		BoundBox mBoundBox = (BoundBox) mBound;
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float minZ = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		float maxZ = Float.MIN_VALUE;
		float x, y, z;

		mVertexBuffer.position(0);
		while (mVertexBuffer.hasRemaining()) {
			x = mVertexBuffer.get();
			y = mVertexBuffer.get();
			z = mVertexBuffer.get();
			minX = (minX < x) ? minX : x;
			minY = (minY < y) ? minY : y;
			minZ = (minZ < z) ? minZ : z;

			maxX = (maxX > x) ? maxX : x;
			maxY = (maxY > y) ? maxY : y;
			maxZ = (maxZ > z) ? maxZ : z;
		}
		mVertexBuffer.position(0);

		mBoundBox.setMaxXmaxYmaxZ(maxX, maxY, maxZ);
		mBoundBox.setMinXminYminZ(minX, minY, minZ);
		// mBoundBox.setMaxXmaxYminZ(maxX, maxY, minZ);
		// mBoundBox.setMaxXminYmaxZ(maxX, minY, maxZ);
		// mBoundBox.setMaxXminYminZ(maxX, minY, minZ);
		// mBoundBox.setMinXmaxYmaxZ(minX, maxY, maxZ);
		// mBoundBox.setMinXmaxYminZ(minX, maxY, minZ);
		// mBoundBox.setMinXminYmaxZ(minX, minY, maxZ);
		// Log.d("Ratmonkey", "mBoundBox:\n" + mBoundBox.toString());
	}

	public void setBoundBox(float minX, float minY, float minZ, float maxX,
			float maxY, float maxZ) {
		BoundBox mBoundBox = (BoundBox) mBound;
		mBoundBox.setMaxXmaxYmaxZ(maxX, maxY, maxZ);
		mBoundBox.setMinXminYminZ(minX, minY, minZ);
	}

	public void setTextureId(int textureId) {
		hasTexture = true;
		this.textureId = textureId;
	}
}
package com.ratmonkey.spacedash.particle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.ratmonkey.spacedash.R;
import com.ratmonkey.spacedash.game.GameEngine;
import com.ratmonkey.spacedash.math.Functions;

public class Emitter {
	Particle[] particles;
	public static final int textureId = R.drawable.explosion;
	static int textureName;
	Context context;

	public float posX, posY, posZ;
	// public float rotX, rotY, rotZ;
	// public float scale;

	public float[] sourcePosition = { 0, 0, 0 };
	public float[] sourcePositionVariance = { 0.5f, 0.5f, 0.5f };
	public float speed = 0.05f;
	public float speedVariance = 0.01f;
	public float particleLifeSpan = 1.0f;
	public float particleLifeSpanVariance = 0.5f;
	public float angle = 90;
	public float angleVariance = 360.0f;
	public float angleZ = 360;
	public float angleZVariance = 0.0f;
	public float[] gravity = { 0, 0, 0 };
	public float[] startColor = { 1.0f, 0.0f, 0.0f, 1.0f };
	public float[] startColorVariance = { 0.0f, 0.0f, 0.0f, 0.0f };
	public float[] finishColor = { 1.0f, 1.0f, 0.0f, 0.0f };
	public float[] finishColorVariance = { 0.0f, 0.0f, 0.0f, 0.0f };
	public int maxParticle = 100;
	public float particleSize = 32;
	public float particleSizeVariance = 32;
	public float duration = -1.0f;
	public boolean blendAdditive = true;

	public boolean active = true;
	public float elapsedTime = 0;
	public float emissionRate;
	public float emitCounter;
	public int particleIndex;
	public int particleCount;
	public long lastTime;

	private float[] vertices;
	private float[] colors;
	private float[] sizes;
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer sizeBuffer;

	public boolean hasTrail = false;

	public Emitter(Context context) {
		this.context = context;
	}

	public Emitter(Context context, float[] sourcePosition,
			float[] sourcePositionVariance, float speed, float speedVariance,
			float particleLifeSpan, float particleLifeSpanVariance,
			float angle, float angleVariance, float angleZ,
			float angleZVariance, float[] gravity, float[] startColor,
			float[] startColorVariance, float[] finishColor,
			float[] finishColorVariance, int maxParticle, float particleSize,
			float particleSizeVariance, float duration, boolean blendAdditive) {
		this(context);

		this.sourcePosition = sourcePosition;
		this.sourcePositionVariance = sourcePositionVariance;
		this.speed = speed;
		this.speedVariance = speedVariance;
		this.particleLifeSpan = particleLifeSpan;
		this.particleLifeSpanVariance = particleLifeSpanVariance;
		this.angle = angle;
		this.angleVariance = angleVariance;
		this.angleZ = angleZ;
		this.angleZVariance = angleZVariance;
		this.gravity = gravity;
		this.startColor = startColor;
		this.startColorVariance = startColorVariance;
		this.finishColor = finishColor;
		this.finishColorVariance = finishColorVariance;
		this.maxParticle = maxParticle;
		this.particleSize = particleSize;
		this.particleSizeVariance = particleSizeVariance;
		this.duration = duration;
		this.blendAdditive = blendAdditive;
	}

	public void init(Context context) {

		// try {
		// loadTexture(gl, context);
		// } catch (IOException e) {
		// Log.d("Ratmonkey", "Error loading particle texture: "
		// + e.getMessage());
		// }

		particles = new Particle[maxParticle];
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle();
		}
		vertices = new float[maxParticle * 3];
		colors = new float[maxParticle * 4];
		sizes = new float[maxParticle];

		particleCount = 0;
		emissionRate = maxParticle / particleLifeSpan;
		elapsedTime = 0;

		active = true;

		vertexBuffer = FloatBuffer.wrap(vertices);
		colorBuffer = FloatBuffer.wrap(colors);
		sizeBuffer = FloatBuffer.wrap(sizes);

		lastTime = System.currentTimeMillis();
	}

	public boolean addParticle() {
		if (particleCount == maxParticle) {
			return false;
		}

		// Particle particle = new Particle();
		// if(particles[particleCount].timeToLive > 0) {
		// Log.d("Ratmonkey",
		// "ttl:"+particles[particleCount].timeToLive+" particleCount:"+particleCount);
		// }
		initParticle(particles[particleCount]);

		particleCount++;
		return true;
	}

	public void initParticle(Particle particle) {
		particle.position[0] = posX + sourcePosition[0]
				+ sourcePositionVariance[0] * Functions.randomMinus1To1();
		particle.position[1] = posY + sourcePosition[1]
				+ sourcePositionVariance[1] * Functions.randomMinus1To1();
		particle.position[2] = posZ + sourcePosition[2]
				+ sourcePositionVariance[2] * Functions.randomMinus1To1();

		float newAngle = Functions.degreesToRadians(angle + angleVariance
				* Functions.randomMinus1To1());
		float newAngleZ = Functions.degreesToRadians(angleZ + angleZVariance
				* Functions.randomMinus1To1());
		// float cos = (float) Math.cos(newAngle);
		float[] vector = {
				(float) Math.cos(newAngle) * (float) Math.cos(newAngleZ),
				(float) Math.sin(newAngle), (float) Math.sin(newAngleZ) };

		float vectorSpeed = speed + speedVariance * Functions.randomMinus1To1();

		Functions.vectorMultiply(vector, vectorSpeed, particle.direction);

		particle.particleSize = particleSize + particleSizeVariance
				* Functions.randomMinus1To1();

		particle.timeToLive = particleLifeSpan + particleLifeSpanVariance
				* Functions.randomMinus1To1();
		// Log.d("Ratmonkey","init.timeToLive:"+particle.timeToLive);

		particle.r = startColor[0] + startColorVariance[0]
				* Functions.randomMinus1To1();
		particle.g = startColor[1] + startColorVariance[1]
				* Functions.randomMinus1To1();
		particle.b = startColor[2] + startColorVariance[2]
				* Functions.randomMinus1To1();
		particle.a = startColor[3] + startColorVariance[3]
				* Functions.randomMinus1To1();

		particle.deltaR = (finishColor[0] + finishColorVariance[0]
				* Functions.randomMinus1To1() - particle.r)
				/ particle.timeToLive;
		particle.deltaG = (finishColor[1] + finishColorVariance[1]
				* Functions.randomMinus1To1() - particle.g)
				/ particle.timeToLive;
		particle.deltaB = (finishColor[2] + finishColorVariance[2]
				* Functions.randomMinus1To1() - particle.b)
				/ particle.timeToLive;
		particle.deltaA = (finishColor[3] + finishColorVariance[3]
				* Functions.randomMinus1To1() - particle.a)
				/ particle.timeToLive;
	}

	public void stopParticleEmitter() {
		active = false;
		elapsedTime = 0;
		emitCounter = 0;
		// Log.d("Ratmonkey", "stopParticleEmitter");
	}

	public void update(float delta, float cameraPosZ) {
		// Log.d("Ratmonkey","*********************************");
		if (active && emissionRate > 0) {
			float rate = 1.0f / (emissionRate);
			emitCounter += delta;
			while (particleCount < maxParticle && emitCounter > rate) {
				addParticle();
				emitCounter -= rate;
			}
			// Log.d("Ratmonkey",
			// "addParticle:"+addParticleCounter+" particleCount:"+particleCount);

			elapsedTime += delta;
			if (duration != -1 && duration < elapsedTime) {
				stopParticleEmitter();
			}
		}
		// Log.d("Ratmonkey", "update:" + particleCount);

		particleIndex = 0;
		while (particleIndex < particleCount) {
			Particle currentParticle = particles[particleIndex];
			// Log.d("Ratmonkey",
			// "timeToLive:"+currentParticle.timeToLive+", delta:"+delta);
			if (currentParticle.timeToLive > 0.0f) {

				Functions.vectorAdd(currentParticle.direction, gravity,
						currentParticle.direction);
				// currentParticle.direction = vectorMultiply(
				// currentParticle.direction, delta);
				Functions.vectorAdd(currentParticle.position,
						currentParticle.direction, currentParticle.position);

				currentParticle.timeToLive -= delta;

				int verticeIndex = particleIndex * 3;
				vertices[verticeIndex] = currentParticle.position[0];
				vertices[verticeIndex + 1] = currentParticle.position[1];
				vertices[verticeIndex + 2] = currentParticle.position[2];

				sizes[particleIndex] = 10 * currentParticle.particleSize
						/ Math.abs(currentParticle.position[2] - cameraPosZ);

				currentParticle.r += currentParticle.deltaR * delta;
				currentParticle.g += currentParticle.deltaG * delta;
				currentParticle.b += currentParticle.deltaB * delta;
				currentParticle.a += currentParticle.deltaA * delta;

				int colorIndex = particleIndex * 4;
				colors[colorIndex] = currentParticle.r;
				colors[colorIndex + 1] = currentParticle.g;
				colors[colorIndex + 2] = currentParticle.b;
				colors[colorIndex + 3] = currentParticle.a;

				particleIndex++;
			} else {
				if (particleIndex != (particleCount - 1)) {
					Particle temp = particles[particleIndex];
					particles[particleIndex] = particles[particleCount - 1];
					particles[particleCount - 1] = temp;
				}
				particleCount--;
			}
		}
		// Log.d("Ratmonkey","killParticleCounter:"+killParticleCounter);

		// synchronized (vertexBuffer) {
		// vertexBuffer.put(vertices,0,particleCount*3);
		// vertexBuffer.position(0);
		//			
		// colorBuffer.put(colors,0,particleCount*4);
		// colorBuffer.position(0);
		//			
		// sizeBuffer.put(sizes,0,particleCount);
		// sizeBuffer.position(0);
		// }
	}

	// public void run() {
	// long time;
	// float delta;
	// while (active || particleCount > 0) {
	// time = System.currentTimeMillis();
	// delta = (time - lastTime) / 1000.0f;
	// try {
	// update(delta);
	// sleep(10);
	// lastTime = time;
	// } catch (Exception e) {
	// Log.d("Ratmonkey", "Error in thread:" + e.getMessage());
	// }
	// }
	// Log.d("Ratmonkey", "Thread ended");
	// }

	public void draw(GL10 gl, float cameraPosZ) {
		if (active || particleCount > 0) {
			long time;
			float delta;
			time = System.currentTimeMillis();
			delta = (time - lastTime) / 1000.0f;
			if (GameEngine.mEngineState == GameEngine.ENGINE_STATE_RUN
					|| GameEngine.mEngineState == GameEngine.ENGINE_STATE_GAMEOVER) {
				update(delta, cameraPosZ);
			}
			render(gl);
			lastTime = time;
		}
	}

	public void render(GL10 gl) {
		gl.glPushMatrix();
		// gl.glTranslatef(posX, posY, posZ);
		// gl.glRotatef(rotX, 1, 0, 0);
		// gl.glRotatef(rotY, 0, 1, 0);
		// gl.glRotatef(rotZ, 0, 0, 1);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		gl.glDisable(GL10.GL_LIGHTING);

		gl.glEnable(GL10.GL_BLEND); // Enable blending
		gl.glDepthMask(false);

		if (blendAdditive) {
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Set The
			// Blending
			// Function For
			// Translucency
		} else {
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		GL11 gl11 = (GL11) gl;
		gl.glEnable(GL11.GL_POINT_SPRITE_OES);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// Point to our buffers
		gl.glEnable(GL11.GL_POINT_SMOOTH);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);
		gl11.glPointSizePointerOES(GL11.GL_FLOAT, 0, sizeBuffer);

		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		gl.glTexEnvf(GL11.GL_POINT_SPRITE_OES, GL11.GL_COORD_REPLACE_OES,
				GL11.GL_TRUE);
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_POINTS, 0, particleIndex);

		// Disable the client state before leaving
		gl.glTexEnvf(GL11.GL_POINT_SPRITE_OES, GL11.GL_COORD_REPLACE_OES,
				GL11.GL_FALSE);

		gl.glDisableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisable(GL11.GL_POINT_SPRITE_OES);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDepthMask(true);

		// gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_LIGHTING);
		// gl.glEnable(GL10.GL_DEPTH_TEST); //Enables Depth Testing
		gl.glPopMatrix();
	}

	public int loadTexture(GL10 gl, Context context) throws IOException {

		if (textureName == 0) {
			int[] tex_out = new int[1];
			gl.glGenTextures(1, tex_out, 0);

			textureName = tex_out[0];

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			InputStream stream = context.getResources().openRawResource(
					textureId);

			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);
			// Log.d("Ratmonkey", "Bitmap width:" + bitmap.getWidth()
			// + ", height:" + bitmap.getHeight());

			stream.close();

			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);

			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
			// GL10.GL_CLAMP_TO_EDGE);
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
			// GL10.GL_CLAMP_TO_EDGE);

			// gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
			// GL10.GL_MODULATE);

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR) {
				Log.e("Ratmonkey", "[Emitter][" + textureId
						+ "]Texture Load GLError: " + error);
			}
		}
		return textureName;
	}
	
	public void setTexture(int textureName) {
		if(Emitter.textureName == 0) { 
			Emitter.textureName = textureName;
		}
	}
}

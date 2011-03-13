package com.ratmonkey.spacedash.object;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.ratmonkey.spacedash.game.GameEngine;
import com.ratmonkey.spacedash.particle.Emitter;

public class Ship3D extends Object3D {
	boolean isStop = false;
	float recoveryRotZ;
	boolean leftSide;

	public Ship3D(GameEngine engine, int boundType, int numberOfCompositeBound) {
		super(engine, boundType, Object3D.TYPE_PROPS, numberOfCompositeBound);
	}

	@Override
	public void draw(GL10 gl) {
		if (posX != oldX) {
			float amount = -((posX - oldX) * 30);
			if (amount > 60)
				amount = 60;
			else if (amount < -60)
				amount = -60;
			rotateZ(amount);
			isStop = false;
		} else {
			if (!isStop) {
				if (rotZ < 0) {
					recoveryRotZ = -rotZ / 10;
					leftSide = false;
				} else if (rotZ > 0) {
					recoveryRotZ = rotZ / 10;
					leftSide = true;
				}
				isStop = true;
			} else {
				if (!leftSide) {
					rotateZ(rotZ + recoveryRotZ);
					if (rotZ > 0)
						rotateZ(0);
				} else {
					rotateZ(rotZ - recoveryRotZ);
					if (rotZ < 0)
						rotateZ(0);
				}
			}
		}
		super.draw(gl);
	}

	public void initEngineEmitter(Context context) {
		Emitter jetR = new Emitter(context, 
				new float[] { 0.5f, 0.2f, 4.8f }, // sourcePosition
				new float[] { 0.25f, 0.25f, 0 }, // sourcePositionVariance
				0.1f, // speed
				0.05f, // speedVariance
				0.5f, // particleLifeSpan
				0.1f, // particleLifeSpanVariance
				0.0f, // angle
				0.0f, // angleVariance
				90,	  // angleZ
				0,    // angleZVariance
				new float[] { 0.0f, 0.0f, 0.0f}, // gravity
				new float[] { 0.0f, 1.0f, 1.0f, 1.0f }, // startColor
				new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // startColorVariance
				new float[] { 1.0f, 1.0f, 0.0f, 0.0f }, // finishColor
				new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, // finishColorVariance
				50, // maxParticle
				32f, // particleSize
				0f, // particleSizeVariance
				-1f, // duration
				true // blendAdditive
		);

		Emitter jetL = new Emitter(context, 
				new float[] { -0.5f, 0.2f, 4.8f }, // sourcePosition
				new float[] { 0.25f, 0.25f, 0 }, // sourcePositionVariance
				jetR.speed, // speed
				jetR.speedVariance, // speedVariance
				jetR.particleLifeSpan, // particleLifeSpan
				jetR.particleLifeSpanVariance, // particleLifeSpanVariance
				jetR.angle, // angle
				jetR.angleVariance, // angleVariance
				jetR.angleZ,	  // angleZ
				jetR.angleZVariance,    // angleZVariance
				jetR.gravity, // gravity
				jetR.startColor, // startColor
				jetR.startColorVariance, // startColorVariance
				jetR.finishColor, // finishColor
				jetR.finishColorVariance, // finishColorVariance
				jetR.maxParticle, // maxParticle
				jetR.particleSize, // particleSize
				jetR.particleSizeVariance, // particleSizeVariance
				jetR.duration, // duration
				true // blendAdditive
		);
				

		this.addEmitter(jetR);
		this.addEmitter(jetL);
	}
}

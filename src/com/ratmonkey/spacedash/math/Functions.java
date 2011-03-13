package com.ratmonkey.spacedash.math;

public class Functions {
	public static void vectorMultiply(float[] v1, float s, float[] out) {
		out[0] = v1[0] * s;
		out[1] = v1[1] * s;
		out[2] = v1[2] * s;
	}

	public static void vectorAdd(float[] v1, float[] v2, float[] out) {
		out[0] = v1[0] + v2[0];
		out[1] = v1[1] + v2[1];
		out[2] = v1[2] + v2[2];
	}

	public static void vectorSub(float[] v1, float[] v2, float[] out) {
		out[0] = v1[0] - v2[0];
		out[1] = v1[1] - v2[1];
		out[2] = v1[2] - v2[2];
	}

	public static float vectorDot(float[] v1, float[] v2) {
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
	}

	public static float vectorLength(float[] v) {
		return (float) Math.sqrt(vectorDot(v, v));
	}

	public static float randomMinus1To1() {
		return (float) (Math.random() * 2.0f - 1.0f);
	}

	public static float degreesToRadians(float angle) {
		return (float) (angle / 180.0 * Math.PI);
	}
}

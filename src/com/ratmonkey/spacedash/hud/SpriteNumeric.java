package com.ratmonkey.spacedash.hud;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class SpriteNumeric extends Sprite {

	int[] numbersX;
	int[] numbersWidth;
	String numberTxt;
	int digit;

	int[] currentU;
	int[] currentW;
	int charWidth;

	public SpriteNumeric(int x, int y, int width, int height, int screenX,
			int screenY, int[] numbersX, int[] numbersWidth, int digit, int charWidth, int textureName) {
		super(x, y, width, height, screenX, screenY, textureName);
		this.numbersX = numbersX;
		this.numbersWidth = numbersWidth;
		this.digit = digit;
		currentU = new int[digit];
		currentW = new int[digit];
		this.charWidth = charWidth;
	}

	public void setNumber(int number) {
		this.numberTxt = format(number);
//		Log.d("Ratmonkey", "[SpriteNumeric] numberTxt:" + numberTxt);
		for (int i = 0; i < digit; i++) {
			char c = numberTxt.charAt(i);
			switch (c) {
			case '0':
				currentU[i] = numbersX[0];
				currentW[i] = numbersWidth[0];
				break;
			case '1':
				currentU[i] = numbersX[1];
				currentW[i] = numbersWidth[1];
				break;
			case '2':
				currentU[i] = numbersX[2];
				currentW[i] = numbersWidth[2];
				break;
			case '3':
				currentU[i] = numbersX[3];
				currentW[i] = numbersWidth[3];
				break;
			case '4':
				currentU[i] = numbersX[4];
				currentW[i] = numbersWidth[4];
				break;
			case '5':
				currentU[i] = numbersX[5];
				currentW[i] = numbersWidth[5];
				break;
			case '6':
				currentU[i] = numbersX[6];
				currentW[i] = numbersWidth[6];
				break;
			case '7':
				currentU[i] = numbersX[7];
				currentW[i] = numbersWidth[7];
				break;
			case '8':
				currentU[i] = numbersX[8];
				currentW[i] = numbersWidth[8];
				break;
			case '9':
				currentU[i] = numbersX[9];
				currentW[i] = numbersWidth[9];
				break;

			default:
				break;
			}
		}
	}

	public String format(int number) {
		StringBuffer str = new StringBuffer();
		if (digit > 0) {
			int ceiling = (int) Math.pow(10, digit) - 1;
			if (number > ceiling) {
				number = ceiling;
			}
		}
		str.append(number);
		if (digit > 0) {
			int length = str.length();
			while (length < digit) {
				str.insert(0, "0");
				length++;
			}
		}
		return str.toString();
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		int x = screenX;
		for (int i = 0; i < digit; i++) {
			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, new int[] { currentU[i], texV,
							currentW[i], texH }, 0);
			((GL11Ext) gl).glDrawTexiOES(x, screenY, 0, currentW[i], -texH);
			x += charWidth;
		}
		gl.glPopMatrix();
	}

}

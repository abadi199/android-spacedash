package com.ratmonkey.spacedash.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.Context;

public abstract class ObjectLoader {
	
	private static void parseData(BufferedReader br, Object3D obj) throws NumberFormatException, IOException {
		int count = Integer.parseInt(br.readLine());
		obj.vertexCount = count;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(count * 3 * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		obj.mVertexBuffer = byteBuf.asFloatBuffer();
		
		String[] tempsplit;
		float[] temp = new float[count * 3];
		int cnt = 0;
		for(int i=0;i<count;i++) {
			tempsplit = br.readLine().split("\\s+");
			temp[cnt++] = Float.parseFloat(tempsplit[0]);
			temp[cnt++] = Float.parseFloat(tempsplit[1]);
			temp[cnt++] = Float.parseFloat(tempsplit[2]);
//			obj.mVertexBuffer.put(Float.parseFloat(tempsplit[0]));
//			obj.mVertexBuffer.put(Float.parseFloat(tempsplit[1]));
//			obj.mVertexBuffer.put(Float.parseFloat(tempsplit[2]));
		}
		obj.mVertexBuffer.put(temp);
		count = Integer.parseInt(br.readLine());
		obj.normalCount = count;
		
		byteBuf = ByteBuffer.allocateDirect(count * 3 * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		obj.mNormalBuffer = byteBuf.asFloatBuffer();
		temp = new float[count * 3];
		cnt = 0;
		for(int i=0;i<count;i++) {
			tempsplit = br.readLine().split("\\s+");
			temp[cnt++] = Float.parseFloat(tempsplit[0]);
			temp[cnt++] = Float.parseFloat(tempsplit[1]);
			temp[cnt++] = Float.parseFloat(tempsplit[2]);
//			obj.mNormalBuffer.put(Float.parseFloat(tempsplit[0]));
//			obj.mNormalBuffer.put(Float.parseFloat(tempsplit[1]));
//			obj.mNormalBuffer.put(Float.parseFloat(tempsplit[2]));
		}
//		Log.d("Ratmonkey", Arrays.toString(temp));
		obj.mNormalBuffer.put(temp);
//		count = Integer.parseInt(br.readLine());
//		obj.faceCount = count;
//		obj.indexCount = count * 3;
		
//		byteBuf = ByteBuffer.allocateDirect(obj.indexCount * 2);
//		byteBuf.order(ByteOrder.nativeOrder());
//		obj.mIndexBuffer = byteBuf.asShortBuffer();
//		
//		for(int i=0;i<count;i++) {
//			tempsplit = br.readLine().split("\\s+");
//			obj.mIndexBuffer.put((short) (Short.parseShort(tempsplit[0])-1));
//			obj.mIndexBuffer.put((short) (Short.parseShort(tempsplit[1])-1));
//			obj.mIndexBuffer.put((short) (Short.parseShort(tempsplit[2])-1));
//		}
		
		count = Integer.parseInt(br.readLine());
		obj.textureCount = count;
		
		byteBuf = ByteBuffer.allocateDirect(obj.textureCount * 2 * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		obj.mTextureBuffer = byteBuf.asFloatBuffer();
		
		temp = new float[obj.textureCount * 2];
		cnt = 0;
		for(int i=0;i<count;i++) {
			tempsplit = br.readLine().split("\\s+");
			temp[cnt++] = Float.parseFloat(tempsplit[0]);
			temp[cnt++] = Float.parseFloat(tempsplit[1]);
//			obj.mTextureBuffer.put(Float.parseFloat(tempsplit[0]));
//			obj.mTextureBuffer.put(Float.parseFloat(tempsplit[1]));
		}
		obj.mTextureBuffer.put(temp);
		
		obj.mVertexBuffer.position(0);
		obj.mNormalBuffer.position(0);
//		obj.mIndexBuffer.position(0);
		obj.mTextureBuffer.position(0);		
	}
	
	public static void copyData(Object3D src, Object3D target) {
		
		target.vertexCount = src.vertexCount;
		target.mVertexBuffer = src.mVertexBuffer.duplicate();
		src.mVertexBuffer.position(0);
		target.mVertexBuffer.position(0);	
		
		target.normalCount = src.normalCount;
		target.mNormalBuffer = src.mNormalBuffer.duplicate();
		src.mNormalBuffer.position(0);
		target.mNormalBuffer.position(0);

		target.textureCount = src.textureCount;
		target.mTextureBuffer = src.mTextureBuffer.duplicate();
		src.mTextureBuffer.position(0);
		target.mTextureBuffer.position(0);
		
		target.textureBufferId = src.textureBufferId;
		target.textureId = src.textureId;
	}
	
	public static void loadData(String path, Object3D obj) throws NumberFormatException, IOException {
		File file = new File(path);
		FileInputStream stream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream), 8192);

		parseData(br, obj);
		
		stream.close();
		br.close();
		
	}
	
	public static void loadRaw(Context context, int raw, Object3D obj) throws NumberFormatException, IOException {
		
		InputStream stream = context.getResources().openRawResource(raw);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		
		parseData(br, obj);

		stream.close();
		br.close();
	}	
	
	public static void loadObject(String path, Object3D obj) throws StreamCorruptedException, IOException, ClassNotFoundException {
		
		String vertexPath;
		if(path.endsWith("/")) vertexPath = path+"vertex.out";
		else vertexPath = path +"/vertex.out";
		
		File file = new File(vertexPath);
		FileInputStream stream = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(stream);
		float[] temp = (float[]) in.readObject();
		
		obj.vertexCount = temp.length;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(temp.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		obj.mVertexBuffer = byteBuf.asFloatBuffer();
		obj.mVertexBuffer.put(temp);
		obj.mVertexBuffer.position(0);
		
		in.close();
		stream.close();
		
		String normalPath;
		if(path.endsWith("/")) normalPath = path+"normal.out";
		else normalPath = path +"/normal.out";
		
		file = new File(normalPath);
		stream = new FileInputStream(file);
		in = new ObjectInputStream(stream);
		temp = (float[]) in.readObject();
		
		byteBuf = ByteBuffer.allocateDirect(temp.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		obj.mNormalBuffer = byteBuf.asFloatBuffer();
		obj.mNormalBuffer.put(temp);
		obj.mNormalBuffer.position(0);
	}	

}

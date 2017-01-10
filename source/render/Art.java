package com.gameinbucket.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

public abstract class Art 
{	
	private Art()
	{

	}

	public static int alpha(int argb)
	{
		return argb >> 24;
	}

	public static int red(int argb)
	{
		return (argb & 0x00FF0000) >> 16;
	}

	public static int green(int argb)
	{
		return (argb & 0x0000FF00) >> 8;
	}

	public static int blue(int argb)
	{
		return (argb & 0x000000FF);
	}

	public static int rgb_red(int rgb)
	{
		return (rgb & 0xFF0000) >> 16;
	}

	public static int rgb_green(int rgb)
	{
		return (rgb & 0x00FF00) >> 8;
	}

	public static int rgb_blue(int rgb)
	{
		return (rgb & 0x0000FF);
	}

	public static int rgb(int argb)
	{
		return (argb & 0x00FFFFFF);
	}

	public static int argb(int a, int r, int g, int b)
	{
		return a << 24 | r << 16 | g << 8 | b;
	}

	public static void transparent_pixels(int[] pixels, int color)
	{
		for (int i = 0; i < pixels.length; i++)
		{
			if ((pixels[i] & 0x00FFFFFF) == color)
				pixels[i] = 0;
		}
	}

	public static void multiply_pixels(int[] pixels, int width, int startx, int starty, int endx, int endy, float r, float g, float b)
	{
		for (int x = startx; x < endx; x++)
		{
			for (int y = starty; y < endy; y++)
			{
				int i = x + y * width;

				if (Art.alpha(pixels[i]) == 0)
					continue;

				pixels[i] = Art.argb(255, (int)(r * Art.red(pixels[i])), (int)(g * Art.green(pixels[i])), (int)(b * Art.blue(pixels[i])));
			}
		}
	}

	public static Bitmap load_image_data(Context c, int r)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		return BitmapFactory.decodeResource(c.getResources(), r, options);
	}

	public static int load_texture(Context c, int r, int wrap)
	{
		Bitmap bitmap = load_image_data(c, r);

		ByteBuffer byte_buffer = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
		byte_buffer.order(ByteOrder.BIG_ENDIAN);

		IntBuffer int_buffer = byte_buffer.asIntBuffer();

		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

		for (int i = 0; i < pixels.length; i++)
			int_buffer.put(pixels[i] << 8 | pixels[i] >>> 24);

		byte_buffer.position(0);

		int[] id = new int[1];

		GLES20.glGenTextures(1, id, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byte_buffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrap);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrap);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		bitmap.recycle();

		return id[0];
	}

	public static int load_texture(int[] pixels, int width, int height, int wrap)
	{
		ByteBuffer byte_buffer = ByteBuffer.allocateDirect(width * height * 4);
		byte_buffer.order(ByteOrder.BIG_ENDIAN);

		IntBuffer int_buffer = byte_buffer.asIntBuffer();

		for (int i = 0; i < pixels.length; i++)
			int_buffer.put(pixels[i] << 8 | pixels[i] >>> 24);

		byte_buffer.position(0);

		int[] id = new int[1];

		GLES20.glGenTextures(1, id, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byte_buffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrap);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrap);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

		return id[0];
	}

	public static String load_text(Context c, int r)
	{
		InputStream is = c.getResources().openRawResource(r);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		try
		{
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line + '\n');
				line = br.readLine();
			}

			is.close();
			br.close();

			return sb.toString();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
}
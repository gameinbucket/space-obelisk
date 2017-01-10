package com.gameinbucket.render;

import android.opengl.GLES20;

public class FrameBuffer 
{
	private int[] frame = new int[1];
	private int[] texture = new int[1];

	public int width;
	public int height;

	public FrameBuffer(boolean depth, int filter, int w, int h)
	{
		width = w;
		height = h;

		load_buffer(filter, depth);
	}

	public int frame_buffer()
	{
		return frame[0];
	}

	public int texture()
	{
		return texture[0];
	}

	private void load_buffer(int filter, boolean depth)
	{
		GLES20.glGenFramebuffers(1, frame, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame[0]);

		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		if (depth)
		{
			int[] render_buffer = new int[1];
			GLES20.glGenRenderbuffers(1, render_buffer, 0);
			GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, render_buffer[0]);
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);

			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture[0], 0);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, render_buffer[0]);

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
		}
		else
		{
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture[0], 0);

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		}
	}
}
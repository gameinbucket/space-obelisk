package com.gameinbucket.render;

import com.gameinbucket.render.GeometryBuffer;

public abstract class Vertex 
{
	public static final int shortsize = 2;
	public static final int floatsize = 4;

	public static final String[] attribute_name = {"a_position", "a_color", "a_uv"};
	public static final int col;
	public static final int uv;
	public static final int[] attribute_size = {3, 4, 2};

	public static int[] offset;

	public static int stride;
	public static int stride_bytes;

	public static String font_chars = "abcdefghijklmnopqrstuvwxyz,;!?()+-.0123456789><:";
	public static int font_size_x = 4;
	public static int font_size_y = 6;
	public static float font_pixel_size_x = font_size_x / 64.0f;
	public static float font_pixel_size_y = font_size_y / 64.0f;
	public static int font_cell = 64 / font_size_x;

	private static float[] rectangle;

	static
	{
		stride = 0;
		stride_bytes = 0;

		offset = new int[attribute_size.length];

		for (int i = 0; i < attribute_size.length; i++)
		{
			for (int p = 0; p < i; p++)
				offset[i] += attribute_size[p];

			stride += attribute_size[i];
		}

		stride_bytes = stride * floatsize;

		col = offset[1];
		uv = offset[2];

		rectangle = vertices(4);
	}

	private Vertex()
	{

	}

	public static float[] vertices(int vert)
	{
		return new float[vert * stride];
	}

	public static float[][] vertices(int f, int vert)
	{
		return new float[f][vert * stride];
	}

	public static void vertex(float[] vert, int i, float x, float y, float r, float g, float b)
	{
		i *= stride;

		vert[i] = x;
		vert[i + 1] = y;
		vert[i + 2] = 0;
		vert[i + col] = r;
		vert[i + col + 1] = g;
		vert[i + col + 2] = b;
		vert[i + col + 3] = 1;
		vert[i + uv] = 0;
		vert[i + uv + 1] = 0;
	}

	public static void vertex(float[] vert, int i, float x, float y, float u, float v)
	{
		i *= stride;

		vert[i] = x;
		vert[i + 1] = y;
		vert[i + 2] = 0;
		vert[i + col] = 1;
		vert[i + col + 1] = 1;
		vert[i + col + 2] = 1;
		vert[i + col + 3] = 1;
		vert[i + uv] = u;
		vert[i + uv + 1] = v;
	}

	public static void translate(float[] vertices, float x, float y, float z)
	{
		for (int i = 0; i < vertices.length; i += stride)
		{
			vertices[i	  ] += x;
			vertices[i + 1] += y;
			vertices[i + 2] += z;
		}
	}

	public static void translate(float[] vertices, int start, int end, float x, float y)
	{
		for (int i = start; i < end; i += stride)
		{
			vertices[i	  ] += x;
			vertices[i + 1] += y;
		}
	}

	public static void rotate_z(float[] vertices, int start, int end, float rot)
	{
		float sine = (float)Math.sin(rot);
		float cosine = (float)Math.cos(rot);

		for (int i = start; i < end; i += stride)
		{
			float x = vertices[i] * cosine - vertices[i + 1] * sine;
			float y = vertices[i] * sine   + vertices[i + 1] * cosine;

			vertices[i] = x;
			vertices[i + 1] = y;
		}
	}

	public static void rotate_x(float[] vertices, float rot)
	{
		float sine = (float)Math.sin(rot);
		float cosine = (float)Math.cos(rot);

		for (int i = 0; i < vertices.length; i += stride)
		{
			float y = vertices[i + 1] * cosine - vertices[i + 2] * sine;
			float z = vertices[i + 1] * sine   + vertices[i + 2] * cosine;

			vertices[i + 1] = y;
			vertices[i + 2] = z;
		}
	}

	public static void rotate_y(float[] vertices, float rot)
	{
		float sine = (float)Math.sin(rot);
		float cosine = (float)Math.cos(rot);

		for (int i = 0; i < vertices.length; i += stride)
		{
			float x = vertices[i]     * cosine + vertices[i + 2] * sine;
			float z = vertices[i + 2] * cosine - vertices[i]     * sine;

			vertices[i] = x;
			vertices[i + 2] = z;
		}
	}

	public static void rotate_z(float[] vertices, float rot)
	{
		float sine = (float)Math.sin(rot);
		float cosine = (float)Math.cos(rot);

		for (int i = 0; i < vertices.length; i += stride)
		{
			float x = vertices[i] * cosine - vertices[i + 1] * sine;
			float y = vertices[i] * sine   + vertices[i + 1] * cosine;

			vertices[i] = x;
			vertices[i + 1] = y;
		}
	}

	public static void color(float[] vertices, float r, float g, float b, float a)
	{
		for (int i = 0; i < vertices.length; i += stride)
		{
			vertices[i + col    ] = r;
			vertices[i + col + 1] = g;
			vertices[i + col + 2] = b;
			vertices[i + col + 3] = a;
		}
	}

	public static void add_text(GeometryBuffer g, String text, float x, float y, float cr, float cg, float cb)
	{
		x = (int)x;
		y = (int)y;

		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) == ' ')
				continue;

			int charLocation = font_chars.indexOf(text.charAt(i));
			int ty = (int)Math.floor(charLocation / font_cell);
			int tx = (int)Math.floor(charLocation % font_cell);

			float top = font_pixel_size_y * ty;
			float bottom = font_pixel_size_y * (ty + 1);

			float left = font_pixel_size_x * tx;
			float right = font_pixel_size_x * (tx + 1);

			int pos_x = (int)(x + i * font_size_x);

			rectangle[0] = pos_x;
			rectangle[1] = y + font_size_y;
			rectangle[2] = 0;
			rectangle[3] = cr;
			rectangle[4] = cg;
			rectangle[5] = cb;
			rectangle[6] = 1;
			rectangle[7] = left;
			rectangle[8] = top;

			rectangle[9] = pos_x;
			rectangle[10] = y;
			rectangle[11] = 0;
			rectangle[12] = cr;
			rectangle[13] = cg;
			rectangle[14] = cb;
			rectangle[15] = 1;
			rectangle[16] = left;
			rectangle[17] = bottom;

			rectangle[18] = pos_x + font_size_x;
			rectangle[19] = y;
			rectangle[20] = 0;
			rectangle[21] = cr;
			rectangle[22] = cg;
			rectangle[23] = cb;
			rectangle[24] = 1;
			rectangle[25] = right;
			rectangle[26] = bottom;

			rectangle[27] = pos_x + font_size_x;
			rectangle[28] = y + font_size_y;
			rectangle[29] = 0;
			rectangle[30] = cr;
			rectangle[31] = cg;
			rectangle[32] = cb;
			rectangle[33] = 1;
			rectangle[34] = right;
			rectangle[35] = top;

			g.add(rectangle, 4);
		}
	}

	/*public static float[] sprite(float x, float y, int hw, int hh, float rad, float s, float t1x, float t1y, float t2x, float t2y)
	{
		float t = s * t1y;
		float b = s * t2y;

		float l = s * t1x;
		float r = s * t2x;

		float sine = (float)Math.sin(rad);
		float cosine = (float)Math.cos(rad);

		rectangle[0] = (int)(-hw * cosine - hh * sine   + x);
		rectangle[1] = (int)(-hw * sine   + hh * cosine + y);
		rectangle[2] = 0;
		rectangle[3] = 1;
		rectangle[4] = 1;
		rectangle[5] = 1;
		rectangle[6] = 1;
		rectangle[7] = l;
		rectangle[8] = t;

		rectangle[9] =  (int)(-hw * cosine + hh * sine   + x);
		rectangle[10] = (int)(-hw * sine   - hh * cosine + y);
		rectangle[11] = 0;
		rectangle[12] = 1;
		rectangle[13] = 1;
		rectangle[14] = 1;
		rectangle[15] = 1;
		rectangle[16] = l;
		rectangle[17] = b;

		rectangle[18] = (int)(hw * cosine + hh * sine   + x);
		rectangle[19] = (int)(hw * sine   - hh * cosine + y);
		rectangle[20] = 0;
		rectangle[21] = 1;
		rectangle[22] = 1;
		rectangle[23] = 1;
		rectangle[24] = 1;
		rectangle[25] = r;
		rectangle[26] = b;

		rectangle[27] = (int)(hw * cosine - hh * sine   + x);
		rectangle[28] = (int)(hw * sine   + hh * cosine + y);
		rectangle[29] = 0;
		rectangle[30] = 1;
		rectangle[31] = 1;
		rectangle[32] = 1;
		rectangle[33] = 1;
		rectangle[34] = r;
		rectangle[35] = t;

		return rectangle;
	}*/

	public static float[] sprite(float x, float y, int hw, int hh, float s, float t1x, float t1y, float t2x, float t2y, float cr, float cg, float cb)
	{
		float t = s * t1y;
		float b = s * t2y;

		float l = s * t1x;
		float r = s * t2x;

		x = (int)x; //gets rid of texture flicker
		y = (int)y;

		rectangle[0] = x - hw;
		rectangle[1] = y + hh;
		rectangle[2] = 0;
		rectangle[3] = cr;
		rectangle[4] = cg;
		rectangle[5] = cb;
		rectangle[6] = 1;
		rectangle[7] = l;
		rectangle[8] = t;

		rectangle[9] = x - hw;
		rectangle[10] = y - hh;
		rectangle[11] = 0;
		rectangle[12] = cr;
		rectangle[13] = cg;
		rectangle[14] = cb;
		rectangle[15] = 1;
		rectangle[16] = l;
		rectangle[17] = b;

		rectangle[18] = x + hw;
		rectangle[19] = y - hh;
		rectangle[20] = 0;
		rectangle[21] = cr;
		rectangle[22] = cg;
		rectangle[23] = cb;
		rectangle[24] = 1;
		rectangle[25] = r;
		rectangle[26] = b;

		rectangle[27] = x + hw;
		rectangle[28] = y + hh;
		rectangle[29] = 0;
		rectangle[30] = cr;
		rectangle[31] = cg;
		rectangle[32] = cb;
		rectangle[33] = 1;
		rectangle[34] = r;
		rectangle[35] = t;

		return rectangle;
	}

	public static float[] ambient(int x, int y, int rd, float s, float t1x, float t1y, float t2x, float t2y, float cr, float cg, float cb)
	{
		float t = s * t1y;
		float b = s * t2y;

		float l = s * t1x;
		float r = s * t2x;

		rectangle[0] = x;
		rectangle[1] = y + rd;
		rectangle[2] = 0;
		rectangle[3] = cr;
		rectangle[4] = cg;
		rectangle[5] = cb;
		rectangle[6] = 1;
		rectangle[7] = l;
		rectangle[8] = t;

		rectangle[9] = x;
		rectangle[10] = y;
		rectangle[11] = 0;
		rectangle[12] = cr;
		rectangle[13] = cg;
		rectangle[14] = cb;
		rectangle[15] = 1;
		rectangle[16] = l;
		rectangle[17] = b;

		rectangle[18] = x + rd;
		rectangle[19] = y;
		rectangle[20] = 0;
		rectangle[21] = cr;
		rectangle[22] = cg;
		rectangle[23] = cb;
		rectangle[24] = 1;
		rectangle[25] = r;
		rectangle[26] = b;

		rectangle[27] = x + rd;
		rectangle[28] = y + rd;
		rectangle[29] = 0;
		rectangle[30] = cr;
		rectangle[31] = cg;
		rectangle[32] = cb;
		rectangle[33] = 1;
		rectangle[34] = r;
		rectangle[35] = t;

		return rectangle;
	}

	public static float[] crectangle(float x, float y, float hw, float hh, float r, float g, float b)
	{
		rectangle[0] = x - hw;
		rectangle[1] = y + hh;
		rectangle[2] = 0;
		rectangle[3] = r;
		rectangle[4] = g;
		rectangle[5] = b;
		rectangle[6] = 1;
		rectangle[7] = 0;
		rectangle[8] = 1;

		rectangle[9] = x - hw;
		rectangle[10] = y - hh;
		rectangle[11] = 0;
		rectangle[12] = r;
		rectangle[13] = g;
		rectangle[14] = b;
		rectangle[15] = 1;
		rectangle[16] = 0;
		rectangle[17] = 0;

		rectangle[18] = x + hw;
		rectangle[19] = y - hh;
		rectangle[20] = 0;
		rectangle[21] = r;
		rectangle[22] = g;
		rectangle[23] = b;
		rectangle[24] = 1;
		rectangle[25] = 1;
		rectangle[26] = 0;

		rectangle[27] = x + hw;
		rectangle[28] = y + hh;
		rectangle[29] = 0;
		rectangle[30] = r;
		rectangle[31] = g;
		rectangle[32] = b;
		rectangle[33] = 1;
		rectangle[34] = 1;
		rectangle[35] = 1;

		return rectangle;
	}

	public static float[] rectangle(float x, float y, float w, float h, float r, float g, float b)
	{
		rectangle[0] = x;
		rectangle[1] = y + h;
		rectangle[2] = 0;
		rectangle[3] = r;
		rectangle[4] = g;
		rectangle[5] = b;
		rectangle[6] = 1;
		rectangle[7] = 0;
		rectangle[8] = 1;

		rectangle[9] = x;
		rectangle[10] = y;
		rectangle[11] = 0;
		rectangle[12] = r;
		rectangle[13] = g;
		rectangle[14] = b;
		rectangle[15] = 1;
		rectangle[16] = 0;
		rectangle[17] = 0;

		rectangle[18] = x + w;
		rectangle[19] = y;
		rectangle[20] = 0;
		rectangle[21] = r;
		rectangle[22] = g;
		rectangle[23] = b;
		rectangle[24] = 1;
		rectangle[25] = 1;
		rectangle[26] = 0;

		rectangle[27] = x + w;
		rectangle[28] = y + h;
		rectangle[29] = 0;
		rectangle[30] = r;
		rectangle[31] = g;
		rectangle[32] = b;
		rectangle[33] = 1;
		rectangle[34] = 1;
		rectangle[35] = 1;

		return rectangle;
	}

	public static float[] rectangle(float x, float y, float w, float h)
	{
		rectangle[0] = x;
		rectangle[1] = y + h;
		rectangle[2] = 0;
		rectangle[3] = 1;
		rectangle[4] = 1;
		rectangle[5] = 1;
		rectangle[6] = 1;
		rectangle[7] = 0;
		rectangle[8] = 1;

		rectangle[9] = x;
		rectangle[10] = y;
		rectangle[11] = 0;
		rectangle[12] = 1;
		rectangle[13] = 1;
		rectangle[14] = 1;
		rectangle[15] = 1;
		rectangle[16] = 0;
		rectangle[17] = 0;

		rectangle[18] = x + w;
		rectangle[19] = y;
		rectangle[20] = 0;
		rectangle[21] = 1;
		rectangle[22] = 1;
		rectangle[23] = 1;
		rectangle[24] = 1;
		rectangle[25] = 1;
		rectangle[26] = 0;

		rectangle[27] = x + w;
		rectangle[28] = y + h;
		rectangle[29] = 0;
		rectangle[30] = 1;
		rectangle[31] = 1;
		rectangle[32] = 1;
		rectangle[33] = 1;
		rectangle[34] = 1;
		rectangle[35] = 1;

		return rectangle;
	}
}
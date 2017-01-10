package com.gameinbucket.math;

public abstract class Matrix4x4 
{
	private static float[] ma = new float[16];
	private static float[] mb = new float[16];
	
	private Matrix4x4()
	{
		
	}

	public static void identity(float[] m0)
	{
		m0[0] = 1.0f;
		m0[1] = 0.0f;
		m0[2] = 0.0f;
		m0[3] = 0.0f;

		m0[4] = 0.0f;
		m0[5] = 1.0f;
		m0[6] = 0.0f;
		m0[7] = 0.0f;

		m0[8] = 0.0f;
		m0[9] = 0.0f;
		m0[10] = 1.0f;
		m0[11] = 0.0f;

		m0[12] = 0.0f;
		m0[13] = 0.0f;
		m0[14] = 0.0f;
		m0[15] = 1.0f;
	}

	public static void orthographic(float[] m0, float left, float right, float top, float bottom, float near, float far)
	{
		m0[0] = 2.0f / (right - left);
		m0[1] = 0.0f;
		m0[2] = 0.0f;
		m0[3] = 0.0f;

		m0[4] = 0.0f;
		m0[5] = 2.0f / (top - bottom);
		m0[6] = 0.0f;
		m0[7] = 0.0f;

		m0[8] = 0.0f;
		m0[9] = 0.0f;
		m0[10] = -2.0f / (far - near);
		m0[11] = 0.0f;

		m0[12] = -((right + left) / (right - left));
		m0[13] = -((top + bottom) / (top - bottom));
		m0[14] = -((far + near) / (far - near));
		m0[15] = 1.0f;
	}

	public static void perspective(float[] m0, float fov, float ar, float near, float far)
	{
		float top = near * (float)Math.tan(fov * Math.PI / 360.0f);
		float bottom = -top;
		float left = bottom * ar;
		float right = top * ar;

		m0[0] = (2.0f * near) / (right - left);
		m0[1] = 0.0f;
		m0[2] = 0.0f;
		m0[3] = 0.0f;

		m0[4] = 0.0f;
		m0[5] = (2.0f * near) / (top - bottom);
		m0[6] = 0.0f;
		m0[7] = 0.0f;

		m0[8] = (right + left) / (right - left);
		m0[9] = (top + bottom) / (top - bottom);
		m0[10] = -(far + near) / (far - near);
		m0[11] = -1.0f;

		m0[12] = 0.0f;
		m0[13] = 0.0f;
		m0[14] = -(2.0f * far * near) / (far - near);
		m0[15] = 0.0f;
	}

	public static void translate(float[] m0, float x, float y, float z)
	{
		m0[12] = x * m0[0] + y * m0[4] + z * m0[8] + m0[12];
		m0[13] = x * m0[1] + y * m0[5] + z * m0[9] + m0[13];
		m0[14] = x * m0[2] + y * m0[6] + z * m0[10] + m0[14];
		m0[15] = x * m0[3] + y * m0[7] + z * m0[11] + m0[15];
	}

	public static void rotate_x(float[] m0, float r)
	{
		ma[0] = 1.0f;
		ma[1] = 0.0f;
		ma[2] = 0.0f;
		ma[3] = 0.0f;

		ma[4] = 0.0f;
		ma[5] = (float)Math.cos(r);
		ma[6] = (float)Math.sin(r);
		ma[7] = 0.0f;

		ma[8] = 0.0f;
		ma[9] = -(float)Math.sin(r);
		ma[10] = (float)Math.cos(r);
		ma[11] = 0.0f;

		ma[12] = 0.0f;
		ma[13] = 0.0f;
		ma[14] = 0.0f;
		ma[15] = 1.0f;

		Matrix4x4.multiply(m0, ma);
	}

	public static void rotate_y(float[] m0, float r)
	{
		ma[0] = (float)Math.cos(r);
		ma[1] = 0.0f;
		ma[2] = -(float)Math.sin(r);
		ma[3] = 0.0f;

		ma[4] = 0.0f;
		ma[5] = 1.0f;
		ma[6] = 0.0f;
		ma[7] = 0.0f;

		ma[8] = (float)Math.sin(r);
		ma[9] = 0.0f;
		ma[10] = (float)Math.cos(r);
		ma[11] = 0.0f;

		ma[12] = 0.0f;
		ma[13] = 0.0f;
		ma[14] = 0.0f;
		ma[15] = 1.0f;

		Matrix4x4.multiply(m0, ma);
	}

	public static void rotate_z(float[] m0, float r)
	{
		ma[0] = (float)Math.cos(r);
		ma[1] = (float)Math.sin(r);
		ma[2] = 0.0f;
		ma[3] = 0.0f;

		ma[4] = -(float)Math.sin(r); 
		ma[5] = (float)Math.cos(r);
		ma[6] = 0.0f;
		ma[7] = 0.0f;

		ma[8] = 0.0f;
		ma[9] = 0.0f;
		ma[10] = 1.0f;
		ma[11] = 0.0f;

		ma[12] = 0.0f;
		ma[13] = 0.0f;
		ma[14] = 0.0f;
		ma[15] = 1.0f;

		Matrix4x4.multiply(m0, ma);
	}

	public static void multiply(float[] m0, float[] m1)
	{
		mb[0] = m0[0];
		mb[1] = m0[1];
		mb[2] = m0[2];
		mb[3] = m0[3];

		mb[4] = m0[4];
		mb[5] = m0[5];
		mb[6] = m0[6];
		mb[7] = m0[7];

		mb[8] = m0[8];
		mb[9] = m0[9];
		mb[10] = m0[10];
		mb[11] = m0[11];

		mb[12] = m0[12];
		mb[13] = m0[13];
		mb[14] = m0[14];
		mb[15] = m0[15];

		m0[0] = mb[0] * m1[0] + mb[4] * m1[1] + mb[8] * m1[2] + mb[12] * m1[3];
		m0[1] = mb[1] * m1[0] + mb[5] * m1[1] + mb[9] * m1[2] + mb[13] * m1[3];
		m0[2] = mb[2] * m1[0] + mb[6] * m1[1] + mb[10] * m1[2] + mb[14] * m1[3];
		m0[3] = mb[3] * m1[0] + mb[7] * m1[1] + mb[11] * m1[2] + mb[15] * m1[3];

		m0[4] = mb[0] * m1[4] + mb[4] * m1[5] + mb[8] * m1[6] + mb[12] * m1[7];
		m0[5] = mb[1] * m1[4] + mb[5] * m1[5] + mb[9] * m1[6] + mb[13] * m1[7];
		m0[6] = mb[2] * m1[4] + mb[6] * m1[5] + mb[10] * m1[6] + mb[14] * m1[7];
		m0[7] = mb[3] * m1[4] + mb[7] * m1[5] + mb[11] * m1[6] + mb[15] * m1[7];

		m0[8] = mb[0] * m1[8] + mb[4] * m1[9] + mb[8] * m1[10] + mb[12] * m1[11];
		m0[9] = mb[1] * m1[8] + mb[5] * m1[9] + mb[9] * m1[10] + mb[13] * m1[11];
		m0[10] = mb[2] * m1[8] + mb[6] * m1[9] + mb[10] * m1[10] + mb[14] * m1[11];
		m0[11] = mb[3] * m1[8] + mb[7] * m1[9] + mb[11] * m1[10] + mb[15] * m1[11];

		m0[12] = mb[0] * m1[12] + mb[4] * m1[13] + mb[8] * m1[14] + mb[12] * m1[15];
		m0[13] = mb[1] * m1[12] + mb[5] * m1[13] + mb[9] * m1[14] + mb[13] * m1[15];
		m0[14] = mb[2] * m1[12] + mb[6] * m1[13] + mb[10] * m1[14] + mb[14] * m1[15];
		m0[15] = mb[3] * m1[12] + mb[7] * m1[13] + mb[11] * m1[14] + mb[15] * m1[15];
	}

	public static void multiply(float[] m0, float[] m1, float[] m2)
	{
		m0[0] = m1[0] * m2[0] + m1[4] * m2[1] + m1[8] * m2[2] + m1[12] * m2[3];
		m0[1] = m1[1] * m2[0] + m1[5] * m2[1] + m1[9] * m2[2] + m1[13] * m2[3];
		m0[2] = m1[2] * m2[0] + m1[6] * m2[1] + m1[10] * m2[2] + m1[14] * m2[3];
		m0[3] = m1[3] * m2[0] + m1[7] * m2[1] + m1[11] * m2[2] + m1[15] * m2[3];

		m0[4] = m1[0] * m2[4] + m1[4] * m2[5] + m1[8] * m2[6] + m1[12] * m2[7];
		m0[5] = m1[1] * m2[4] + m1[5] * m2[5] + m1[9] * m2[6] + m1[13] * m2[7];
		m0[6] = m1[2] * m2[4] + m1[6] * m2[5] + m1[10] * m2[6] + m1[14] * m2[7];
		m0[7] = m1[3] * m2[4] + m1[7] * m2[5] + m1[11] * m2[6] + m1[15] * m2[7];

		m0[8] = m1[0] * m2[8] + m1[4] * m2[9] + m1[8] * m2[10] + m1[12] * m2[11];
		m0[9] = m1[1] * m2[8] + m1[5] * m2[9] + m1[9] * m2[10] + m1[13] * m2[11];
		m0[10] = m1[2] * m2[8] + m1[6] * m2[9] + m1[10] * m2[10] + m1[14] * m2[11];
		m0[11] = m1[3] * m2[8] + m1[7] * m2[9] + m1[11] * m2[10] + m1[15] * m2[11];

		m0[12] = m1[0] * m2[12] + m1[4] * m2[13] + m1[8] * m2[14] + m1[12] * m2[15];
		m0[13] = m1[1] * m2[12] + m1[5] * m2[13] + m1[9] * m2[14] + m1[13] * m2[15];
		m0[14] = m1[2] * m2[12] + m1[6] * m2[13] + m1[10] * m2[14] + m1[14] * m2[15];
		m0[15] = m1[3] * m2[12] + m1[7] * m2[13] + m1[11] * m2[14] + m1[15] * m2[15];
	}
}

package com.gameinbucket.math;

public class Vector2
{
	public float x;
	public float y;

	public Vector2()
	{
		x = 0;
		y = 0;
	}

	public Vector2(float ix, float iy)
	{
		x = ix;
		y = iy;
	}

	public Vector2(Vector2 v)
	{
		x = v.x;
		y = v.y;
	}

	public void copy(Vector2 v)
	{
		x = v.x;
		y = v.y;
	}

	public void clear()
	{
		x = 0;
		y = 0;
	}

	public void invert()
	{
		x = -x;
		y = -y;
	}

	public void perpendicular()
	{
		float t = x;

		x = -y;
		y = t;
	}

	public float magnitude()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

	public float sq_magnitude()
	{
		return x * x + y * y;
	}

	public void normalize()
	{
		float m = magnitude();

		if (m > 0.0f)
			multiply(1.0f / m);
	}

	public void multiply(float v)
	{
		x *= v;
		y *= v;
	}

	public void multiply(Vector2 v)
	{
		x *= v.x;
		y *= v.y;
	}

	public void multiply(Vector2 v, float s)
	{
		x *= v.x * s;
		y *= v.y * s;
	}

	public void add(float ax, float ay)
	{
		x += ax;
		y += ay;
	}

	public void add(float ax, float ay, float s)
	{
		x += ax * s;
		y += ay * s;
	}

	public void add(Vector2 v)
	{
		x += v.x;
		y += v.y;
	}

	public void add(Vector2 v, float s)
	{
		x += v.x * s;
		y += v.y * s;
	}

	public void subtract(float ax, float ay)
	{
		x -= ax;
		y -= ay;
	}

	public void subtract(float ax, float ay, float s)
	{
		x -= ax * s;
		y -= ay * s;
	}

	public void subtract(Vector2 v)
	{
		x -= v.x;
		y -= v.y;
	}

	public void subtract(Vector2 v, float s)
	{
		x -= v.x * s;
		y -= v.y * s;
	}

	public float dot(Vector2 v)
	{
		return x * v.x + y * v.y;
	}
}

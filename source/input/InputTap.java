package com.gameinbucket.input;

public class InputTap 
{
	static protected final float cr = 0.7490f;
	static protected final float cg = 0.7970f;
	static protected final float cb = 0.5411f;
	
	protected int bound_top;
	protected int bound_bottom;
	protected int bound_left;
	protected int bound_right;

	protected int id;

	public InputTap(int left, int bottom, int right, int top)
	{
		bound_top = top;
		bound_bottom = bottom;
		bound_left = left;
		bound_right = right;

		reset();
	}

	protected void reset()
	{
		id = -1;
	}

	public void press(float x, float y, int i)
	{
		if (id == -1 && y >= bound_bottom && y <= bound_top && x >= bound_left && x <= bound_right)
			id = i;
	}

	public void release(int i)
	{
		if (id == i)
			reset();
	}

	public void release()
	{
		reset();
	}

	public boolean is_active()
	{
		if (id > -1)
			return true;

		return false;
	}
}

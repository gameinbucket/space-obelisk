package com.gameinbucket.input;

import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;

public class InputMove extends InputTap
{
	protected static final float distance_limit = 40.0f;

	private float origin_x;
	private float origin_y;

	private float move_x;
	private float move_y;

	private int first_x;
	private int first_y;

	private int rhw;
	private int rhh;

	public float distance;
	public float angle;

	public InputMove(int left, int bottom, int right, int top, int x, int y, int rwi, int rhe)
	{
		super(left, bottom, right, top);

		first_x = x;
		first_y = y;

		rhw = rwi / 2;
		rhh = rhe / 2;
	}

	public void update(int x, int y, int rwi, int rhe)
	{
		first_x = x;
		first_y = y;

		rhw = rwi / 2;
		rhh = rhe / 2;
	}

	protected void reset()
	{
		id = -1;
		distance = 0;
	}

	public void press(float x, float y, int i)
	{
		if (id == -1 && y >= bound_bottom && y <= bound_top && x >= bound_left && x <= bound_right)
		{
			origin_x = x;
			origin_y = y;

			move_x = x;
			move_y = y;

			id = i;
		}
	}

	public void move(float x, float y, int i)
	{
		if (id == i)
		{
			distance = (origin_x - x) * (origin_x - x) + (origin_y - y) * (origin_y - y);

			move_x = x;
			move_y = y;

			angle = (float)Math.atan2(origin_y - y, origin_x - x);
		}
	}

	public void render(GeometryBuffer g)
	{
		if (id > -1)
		{
			g.add(Vertex.sprite(origin_x, origin_y, rhw, rhh, 0.25f, 0, 1, 1, 2, cr, cg, cb), 4);
			g.add(Vertex.sprite(move_x, move_y, rhw, rhh, 0.25f, 0, 0, 1, 1, cr, cg, cb), 4);
		}
		else
		{
			g.add(Vertex.sprite(first_x, first_y, rhw, rhh, 0.25f, 0, 0, 1, 1, cr, cg, cb), 4);
		}
	}
}
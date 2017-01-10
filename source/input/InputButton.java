package com.gameinbucket.input;

import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;

public class InputButton
{
	private int posx;
	private int posy;

	private int hw;
	private int hh;

	private int rhw;
	private int rhh;

	private int id;

	public InputButton(int x, int y, int wi, int he, int rwi, int rhe)
	{
		posx = x;
		posy = y;

		hw = wi / 2;
		hh = he / 2;

		rhw = rwi / 2;
		rhh = rhe / 2;

		id = -1;
	}

	public void update(int x, int y, int wi, int he, int rwi, int rhe)
	{
		posx = x;
		posy = y;

		hw = wi / 2;
		hh = he / 2;

		rhw = rwi / 2;
		rhh = rhe / 2;
	}

	public void press(float xx, float yy, int i)
	{
		if (id > -1)
			return;

		if (xx >= posx - hw && xx <= posx + hw && yy >= posy - hh && yy <= posy + hh)
			id = i;
	}

	public void release(int i)
	{
		if (id == i)
			id = -1;
	}

	public void release()
	{
		id = -1;
	}

	public boolean is_active()
	{
		if (id > -1)
			return true;

		return false;
	}

	public void render(GeometryBuffer g)
	{
		g.add(Vertex.sprite(posx, posy, rhw, rhh, 0.25f, 0, 0, 1, 1, InputTap.cr, InputTap.cg, InputTap.cb), 4);
	}
}
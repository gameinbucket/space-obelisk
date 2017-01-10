package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;
import com.gameinbucket.spaceobelisk.WorldExecutor;

public abstract class Sprite
{
	protected final WorldExecutor world;
	protected final SpriteSheet sheet;

	public final int hw;
	public final int hh;

	protected final int rw;
	protected final int rh;

	public float posx;
	public float posy;

	public float velx;
	public float vely;

	public int look;

	protected int tex0;
	protected int tex1;
	protected int tex2;
	protected int tex3;
	
	public final float cr;
	public final float cg;
	public final float cb;

	protected int frame;
	protected final ArrayList<Sprite> l;

	public boolean remove;

	public Sprite(WorldExecutor wo, SpriteSheet s, float x, float y, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li)
	{
		world = wo;
		sheet = s;

		hw = wi / 2;
		hh = he / 2;

		rw = rwi / 2;
		rh = rhe / 2;
		
		cr = br;
		cg = bg;
		cb = bb;

		posx = x;
		posy = y;

		velx = 0;
		vely = 0;

		look = 1;
		frame = f;

		l = li;
		l.add(this);

		remove = false;
	}

	public abstract boolean integrate(float time);

	public void look(int l)
	{
		if (look == l)
			return;

		look = l;
		texture_position();
	}

	protected void texture_position()
	{
		int x = frame % sheet.columns;
		int y = frame / sheet.columns;

		tex1 = y;
		tex3 = y + 1;

		switch(look)
		{
		case 1:
			tex0 = x;
			tex2 = x + 1;
			break;
		case -1:
			tex0 = x + 1;
			tex2 = x;
			break;
		}
	}

	public void remove(int d)
	{
		remove = true;
		l.remove(this);
	}

	public float sprite_radian(Sprite other)
	{		
		return (float)Math.atan2(other.posy - posy, other.posx - posx);
	}

	public float sprite_distance(Sprite other)
	{
		float dist_x = other.posx - posx;
		float dist_z = other.posy - posy;

		return (float)Math.sqrt(dist_x * dist_x + dist_z * dist_z);
	}

	public boolean sprite_overlap(Sprite other)
	{
		if (posx + hw >= other.posx - other.hw && posx - hw <= other.posx + other.hw && posy + hh >= other.posy - other.hh && posy - hh <= other.posy + other.hh)
			return true;

		return false;
	}

	protected void grid_overlapx(float v)
	{
		int x0;

		if (v > 0) x0 = (int)((posx + v + hw) / WorldExecutor.grid_scale);
		else x0 = (int)((posx + v - hw) / WorldExecutor.grid_scale);

		int y0 = (int)((posy - hh + 0.01f) / WorldExecutor.grid_scale);
		int y1 = (int)((posy + hh - 0.01f) / WorldExecutor.grid_scale);

		if (world.cell(x0, y0) == 1 || world.cell(x0, y1) == 1)
		{
			if (v > 0) posx += x0 * WorldExecutor.grid_scale - posx - hw;
			else posx += (x0 + 1) * WorldExecutor.grid_scale - posx + hw;

			velx = 0;
		}
	}

	protected void grid_overlapy(float v)
	{
		int x0 = (int)(posx / WorldExecutor.grid_scale);
		int x1 = (int)((posx - hw + 0.01f) / WorldExecutor.grid_scale);
		int x2 = (int)((posx + hw - 0.01f) / WorldExecutor.grid_scale);

		int y0;

		if (v > 0) y0 = (int)((posy + v + hh) / WorldExecutor.grid_scale);
		else y0 = (int)((posy + v - hh) / WorldExecutor.grid_scale);

		if (world.cell(x0, y0) == 1 || world.cell(x1, y0) == 1 || world.cell(x2, y0) == 1)
		{
			if (v > 0) posy += y0 * WorldExecutor.grid_scale - posy - hh;
			else posy += (y0 + 1) * WorldExecutor.grid_scale - posy + hh;

			vely = 0;
		}
	}

	public void render(GeometryBuffer g)
	{
		g.add(Vertex.sprite(posx, posy, rw, rh, sheet.scale, tex0, tex1, tex2, tex3, cr, cg, cb), 4);
	}
}
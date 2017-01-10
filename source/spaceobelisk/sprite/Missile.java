package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class Missile extends Sprite
{
	private static int[] p3 = {3};
	private static int[] p4 = {6};
	private static int[] p7 = {13, 14, 15, 14};
	private static int[] p8 = {8};
	private static int[] p17 = {17, 18, 19};
	private static int[] p24 = {24, 25, 26};
	private static int[] p12 = {12, 11, 10};
	private static int[] p5 = {5};
	private static int[] p32 = {32, 33, 34};

	protected final Adversary o;
	protected final ArrayList<Sprite> c;
	protected final int damage;

	protected final int[] a;
	protected int frame_increment;

	public Missile(WorldExecutor wo, SpriteSheet s, float x, float y, float v, float t, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li, ArrayList<Sprite> collides, int d, Adversary origin)
	{
		super(wo, s, x, y, wi, he, rwi, rhe, br, bg, bb, f, li);

		velx = v * (float)Math.cos(t);
		vely = v * (float)Math.sin(t);

		o = origin;
		c = collides;
		damage = d;

		frame = 0;
		frame_increment = 0;

		if (f == 7) a = p7;
		else if (f == 4) a = p4;
		else if (f == 8) a = p8;
		else if (f == 17) a = p17;
		else if (f == 24) a = p24;
		else if (f == 12) a = p12;
		else if (f == 5) a = p5;
		else if (f == 32) a = p32;
		else a = p3;

		if (velx < 0) look = -1;
		else look = 1;

		texture_position();
	}

	public void remove(int d)
	{
		remove = true;
		l.remove(this);

		if (a == p3)
		{
			if (d == 0) world.sfx.missilee(posx, posy, cr, cg, cb, look);
			else
			{
				world.sfx.smalle(posx, posy, cr, cg, cb);
				world.audio.play(R.raw.a_laser1);
			}
		}
		else
			world.sfx.smalle(posx, posy, cr, cg, cb);
	}

	protected void texture_position()
	{
		int x = a[frame] % sheet.columns;
		int y = a[frame] / sheet.columns;

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

	public boolean integrate(float time)
	{
		for (int i = 0; i < c.size(); i++)
		{
			if (c.get(i).sprite_overlap(this))
			{
				Sprite s = c.get(i);
				s.remove(damage);

				if (s.remove) {i--; if (o != null) o.target(null);}
				else {if (o != null) o.target(c.get(i));}

				remove(1);

				return remove;
			}
		}

		boundaries();
		grid_overlap();

		posx += velx * time;
		posy += vely * time;

		next_frame();

		return remove;
	}

	protected void boundaries()
	{
		if (posx - hw < 0)
		{
			posx = hw;
			remove(0);
		}
		else if (posy - hh < 0)
		{
			posy = hh;
			remove(0);
		}
	}

	protected void grid_overlap()
	{
		int x0 = (int)((posx + hw) / WorldExecutor.grid_scale);
		int x1 = (int)((posx - hw) / WorldExecutor.grid_scale);

		int y0 = (int)((posy - hh) / WorldExecutor.grid_scale);
		int y1 = (int)((posy + hh) / WorldExecutor.grid_scale);

		if (world.cell(x0, y0) == 1 || world.cell(x0, y1) == 1)
		{
			if (Math.abs(velx) > Math.abs(vely))
				posx += x0 * WorldExecutor.grid_scale - posx - hw - 1;

			remove(0);
		}
		else if (world.cell(x1, y0) == 1 || world.cell(x1, y1) == 1)
		{
			if (Math.abs(velx) > Math.abs(vely))
				posx += (x1 + 1) * WorldExecutor.grid_scale - posx + hw + 1;

			remove(0);
		}
	}

	protected void next_frame()
	{
		frame_increment++;

		if (frame_increment == 5)
		{
			frame++;
			frame_increment = 0;

			if (frame > a.length - 1)
				frame = 0;

			texture_position();
		}
	}
}
package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.WorldExecutor;

public class Sfx extends Sprite
{
	private static int[] p4 = {10, 11, 12};
	private static int[] p8 = {9};
	private static int[] p13 = {10, 13, 14, 15, 17};

	private final int[] a;
	private int frame_increment;
	private int frame_mod;

	public Sfx(WorldExecutor wo, SpriteSheet s, float x, float y, int rwi, int rhe, float br, float bg, float bb, int f, int l, int fm)
	{
		super(wo, s, x, y, 0, 0, rwi, rhe, br, bg, bb, f, wo.sfxs);

		frame = 0;
		frame_increment = 0;
		frame_mod = fm;

		if (f == 13) a = p13;
		else if (f == 8) a = p8;
		else a = p4;

		look = l;
		texture_position();
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
		next_frame();

		if (frame == 0 && frame_increment == 0)
			return true;
		else
			return false;
	}

	protected void next_frame()
	{
		frame_increment++;

		if (frame_increment == frame_mod)
		{
			frame++;
			frame_increment = 0;

			if (frame > a.length - 1)
				frame = 0;

			texture_position();
		}
	}
}
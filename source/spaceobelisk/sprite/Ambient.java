package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;

public class Ambient
{
	private static int[] p0 = {5, 6, 7, 8, 9, 10, 11, 12};
	private static int[] p1 = {13, 14, 15, 16, 17, 18, 19, 20};
	private static int[] p2 = {32, 33, 34, 35};

	private final SpriteSheet sheet;

	public final int posx;
	public final int posy;

	private final int rd;

	private int tex0;
	private int tex1;
	private int tex2;
	private int tex3;
	
	private final float cr;
	private final float cg;
	private final float cb;

	private final int[] a;

	private int frame;
	private int frame_increment;

	private final int look;

	public Ambient(SpriteSheet s, int x, int y, int r, int f, int fr, int l, float br, float bg, float bb)
	{
		sheet = s;

		posx = x;
		posy = y;

		rd = r;
		
		cr = br;
		cg = bg;
		cb = bb;

		frame = fr;
		frame_increment = 0;

		if (f == 1) a = p1;
		else if (f == 2) a = p2;
		else a = p0;

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

	public void next_frame()
	{
		frame_increment++;

		if (frame_increment == 7)
		{
			frame++;
			frame_increment = 0;

			if (frame > a.length - 1)
				frame = 0;

			texture_position();
		}
	}

	public void render(GeometryBuffer g)
	{
		g.add(Vertex.ambient(posx, posy, rd, sheet.scale, tex0, tex1, tex2, tex3, cr, cg, cb), 4);
	}
}
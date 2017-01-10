package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AttPortal;

public class Portal extends Adversary
{
	private static final int[] a = {20, 21, 22, 23};

	private int k;
	protected int frame_increment = 0;

	public Portal(WorldExecutor w, float x, float y, float br, float bg, float bb, int key)
	{
		super(w, w.sprites, x, y, 8, 16, 8, 16, br, bg, bb, 0, w.enemies, 22);
		attach_accelement(null);
		attach_attelement(new AttPortal(this));

		k = key;
	}

	public void remove(int d)
	{
		hp -= d;

		if (hp < 1)
		{
			remove = true;
			l.remove(this);

			world.sfx.circlee(posx, posy, cr, cg, cb);
			
			if (!world.kused[k])
			{
				world.kused[k] = true;
				world.message.set(WorldExecutor.c3);
			}
		}

		world.viewoffsety += 2;
	}

	public boolean integrate(float time)
	{
		att.integrate(time);
		next_frame();

		return remove;
	}

	protected void texture_position()
	{
		int x = a[frame] % sheet.columns;
		int y = a[frame] / sheet.columns;

		tex1 = y;
		tex3 = y + 2;

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

	protected void next_frame()
	{
		frame_increment++;

		if (frame_increment == 10)
		{
			frame++;
			frame_increment = 0;

			if (frame > a.length - 1)
				frame = 0;

			texture_position();
		}
	}
}
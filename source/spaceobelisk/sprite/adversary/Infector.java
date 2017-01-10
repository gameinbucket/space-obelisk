package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AccInfector;
import com.gameinbucket.spaceobelisk.sprite.AttInfector;

public class Infector extends Adversary
{
	private int spark;

	public Infector(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 6, 6, 8, 8, 0.8941f, 0.4156f, 0.1098f, 2, w.enemies, 2);
		attach_accelement(new AccInfector(this));
		attach_attelement(new AttInfector(this));

		spark = 0;
	}

	public boolean integrate(float time)
	{
		spark++;

		if (spark == 10)
		{
			world.ptc.spark((int)posx, (int)posy, cr, cg, cb);
			spark = 0;
		}

		return super.integrate(time);
	}
}
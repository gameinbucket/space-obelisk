package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.math.MathUtil;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttPortal extends Element
{
	private static final float offsetrad = MathUtil.radian(5);

	private float att_time;
	private boolean direct;

	public AttPortal(Adversary adv)
	{
		super(adv);

		att_time = 1000 + WorldExecutor.frandom.nextInt(500);
		direct = false;
	}

	public void integrate(float time)
	{
		if (att_time > 0)
			att_time -= time;
		else
			attack0();
	}

	private void attack0()
	{
		float mrad = a.sprite_radian(a.world.you);
		int look = Math.cos(mrad) > 0 ? 1 : -1 * 5;

		if (direct)
		{
			Missile m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.07258f, mrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 17, a.world.emissiles, a.world.allies, 1, a);
			a.world.sprites.add(m);
		}
		else
		{
			Missile m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.06258f, mrad - offsetrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
			a.world.sprites.add(m);

			m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.06258f, mrad + offsetrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
			a.world.sprites.add(m);
		}

		att_time = 1000 + WorldExecutor.frandom.nextInt(500);
		direct = !direct;
	}
}
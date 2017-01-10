package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttInfector extends Element
{
	private float att_time;

	public AttInfector(Adversary adv)
	{
		super(adv);
		att_time = 600 + WorldExecutor.frandom.nextInt(500);
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
		Missile m = new Missile(a.world, a.world.sprites, a.posx, a.posy, 0.06258f, a.sprite_radian(a.world.you), 5, 5, 8, 8, a.cr, a.cg, a.cb, 4, a.world.emissiles, a.world.allies, 1, a);
		a.world.sprites.add(m);

		att_time = 600 + WorldExecutor.frandom.nextInt(500);
	}
}
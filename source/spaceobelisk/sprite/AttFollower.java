package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttFollower extends Element
{
	private float att_time;

	public AttFollower(Adversary adv)
	{
		super(adv);
		att_time = 900 + WorldExecutor.frandom.nextInt(500);
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
		Missile m = new Missile(a.world, a.world.sprites, a.posx - 2 * a.look, a.posy + 2, -0.06258f * a.look, 0, 5, 3, 8, 8, a.cr, a.cg, a.cb, 7, a.world.emissiles, a.world.allies, 1, a);
		a.world.sprites.add(m);

		att_time = 900 + WorldExecutor.frandom.nextInt(500);
	}
}
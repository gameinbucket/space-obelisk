package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttWarMachine extends Element
{
	private float att_time;
	private byte atype;

	public AttWarMachine(Adversary adv)
	{
		super(adv);

		att_time = 900 + WorldExecutor.frandom.nextInt(1000);
		atype = 0;
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
		if (atype == 0) 
		{
			if (Math.abs(Math.cos(a.sprite_radian(a.world.you))) > Math.abs(Math.sin(a.sprite_radian(a.world.you))))
				a.world.sprites.add(new MissileCluster(a.world, a.world.sprites, a.posx - 2 * a.look, a.posy + 2, -0.06258f * a.look, 0, 3, 3, 8, 8, a.cr, a.cg, a.cb, 5, a.world.emissiles, a.world.allies, 1, a));
			else
				a.world.sprites.add(new MissileCluster(a.world, a.world.sprites, a.posx, a.posy - 2, -0.06258f, 1.57f, 3, 3, 8, 8, a.cr, a.cg, a.cb, 5, a.world.emissiles, a.world.allies, 1, a));
		}
		else if (atype == 1 || atype == 2)
			a.world.sprites.add(new Missile(a.world, a.world.sprites, a.posx - 2 * a.look, a.posy + 2, -0.06258f * a.look, 0, 5, 3, 8, 8, a.cr, a.cg, a.cb, 7, a.world.emissiles, a.world.allies, 1, a));

		att_time = 800 + WorldExecutor.frandom.nextInt(1500);

		atype++;

		if (atype == 3)
			atype = 0;
	}
}
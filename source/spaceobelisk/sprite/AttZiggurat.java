package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;
import com.gameinbucket.spaceobelisk.sprite.adversary.Infector;

public class AttZiggurat extends Element
{
	private float att_time;
	private byte atype;

	public AttZiggurat(Adversary adv)
	{
		super(adv);

		att_time = 1000 + WorldExecutor.frandom.nextInt(500);
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
		float mrad = a.sprite_radian(a.world.you);
		int look = Math.cos(mrad) > 0 ? 1 : -1 * 5;

		if (atype < 3)
		{
			Missile m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.07258f, mrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 17, a.world.emissiles, a.world.allies, 1, a);
			a.world.sprites.add(m);

			att_time = 500 + WorldExecutor.frandom.nextInt(500);
		}
		else if (atype == 3)
		{
			MissileHold m = new MissileHold(a.world, a.world.sprites, a.posx, a.posy + a.hh + 3, 0.056f, 
					(float)Math.atan2(a.world.you.posy - a.posy - a.hh - 4, a.world.you.posx - a.posx), 7, 7, 8, 8, a.cr, a.cg, a.cb, 12, a.world.emissiles, a.world.allies, 2, a);

			a.world.sprites.add(m);
			a.world.audio.play(R.raw.a_jump1);

			att_time = 1700 + WorldExecutor.frandom.nextInt(1000);
		}
		else
		{
			Infector s = new Infector(a.world, a.posx, a.posy + a.hh + 1);
			a.world.sprites.add(s);

			att_time = 1500 + WorldExecutor.frandom.nextInt(500);
		}

		atype++;

		if (atype == 5)
			atype = 0;
	}
}
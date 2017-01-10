package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.math.MathUtil;
import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttObelisk extends Element
{
	private static final float offsetrad = MathUtil.radian(10);
	private static final float offsetrad2 = MathUtil.radian(9);

	private float att_time;
	private byte atype;

	private float franticradian;
	private float franstici;
	private int frantics;

	public AttObelisk(Adversary adv)
	{
		super(adv);

		att_time = 1010 + WorldExecutor.frandom.nextInt(500);
		atype = 0;

		franticradian = 0;
		franstici = 0;
		frantics = 1;
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
		if (a.hp < 20)
		{
			if (franstici == 0)
				franticradian = (float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx) - 10 * frantics * offsetrad2;

			Missile m = new Missile(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.06258f, franticradian, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
			a.world.sprites.add(m);

			if (franstici == 20)
			{
				MissileHold m1 = new MissileHold(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.056f, 
						(float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx), 7, 7, 8, 8, a.cr, a.cg, a.cb, 12, a.world.emissiles, a.world.allies, 2, a);

				a.world.sprites.add(m1);
				a.world.audio.play(R.raw.a_jump1);

				franstici = 0;
				frantics *= -1;
				att_time = 1210 + WorldExecutor.frandom.nextInt(500);
			}
			else
			{
				if (franstici == 10)
				{
					MissileSeeker m1 = new MissileSeeker(a.world, a.world.sprites, a.posx, a.posy, 0.00042f, 6, 6, 8, 8, a.cr, a.cg, a.cb, 8, a.world.emissiles, a.world.allies, 1, a);
					a.world.sprites.add(m1);
				}
				
				if (franstici % 5 == 0)
				{
					MissileHold m1 = new MissileHold(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.056f, 
							(float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx), 7, 7, 8, 8, a.cr, a.cg, a.cb, 12, a.world.emissiles, a.world.allies, 2, a);

					a.world.sprites.add(m1);
					a.world.audio.play(R.raw.a_jump1);
				}

				franticradian += offsetrad2 * frantics;
				franstici++;

				att_time = 180;
			}
		}
		else if (a.hp < 40)
		{
			if (franstici == 5)
			{
				float mrad = (float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx);
				a.world.sprites.add(new MissileCluster(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.06258f, mrad + offsetrad, 3, 3, 8, 8, a.cr, a.cg, a.cb, 5, a.world.emissiles, a.world.allies, 1, a));
				a.world.sprites.add(new MissileCluster(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.06258f, mrad - offsetrad, 3, 3, 8, 8, a.cr, a.cg, a.cb, 5, a.world.emissiles, a.world.allies, 1, a));
				
				franstici = 0;
			}
			else
				franstici++;
			
			MissileHold m = new MissileHold(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.056f, 
					(float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx), 7, 7, 8, 8, a.cr, a.cg, a.cb, 12, a.world.emissiles, a.world.allies, 2, a);
			
			a.world.sprites.add(m);
			a.world.audio.play(R.raw.a_jump1);
			
			att_time = 400 + WorldExecutor.frandom.nextInt(500);
		}
		else
		{
			if (atype == 0)
			{
				float mrad = a.sprite_radian(a.world.you);
				int look = Math.cos(mrad) > 0 ? 1 : -1 * 5;

				Missile m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.06258f, mrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
				a.world.sprites.add(m);

				m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.06258f, mrad + offsetrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
				a.world.sprites.add(m);

				m = new Missile(a.world, a.world.sprites, a.posx + look, a.posy, 0.06258f, mrad - offsetrad, 6, 6, 8, 8, a.cr, a.cg, a.cb, 24, a.world.emissiles, a.world.allies, 1, a);
				a.world.sprites.add(m);
			}
			else if (atype == 1)
			{
				MissileHold m = new MissileHold(a.world, a.world.sprites, a.posx, a.posy + a.hh, 0.056f, 
						(float)Math.atan2(a.world.you.posy - a.posy - a.hh, a.world.you.posx - a.posx), 7, 7, 8, 8, a.cr, a.cg, a.cb, 12, a.world.emissiles, a.world.allies, 2, a);

				a.world.sprites.add(m);
				a.world.audio.play(R.raw.a_jump1);
			}
			else
			{
				MissileSeeker m = new MissileSeeker(a.world, a.world.sprites, a.posx, a.posy, 0.00042f, 6, 6, 8, 8, a.cr, a.cg, a.cb, 8, a.world.emissiles, a.world.allies, 1, a);
				a.world.sprites.add(m);
			}

			att_time = 1010 + WorldExecutor.frandom.nextInt(500);

			atype++;

			if (atype == 3)
				atype = 0;
		}
	}
}
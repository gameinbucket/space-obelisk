package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AttButtons extends Element
{
	public AttButtons(Adversary adv)
	{
		super(adv);
	}

	public void integrate(float time)
	{
		if (a.world.view.render.loop_run.i_bleft.is_active())
		{
			a.look(-1);
			attack0();

			a.world.view.render.loop_run.i_bleft.release();
		}
		else if (a.world.view.render.loop_run.i_bright.is_active())
		{
			a.look(1);
			attack0();

			a.world.view.render.loop_run.i_bright.release();
		}
	}

	private void attack0()
	{
		if (a.world.fmissiles.size() > 6)
			return;

		a.world.audio.play(R.raw.a_laser0);

		Missile m = new Missile(a.world, a.world.sprites, a.posx, a.posy, 0.08258f * a.look, 0, 3, 3, 8, 8, a.cr, a.cg, a.cb, 3, a.world.fmissiles, a.world.enemies, 1, a);
		a.world.sprites.add(m);
	}
}
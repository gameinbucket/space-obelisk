package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AccJoyStick extends Element
{
	private float speed = 0.00082258f;

	public AccJoyStick(Adversary adv)
	{
		super(adv);
	}

	public void integrate(float time)
	{
		if (a.world.view.render.loop_run.i_pos.distance > 0)
		{
			a.accx += Math.cos(a.world.view.render.loop_run.i_pos.angle) * -speed;
			a.accy += Math.sin(a.world.view.render.loop_run.i_pos.angle) * -speed;
		}
	}
}
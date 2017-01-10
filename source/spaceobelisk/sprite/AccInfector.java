package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AccInfector extends Element
{
	public AccInfector(Adversary adv)
	{
		super(adv);
	}

	public void integrate(float time)
	{
		float radian = a.sprite_radian(a.world.you);

		a.accx += Math.cos(radian) * 0.0002258f;
		a.accy += Math.sin(radian) * 0.0002258f;

		if (a.accx < 0) a.look(1);
		else a.look(-1);
	}
}
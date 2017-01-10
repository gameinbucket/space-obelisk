package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class AccWarMachine extends Element
{
	float xds;
	float yds;

	public AccWarMachine(Adversary adv)
	{
		super(adv);

		float radian = a.sprite_radian(a.world.you);

		if (Math.cos(radian) < 0) xds = -1;
		else xds = 1;

		xds *= 0.0002458f;

		if (Math.sin(radian) > 0) yds = -1;
		else yds = 1;

		yds *= 0.0002858f;
	}

	public void integrate(float time)
	{
		float radian = a.sprite_radian(a.world.you);

		if (Math.cos(radian) < 0) a.look(1);
		else a.look(-1);

		if (a.velx == 0) xds *= -1;
		if (a.vely == 0) yds *= -1;

		a.accx += xds;
		a.accy += yds;
	}
}
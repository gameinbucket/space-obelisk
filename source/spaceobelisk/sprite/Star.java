package com.gameinbucket.spaceobelisk.sprite;

import java.util.Random;

public class Star extends Particle
{
	private static Random r = new Random(3141567);

	public Star(int x, int y)
	{
		super(x, y, 0.7490f, 0.7970f, 0.5411f);
	}

	public boolean integrate()
	{
		return false;
	}

	public boolean render()
	{
		if (r.nextInt(112) == 0)
			return false;
		else
			return true;
	}
}
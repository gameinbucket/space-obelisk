package com.gameinbucket.spaceobelisk.sprite;

public class Spark extends Particle
{
	private int frame;

	public Spark(int x, int y, float br, float bg, float bb)
	{
		super(x, y, br, bg, bb);
	}

	public boolean integrate()
	{
		frame++;

		if (frame == 32)
			return true;
		else
			return false;
	}

	public boolean render()
	{
		return true;
	}
}
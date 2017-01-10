package com.gameinbucket.spaceobelisk.sprite;

public abstract class Particle
{
	public int posx;
	public int posy;
	
	public final float cr;
	public final float cg;
	public final float cb;

	public Particle(int x, int y, float br, float bg, float bb)
	{
		posx = x;
		posy = y;
		
		cr = br;
		cg = bg;
		cb = bb;
	}

	public abstract boolean integrate();
	public abstract boolean render();
}
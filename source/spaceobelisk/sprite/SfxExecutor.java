package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.WorldExecutor;

public class SfxExecutor
{
	private final WorldExecutor world;
	private final SpriteSheet sheet;

	public SfxExecutor(WorldExecutor w, SpriteSheet s)
	{
		world = w;
		sheet = s;
	}

	public void regen(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 4, 1, 5));
	}

	public void missilee(float x, float y, float br, float bg, float bb, int l)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 8, l, 2));
	}

	public void smalle(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 4, 1, 5));
	}

	public void blast(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 13, 1, 5));
	}

	public void clusterblast(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x - 8, y, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x + 8, y, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x, y - 8, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x, y + 8, 8, 8, br, bg, bb, 4, 1, 5));
	}

	public void circlee(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y + 8, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x, y - 8, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x + 8, y, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x - 8, y, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x + 5.6568542f, y + 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x + 5.6568542f, y - 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x - 5.6568542f, y + 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x - 5.6568542f, y - 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
	}

	public void pillare(float x, float y, float br, float bg, float bb)
	{
		world.sprites.add(new Sfx(world, sheet, x, y, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x + 5.6568542f, y + 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x + 11.313708f, y + 11.313708f, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x - 5.6568542f, y - 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x - 11.313708f, y - 11.313708f, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x + 5.6568542f, y - 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x + 11.313708f, y - 11.313708f, 8, 8, br, bg, bb, 4, 1, 5));

		world.sprites.add(new Sfx(world, sheet, x - 5.6568542f, y + 5.6568542f, 8, 8, br, bg, bb, 4, 1, 5));
		world.sprites.add(new Sfx(world, sheet, x - 11.313708f, y + 11.313708f, 8, 8, br, bg, bb, 4, 1, 5));
	}
}
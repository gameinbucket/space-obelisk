package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class MissileCluster extends Missile
{
	public MissileCluster(WorldExecutor wo, SpriteSheet s, float x, float y, float v, float t, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li, ArrayList<Sprite> collides, int d, Adversary origin)
	{
		super(wo, s, x, y, v, t, wi, he, rwi, rhe, br, bg, bb, f, li, collides, d, origin);
	}

	public void remove(int d)
	{
		remove = true;
		l.remove(this);

		posy += 1;
		world.sfx.clusterblast(posx, posy, cr, cg, cb);

		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 0, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 0.7853f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 1.57f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 2.357f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 3.14f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 3.926f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 4.71f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
		world.sprites.add(new Missile(world, world.sprites, posx, posy, 0.0689f, 5.497f, 3, 3, 8, 8, cr, cg, cb, 32, world.emissiles, world.allies, 1, null));
	}
}
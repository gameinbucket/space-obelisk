package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AccWarMachine;
import com.gameinbucket.spaceobelisk.sprite.AttWarMachine;
import com.gameinbucket.spaceobelisk.sprite.MissileCluster;

public class WarMachine extends Adversary
{
	public WarMachine(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 4, 8, 8, 8, 0.8941f, 0.4156f, 0.1098f, 4, w.enemies, 10);
		attach_accelement(new AccWarMachine(this));
		attach_attelement(new AttWarMachine(this));
	}

	public void remove(int d)
	{
		hp -= d;

		if (hp < 1)
		{
			remove = true;
			l.remove(this);

			world.sfx.clusterblast(posx, posy, cr, cg, cb);
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 0, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 0.7853f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 1.57f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 2.357f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 3.14f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 3.926f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 4.71f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
			world.sprites.add(new MissileCluster(world, world.sprites, posx, posy, 0.0689f, 5.497f, 3, 3, 8, 8, cr, cg, cb, 5, world.emissiles, world.allies, 1, null));
		}
	}
}
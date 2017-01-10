package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AccFollower;
import com.gameinbucket.spaceobelisk.sprite.AttFollower;

public class Follower extends Adversary
{
	public Follower(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 4, 6, 8, 8, 0.8941f, 0.4156f, 0.1098f, 1, w.enemies, 3);
		attach_accelement(new AccFollower(this));
		attach_attelement(new AttFollower(this));
	}
}
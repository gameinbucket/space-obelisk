package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class MissileHold extends Missile
{
	public MissileHold(WorldExecutor wo, SpriteSheet s, float x, float y, float v, float t, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li, ArrayList<Sprite> collides, int d, Adversary origin)
	{
		super(wo, s, x, y, v, t, wi, he, rwi, rhe, br, bg, bb, f, li, collides, d, origin);
	}

	public void remove(int d)
	{
		remove = true;
		l.remove(this);

		world.sfx.circlee(posx, posy, cr, cg, cb);
		world.audio.play(R.raw.a_laser1);
	}

	public boolean integrate(float time)
	{
		if (frame_increment > -1)
		{
			next_frame();
			return false;
		}

		for (int i = 0; i < c.size(); i++)
		{
			if (c.get(i).sprite_overlap(this))
			{
				Sprite s = c.get(i);
				s.remove(damage);

				if (s.remove) {i--; o.target(null);}
				else o.target(c.get(i));

				remove(1);

				return remove;
			}
		}

		boundaries();
		grid_overlap();

		posx += velx * time;
		posy += vely * time;

		return remove;
	}

	protected void next_frame()
	{
		frame_increment++;

		if (frame_increment == 5)
		{
			frame++;
			frame_increment = 0;

			if (frame == a.length - 1)
				frame_increment = -1;

			texture_position();
		}
	}
}
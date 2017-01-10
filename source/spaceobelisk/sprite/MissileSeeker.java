package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.adversary.Adversary;

public class MissileSeeker extends Missile
{
	private int spark;

	private float accx;
	private float accy;

	private float speed;
	
	private int lifetime;

	public MissileSeeker(WorldExecutor wo, SpriteSheet s, float x, float y, float v, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li, ArrayList<Sprite> collides, int d, Adversary origin)
	{
		super(wo, s, x, y, 0, 0, wi, he, rwi, rhe, br, bg, bb, f, li, collides, d, origin);

		spark = 0;

		accx = 0;
		accy = 0;

		speed = v;
		
		lifetime = 600;
	}

	public boolean integrate(float time)
	{
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

		float radian = sprite_radian(world.you);

		accx += Math.cos(radian) * speed;
		accy += Math.sin(radian) * speed;

		if (accx < 0) look(1);
		else look(-1);

		velx += accx * time;
		vely += accy * time;

		velx *= world.damp_bias;
		vely *= world.damp_bias;

		posx += velx * time;
		posy += vely * time;

		accx = 0;
		accy = 0;

		next_frame();

		spark++;
		if (spark == 12)
		{
			world.ptc.spark((int)posx, (int)posy, cr, cg, cb);
			spark = 0;
		}
		
		lifetime--;
		if (lifetime == 0)
		{
			l.remove(this);
			world.sfx.smalle(posx, posy, cr, cg, cb);
			return true;
		}

		return remove;
	}
}
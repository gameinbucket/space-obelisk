package com.gameinbucket.spaceobelisk.sprite.adversary;

import java.util.ArrayList;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.Element;
import com.gameinbucket.spaceobelisk.sprite.Sprite;
import com.gameinbucket.spaceobelisk.sprite.SpriteSheet;

public abstract class Adversary extends Sprite
{
	public float accx;
	public float accy;

	protected Element acc;
	protected Element att;

	public int hplimit;
	public int hp;

	public Adversary(WorldExecutor wo, SpriteSheet s, float x, float y, int wi, int he, int rwi, int rhe, float br, float bg, float bb, int f, ArrayList<Sprite> li, int hea)
	{
		super(wo, s, x, y, wi, he, rwi, rhe, br, bg, bb, f, li);

		accx = 0;
		accy = 0;

		hplimit = hea;
		hp = hplimit;

		texture_position();
	}

	protected void attach_accelement(Element ac)
	{
		acc = ac;
	}

	protected void attach_attelement(Element at)
	{
		att = at;
	}

	public void target(Sprite s)
	{

	}

	public void remove(int d)
	{
		hp -= d;

		if (hp < 1)
		{
			remove = true;
			l.remove(this);

			world.sfx.smalle(posx, posy, cr, cg, cb);
		}
	}

	public boolean integrate(float time)
	{
		acc.integrate(time);
		att.integrate(time);

		velx += accx * time;
		vely += accy * time;

		velx *= world.damp_bias;
		vely *= world.damp_bias;

		grid_overlapx(velx * time);
		posx += velx * time;

		grid_overlapy(vely * time);
		posy += vely * time;

		accx = 0;
		accy = 0;

		return remove;
	}
}
package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AccJoyStick;
import com.gameinbucket.spaceobelisk.sprite.AttButtons;
import com.gameinbucket.spaceobelisk.sprite.Sprite;

public class You extends Adversary
{
	private int spark;
	public int regen;
	public int shield;
	public float hprender;

	public float targethprender;
	public Adversary target;

	public You(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 4, 8, 8, 8, 0.7490f, 0.7970f, 0.5411f, 0, w.allies, 4);
		attach_accelement(new AccJoyStick(this));
		attach_attelement(new AttButtons(this));

		spark = 0;
		regen = 0;
		shield = 0;
		hprender = hp;
	}

	public void respawn()
	{
		hp = hplimit;
		spark = 0;
		regen = 0;
		shield = 0;
		hprender = hp;

		target = null;

		look(1);

		if (!l.contains(this))
			l.add(this);

		remove = false;
	}

	public void target(Sprite s)
	{
		if (s == null)
			target = null;
		else if (target != s && s instanceof Adversary)
		{
			target = (Adversary)s;
			targethprender = target.hp;
		}
	}

	public void remove(int d)
	{
		if (shield > 0)
			return;

		hp -= d;
		shield = 48;

		if (hp < 1)
		{
			hp = 0;
			hprender = 0;

			if (!remove)
			{
				remove = true;
				l.remove(this);

				world.sfx.clusterblast(posx, posy, cr, cg, cb);
				world.audio.play(R.raw.a_death0);
				regen = 64;
			}
		}
	}

	private void boundaries(float x, float y)
	{
		x += posx;
		y += posy;

		if (!world.levelsaved && world.cryotube != null)
		{
			if (x - hw < world.cryotube.posx + WorldExecutor.grid_scale && x + hw > world.cryotube.posx && y - hh < world.cryotube.posy +  WorldExecutor.grid_scale && y + hh > world.cryotube.posy)
			{
				hp = hplimit;

				world.save_level();
				world.levelsaved = true;

				world.sfx.circlee(posx, posy, cr, cg, cb);
			}
		}

		if (x - hw < 0)
		{
			posx = WorldExecutor.gridwidth - hw;
			velx = 0;
			world.shift_level(-1, 0);
		}
		else if (x + hw > WorldExecutor.gridwidth)
		{
			posx = hw;
			velx = 0;
			world.shift_level(1, 0);
		}
		else if (y - hh < 0)
		{
			posy = WorldExecutor.gridheight - hh;
			vely = 0;
			world.shift_level(0, 1);
		}
		else if (y + hh > WorldExecutor.gridheight)
		{
			posy = hh;
			vely = 0;
			world.shift_level(0, -1);
		}
	}

	private void overlape()
	{
		for (int i = 0; i < world.enemies.size(); i++)
		{
			if (world.enemies.get(i).sprite_overlap(this))
			{
				remove(1);
				break;
			}
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

		float nx = velx * time;
		float ny = vely * time;

		boundaries(nx, ny);

		grid_overlapx(nx);
		posx += velx * time;

		grid_overlapy(ny);
		posy += vely * time;

		if (shield > 0)
			shield--;
		else
			overlape();

		hprender += (hp - hprender) * 0.1f;

		if (Math.abs(accx) > 0 || Math.abs(accy) > 0)
			spark++;

		if (spark == 14)
		{
			world.ptc.spark((int)posx - look * 2, (int)posy - 1, cr, cg, cb);
			spark = 0;
		}

		accx = 0;
		accy = 0;

		if (target != null)
			targethprender += (target.hp - targethprender) * 0.1f;

		return remove;
	}
}
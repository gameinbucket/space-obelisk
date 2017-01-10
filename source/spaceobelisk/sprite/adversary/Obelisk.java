package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AttObelisk;

public class Obelisk extends Adversary
{
	public Obelisk(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 8, 32, 8, 32, 0.8941f, 0.4156f, 0.1098f, 27, w.enemies, 66);
		attach_accelement(null);
		attach_attelement(new AttObelisk(this));
	}

	public boolean integrate(float time)
	{
		att.integrate(time);
		return remove;
	}

	protected void texture_position()
	{
		int x = frame % sheet.columns;
		int y = frame / sheet.columns;

		tex1 = y;
		tex3 = y + 4;

		switch(look)
		{
		case 1:
			tex0 = x;
			tex2 = x + 1;
			break;
		case -1:
			tex0 = x + 1;
			tex2 = x;
			break;
		}
	}

	public void remove(int d)
	{
		hp -= d;

		if (hp < 1)
		{
			remove = true;
			l.remove(this);

			world.sfx.pillare(posx, posy, cr, cg, cb);
			world.obeliskdestroyed = 256;
			
			world.audio.play(R.raw.a_destroy);
			world.message.set(WorldExecutor.cdestroy);
		}

		world.viewoffsety += 2;
	}
}
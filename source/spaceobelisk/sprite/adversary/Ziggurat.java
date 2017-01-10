package com.gameinbucket.spaceobelisk.sprite.adversary;

import com.gameinbucket.spaceobelisk.WorldExecutor;
import com.gameinbucket.spaceobelisk.sprite.AttZiggurat;

public class Ziggurat extends Adversary
{
	protected int frame_increment = 0;

	public Ziggurat(WorldExecutor w, float x, float y)
	{
		super(w, w.sprites, x, y, 14, 13, 16, 16, 0.8941f, 0.4156f, 0.1098f, 36, w.enemies, 18);
		attach_accelement(null);
		attach_attelement(new AttZiggurat(this));
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
		tex3 = y + 2;

		switch(look)
		{
		case 1:
			tex0 = x;
			tex2 = x + 2;
			break;
		case -1:
			tex0 = x + 2;
			tex2 = x;
			break;
		}
	}
}
package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

public class SpriteSheet
{
	public final float scale;
	public final int columns;
	public final ArrayList<Sprite> sprites;

	public SpriteSheet(float s, int c)
	{
		scale = s;
		columns = c;

		sprites = new ArrayList<Sprite>();
	}

	public void add(Sprite s)
	{
		sprites.add(s);
	}
}
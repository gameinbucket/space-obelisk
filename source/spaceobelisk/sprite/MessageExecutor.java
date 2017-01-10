package com.gameinbucket.spaceobelisk.sprite;

import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.WorldExecutor;

public class MessageExecutor
{
	private final WorldExecutor world;
	private String text;
	private int frame;
	private int frame_increment;

	public MessageExecutor(WorldExecutor w)
	{
		world = w;
		text = null;
		frame = 0;
		frame_increment = 0;
	}

	public void set(String t)
	{
		text = t;
		frame = 0;
		frame_increment = 0;
	}

	public void integrate()
	{
		if (text != null)
		{
			if (frame == text.length())
				return;

			frame_increment++;

			if (frame_increment == 4)
			{
				if (text.charAt(frame) != ' ')
					world.audio.play(R.raw.a_laser1, 0.25f);

				frame++;
				frame_increment = 0;
			}
		}
	}

	public int render_bar()
	{
		if (text != null) return 1;
		else return 0;
	}

	public String render_text()
	{
		if (text != null) return text.substring(0, frame);
		else return null;
	}
}
package com.gameinbucket.spaceobelisk;

import com.gameinbucket.input.InputTap;

public class LoopEnding extends LoopExecutor
{
	private final static String[] story = 
		{
		"the wicked obelisk was destroyed",
		"i had crushed their rebellion once and for all",
		"and ended years of unrest and turmoil",
		};

	public int storyindex;
	public final InputTap i_continue;

	LoopEnding(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);
		i_continue = new InputTap(0, 0, r.render_width, r.render_height);
	}

	public void open()
	{
		storyindex = 0;
		w.message.set(story[0]);

		w.music.load(R.raw.obelisk_ending);
	}

	public void release()
	{
		i_continue.release();
	}

	public void input_down(float x, float y, int i)
	{
		y = r.screen_height - y;

		x /= r.input_scale;
		y /= r.input_scale;

		i_continue.press(x, y, i);

		if (i_continue.is_active())
		{
			storyindex++;

			if (storyindex < story.length)
				w.message.set(story[storyindex]);
		}
	}

	public void input_move(float x, float y, int i)
	{

	}

	public void input_up(int i)
	{
		i_continue.release(i);
	}

	public void input_back(Main m)
	{
		m.moveTaskToBack(true);
	}

	public void update()
	{
		if (storyindex == story.length)
		{
			r.loop_title.open();
			r.loop = r.loop_title;

			release();
		}
		else
			w.message.integrate();
	}

	public void draw()
	{
		r.draw_ending();
	}
}
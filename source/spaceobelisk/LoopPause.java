package com.gameinbucket.spaceobelisk;

import com.gameinbucket.render.Vertex;

public class LoopPause extends LoopExecutor
{
	public String[] optionsstring;
	public float[] optionsposx;
	public float[] optionsposy;
	public float[] optionstargety;

	LoopPause(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);

		optionsstring = new String[4];
		optionsposx = new float[4];
		optionsposy = new float[4];
		optionstargety = new float[4];

		optionsstring[0] = "pause";
		optionsposx[0] = r.render_width / 2 - optionsstring[0].length() / 2 * Vertex.font_size_x;
		optionstargety[0] = r.render_height * 0.75f;

		optionsstring[1] = "continue";
		optionsstring[2] = "options";
		optionsstring[3] = "return to title";

		for (int i = 0; i < 3; i++)
		{
			optionsposx[i + 1] = r.render_width / 2 - 3 * Vertex.font_size_x;
			optionstargety[i + 1] = r.render_height * 0.5f - Vertex.font_size_y * i;
		}
	}

	public void open()
	{
		r.loop_title.selectionid = 0;

		optionsposy[0] = r.render_height;

		for (int i = 1; i < 4; i++)
			optionsposy[i] = 0;
	}

	public void release()
	{
		r.loop_title.i_blleft.release();
		r.loop_title.i_bleft.release();
		r.loop_title.i_bright.release();
	}

	public void input_down(float x, float y, int i)
	{
		y = r.screen_height - y;

		x /= r.input_scale;
		y /= r.input_scale;

		r.loop_title.i_blleft.press(x, y, i);
		r.loop_title.i_bleft.press(x, y, i);
		r.loop_title.i_bright.press(x, y, i);

		if (r.loop_title.i_blleft.is_active())
		{
			w.audio.play(R.raw.a_jump0);

			if (r.loop_title.selectionid == 0)
			{
				r.loop_run.open();
				r.loop = r.loop_run;
			}
			else if (r.loop_title.selectionid == 1)
			{
				r.loop_options.fallback = this;
				r.loop_options.open();
				r.loop = r.loop_options;
			}
			else
			{
				r.loop_title.open();
				r.loop = r.loop_title;
			}

			release();
		}
		else if (r.loop_title.i_bleft.is_active() && r.loop_title.selectionid < 2)
		{
			w.audio.play(R.raw.a_jump1);
			r.loop_title.selectionid++;
		}
		else if (r.loop_title.i_bright.is_active() && r.loop_title.selectionid > 0)
		{
			w.audio.play(R.raw.a_jump1);
			r.loop_title.selectionid--;
		}
	}

	public void input_move(float x, float y, int i)
	{

	}

	public void input_up(int i)
	{
		r.loop_title.i_blleft.release(i);
		r.loop_title.i_bleft.release(i);
		r.loop_title.i_bright.release(i);
	}

	public void input_back(Main m)
	{
		m.moveTaskToBack(true);
	}

	public void update()
	{
		optionsposy[0] += (optionstargety[0] - optionsposy[0]) * 0.16f;
		optionsposy[1] += (optionstargety[1] - optionsposy[1]) * 0.16f;
		optionsposy[2] += (optionstargety[2] - optionsposy[2]) * 0.16f;
		optionsposy[3] += (optionstargety[3] - optionsposy[3]) * 0.16f;

		r.loop_title.ptc.integrate();
	}

	public void draw()
	{
		r.draw_pause();
	}
}
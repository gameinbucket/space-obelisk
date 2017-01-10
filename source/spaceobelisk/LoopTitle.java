package com.gameinbucket.spaceobelisk;

import java.util.Random;

import com.gameinbucket.input.InputButton;
import com.gameinbucket.render.Vertex;
import com.gameinbucket.spaceobelisk.sprite.ParticleExecutor;

public class LoopTitle extends LoopExecutor
{
	public final static String title = "space obelisk";
	public final static String newgame = "new game";
	public final static String load = "load";
	public final static String options = "options";

	public float[] optionsposx;
	public float[] optionsposy;
	public float[] optionstargety;

	public final InputButton i_blleft;
	public final InputButton i_bleft;
	public final InputButton i_bright;

	public final ParticleExecutor ptc;
	public int selectionid;

	LoopTitle(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);

		optionsposx = new float[4];
		optionsposy = new float[4];
		optionstargety = new float[4];

		optionsposx[0] = r.render_width / 2 - title.length() / 2 * Vertex.font_size_x;
		optionsposx[1] = r.render_width / 2 - 3 * Vertex.font_size_x;
		optionsposx[2] = r.render_width / 2 - 3 * Vertex.font_size_x;
		optionsposx[3] = r.render_width / 2 - 3 * Vertex.font_size_x;

		optionstargety[0] = r.render_height * 0.75f;
		optionstargety[1] = r.render_height * 0.5f;
		optionstargety[2] = r.render_height * 0.5f - Vertex.font_size_y;
		optionstargety[3] = r.render_height * 0.5f - Vertex.font_size_y * 2;

		open();

		int dim = r.button_scale;
		i_blleft = new InputButton(dim, (int)(dim * 1.5f), dim, dim, dim, dim);
		i_bleft = new InputButton(r.render_width - dim / 2 - dim, dim, dim, dim, dim, dim);
		i_bright = new InputButton(r.render_width - dim / 2, dim * 2, dim, dim, dim, dim);

		ptc = new ParticleExecutor();

		Random rr = new Random(304033);

		for (int i = 0; i < 100; i++)
			ptc.star(rr.nextInt(WorldExecutor.gridwidth), rr.nextInt(WorldExecutor.gridheight));
	}

	public void open()
	{
		selectionid = 0;

		optionsposy[0] = r.render_height;
		optionsposy[1] = 0;
		optionsposy[2] = 0;
		optionsposy[3] = 0;

		if (w.music.switchtrack(R.raw.obelisk_title))
			w.music.load(R.raw.obelisk_title);
	}

	public void update_input()
	{
		int dim = r.button_scale;
		i_blleft.update(dim, (int)(dim * 1.5f), dim, dim, dim, dim);
		i_bleft.update(r.render_width - dim / 2 - dim, dim, dim, dim, dim, dim);
		i_bright.update(r.render_width - dim / 2, dim * 2, dim, dim, dim, dim);
	}

	public void release()
	{
		i_blleft.release();
		i_bleft.release();
		i_bright.release();
	}

	public void input_down(float x, float y, int i)
	{
		y = r.screen_height - y;

		x /= r.input_scale;
		y /= r.input_scale;
		
		i_blleft.press(x, y, i);
		i_bleft.press(x, y, i);
		i_bright.press(x, y, i);

		if (i_blleft.is_active())
		{
			w.audio.play(R.raw.a_jump0);

			if (selectionid == 0)
			{
				r.loop_new.open();
				r.loop = r.loop_new;
			}
			else if (selectionid == 1)
			{
				r.loop_loadfile.open();
				r.loop = r.loop_loadfile;
			}
			else
			{
				r.loop_options.fallback = this;
				r.loop_options.open();
				r.loop = r.loop_options;
			}

			release();
		}
		else if (i_bleft.is_active() && selectionid < 2)
		{
			w.audio.play(R.raw.a_jump1);
			selectionid++;
		}
		else if (i_bright.is_active() && selectionid > 0)
		{
			w.audio.play(R.raw.a_jump1);
			selectionid--;
		}
	}

	public void input_move(float x, float y, int i)
	{

	}

	public void input_up(int i)
	{
		i_blleft.release(i);
		i_bleft.release(i);
		i_bright.release(i);
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

		ptc.integrate();
	}

	public void draw()
	{
		r.draw_title();
	}
}
package com.gameinbucket.spaceobelisk;

import com.gameinbucket.render.Vertex;

public class LoopOptions extends LoopExecutor
{
	public String[] optionsstring;
	public float[] optionsposx;
	public float[] optionsposy;
	public float[] optionstargety;

	public LoopExecutor fallback;

	private boolean optionselect;

	LoopOptions(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);

		optionsstring = new String[5];
		optionsposx = new float[5];
		optionsposy = new float[5];
		optionstargety = new float[5];

		optionsstring[0] = "options";
		optionsposx[0] = r.render_width / 2 - optionsstring[0].length() / 2 * Vertex.font_size_x;
		optionstargety[0] = r.render_height * 0.75f;

		optionsstring[1] = "render scale " + r.render_scale;
		optionsstring[2] = "button scale " + r.button_scale;
		optionsstring[3] = "music volume " + (int)(w.music.fullvolume * 100);
		optionsstring[4] = "back";

		for (int i = 0; i < 4; i++)
		{
			optionsposx[i + 1] = r.render_width / 2 - 3 * Vertex.font_size_x;
			optionstargety[i + 1] = r.render_height * 0.5f - Vertex.font_size_y * i;
		}
	}

	public void open()
	{
		r.loop_title.selectionid = 0;
		optionselect = false;

		optionsposy[0] = r.render_height;

		for (int i = 1; i < 5; i++)
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

			if (optionselect)
				optionselect = false;
			else if (r.loop_title.selectionid < 3)
				optionselect = true;
			else
			{
				fallback.open();
				r.loop = fallback;

				release();
			}
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
		if (r.loop_title.i_bleft.is_active())
		{
			if (optionselect)
			{
				if (r.loop_title.selectionid == 2)
				{
					w.music.shiftvolume(-0.25f);
					optionsstring[3] = "music volume " + (int)(w.music.fullvolume * 100);
					r.save_options();
				}
				else if (r.loop_title.selectionid == 1)
				{
					if (r.button_scale > 8)
					{
						r.button_scale -= 2;

						optionsstring[2] = "button scale " + r.button_scale;
						r.loop_title.update_input();
						r.loop_run.update_input();
						r.save_options();
					}
				}
				else if (r.loop_title.selectionid == 0)
				{
					if (r.render_scale > 0.25f)
					{
						r.render_scale -= 0.25f;

						optionsstring[1] = "render scale " + r.render_scale;
						r.onSurfaceChanged(null, r.screen_width, r.screen_height);
						r.save_options();
					}
				}

				w.audio.play(R.raw.a_jump1);
			}
			else if (r.loop_title.selectionid < 3)
			{
				r.loop_title.selectionid++;
				w.audio.play(R.raw.a_jump1);
			}

			r.loop_title.i_bleft.release();
		}
		else if (r.loop_title.i_bright.is_active())
		{
			if (optionselect)
			{
				if (r.loop_title.selectionid == 2)
				{
					w.music.shiftvolume(0.25f);
					optionsstring[3] = "music volume " + (int)(w.music.fullvolume * 100);
					r.save_options();
				}
				else if (r.loop_title.selectionid == 1)
				{
					if (r.button_scale < 32)
					{
						r.button_scale += 2;

						optionsstring[2] = "button scale " + r.button_scale;
						r.loop_title.update_input();
						r.loop_run.update_input();
						r.save_options();
					}
				}
				else if (r.loop_title.selectionid == 0)
				{
					if (r.render_scale < 8)
					{
						r.render_scale += 0.25f;

						optionsstring[1] = "render scale " + r.render_scale;
						r.onSurfaceChanged(null, r.screen_width, r.screen_height);
						r.save_options();
					}
				}

				w.audio.play(R.raw.a_jump1);
			}
			else if (r.loop_title.selectionid > 0)
			{
				r.loop_title.selectionid--;
				w.audio.play(R.raw.a_jump1);
			}

			r.loop_title.i_bright.release();
		}

		optionsposy[0] += (optionstargety[0] - optionsposy[0]) * 0.16f;
		optionsposy[1] += (optionstargety[1] - optionsposy[1]) * 0.16f;
		optionsposy[2] += (optionstargety[2] - optionsposy[2]) * 0.16f;
		optionsposy[3] += (optionstargety[3] - optionsposy[3]) * 0.16f;
		optionsposy[4] += (optionstargety[4] - optionsposy[4]) * 0.16f;

		r.loop_title.ptc.integrate();
	}

	public void draw()
	{
		r.draw_options();
	}
}
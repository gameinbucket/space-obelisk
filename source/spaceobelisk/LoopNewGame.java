package com.gameinbucket.spaceobelisk;

import java.io.File;
import com.gameinbucket.render.Vertex;

public class LoopNewGame extends LoopExecutor
{
	public String[] optionsstring;
	public float[] optionsposx;
	public float[] optionsposy;
	public float[] optionstargety;

	LoopNewGame(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);

		optionsstring = new String[6];
		optionsposx = new float[6];
		optionsposy = new float[6];
		optionstargety = new float[6];

		optionsstring[0] = "new game";
		optionsposx[0] = r.render_width / 2 - optionsstring[0].length() / 2 * Vertex.font_size_x;
		optionstargety[0] = r.render_height * 0.75f;

		optionsstring[4] = "delete";
		optionsstring[5] = "back";

		for (int i = 0; i < 5; i++)
		{
			optionsposx[i + 1] = r.render_width / 2 - 3 * Vertex.font_size_x;
			optionstargety[i + 1] = r.render_height * 0.5f - Vertex.font_size_y * i;
		}
	}

	public void open()
	{
		r.loop_title.selectionid = 0;

		optionsposy[0] = r.render_height;
		optionsposy[1] = 0;
		optionsposy[2] = 0;
		optionsposy[3] = 0;
		optionsposy[4] = 0;
		optionsposy[5] = 0;

		for (int i = 0; i < 3; i++)
		{
			File file = w.view.context.getFileStreamPath("save_file" + i + ".dat");

			if (file.exists())
				optionsstring[i + 1] = "file " + i;
			else
				optionsstring[i + 1] = "empty";
		}
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

			if (r.loop_title.selectionid < 3)
			{
				if (optionsstring[4].equals("delete"))
				{
					if (optionsstring[r.loop_title.selectionid + 1].equals("empty"))
					{
						w.savefile = r.loop_title.selectionid;

						//r.loop_story.open();
						//r.loop = r.loop_story;
						
						w.new_game();
						r.loop = r.loop_run;

						release();
					}
				}
				else
				{
					if (!optionsstring[r.loop_title.selectionid + 1].equals("empty"))
					{
						File file = w.view.context.getFileStreamPath("save_file" + r.loop_title.selectionid + ".dat");
						file.delete();

						optionsstring[r.loop_title.selectionid + 1] = "empty";

						optionsstring[4] = "delete";
						r.loop_title.selectionid = 0;
					}
				}
			}
			else
			{
				if (r.loop_title.selectionid == 4)
				{
					if (optionsstring[4].equals("delete"))
					{
						r.loop_title.open();
						r.loop = r.loop_title;

						release();
					}
				}
				else
				{
					if (optionsstring[4].equals("delete")) optionsstring[4] = "cancel";
					else optionsstring[4] = "delete";

					r.loop_title.selectionid = 0;
				}
			}
		}
		else if (r.loop_title.i_bleft.is_active() && r.loop_title.selectionid < 4)
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
		optionsposy[4] += (optionstargety[4] - optionsposy[4]) * 0.16f;
		optionsposy[5] += (optionstargety[5] - optionsposy[5]) * 0.16f;

		r.loop_title.ptc.integrate();
	}

	public void draw()
	{
		r.draw_newgame();
	}
}
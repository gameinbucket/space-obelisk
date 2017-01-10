package com.gameinbucket.spaceobelisk;

import com.gameinbucket.input.InputButton;
import com.gameinbucket.input.InputMove;

public class LoopRun extends LoopExecutor
{
	public final InputMove i_pos;

	public final InputButton i_bleft;
	public final InputButton i_bright;

	LoopRun(RenderExecutor render, WorldExecutor world)
	{
		super(render, world);

		int dim = r.button_scale;
		i_pos = new InputMove(0, 0, (int)(r.render_width * (3.0f / 4.0f)), r.render_height, dim, (int)(dim * 1.5f), dim, dim);
		i_bleft = new InputButton(r.render_width - dim / 2 - dim, dim, dim, dim, dim, dim);
		i_bright = new InputButton(r.render_width - dim / 2, dim * 2, dim, dim, dim, dim);
	}

	public void open()
	{

	}

	public void update_input()
	{
		int dim = r.button_scale;
		i_pos.update(dim, (int)(dim * 1.5f), dim, dim);
		i_bleft.update(r.render_width - dim / 2 - dim, dim, dim, dim, dim, dim);
		i_bright.update(r.render_width - dim / 2, dim * 2, dim, dim, dim, dim);
	}

	public void release()
	{
		i_pos.release();
		i_bleft.release();
		i_bright.release();
	}

	public void input_down(float x, float y, int i)
	{
		y = r.screen_height - y;

		x /= r.input_scale;
		y /= r.input_scale;

		i_pos.press(x, y, i);

		i_bleft.press(x, y, i);
		i_bright.press(x, y, i);
	}

	public void input_move(float x, float y, int i)
	{
		y = r.screen_height - y;

		x /= r.input_scale;
		y /= r.input_scale;

		i_pos.move(x, y, i);
	}

	public void input_up(int i)
	{
		i_pos.release(i);

		i_bleft.release(i);
		i_bright.release(i);
	}

	public void input_back(Main m)
	{
		r.loop_pause.open();
		r.loop = r.loop_pause;
	}

	public void update()
	{
		w.loop();
	}

	public void draw()
	{
		r.draw();
	}
}
package com.gameinbucket.spaceobelisk;

public abstract class LoopExecutor 
{
	protected final RenderExecutor r;
	protected final WorldExecutor w;

	LoopExecutor(RenderExecutor render, WorldExecutor world)
	{
		r = render;
		w = world;
	}

	public abstract void open();
	public abstract void release();
	public abstract void input_down(float x, float y, int i);
	public abstract void input_move(float x, float y, int i);
	public abstract void input_up(int i);
	public abstract void input_back(Main m);

	public abstract void update();
	public abstract void draw();
}

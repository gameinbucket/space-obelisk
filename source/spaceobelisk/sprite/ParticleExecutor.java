package com.gameinbucket.spaceobelisk.sprite;

import java.util.ArrayList;

import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;

public class ParticleExecutor
{
	private final ArrayList<Particle> particles;

	public ParticleExecutor()
	{
		particles = new ArrayList<Particle>();
	}

	public void clear()
	{
		particles.clear();
	}

	public void star(int x, int y)
	{
		Star p = new Star(x, y);
		particles.add(p);
	}

	public void spark(int x, int y, float br, float bg, float bb)
	{
		Spark p = new Spark(x, y, br, bg, bb);
		particles.add(p);
	}

	public void integrate()
	{
		for (int i = 0; i < particles.size(); i++)
		{
			if (particles.get(i).integrate())
			{
				particles.remove(i);
				i--;
			}
		}
	}

	public void render(GeometryBuffer g)
	{
		for (int i = 0; i < particles.size(); i++)
		{
			Particle p = particles.get(i);

			if (p.render())
				g.add(Vertex.rectangle(p.posx, p.posy, 1, 1, p.cr, p.cg, p.cb), 4);
		}
	}
}
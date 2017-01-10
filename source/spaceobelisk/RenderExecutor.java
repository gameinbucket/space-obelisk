package com.gameinbucket.spaceobelisk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.gameinbucket.render.Art;
import com.gameinbucket.render.FrameBuffer;
import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Shader;
import com.gameinbucket.render.Vertex;
import com.gameinbucket.math.Matrix4x4;
import com.gameinbucket.spaceobelisk.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class RenderExecutor implements GLSurfaceView.Renderer 
{
	private ViewExecutor view;

	private int t_input;
	private int t_font;
	private int t_background;
	private int t_sprites;
	private int t_story;

	private Shader s_current;
	private Shader s_pre_texture;
	private Shader s_pre_color;
	private Shader s_post_texture;

	public int render_width = 320;
	public int render_height = 180;

	public int screen_width;
	public int screen_height;

	private GeometryBuffer geometry_post0;
	private GeometryBuffer geometry_screen;

	private float[] post_mvp_matrix0 = new float[16];
	private float[] screen_mvp_matrix = new float[16];

	private float[] orthographic_matrix = new float[16];
	private float[] modelview_matrix = new float[16];
	private float[] mvp_matrix = new float[16];

	private GeometryBuffer geometry_buffer;
	private FrameBuffer[] frame_buffer;

	public LoopExecutor loop;
	public LoopRun loop_run;
	public LoopTitle loop_title;
	public LoopStory loop_story;
	public LoopLoad loop_loadfile;
	public LoopNewGame loop_new;
	public LoopPause loop_pause;
	public LoopOptions loop_options;
	public LoopEnding loop_ending;

	private int sleep_time;
	private final int fixed_step = 1000 / 60;
	private long begin_time = System.currentTimeMillis();

	public float render_scale;
	public int button_scale;
	public int input_scale;

	public RenderExecutor(ViewExecutor v)
	{
		view = v;

		final int rectangles = 1000;
		geometry_buffer = new GeometryBuffer(rectangles * 4, rectangles * 6);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glEnable(GLES20.GL_BACK);

		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);

		//GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE); //add
		//GLES20.glBlendFunc(GLES20.GL_DST_COLOR, GLES20.GL_ZERO); //multiply
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA); //alpha blend

		t_input = Art.load_texture(view.context, R.raw.t_input, GLES20.GL_CLAMP_TO_EDGE);
		t_font = Art.load_texture(view.context, R.raw.t_font, GLES20.GL_CLAMP_TO_EDGE);
		t_background = Art.load_texture(view.context, R.raw.t_background, GLES20.GL_CLAMP_TO_EDGE);

		//t_sprites
		{
			Bitmap img = Art.load_image_data(view.context, R.raw.t_sprites);

			int i_width = img.getWidth();
			int i_height = img.getHeight();

			int[] pixels = new int[i_width * i_height];

			img.getPixels(pixels, 0, i_width, 0, 0, i_width, i_height);
			img.recycle();

			Art.transparent_pixels(pixels, 0xff00ff);
			t_sprites = Art.load_texture(pixels, i_width, i_height, GLES20.GL_CLAMP_TO_EDGE);
		}

		//t_story
		{
			Bitmap img = Art.load_image_data(view.context, R.raw.t_story);

			int i_width = img.getWidth();
			int i_height = img.getHeight();

			int[] pixels = new int[i_width * i_height];

			img.getPixels(pixels, 0, i_width, 0, 0, i_width, i_height);
			img.recycle();

			Art.transparent_pixels(pixels, 0xff00ff);
			t_story = Art.load_texture(pixels, i_width, i_height, GLES20.GL_CLAMP_TO_EDGE);
		}

		int[] attributes0 = {0, 2};
		s_post_texture = generate_program(R.raw.s_post_texture_v, R.raw.s_post_texture_f, attributes0);

		int[] attributes1 = {0, 1};
		s_pre_color = generate_program(R.raw.s_pre_color_v, R.raw.s_pre_color_f, attributes1);

		int[] attributes2 = {0, 1, 2};
		s_pre_texture = generate_program(R.raw.s_pre_texture_v, R.raw.s_pre_texture_f, attributes2);

		Matrix4x4.identity(modelview_matrix);
		Matrix4x4.translate(modelview_matrix, 0, 0, -1);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{
		if (loop == null)
		{
			screen_width = width;
			screen_height = height;
			
			Matrix4x4.orthographic(orthographic_matrix, 0, screen_width, screen_height, 0, 0, 1);
			Matrix4x4.multiply(screen_mvp_matrix, orthographic_matrix, modelview_matrix);
			
			int potentialwidth = screen_width / render_width;
			int potentialheight = screen_height / render_height;
			
			input_scale = potentialwidth < potentialheight ? potentialwidth : potentialheight;

			if (!load_options())
			{
				render_scale = input_scale;
				button_scale = (int)(render_scale * 4);
				
				save_options();
			}
		}

		frame_buffer = new FrameBuffer[1];
		frame_buffer[0] = new FrameBuffer(false, GLES20.GL_NEAREST, render_width, render_height);
		
		Matrix4x4.orthographic(orthographic_matrix, 0, render_width, render_height, 0, 0, 1);
		Matrix4x4.multiply(post_mvp_matrix0, orthographic_matrix, modelview_matrix);

		geometry_screen = new GeometryBuffer(4, 6);
		geometry_screen.add(Vertex.crectangle(screen_width / 2 + 1, screen_height / 2, render_width * (render_scale / 2.0f), render_height * (render_scale / 2.0f), 1, 1, 1), 4);
		geometry_screen.end();

		geometry_post0 = new GeometryBuffer(4, 6);
		geometry_post0.add(Vertex.rectangle(0, 0, render_width, render_height), 4);
		geometry_post0.end();

		if (loop == null)
		{
			loop_run = new LoopRun(this, view.game);
			loop_title = new LoopTitle(this, view.game);
			loop_story = new LoopStory(this, view.game);
			loop_loadfile = new LoopLoad(this, view.game);
			loop_new = new LoopNewGame(this, view.game);
			loop_pause = new LoopPause(this, view.game);
			loop_options = new LoopOptions(this, view.game);
			loop_ending = new LoopEnding(this, view.game);

			loop = loop_title;
		}
	}

	public boolean load_options()
	{
		try
		{
			File file = view.context.getFileStreamPath("options_file.dat");

			if (file.exists())
			{
				DataInputStream dis = new DataInputStream(view.context.openFileInput("options_file.dat"));

				render_scale = dis.readFloat();
				button_scale = dis.readInt();
				view.game.music.fullvolume = dis.readFloat();

				dis.close();
				return true;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return false;
	}

	public void save_options()
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(view.context.openFileOutput("options_file.dat", Context.MODE_PRIVATE));

			dos.writeFloat(render_scale);
			dos.writeInt(button_scale);
			dos.writeFloat(view.game.music.fullvolume);

			dos.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void sleep()
	{
		sleep_time = (int)(fixed_step - (System.currentTimeMillis() - begin_time));

		if (sleep_time > 0) 
		{
			try 
			{
				Thread.sleep(sleep_time);
			} 
			catch (Exception e)
			{

			}
		}

		begin_time = System.currentTimeMillis();
	}

	@Override
	public void onDrawFrame(GL10 glUnused) 
	{
		sleep();

		loop.update();
		loop.draw();
	}

	private void render_level()
	{
		//particles
		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		view.game.ptc.render(geometry_buffer);
		geometry_buffer.end();
		draw_batch(geometry_buffer);

		//background
		set_shader(s_pre_texture);
		mvp_matrix();

		u_texture(0, t_background);
		draw_batch(view.game.geometry_grid);

		//ambient
		geometry_buffer.begin();

		for (int i = 0; i < view.game.ambient.size(); i++)
			view.game.ambient.get(i).render(geometry_buffer);

		geometry_buffer.end();
		draw_batch(geometry_buffer);

		//sprites
		GLES20.glEnable(GLES20.GL_BLEND);

		geometry_buffer.begin();

		for (int i = 0; i < view.game.sprites.sprites.size(); i++)
			view.game.sprites.sprites.get(i).render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_sprites);
		draw_batch(geometry_buffer);

		GLES20.glDisable(GLES20.GL_BLEND);
	}

	private void render_hud()
	{
		//rectangles
		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();

		for (int i = 0; i < WorldExecutor.level_h; i++)
		{
			if (view.game.borderl[i] == 1) geometry_buffer.add(Vertex.rectangle(30, WorldExecutor.grid_scale * i, 2, WorldExecutor.grid_scale, view.game.cr, view.game.cg, view.game.cb), 4);
			if (view.game.borderr[i] == 1) geometry_buffer.add(Vertex.rectangle(render_width - 32, WorldExecutor.grid_scale * i, 2, WorldExecutor.grid_scale, view.game.cr, view.game.cg, view.game.cb), 4);
		}

		if (view.game.message.render_bar() > 0)
			geometry_buffer.add(Vertex.rectangle(30, 0, WorldExecutor.gridwidth + 4, Vertex.font_size_y + 1, 0, 0, 0), 4);

		geometry_buffer.end();
		draw_batch(geometry_buffer);

		//text
		String ms = view.game.message.render_text();

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		if (ms != null)
			Vertex.add_text(geometry_buffer, ms, 30, 0, 0.7490f, 0.7970f, 0.5411f);

		if (view.game.you.target != null)
			Vertex.add_text(geometry_buffer, "enmy" + Integer.toString((int)((float)view.game.you.targethprender / view.game.you.target.hplimit * 100)), render_width - 7 * Vertex.font_size_x, render_height - Vertex.font_size_y - 4, 0.7490f, 0.7970f, 0.5411f);
		else
			Vertex.add_text(geometry_buffer, "enmy", render_width - 7 * Vertex.font_size_x, render_height - Vertex.font_size_y - 4, 0.7490f, 0.7970f, 0.5411f);

		Vertex.add_text(geometry_buffer, "you " + Integer.toString((int)Math.round(((float)view.game.you.hprender / view.game.you.hplimit * 100))), 0, render_height - Vertex.font_size_y - 4, 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);
	}

	public void draw()
	{
		frame_buffer(0);

		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);
		render_level();

		orthographic_matrix(0, 0);
		render_hud();

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		GLES20.glEnable(GLES20.GL_BLEND);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_run.i_pos.render(geometry_buffer);
		loop_run.i_bleft.render(geometry_buffer);
		loop_run.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);

		GLES20.glDisable(GLES20.GL_BLEND);
	}

	public void draw_title()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//text
		orthographic_matrix(0, 0);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		Vertex.add_text(geometry_buffer, LoopTitle.title,   loop_title.optionsposx[0], loop_title.optionsposy[0], 0.8941f, 0.4156f, 0.1098f);
		Vertex.add_text(geometry_buffer, LoopTitle.newgame, loop_title.optionsposx[1], loop_title.optionsposy[1], 0.7490f, 0.7970f, 0.5411f);
		Vertex.add_text(geometry_buffer, LoopTitle.load,    loop_title.optionsposx[2], loop_title.optionsposy[2], 0.7490f, 0.7970f, 0.5411f);
		Vertex.add_text(geometry_buffer, LoopTitle.options, loop_title.optionsposx[3], loop_title.optionsposy[3], 0.7490f, 0.7970f, 0.5411f);
		Vertex.add_text(geometry_buffer, ">", loop_title.optionsposx[1] - Vertex.font_size_x, loop_title.optionsposy[loop_title.selectionid + 1], 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_title.i_blleft.render(geometry_buffer);
		loop_title.i_bleft.render(geometry_buffer);
		loop_title.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);
	}

	public void draw_loadfile()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//text
		orthographic_matrix(0, 0);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();
		
		Vertex.add_text(geometry_buffer, loop_loadfile.optionsstring[0], loop_loadfile.optionsposx[0], loop_loadfile.optionsposy[0], 0.8941f, 0.4156f, 0.1098f);
		for (int i = 1; i < loop_loadfile.filesfound + 2; i++)
			Vertex.add_text(geometry_buffer, loop_loadfile.optionsstring[i], loop_loadfile.optionsposx[i], loop_loadfile.optionsposy[i], 0.7490f, 0.7970f, 0.5411f);

		if (loop_loadfile.filesfound > 0)
			Vertex.add_text(geometry_buffer, ">", loop_loadfile.optionsposx[1] - Vertex.font_size_x, loop_loadfile.optionsposy[loop_title.selectionid + 1], 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_title.i_blleft.render(geometry_buffer);
		loop_title.i_bleft.render(geometry_buffer);
		loop_title.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);
	}

	public void draw_newgame()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//text
		orthographic_matrix(0, 0);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		Vertex.add_text(geometry_buffer, loop_new.optionsstring[0], loop_new.optionsposx[0], loop_new.optionsposy[0], 0.8941f, 0.4156f, 0.1098f);
		for (int i = 1; i < 6; i++)
			Vertex.add_text(geometry_buffer, loop_new.optionsstring[i], loop_new.optionsposx[i], loop_new.optionsposy[i], 0.7490f, 0.7970f, 0.5411f);

		Vertex.add_text(geometry_buffer, ">", loop_new.optionsposx[1] - Vertex.font_size_x, loop_new.optionsposy[loop_title.selectionid + 1], 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_title.i_blleft.render(geometry_buffer);
		loop_title.i_bleft.render(geometry_buffer);
		loop_title.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);
	}

	public void draw_pause()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//text
		orthographic_matrix(0, 0);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		Vertex.add_text(geometry_buffer, loop_pause.optionsstring[0], loop_pause.optionsposx[0], loop_pause.optionsposy[0], 0.8941f, 0.4156f, 0.1098f);
		for (int i = 1; i < 4; i++)
			Vertex.add_text(geometry_buffer, loop_pause.optionsstring[i], loop_pause.optionsposx[i], loop_pause.optionsposy[i], 0.7490f, 0.7970f, 0.5411f);

		Vertex.add_text(geometry_buffer, ">", loop_pause.optionsposx[1] - Vertex.font_size_x, loop_pause.optionsposy[loop_title.selectionid + 1], 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_title.i_blleft.render(geometry_buffer);
		loop_title.i_bleft.render(geometry_buffer);
		loop_title.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);
	}

	public void draw_options()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//text
		orthographic_matrix(0, 0);

		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		Vertex.add_text(geometry_buffer, loop_options.optionsstring[0], loop_options.optionsposx[0], loop_options.optionsposy[0], 0.8941f, 0.4156f, 0.1098f);
		for (int i = 1; i < 5; i++)
			Vertex.add_text(geometry_buffer, loop_options.optionsstring[i], loop_options.optionsposx[i], loop_options.optionsposy[i], 0.7490f, 0.7970f, 0.5411f);

		Vertex.add_text(geometry_buffer, ">", loop_options.optionsposx[1] - Vertex.font_size_x, loop_options.optionsposy[loop_title.selectionid + 1], 0.7490f, 0.7970f, 0.5411f);

		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		draw_screen(s_post_texture, frame_buffer[0].texture());

		//input
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();

		loop_title.i_blleft.render(geometry_buffer);
		loop_title.i_bleft.render(geometry_buffer);
		loop_title.i_bright.render(geometry_buffer);

		geometry_buffer.end();

		u_texture(0, t_input);
		draw_batch(geometry_buffer);
	}

	public void draw_story()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//rectangles
		orthographic_matrix(0, 0);

		geometry_buffer.begin();
		geometry_buffer.add(Vertex.rectangle(30, 0, WorldExecutor.gridwidth + 4, Vertex.font_size_y + 1, 0, 0, 0), 4);
		geometry_buffer.end();
		draw_batch(geometry_buffer);

		//text
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();
		Vertex.add_text(geometry_buffer, view.game.message.render_text(), 30, 0, 0.7490f, 0.7970f, 0.5411f);
		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		//depictions
		geometry_buffer.begin();

		if (loop_story.storyindex == 0) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 1) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(280, 50, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 2) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(200, 90, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 3) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(160, 110, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}

		geometry_buffer.end();
		
		GLES20.glEnable(GLES20.GL_BLEND);

		u_texture(0, t_story);
		draw_batch(geometry_buffer);
		
		GLES20.glDisable(GLES20.GL_BLEND);

		//final render
		draw_screen(s_post_texture, frame_buffer[0].texture());
	}

	public void draw_ending()
	{
		frame_buffer(0);

		//particles
		orthographic_matrix(view.game.viewoffsetx, view.game.viewoffsety);

		set_shader(s_pre_color);
		mvp_matrix();

		geometry_buffer.begin();
		loop_title.ptc.render(geometry_buffer);
		geometry_buffer.end();

		draw_batch(geometry_buffer);

		//rectangles
		orthographic_matrix(0, 0);

		geometry_buffer.begin();
		geometry_buffer.add(Vertex.rectangle(30, 0, WorldExecutor.gridwidth + 4, Vertex.font_size_y + 1, 0, 0, 0), 4);
		geometry_buffer.end();
		draw_batch(geometry_buffer);

		//text
		set_shader(s_pre_texture);
		mvp_matrix();

		geometry_buffer.begin();
		Vertex.add_text(geometry_buffer, view.game.message.render_text(), 30, 0, 0.7490f, 0.7970f, 0.5411f);
		geometry_buffer.end();

		u_texture(0, t_font);
		draw_batch(geometry_buffer);

		//depictions
		geometry_buffer.begin();

		if (loop_story.storyindex == 3) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 2) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(280, 50, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 1) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(200, 90, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}
		else if (loop_story.storyindex == 0) 
		{
			geometry_buffer.add(Vertex.sprite(140, 100, 32, 32, 0.5f, 0, 0, 1, 1, 1, 1, 1), 4);
			geometry_buffer.add(Vertex.sprite(160, 110, 32, 32, 0.5f, 1, 0, 2, 1, 1, 1, 1), 4);
		}

		geometry_buffer.end();

		u_texture(0, t_story);
		draw_batch(geometry_buffer);

		//final render
		draw_screen(s_post_texture, frame_buffer[0].texture());
	}

	private void frame_buffer(int i)
	{
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame_buffer[i].frame_buffer());

		GLES20.glViewport(0, 0, frame_buffer[i].width, frame_buffer[i].height);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}

	private void draw_screen(Shader shader, int texture0)
	{
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		GLES20.glViewport(0, 0, screen_width, screen_height);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		set_shader(shader);
		GLES20.glUniformMatrix4fv(uniform("u_mvp"), 1, false, screen_mvp_matrix, 0);

		u_texture(0, texture0);
		draw_batch(geometry_screen);
	}

	private void draw_batch(GeometryBuffer gb)
	{
		s_current.pointers(gb);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, gb.ic(), GLES20.GL_UNSIGNED_SHORT, gb.ib());
	}

	private void orthographic_matrix(float x, float y)
	{
		Matrix4x4.identity(modelview_matrix);
		Matrix4x4.translate(modelview_matrix, x, y, -1);

		Matrix4x4.multiply(mvp_matrix, orthographic_matrix, modelview_matrix);
	}

	private void mvp_matrix()
	{
		GLES20.glUniformMatrix4fv(uniform("u_mvp"), 1, false, mvp_matrix, 0);
	}

	private void u_texture(int i, int t)
	{
		switch (i)
		{
		case 0:
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t);
			GLES20.glUniform1i(uniform("u_texture0"), 0);
			break;
		case 1:
			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t);
			GLES20.glUniform1i(uniform("u_texture1"), 1);
			break;
		case 2:
			GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t);
			GLES20.glUniform1i(uniform("u_texture2"), 2);
			break;
		case 3:
			GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t);
			GLES20.glUniform1i(uniform("u_texture3"), 3);
			break;
		case 4:
			GLES20.glActiveTexture(GLES20.GL_TEXTURE4);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t);
			GLES20.glUniform1i(uniform("u_texture4"), 4);
			break;
		}
	}

	private int uniform(String u)
	{
		return GLES20.glGetUniformLocation(s_current.id, u);
	}

	private void set_shader(Shader s)
	{
		s_current = s;
		s_current.use();
	}

	private int generate_shader(String s, int t)
	{
		int shader = GLES20.glCreateShader(t);

		GLES20.glShaderSource(shader, s);
		GLES20.glCompileShader(shader);

		int[] status = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);

		if (status[0] == 0) 
		{
			android.util.Log.e("printout", "could not compile shader " +  t + ": " + s);
			android.util.Log.e("printout", "info log: " + GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);

			throw new RuntimeException("error compiling shader");
		}

		return shader;
	}

	private Shader generate_program(int v, int f, int[] attributes)
	{
		int vertex = generate_shader(Art.load_text(view.context, v), GLES20.GL_VERTEX_SHADER);
		int fragment = generate_shader(Art.load_text(view.context, f), GLES20.GL_FRAGMENT_SHADER);

		int program = GLES20.glCreateProgram();

		GLES20.glAttachShader(program, vertex);			
		GLES20.glAttachShader(program, fragment);

		for (int i = 0; i < attributes.length; i++)
			GLES20.glBindAttribLocation(program, i, Vertex.attribute_name[attributes[i]]);

		GLES20.glLinkProgram(program);

		int[] status = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);

		if (status[0] == 0) 
		{
			android.util.Log.e("printout", "could not link program: ");
			android.util.Log.e("printout", GLES20.glGetProgramInfoLog(program));
			GLES20.glDeleteProgram(program);

			throw new RuntimeException("error compiling shader program");
		}

		return new Shader(program, attributes);
	}
}
package com.gameinbucket.spaceobelisk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;

import com.gameinbucket.render.Art;
import com.gameinbucket.render.GeometryBuffer;
import com.gameinbucket.render.Vertex;
import com.gameinbucket.sound.MusicExecutor;
import com.gameinbucket.sound.SoundPoolExecutor;
import com.gameinbucket.spaceobelisk.R;
import com.gameinbucket.spaceobelisk.sprite.Ambient;
import com.gameinbucket.spaceobelisk.sprite.MessageExecutor;
import com.gameinbucket.spaceobelisk.sprite.ParticleExecutor;
import com.gameinbucket.spaceobelisk.sprite.Sprite;
import com.gameinbucket.spaceobelisk.sprite.SpriteSheet;
import com.gameinbucket.spaceobelisk.sprite.adversary.Infector;
import com.gameinbucket.spaceobelisk.sprite.adversary.Follower;
import com.gameinbucket.spaceobelisk.sprite.adversary.Obelisk;
import com.gameinbucket.spaceobelisk.sprite.adversary.Portal;
import com.gameinbucket.spaceobelisk.sprite.adversary.WarMachine;
import com.gameinbucket.spaceobelisk.sprite.adversary.You;
import com.gameinbucket.spaceobelisk.sprite.adversary.Ziggurat;
import com.gameinbucket.spaceobelisk.sprite.SfxExecutor;

public class WorldExecutor
{
	//public final static String c0 = "i can move using the left side of the screen as a joystick";
	//public final static String c1 = "i can shoot using the buttons on the right";
	//public final static String c2 = "i can save by landing on a repair station";
	public final static String c3 = "neighboring gateways destroyed";
	public final static String cstart = "i must find the center of this planet";
	public final static String cdestroy = "obelisk defeated!";
	public final static Random frandom = new Random();

	public boolean c0used;
	public boolean c1used;
	public boolean c2used;

	public boolean[] kused = new boolean[4];

	public int savefile;

	public static final float time = 1000.0f / 60.0f;
	public static final int grid_scale = 8;

	public static final int level_w = 32;
	public static final int level_h = 22;
	public static final int level_length = level_w * level_h;

	public static final int gridwidth = grid_scale * level_w;
	public static final int gridheight = grid_scale * level_h;

	public final ViewExecutor view;

	private int[] overworld;
	public float[][] tile;
	public GeometryBuffer geometry_grid;
	float[] translation_tile;
	public int[] grid;
	public int[] borderl;
	public int[] borderr;

	private int levelx;
	private int levely;
	public float cr;
	public float cg;
	public float cb;

	public final int viewoffsetx;
	public int viewoffsety;

	public final float damp_bias;

	public final SpriteSheet background;
	public final SpriteSheet sprites;

	public final SfxExecutor sfx;
	public final ParticleExecutor ptc;

	public final ArrayList<Sprite> allies;
	public final ArrayList<Sprite> enemies;
	public final ArrayList<Sprite> emissiles;
	public final ArrayList<Sprite> fmissiles;
	public final ArrayList<Sprite> sfxs;
	public final ArrayList<Ambient> ambient;

	public boolean levelsaved;
	public Ambient cryotube;
	public int obeliskdestroyed;
	public You you;

	public final MessageExecutor message;
	public final SoundPoolExecutor audio;
	public final MusicExecutor music;

	public WorldExecutor(ViewExecutor v)
	{
		view = v;

		background = new SpriteSheet(0.125f, 8);
		sprites = new SpriteSheet(0.125f, 8);

		sfx = new SfxExecutor(this, sprites);
		ptc = new ParticleExecutor();

		message = new MessageExecutor(this);

		music = new MusicExecutor(view.context);
		audio = new SoundPoolExecutor(10);
		audio.load(view.context, R.raw.a_jump0);
		audio.load(view.context, R.raw.a_jump1);
		audio.load(view.context, R.raw.a_laser0);
		audio.load(view.context, R.raw.a_laser1);
		audio.load(view.context, R.raw.a_death0);
		audio.load(view.context, R.raw.a_destroy);

		damp_bias = (float)Math.pow(0.988f, time);

		viewoffsetx = 160 - level_w * grid_scale / 2;
		viewoffsety = 0;

		grid = new int[level_length];
		tile = Vertex.vertices(16, 4);
		translation_tile = new float[tile[0].length];

		borderl = new int[level_h];
		borderr = new int[level_h];

		//generate tiles
		for (int i = 0; i < 5; i++)
		{
			float t0 = i * background.scale;
			float t1 = (i + 1) * background.scale;

			Vertex.vertex(tile[i], 0, 0, grid_scale, t0, background.scale);
			Vertex.vertex(tile[i], 1, 0, 0, t0, 0);
			Vertex.vertex(tile[i], 2, grid_scale, 0, t1, 0);
			Vertex.vertex(tile[i], 3, grid_scale, grid_scale, t1, background.scale);
		}

		Vertex.vertex(tile[5], 0, 0, grid_scale, 5 * background.scale, 3 * background.scale);
		Vertex.vertex(tile[5], 1, 0, 0, 5 * background.scale, 2 * background.scale);
		Vertex.vertex(tile[5], 2, grid_scale, 0, 6 * background.scale, 2 * background.scale);
		Vertex.vertex(tile[5], 3, grid_scale, grid_scale, 6 * background.scale, 3 * background.scale);

		Vertex.vertex(tile[6], 0, 0, grid_scale, 6 * background.scale, 2 * background.scale);
		Vertex.vertex(tile[6], 1, 0, 0, 6 * background.scale, 3 * background.scale);
		Vertex.vertex(tile[6], 2, grid_scale, 0, 7 * background.scale, 3 * background.scale);
		Vertex.vertex(tile[6], 3, grid_scale, grid_scale, 7 * background.scale, 2 * background.scale);

		Vertex.vertex(tile[7], 0, 0, grid_scale, 7 * background.scale, 2 * background.scale);
		Vertex.vertex(tile[7], 1, 0, 0, 7 * background.scale, 3 * background.scale);
		Vertex.vertex(tile[7], 2, grid_scale, 0, 8 * background.scale, 3 * background.scale);
		Vertex.vertex(tile[7], 3, grid_scale, grid_scale, 8 * background.scale, 2 * background.scale);
		
		for (int i = 0; i < 8; i++)
		{
			float t0 = i * background.scale;
			float t1 = (i + 1) * background.scale;

			Vertex.vertex(tile[i + 8], 0, 0, grid_scale, t0, 4 * background.scale);
			Vertex.vertex(tile[i + 8], 1, 0, 0, t0, 3 * background.scale);
			Vertex.vertex(tile[i + 8], 2, grid_scale, 0, t1, 3 * background.scale);
			Vertex.vertex(tile[i + 8], 3, grid_scale, grid_scale, t1, 4 * background.scale);
		}
		//!generate tiles

		load_overworld();

		allies = new ArrayList<Sprite>();
		enemies = new ArrayList<Sprite>();
		emissiles = new ArrayList<Sprite>();
		fmissiles = new ArrayList<Sprite>();
		sfxs = new ArrayList<Sprite>();
		ambient = new ArrayList<Ambient>();

		obeliskdestroyed = 0;
		you = new You(this, 30 * grid_scale, 15 * grid_scale);

		geometry_grid = new GeometryBuffer(level_length * 4, level_length * 6);
	}

	public void new_game()
	{
		obeliskdestroyed = 0;
		you.respawn();

		levelx = 1;
		levely = 6;

		c0used = false;
		c1used = false;
		c2used = false;

		for (int i = 0; i < kused.length; i++)
			kused[i] = false;

		save_level();
		load_level(false);
	}

	private int save_boolean(boolean b)
	{
		if (b) return 0;
		else return 1;
	}

	private boolean load_boolean(int b)
	{
		if (b == 0) return true;
		else return false;
	}

	public void load_save()
	{
		try
		{
			File file = view.context.getFileStreamPath("save_file" + savefile + ".dat");

			if (file.exists())
			{
				FileInputStream fis = view.context.openFileInput("save_file" + savefile + ".dat");

				levelx = fis.read();
				levely = fis.read();

				c0used = load_boolean(fis.read());
				c1used = load_boolean(fis.read());
				c2used = load_boolean(fis.read());
				
				for (int i = 0; i < kused.length; i++)
					kused[i] = load_boolean(fis.read());

				fis.close();
			}
			else
				save_level();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void save_level()
	{
		try
		{
			FileOutputStream fos = view.context.openFileOutput("save_file" + savefile + ".dat", Context.MODE_PRIVATE);

			fos.write(levelx);
			fos.write(levely);

			fos.write(save_boolean(c0used));
			fos.write(save_boolean(c1used));
			fos.write(save_boolean(c2used));

			for (int i = 0; i < kused.length; i++)
				fos.write(save_boolean(kused[i]));

			fos.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public int cell(int x, int y)
	{
		if (x < 0 || x >= level_w || y < 0 || y >= level_h)
			return 1;

		return grid[x + y * level_w];
	}

	public void shift_level(int x, int y)
	{
		levelx += x;
		levely += y;

		load_level(true);
	}

	public void load_overworld()
	{
		Bitmap image = Art.load_image_data(view.context, R.raw.t_overworld);

		int width = image.getWidth();
		int height = image.getHeight();

		overworld = new int[width * height];
		image.getPixels(overworld, 0, width, 0, 0, width, height);

		image.recycle();

		for (int i = 0; i < overworld.length; i++)
			overworld[i] = Art.rgb(overworld[i]);
	}
	
	private boolean noland(int x, int y)
	{
		if (x < 0 || x >= level_w || y < 0 || y >= level_h)
			return false;

		return (grid[x + y * level_w] == 0xffffff || grid[x + y * level_w] == 1) ? false : true;
	}

	public void load_level(boolean shift)
	{
		sprites.sprites.clear();

		enemies.clear();
		emissiles.clear();
		fmissiles.clear();
		sfxs.clear();
		ptc.clear();
		ambient.clear();

		levelsaved = shift ? false : true;
		cryotube = null;

		sprites.sprites.add(you);

		//grid
		for (int y = 0; y < level_h; y++)
		{
			int yy = level_h - y - 1;

			for (int x = 0; x < level_w; x++)
				grid[x + yy * level_w] = overworld[levelx * (level_w + 1) + x + (levely * (level_h + 1) + y) * 512];
		}

		int dataline = levelx * (level_w + 1) + (levely * (level_h + 1) + level_h) * 512;
		int cstar = overworld[dataline];
		//int cmessage = overworld[dataline + 1];
		//int cmusic = overworld[dataline + 2];
		
		cr = overworld[dataline + 3];
		cg = Art.green((int)cr) / 255.0f;
		cb = Art.blue((int)cr) / 255.0f;
		cr = Art.red((int)cr) / 255.0f;
		//!grid
		
		//palette
		//0.8823f, 0.2274f, 0.9294f (magenta)
		//0.2470f, 0.2039f, 0.4627f (purple)
		//0.8941f, 0.4156f, 0.1098f (orange)
		//0.7490f, 0.7970f, 0.5411f (yellow)
		//!palette

		for (int y = 0; y < level_h; y++)
		{
			switch (grid[y * level_w])
			{
			case 0xffffff:
			case 0x0000ff:
			case 0xff0000:
			case 0x00ff00:
			case 0xff00ff:
				borderl[y] = 1;
				break;
			default:
				borderl[y] = 0;
			}

			switch (grid[level_w - 1 + y * level_w])
			{
			case 0xffffff:
			case 0x00ff00:
			case 0xff00ff:
				borderr[y] = 1;
				break;
			default:
				borderr[y] = 0;
			}
		}

		geometry_grid.begin();

		for (int x = 0; x < level_w; x++)
		{
			for (int y = 0; y < level_h; y++)
			{
				switch (grid[x + y * level_w])
				{
				case 0xffffff:
					boolean u = noland(x, y - 1);
					boolean d = noland(x, y + 1);
					boolean l = noland(x - 1, y);
					boolean r = noland(x + 1, y);
					
					if (u)
					{
						if (d)
						{
							if (l) translation_tile = tile[14].clone();
							else if (r) translation_tile = tile[15].clone();
							else translation_tile = tile[0].clone();
						}
						else if (l)
						{
							if (r) translation_tile = tile[12].clone();
							else translation_tile = tile[8].clone();
						}
						else if (r) translation_tile = tile[9].clone();
						else translation_tile = tile[0].clone();
					}
					else if (d)
					{
						if (l)
						{
							if (r) translation_tile = tile[13].clone();
							else translation_tile = tile[10].clone();
						}
						else if (r) translation_tile = tile[11].clone();
						else translation_tile = tile[0].clone();
					}
					else translation_tile = tile[0].clone();
					
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 1;
					break;
				case 0xcccccc:
					grid[x + y * level_w] = 1;
					break;
				case 0xff33ff:
					translation_tile = tile[6].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				case 0x33ff33:
					translation_tile = tile[7].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				case 0xffcccc:
					cryotube = new Ambient(background, x * grid_scale, y * grid_scale, 8, 2, 0, 1, 0.7490f, 0.7970f, 0.5411f);
					ambient.add(cryotube);

					if (!shift)
					{
						you.posx = cryotube.posx + grid_scale / 2;
						you.posy = cryotube.posy + grid_scale + you.hh + 1;
					}

					grid[x + y * level_w] = 0;
					break;
				case 0xccccff:
					Ambient liquid = new Ambient(background, x * grid_scale, y * grid_scale, 8, 0, 0, 1, 0, 0.5843f, 0.9294f);
					ambient.add(liquid);

					grid[x + y * level_w] = 0;
					break;
				case 0xccffcc:
					Ambient liquidceil = new Ambient(background, x * grid_scale, y * grid_scale, 8, 1, 0, 1, 0, 0.5843f, 0.9294f);
					ambient.add(liquidceil);

					grid[x + y * level_w] = 0;
					break;
				case 0xffff00:
					Follower follower = new Follower(this, (x + 0.5f) * grid_scale, y * grid_scale);
					sprites.add(follower);

					grid[x + y * level_w] = 0;
					break;
				case 0x00ffff:
					Infector infector = new Infector(this, (x + 0.5f) * grid_scale, y * grid_scale);
					sprites.add(infector);

					grid[x + y * level_w] = 0;
					break;
				case 0xff0033:
					Obelisk obelisk = new Obelisk(this, (x + 0.5f) * grid_scale, (y + 2) * grid_scale);
					sprites.add(obelisk);

					grid[x + y * level_w] = 0;
					break;
				case 0x3300ff:
					Ziggurat ziggurat = new Ziggurat(this, (x + 1) * grid_scale, (y + 0.7f) * grid_scale);
					sprites.add(ziggurat);

					grid[x + y * level_w] = 0;
					break;
				case 0x00ff33:
					WarMachine warmachine = new WarMachine(this, (x + 0.5f) * grid_scale, (y + 0.5f) * grid_scale);
					sprites.add(warmachine);

					grid[x + y * level_w] = 0;
					break;
				case 0x99ff99:
					Portal ptrl0 = new Portal(this, (x + 0.5f) * grid_scale, (y + 1) * grid_scale, cr, cg, cb, 0);
					sprites.add(ptrl0);

					grid[x + y * level_w] = 0;
					break;
				case 0x66ff66:
					if (kused[0])
						grid[x + y * level_w] = 0;
					else
					{
						translation_tile = tile[5].clone();
						Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
						Vertex.color(translation_tile, cr, cg, cb, 1.0f);

						geometry_grid.add(translation_tile, 4);
						grid[x + y * level_w] = 1;
					}
					break;
				case 0x9999ff:
					Portal ptrl1 = new Portal(this, (x + 0.5f) * grid_scale, (y + 1) * grid_scale, cr, cg, cb, 1);
					sprites.add(ptrl1);

					grid[x + y * level_w] = 0;
					break;
				case 0x6666ff:
					if (kused[1])
						grid[x + y * level_w] = 0;
					else
					{
						translation_tile = tile[5].clone();
						Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
						Vertex.color(translation_tile, cr, cg, cb, 1.0f);

						geometry_grid.add(translation_tile, 4);
						grid[x + y * level_w] = 1;
					}
					break;
				case 0xff9999:
					Portal ptrl2 = new Portal(this, (x + 0.5f) * grid_scale, (y + 1) * grid_scale, cr, cg, cb, 2);
					sprites.add(ptrl2);

					grid[x + y * level_w] = 0;
					break;
				case 0xff6666:
					if (kused[2])
						grid[x + y * level_w] = 0;
					else
					{
						translation_tile = tile[5].clone();
						Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
						Vertex.color(translation_tile, cr, cg, cb, 1.0f);

						geometry_grid.add(translation_tile, 4);
						grid[x + y * level_w] = 1;
					}
					break;
				case 0x99ffff:
					Portal ptrl3 = new Portal(this, (x + 0.5f) * grid_scale, (y + 1) * grid_scale, cr, cg, cb, 3);
					sprites.add(ptrl3);

					grid[x + y * level_w] = 0;
					break;
				case 0x66ffff:
					if (kused[3])
						grid[x + y * level_w] = 0;
					else
					{
						translation_tile = tile[5].clone();
						Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
						Vertex.color(translation_tile, cr, cg, cb, 1.0f);

						geometry_grid.add(translation_tile, 4);
						grid[x + y * level_w] = 1;
					}
					break;
				case 0x0000ff:
					translation_tile = tile[1].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				case 0xff0000:
					translation_tile = tile[2].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				case 0x00ff00:
					translation_tile = tile[3].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				case 0xff00ff:
					translation_tile = tile[4].clone();
					Vertex.translate(translation_tile, x * grid_scale, y * grid_scale, 0);
					Vertex.color(translation_tile, cr, cg, cb, 1.0f);

					geometry_grid.add(translation_tile, 4);
					grid[x + y * level_w] = 0;
					break;
				}
			}
		}

		if (cstar == 0xffff00)
		{
			Random rr = new Random(207879576 + levelx * levelx + levely * levely);
			for (int i = 0; i < 100; i++)
				ptc.star(rr.nextInt(gridwidth), rr.nextInt(gridheight));
		}

		if (music.switchtrack(R.raw.obelisk_title))
			music.fadeload(R.raw.obelisk_title);
			
		/*int track = -1;
		switch (cmusic)
		{
		case 0x0000ff: track = R.raw.obelisk_story; break;
		case 0x00ff00: track = R.raw.obelisk_title; break;
		}

		if (track != -1 && music.switchtrack(track))
		{
			music.fadeload(track);
			//audio.play(R.raw.a_jump1);
		}*/

		/*String ms = null;

		if (!c0used && cmessage == 0xff0000) {ms = c0; c0used = true;}
		else if (!c1used && cmessage == 0x00ff00) {ms = c1; c1used = true;}
		else if (!c2used && cmessage == 0x0000ff) {ms = c2; c2used = true;}
		message.set(ms);*/
		if (levelx == 1 && levely == 6) message.set(cstart);
		else message.set(null);

		geometry_grid.end();
	}

	public void loop()
	{
		if (viewoffsety > 0)
			viewoffsety--;

		if (obeliskdestroyed > 0)
		{
			obeliskdestroyed--;

			if (obeliskdestroyed == 0)
			{
				view.render.loop_title.open();
				view.render.loop = view.render.loop_title;
				
				//view.render.loop_ending.open();
				//view.render.loop = view.render.loop_ending;
			}
		}

		if (you.regen > 0)
		{
			you.regen--;

			if (you.regen == 0)
			{
				you.respawn();
				load_save();
				load_level(false);
				return;
			}
		}

		music.integrate();
		message.integrate();
		ptc.integrate();

		for (int i = 0; i < ambient.size(); i++)
			ambient.get(i).next_frame();

		for (int i = 0; i < sprites.sprites.size(); i++)
		{
			if (sprites.sprites.get(i).integrate(time))
			{
				sprites.sprites.remove(i);
				i--;
			}
		}
	}
}
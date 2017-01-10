package com.gameinbucket.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundPoolExecutor 
{
	private final SoundPool pool;
	private final SparseIntArray index;

	public SoundPoolExecutor(int stream_limit)
	{
		index = new SparseIntArray();
		pool = new SoundPool(stream_limit, AudioManager.STREAM_MUSIC, 0);
	}

	public void load(Context c, int r)
	{
		index.put(r, pool.load(c, r, 1));
	}

	public void play(int r)
	{
		pool.play(index.get(r), 1, 1, 1, 0, 1);
	}

	public void play(int r, float gain)
	{
		pool.play(index.get(r), gain, gain, 1, 0, 1);
	}

	public void play(int r, float gain, float pan)
	{
		float left = Math.max(0, Math.min(1, gain * Math.max(0, Math.min(1, 1 - pan))));
		float right = Math.max(0, Math.min(1, gain * Math.max(0, Math.min(1, 1 + pan))));

		pool.play(index.get(r), left * gain, right * gain, 1, 0, 1);
	}

	public void play(int r, float gain, float pan, float pitch)
	{
		float rate = Math.max(0.5f, Math.min(2, pitch));

		float left = Math.max(0, Math.min(1, gain * Math.max(0, Math.min(1, 1 - pan))));
		float right = Math.max(0, Math.min(1, gain * Math.max(0, Math.min(1, 1 + pan))));

		pool.play(index.get(r), left * gain, right * gain, 1, 0, rate);
	}
}
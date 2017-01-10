package com.gameinbucket.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SynthExecutor
{
	AudioTrack audio;

	public void load(BasicOscillator osc)
	{
		byte[] sample_bytes = new byte[BasicOscillator.BUFFER_SIZE];

		osc.generate_samples(sample_bytes);

		audio = new AudioTrack(AudioManager.STREAM_MUSIC, (int)BasicOscillator.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, sample_bytes.length, AudioTrack.MODE_STATIC);
		audio.write(sample_bytes, 0, sample_bytes.length);
	}

	public void play()
	{
		switch (audio.getPlayState()) 
		{
		case AudioTrack.PLAYSTATE_PAUSED:
		case AudioTrack.PLAYSTATE_PLAYING:
			audio.stop();
			audio.reloadStaticData();
			audio.play();
			break;
		case AudioTrack.PLAYSTATE_STOPPED:
			audio.reloadStaticData();
			audio.play();
			break;
		}
	}
}

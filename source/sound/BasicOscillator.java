package com.gameinbucket.sound;

public class BasicOscillator
{
	public static final short WAVE_SIN = 0;
	public static final short WAVE_SQR = 1;
	public static final short WAVE_SAW = 2;
	public static final short WAVE_TRI = 3;

	public static final int SAMPLE_RATE = 22050;

	public static final int BUFFER_SIZE = 1000;
	public static final int SAMPLES_PER_BUFFER = BUFFER_SIZE / 2;

	private short waveshape;
	private long periodSamples;
	private long sampleNumber;

	public BasicOscillator(short wave, double frequency)
	{
		waveshape = wave;
		frequency(frequency);    
	}

	public void frequency(double frequency)
	{
		periodSamples = (long)(SAMPLE_RATE / frequency);
	}

	protected double sample()
	{   
		double value;  
		double x = sampleNumber / (double)periodSamples;

		switch (waveshape)
		{
		default:
		case WAVE_SIN:
			value = Math.sin(2.0 * Math.PI * x);
			break;
		case WAVE_SQR:
			if (sampleNumber < (periodSamples / 2))
				value = 1.0;
			else
				value = -1.0;
			break;
		case WAVE_SAW:
			value = 2.0 * (x - Math.floor(x + 0.5));
			break;
		}

		sampleNumber = (sampleNumber + 1) % periodSamples;
		return value;
	}

	public int generate_samples(byte[] buffer)
	{
		int index = 0;

		for (int i = 0; i < SAMPLES_PER_BUFFER; i++)
		{
			double ds = sample() * Short.MAX_VALUE;
			short ss = (short)Math.round(ds);

			buffer[index++] = (byte)(ss >> 8);
			buffer[index++] = (byte)(ss & 0xff);      
		}

		return BUFFER_SIZE;
	}
}
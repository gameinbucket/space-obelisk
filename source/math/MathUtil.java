package com.gameinbucket.math;

public abstract class MathUtil
{
	//private static long m_w = 521288629;
	//private static long m_z = 362436069;
	//private static final double magic = 1.0 / (4294967296.0 + 2.0); //4294967296 = 2 ^ 32

	private MathUtil()
	{

	}

	/*static unsigned long next = 1;

	int myrand() 
	{
	    next = next * 1103515245 + 12345;
	    return((unsigned)(next/65536) % 32768);
	}

	public static void set_random_seed(long w)
	{
		if (w != 0)
			m_w = w;
	}

	public static void set_random_seeds(long w, long z)
	{
		if (w != 0) m_w = w;
		if (z != 0) m_z = z;
	}

	public static void set_random_seeds_system_time()
	{
		long x = System.currentTimeMillis();
		set_random_seeds(x >> 16, x % 4294967296L);
	}

	public static double random_double(int n)
	{
		n = (n >> 13) ^ n;

		int nn = (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
		return 1.0 - ((double)nn / 1073741824.0);
	}

	public static long random_long()
	{
		m_z = 36969 * (m_z & 65535) + (m_z >> 16);
		m_w = 18000 * (m_w & 65535) + (m_w >> 16);

		return (m_z << 16) + m_w;
	}

	public static float random_float()
	{
		long u = random_long();
		return (float)((u + 1.0) * magic);
	}*/

	public static float average(float a, float b)
	{
		return (a + b) / 2;
	}

	public static float saturate(float a)
	{
		if (a < 0) return 0;
		else if (a > 1) return 1;
		return a;
	}

	public static float lerp(float a, float b, float w)
	{
		return (a + (b - a) * w);
	}

	public static float smoothstep(float a, float b, float w)
	{
		w = saturate((w - a) / (b - a)); 
		return w * w * (3 - 2 * w);
	}

	public static float radian(float degree)
	{
		return degree * 0.017453292519943f;
	}

	public static float degree(float radian)
	{
		float degree = radian * 57.29577951308232f;

		while (degree >= 360) degree -= 360;
		while (degree < 0) degree += 360;

		return degree;
	}

	/*public static final float sin(float rad)
	   {
	      return sin[(int) (rad * radToIndex) & SIN_MASK];
	   }

	   public static final float cos(float rad)
	   {
	      return cos[(int) (rad * radToIndex) & SIN_MASK];
	   }

	   public static final float sinDeg(float deg)
	   {
	      return sin[(int) (deg * degToIndex) & SIN_MASK];
	   }

	   public static final float cosDeg(float deg)
	   {
	      return cos[(int) (deg * degToIndex) & SIN_MASK];
	   }

	   public static final int FIXED_POINT = 16;
	   public static final int ONE = 1 << FIXED_POINT;

	    public static int mul(int a, int b) {
	        return (int) ((long) a * (long) b >> FIXED_POINT);
	    }

	    public static int toFix( double val ) {
	        return (int) (val * ONE);
	    }

	    public static int intVal( int fix ) {
	        return fix >> FIXED_POINT;
	    }

	    public static double doubleVal( int fix ) {
	        return ((double) fix) / ONE;
	    }


	   private static final float   RAD,DEG;
	   private static final int     SIN_BITS,SIN_MASK,SIN_COUNT;
	   private static final float   radFull,radToIndex;
	   private static final float   degFull,degToIndex;
	   private static final float[] sin, cos;

	   static
	   {
	      RAD = (float) Math.PI / 180.0f;
	      DEG = 180.0f / (float) Math.PI;

	      SIN_BITS  = 12;
	      SIN_MASK  = ~(-1 << SIN_BITS);
	      SIN_COUNT = SIN_MASK + 1;

	      radFull    = (float) (Math.PI * 2.0);
	      degFull    = (float) (360.0);
	      radToIndex = SIN_COUNT / radFull;
	      degToIndex = SIN_COUNT / degFull;

	      sin = new float[SIN_COUNT];
	      cos = new float[SIN_COUNT];

	      for (int i = 0; i < SIN_COUNT; i++)
	      {
	         sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
	         cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
	      }

	      // Four cardinal directions (credits: Nate)
	      for (int i = 0; i < 360; i += 90)
	      {
	         sin[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * Math.PI / 180.0);
	         cos[(int)(i * degToIndex) & SIN_MASK] = (float)Math.cos(i * Math.PI / 180.0);
	      }
	   }
	 */

	public static void print(String t)
	{
		android.util.Log.e("print", t);
	}
}
package engine.graph.hlib.graphics.paint;

public class PerlinNoise {
	private int octaves;
	private double amplitude;
	private double frequency;
	private double persistence;
	private int seed;

	/**
	 * 
	 * @param seed
	 * @param persistence
	 * @param frequency
	 * @param amplitude
	 * @param octaves
	 */
	public PerlinNoise(int seed, double persistence, double frequency, double amplitude, int octaves) {
		set(seed, persistence, frequency, amplitude, octaves);
	}

	public PerlinNoise() {
		set(0, 0, 0, 0, 0);
	}

	/**
	 * Pass in the x and y coordinates that you desire
	 *
	 * @param x
	 * @param y
	 * @return The height of the (x,y) coordinates multiplied by the amplitude
	 */
	public double getHeight(double x, double y) {
		return amplitude * total(x, y);
	}

	public double getHeightO(final double x, final double y) {
		return amplitude * getValue(y * frequency + seed, x * frequency + seed);
	}

	// ========================================================================
	// GETTERS
	// ========================================================================

	public int getSeed() {
		return seed;
	}

	public int getOctaves() {
		return octaves;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getPersistence() {
		return persistence;
	}

	// ========================================================================
	// SETTERS
	// ========================================================================

	/**
	 * Set all of the properties of the noise generator in one swoop
	 * 
	 * @param seed
	 * @param persistence How persistent it is
	 * @param frequency   The frequency level
	 * @param amplitude   The amplitude you want to apply
	 * @param octaves     The octaves of the frequency
	 */
	public final void set(final int seed, final double persistence, double frequency, double amplitude, int octaves) {
		this.seed = 2 + seed * seed;
		this.octaves = octaves;
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.persistence = persistence;
	}

	public void setSeed(int seed) {
		this.seed = 2 + seed * seed;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public void setPersistence(double persistence) {
		this.persistence = persistence;
	}

	// ========================================================================
	// PRIVATE
	// ========================================================================

	private double total(double x, double y) {
		double t = 0.0;
		double amp = 1;
		double freq = frequency;

		for (int k = 0; k < octaves; k++) {
			t += getValue(y * freq + seed, x * freq + seed) * amp;
			amp *= persistence;
			freq *= 2;
		}

		return t;
	}

	private double getValue(final double x, final double y) {
		final int Xint = (int) x, Yint = (int) y;
		final double Xfrac = x - Xint, Yfrac = y - Yint;

		// noise values
		final double n01 = noise(Xint - 1, Yint - 1);
		final double n02 = noise(Xint + 1, Yint - 1);
		final double n03 = noise(Xint - 1, Yint + 1);
		final double n04 = noise(Xint + 1, Yint + 1);
		final double n05 = noise(Xint - 1, Yint);
		final double n06 = noise(Xint + 1, Yint);
		final double n07 = noise(Xint, Yint - 1);
		final double n08 = noise(Xint, Yint + 1);
		final double n09 = noise(Xint, Yint);

		final double n12 = noise(Xint + 2, Yint - 1);
		final double n14 = noise(Xint + 2, Yint + 1);
		final double n16 = noise(Xint + 2, Yint);

		final double n23 = noise(Xint - 1, Yint + 2);
		final double n24 = noise(Xint + 1, Yint + 2);
		final double n28 = noise(Xint, Yint + 2);

		final double n34 = noise(Xint + 2, Yint + 2);

		// find the noise values of the four corners
		final double x0y0 = 0.0625 * (n01 + n02 + n03 + n04) + 0.125 * (n05 + n06 + n07 + n08) + 0.25 * (n09),
				x1y0 = 0.0625 * (n07 + n12 + n08 + n14) + 0.125 * (n09 + n16 + n02 + n04) + 0.25 * (n06),
				x0y1 = 0.0625 * (n05 + n06 + n23 + n24) + 0.125 * (n03 + n04 + n09 + n28) + 0.25 * (n08),
				x1y1 = 0.0625 * (n09 + n16 + n28 + n34) + 0.125 * (n08 + n14 + n06 + n24) + 0.25 * (n04);

		// interpolate between those values according to the x and y fractions
		final double v1 = interpolate(x0y0, x1y0, Xfrac); // interpolate in x
															// direction (y)
		final double v2 = interpolate(x0y1, x1y1, Xfrac); // interpolate in x
															// direction (y+1)
		final double fin = interpolate(v1, v2, Yfrac); // interpolate in y direction

		return fin;
	}

	private double interpolate(final double x, final double y, final double a) {
		final double negA = 1.0 - a;
		final double negASqr = negA * negA;
		final double fac1 = 3.0 * (negASqr) - 2.0 * (negASqr * negA);
		final double aSqr = a * a;
		final double fac2 = 3.0 * aSqr - 2.0 * (aSqr * a);

		return x * fac1 + y * fac2; // add the weighted factors
	}

	private double noise(final int x, final int y) {
		int n = x + y * 57;
		n = (n << 13) ^ n;
		final int t = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff;
		return 1.0 - (double) (t) * 0.931322574615478515625e-9;
	}
}

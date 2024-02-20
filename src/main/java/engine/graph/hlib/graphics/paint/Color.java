package engine.graph.hlib.graphics.paint;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

public class Color implements Paint {

	/**
	 * The color alice blue with an RGB value of #F0F8FF
	 * <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#F0F8FF;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color ALICEBLUE = new Color(0.9411765f, 0.972549f, 1.0f);

	/**
	 * The color black with an RGB value of #000000 <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#000000;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);

	/**
	 * The color cornflower blue with an RGB value of #6495ED
	 * <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#6495ED;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color CORNFLOWERBLUE = new Color(0.39215687f, 0.58431375f, 0.92941177f);

	/**
	 * The color dark gray with an RGB value of #A9A9A9 <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#A9A9A9;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color DARKGRAY = new Color(0.6627451f, 0.6627451f, 0.6627451f);

	/**
	 * The color gray with an RGB value of #808080 <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#808080;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color GRAY = new Color(0.5019608f, 0.5019608f, 0.5019608f);

	/**
	 * The color red with an RGB value of #FF0000 <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#FF0000;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color RED = new Color(1.0f, 0.0f, 0.0f);

	/**
	 * The color white with an RGB value of #FFFFFF <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#FFFFFF;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);

	/**
	 * The color yellow with an RGB value of #FFFF00 <div style="border:1px solid
	 * black;width:40px;height:20px;background-color:#FFFF00;float:right;margin: 0
	 * 10px 0 0"></div>
	 */
	public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f);

	public static ArrayList<Color> needToFree;

	private final NVGColor color;

	private boolean isFreed = false;

	/**
	 * Creates an sRGB color with the specified red, green, blue, and alpha values
	 * in the range (0 - 255).
	 * 
	 * @throws IllegalArgumentException if {@code r}, {@code g}, {@code b} or
	 *                                  {@code a} are outside of the range 0 to 255,
	 *                                  inclusive
	 * @param r the Red value
	 * @param g the Green value
	 * @param b the Blue value
	 * @param a the Alpha value
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getAlpha
	 */
	public Color(final int r, final int g, final int b, final int a) {
		color = NVGColor.create();
		setColor((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
	}

	/**
	 * Creates an opaque sRGB color with the specified red, green, and blue values
	 * in the range (0 - 255). Alpha is set to opaque
	 * 
	 * @throws IllegalArgumentException if {@code r}, {@code g} or {@code b} are
	 *                                  outside of the range 0 to 255, inclusive
	 * @param r the Red value
	 * @param g the Green value
	 * @param b the Blue value
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 */
	public Color(final int r, final int g, final int b) {
		this((float) r / 255, (float) g / 255, (float) b / 255, 1f);
	}

	/**
	 * Creates an sRGB color with the specified red, green, blue, and alpha values
	 * in the range (0 - 1).
	 * 
	 * @throws IllegalArgumentException if {@code r}, {@code g}, {@code b} or
	 *                                  {@code a} are outside of the range 0 to 1,
	 *                                  inclusive
	 * @param r the Red value
	 * @param g the Green value
	 * @param b the Blue value
	 * @param a the Alpha value
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getAlpha
	 */
	public Color(final float r, final float g, final float b, final float a) {
		color = NVGColor.create();
		setColor(r, g, b, a);
	}

	/**
	 * Creates an sRGB color with the specified red, green and blue values in the
	 * range (0 - 1).
	 * 
	 * @throws IllegalArgumentException if {@code r}, {@code g} or {@code b} are
	 *                                  outside of the range 0 to 1, inclusive
	 * @param r the Red value
	 * @param g the Green value 0-1
	 * @param b the Blue value 0-1
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 */
	public Color(final float r, final float g, final float b) {
		this(r, g, b, 1);
	}

	/***
	 * Check if the specified red, green, blue and alpha color are in the 0-1 range
	 * and write the specified color in the Buffer
	 * 
	 * @param r the Red value
	 * @param g the Green value
	 * @param b the Blue value
	 * @param a the Alpha value
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #checkColor
	 */
	public void setColor(final float r, final float g, final float b, final float a) {
		checkColor(r, g, b, a);
		try (final MemoryStack stack = MemoryStack.stackPush()) {
			final FloatBuffer buf = stack.floats(r, g, b, a);
			color.rgba(buf);
		}
		
		
		if(needToFree == null)
			needToFree = new ArrayList<>();
		
		needToFree.add(this);
	}

	/**
	 * Checks the color {@code float} value supplied for validity. Throws an
	 * {@code IllegalArgumentException} if the value is out of range 0-1.
	 * 
	 * @param r the Red value
	 * @param g the Green value
	 * @param b the Blue value
	 * @param a the Alpha value
	 **/
	public void checkColor(final float r, final float g, final float b, final float a) {
		boolean rangeError = false;
		String badComponentString = "";

		if (a < 0 || a > 1) {
			rangeError = true;
			badComponentString += " Alpha";
		}
		if (r < 0 || r > 1) {
			rangeError = true;
			badComponentString += " Red(" + r + ")";
		}
		if (g < 0 || g > 1) {
			rangeError = true;
			badComponentString += " Green(" + g + ")";
		}
		if (b < 0 || b > 1) {
			rangeError = true;
			badComponentString += " Blue(" + b + ")";
		}
		if (rangeError)
			throw new IllegalArgumentException("Color parameter outside of expected range:" + badComponentString);
	}

	/**
	 * Creates a new {@code HColor} that is a darker version of this {@code HColor}.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this {@code HColor} to create a darker version of this
	 * {@code HColor}. The {@code alpha} value is preserved. Although
	 * {@code brighter} and {@code darker} are inverse operations.
	 * 
	 * @return a new {@code HColor} object that is a darker version of this
	 *         {@code HColor} with the same {@code alpha} value.
	 * @see Color#brighter
	 */
	public Color brighter() {
		final float r = getRed(), g = getGreen(), b = getBlue();

		if (r == 0 && g == 0 && b == 0) {
			final float value = .1f / .7f;
			return new Color(value, value, value, getAlpha());
		}
		return new Color(Math.min(r / .7f, 1), Math.min(g / .7f, 1), Math.min(b / .7f, 1), getAlpha());
	}

	/**
	 * Creates a new {@code HColor} that is a darker version of this {@code HColor}.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this {@code HColor} to create a darker version of this
	 * {@code HColor}. The {@code alpha} value is preserved. Although
	 * {@code brighter} and {@code darker} are inverse operations.
	 * 
	 * @return a new {@code HColor} object that is a darker version of this
	 *         {@code HColor} with the same {@code alpha} value.
	 * @see HColor#brighter
	 */
	public Color darker() {
		return new Color(getRed() * .7f, getGreen() * .7f, getBlue() * .7f, getAlpha());
	}

	/**
	 * Returns the red value in the range 0-1 in the default sRGB space.
	 * 
	 * @return the red value.
	 */
	public float getRed() {
		return color.r();
	}

	/**
	 * Returns the green value in the range 0-1 in the default sRGB space.
	 * 
	 * @return the green value.
	 */
	public float getGreen() {
		return color.g();
	}

	/**
	 * Returns the blue value in the range 0-1 in the default sRGB space.
	 * 
	 * @return the blue value.
	 */
	public float getBlue() {
		return color.b();
	}

	/**
	 * Returns the alpha value in the range 0-1 in the default sRGB space.
	 * 
	 * @return the alpha value.
	 */
	public float getAlpha() {
		return color.a();
	}

	/**
	 * Returns a string representation of this {@code Color}. This method is
	 * intended to be used only for debugging purposes. The content and format of
	 * the returned string might vary between implementations. The returned string
	 * might be empty but cannot be {@code null}.
	 *
	 * @return a string representation of this {@code Color}.
	 */
	public String toString() {
		return getClass().getName() + "[r=" + getRed() + ", g=" + getGreen() + ", b=" + getBlue() + ", a=" + getAlpha()
				+ "]";
	}

	/**
	 * 
	 * @return the {@code NVGColor} color
	 */
	public NVGColor getColor() {
		return color;
	}

	/**
	 * Free all the native resources used by this object
	 */
	public void cleanUp(final long ctx) {
		if (!isFreed && color != null) {
			color.free();
			isFreed = true;
		}
	}

	public boolean isColor() {
		return true;
	}

	public static void cleanUpStatic(final long ctx) {
		needToFree.forEach(e -> e.cleanUp(ctx));
	}
}

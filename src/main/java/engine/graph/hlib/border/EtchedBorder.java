package engine.graph.hlib.border;

import java.beans.ConstructorProperties;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;

public class EtchedBorder implements Border {

	/** Raised etched type. */
	public static final int RAISED = 0;

	/** Lowered etched type. */
	public static final int LOWERED = 1;

	/** The type of etch to be drawn by the border. */
	private final int etchType;

	/** The color to use for the etched highlight. */
	private Color highlight;

	/** The color to use for the etched shadow. */
	private Color shadow;

	/**
	 * Creates an etched border with the specified etch-type whose colors will be
	 * derived from Color passed in parameter
	 *
	 * @param etchType The type of etch to be drawn by the border
	 * @param col      The Color to be derived
	 */
	public EtchedBorder(final int etchType, final Color col) {
		this(etchType, col.brighter(), col.darker());
	}

	/**
	 * Creates a lowered etched border with the specified highlight and shadow
	 * colors.
	 *
	 * @param highlight The color to use for the etched highlight
	 * @param shadow    The color to use for the etched shadow
	 */
	public EtchedBorder(final Color highlight, final Color shadow) {
		this(LOWERED, highlight, shadow);
	}

	/**
	 * Creates an etched border with the specified etch-type, highlight and shadow
	 * colors.
	 *
	 * @param etchType  The type of etch to be drawn by the border
	 * @param highlight The color to use for the etched highlight
	 * @param shadow    The color to use for the etched shadow
	 */
	@ConstructorProperties({ "etchType", "highlightColor", "shadowColor" })
	public EtchedBorder(final int etchType, final Color highlight, final Color shadow) {
		this.etchType = etchType;
		this.highlight = highlight;
		this.shadow = shadow;
	}

	/**
	 * Set the {@code highlight} and {@code shadow} Color from the Paint object
	 * passed in parameter
	 * 
	 * @param paint The paint object to be derived
	 */
	public void setPaint(final Paint paint) {
		final Color col = (Color) paint;
		this.highlight = col.brighter();
		this.shadow = col.darker();
	}

	/**
	 * Paints the border for the specified component with the specified position and
	 * size.
	 *
	 * @param c      the component for which this border is being painted
	 * @param g      the paint graphics
	 * @param x      the x position of the painted border
	 * @param y      the y position of the painted border
	 * @param width  the width of the painted border
	 * @param height the height of the painted border
	 */
	public void paintBorder(final HComponent c, final Graphics g, final int x, final int y, final int width,
			final int height) {
		int w = width;
		int h = height;

		g.setPaint(etchType == LOWERED ? getShadowColor() : getHighlightColor());
		g.drawRect(0, 0, w - 1, h - 1);

		g.setPaint(etchType == LOWERED ? getHighlightColor() : getShadowColor());
		g.drawLine(1, h - 2.5f, 1, 1);
		g.drawLine(.5f, 1, w - 2.5f, 1);

		g.drawLine(-.5f, h - 1, w - .5f, h - 1);
		g.drawLine(w - 1, h - 1, w - 1, -.5f);
	}

	/**
	 * @return The insets of this border
	 */
	public Insets getBorderInsets() {
		return new Insets(2);
	}

	/**
	 * Returns which etch-type is set on the etched border.
	 *
	 * @return the etched border type, either {@code RAISED} or {@code LOWERED}
	 */
	public int getEtchType() {
		return etchType;
	}

	/**
	 * Returns the highlight color of the etched border. Will return null if no
	 * highlight color was specified at instantiation.
	 *
	 * @return the highlight {@code Color} of this {@code EtchedBorder} or null if
	 *         none was specified
	 */
	public Color getHighlightColor() {
		return highlight;
	}

	/**
	 * Returns the shadow color of the etched border. Will return null if no shadow
	 * color was specified at instantiation.
	 *
	 * @return the shadow {@code Color} of this {@code EtchedBorder} or null if none
	 *         was specified
	 */
	public Color getShadowColor() {
		return shadow;
	}

	public void cleanUp(final long ctx) {
		highlight.cleanUp(ctx);
		shadow.cleanUp(ctx);
	}
}
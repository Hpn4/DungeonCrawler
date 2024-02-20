package engine.graph.hlib.border;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;

public class BevelBorder implements Border {

	/** Raised bevel type. */
	public static final int RAISED = 0;

	/** Lowered bevel type. */
	public static final int LOWERED = 1;

	private final int bevelType;

	private Color lightOuter;
	
	private Color lightInner;
	
	private Color darkInner;
	
	private Color darkOuter;

	/**
	 * Creates a bevel border with the specified type and whose colors will be
	 * derived from the background color of the component passed into the
	 * paintBorder method.
	 * 
	 * @param bevelType the type of bevel for the border
	 */
	public BevelBorder(final int bevelType) {
		this.bevelType = bevelType;
	}

	/**
	 * Creates a bevel border with the specified type and color
	 * 
	 * @param bevelType the type of bevel for the border
	 * @param color     the color to use
	 */
	public BevelBorder(final int bevelType, final Color color) {
		this(bevelType, color.brighter(), color.darker());
	}

	/**
	 * Creates a bevel border with the specified type, highlight and shadow colors.
	 * 
	 * @param bevelType the type of bevel for the border
	 * @param highlight the color to use for the bevel highlight
	 * @param shadow    the color to use for the bevel shadow
	 */
	public BevelBorder(final int bevelType, final Color highlight, final Color shadow) {
		this(bevelType, highlight.brighter(), highlight, shadow, shadow.brighter());
	}

	/**
	 * Creates a bevel border with the specified type, highlight and shadow colors.
	 *
	 * @param bevelType           the type of bevel for the border
	 * @param highlightOuterColor the color to use for the bevel outer highlight
	 * @param highlightInnerColor the color to use for the bevel inner highlight
	 * @param shadowOuterColor    the color to use for the bevel outer shadow
	 * @param shadowInnerColor    the color to use for the bevel inner shadow
	 */
	public BevelBorder(final int bevelType, final Color highlightOuterColor, final Color highlightInnerColor,
			final Color shadowOuterColor, final Color shadowInnerColor) {
		this(bevelType);
		this.lightOuter = highlightOuterColor;
		this.lightInner = highlightInnerColor;
		this.darkOuter = shadowOuterColor;
		this.darkInner = shadowInnerColor;
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
		if (bevelType == RAISED) {
			paintRaisedBevel(c, g, x, y, width, height);

		} else if (bevelType == LOWERED) {
			paintLoweredBevel(c, g, x, y, width, height);
		}
	}

	/**
	 * Reinitialize the insets parameter with this Border's current Insets.
	 * 
	 * @param c      the component for which this border insets value applies
	 * @param insets the object to be reinitialized
	 */
	public Insets getBorderInsets() {
		return new Insets(2, 2, 2, 2);
	}

	/**
	 * Returns the outer highlight color of the bevel border when rendered on the
	 * specified component.
	 *
	 * @return the outer highlight {@code Color}
	 */
	public Color getHighlightOuterColor() {
		return lightOuter;
	}

	/**
	 * Returns the inner highlight color of the bevel border when rendered on the
	 * specified component.
	 *
	 * @return the inner highlight {@code Color}
	 */
	public Color getHighlightInnerColor() {
		return lightInner;
	}

	/**
	 * Returns the inner shadow color of the bevel border when rendered on the
	 * specified component.
	 * 
	 * @return the inner shadow {@code Color}
	 */
	public Color getShadowInnerColor() {
		return darkInner;
	}

	/**
	 * Returns the outer shadow color of the bevel border when rendered on the
	 * specified component.
	 *
	 * @return the outer shadow {@code Color}
	 */
	public Color getShadowOuterColor() {
		return darkOuter;
	}

	public int getBevelType() {
		return bevelType;
	}

	public void setPaint(Paint paint) {
		final Color col = (Color) paint;
		this.lightOuter = col.brighter().brighter();
		this.lightInner = col.brighter();
		this.darkOuter = col.darker().darker();
		this.darkInner = col.darker();
	}

	/**
	 * Paints a raised bevel for the specified component with the specified position
	 * and size.
	 *
	 * @param c      the component for which the raised bevel is being painted
	 * @param g      the paint graphics
	 * @param x      the x position of the raised bevel
	 * @param y      the y position of the raised bevel
	 * @param width  the width of the raised bevel
	 * @param height the height of the raised bevel
	 */
	protected void paintRaisedBevel(final HComponent c, final Graphics g, final int x, final int y, final int w,
			final int h) {
		g.setPaint(getHighlightOuterColor());
		g.drawLine(0, 0, 0, h - 1);
		g.drawLine(0, 0, w - 1, 0);

		g.setPaint(getHighlightInnerColor());
		g.drawLine(1, 1, 1, h - 2);
		g.drawLine(1, 1, w - 2, 1);

		g.setPaint(getShadowOuterColor());
		g.drawLine(0, h - 1, w, h - 1);
		g.drawLine(w - 1, -1, w - 1, h - 1);

		g.setPaint(getShadowInnerColor());
		g.drawLine(1, h - 2, w - 2, h - 2);
		g.drawLine(w - 2, 0, w - 2, h - 2);
	}

	/**
	 * Paints a lowered bevel for the specified component with the specified
	 * position and size.
	 *
	 * @param c      the component for which the lowered bevel is being painted
	 * @param g      the paint graphics
	 * @param x      the x position of the lowered bevel
	 * @param y      the y position of the lowered bevel
	 * @param width  the width of the lowered bevel
	 * @param height the height of the lowered bevel
	 */
	protected void paintLoweredBevel(final HComponent c, final Graphics g, final int x, final int y, final int w,
			final int h) {
		g.setPaint(getShadowInnerColor());
		g.drawLine(0, 0, 0, h - 1);
		g.drawLine(0, 0, w - 1, 0);

		g.setPaint(getShadowOuterColor());
		g.drawLine(1, 1, 1, h - 2);
		g.drawLine(1, 1, w - 2, 1);

		g.setPaint(getHighlightOuterColor());
		g.drawLine(0, h - 1, w, h - 1);
		g.drawLine(w - 1, -1, w - 1, h - 1);

		g.setPaint(getHighlightInnerColor());
		g.drawLine(1, h - 2, w - 2, h - 2);
		g.drawLine(w - 2, 0, w - 2, h - 2);
	}

	public void cleanUp(final long ctx) {
		darkInner.cleanUp(ctx);
		darkOuter.cleanUp(ctx);
		lightInner.cleanUp(ctx);
		lightOuter.cleanUp(ctx);
	}
}
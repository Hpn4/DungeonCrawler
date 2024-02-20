package engine.graph.hlib.component;

import org.joml.Vector2i;

import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Rectangle;

/**
 * This class is the base for all {@code HComponent} with text rendering.
 * 
 * This class include the fontSize, the foreground and the text to be rendering.
 * 
 * @author Hpn4
 *
 */
public abstract class Labeled extends HComponent {

	private Paint fg;

	protected String text;

	private final Vector2i fontSize;

	private int fontHeight;

	public Labeled() {
		this("");
	}

	public Labeled(final String txt) {
		this(txt, Color.BLACK);
	}

	public Labeled(final String txt, final Paint fg) {
		this(txt, fg, Graphics.FONT_SIZE);
	}

	public Labeled(final String txt, final Paint fg, final int fontHeight) {
		super();
		fontSize = new Vector2i();
		setFontHeight(fontHeight);
		setTextAndColor(txt, fg);
	}

	public void setTextAndColor(final String txt, final Paint fg) {
		setText(txt);
		setForeground(fg);
	}

	public void setText(final String txt) {
		text = txt;
		setRecalcSizeNeeded(true);
	}

	public String getText() {
		return text;
	}

	protected void insertText(final String txt) {
		text = txt;
	}

	public void setFontHeight(final int fontHeight) {
		this.fontHeight = fontHeight;
		setRecalcSizeNeeded(true);
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setForeground(final Paint fg) {
		this.fg = fg;
	}

	public Paint getForeground() {
		return fg;
	}

	public void initSize(final Graphics g) {
		g.setFontSize(getFontHeight());
		final Rectangle rect = g.getFontBounds(0, 0, getText());
		fontSize.x = rect.x + rect.width;
		fontSize.y = rect.y + rect.height;
	}

	public Vector2i getFontDimension(final Graphics g) {
		g.setFontSize(getFontHeight());
		final Rectangle rect = g.getFontBounds(0, 0, getText());

		return new Vector2i(Math.abs(rect.x) + rect.width, Math.abs(rect.y) + rect.height);
	}

	public Vector2i getFontSize() {
		return fontSize;
	}

	public void cleanUp(final long ctx) {
		super.cleanUp(ctx);
		if (fg != null)
			fg.cleanUp(ctx);
	}
}

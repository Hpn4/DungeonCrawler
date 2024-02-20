package engine.graph.hlib.component;

import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.utils.HLib;
import engine.graph.hlib.utils.Rectangle;

public class HProgressBar extends Labeled {

	private int max;

	private int value;

	private boolean drawValue;

	public HProgressBar() {
		this(0);
	}

	public HProgressBar(final int value) {
		this(100, value, false, "");
	}

	public HProgressBar(final int max, final int value, final boolean drawValue, final String txt) {
		super(txt);
		setRange(max, value);
		setDrawValue(drawValue);
		setRecalcSizeNeeded(true);
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setMax(final int max) {
		this.max = max;
	}

	public int getMax() {
		return max;
	}

	public void setDrawValue(final boolean drawValue) {
		this.drawValue = drawValue;
	}

	public boolean getDrawValue() {
		return drawValue;
	}

	public void setRange(final int max, final int value) {
		setMax(max);
		setValue(value);
	}

	public void initSize(final Graphics g) {
		super.initSize(g);
		int width = getAllInsetsWidth(), height = getAllInsetsHeight();
		String txt = getText();

		if (txt != null && drawValue)
			txt += getMax() + "/" + getMax();

		if (txt != null && !txt.equals("")) {
			final Rectangle rect = g.getFontBounds(0, 0, txt);
			width += Math.abs(rect.x) + rect.width;
			height += Math.abs(rect.y) + rect.height;
		} else {
			width += max;
			height += 20;
		}

		setSize(width, height);
	}

	public void paintComponent(final Graphics g) {
		int x = 0, y = 0, width = getPaintWidth(), height = getPaintHeight();
		String txt = getText();

		g.setPaint(Color.RED);
		g.fillRect(100, 100, 2, 2);
		g.setPaint(colorDefault);
		g.drawRect(x, y, width, height);

		x += HLib.lineWidth;
		y += HLib.lineWidth;
		width -= 2 * HLib.lineWidth;
		height -= 2 * HLib.lineWidth;

		g.setPaint(Color.CORNFLOWERBLUE);
		g.fillRect(x, y, (int) ((float) width / getMax() * getValue()), height);

		if (drawValue)
			txt += " " + getValue() + "/" + getMax();

		if (txt != null & !txt.equals("")) {
			final Rectangle rect = g.getFontBounds(x, y, txt);
			g.setPaint(getForeground());
			g.setFontSize(getFontHeight());
			g.drawText((height - rect.height) / 2, (width - rect.width) / 2, txt);
		}
	}
}

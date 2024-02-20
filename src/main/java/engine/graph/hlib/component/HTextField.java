package engine.graph.hlib.component;

import org.joml.Vector2i;

import engine.graph.hlib.border.LineBorder;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.utils.Background;
import engine.graph.hlib.utils.Rectangle;

public class HTextField extends AbstractText {

	private boolean caretVisible;

	private int caretTime = 0;

	public HTextField() {
		this("");
	}

	public HTextField(final String txt) {
		super(txt);

		setBorder(new LineBorder(Color.WHITE, 1));
		setBackground(new Background(Color.BLACK));
		setForeground(Color.WHITE);
	}

	public void setFocus(final boolean focus) {
		super.setFocus(focus);
		caretVisible = focus;
	}

	public void paintComponent(final Graphics g) {
		g.setFontSize(getFontHeight());
		final String text = getText();
		if (text != null && !text.equals("")) {
			final Rectangle rect = g.getFontBounds(0, 0, getText());
			final Vector2i dim = new Vector2i(rect.x + rect.width, rect.y + rect.height);
			final String txt = getText();
			final int fontHeight = dim.y;

			int x = 2, y = (getHeight() - fontHeight) / 2;

			if (isFocused() && caretTime++ > 15) {
				caretTime = 0;
				caretVisible = !caretVisible;
			}

			g.setPaint(getForeground());
			g.drawText(x, y, txt);

			if (caretVisible) {
				g.setPaint(Color.WHITE);
				g.fillRect(dim.x, getPaintY(), 3, (Math.abs(y) + rect.height) - getAllInsetsYMax() * 2);
			}
		}
	}
}

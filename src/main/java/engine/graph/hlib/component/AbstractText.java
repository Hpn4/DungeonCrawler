package engine.graph.hlib.component;

import org.joml.Vector2i;

import engine.graph.hlib.event.KeyEvent;
import engine.graph.hlib.graphics.Graphics;

public abstract class AbstractText extends Labeled {

	public AbstractText() {
		this("");
	}

	public AbstractText(final String txt) {
		super(txt);
	}

	public void keyPressed(final KeyEvent event) {
		super.keyPressed(event);
		keyPress(event);
	}

	public void keyTyped(final KeyEvent event) {
		super.keyTyped(event);
		keyPress(event);
	}

	private void keyPress(final KeyEvent event) {
		final char c = event.getText();
		final String txt = getText();

		if (c == 65535) {

			final int code = event.getKeyCode();

			if (code == 259 && !txt.equals(""))
				insertText(txt.substring(0, txt.length() - 1));
			else if (code == 258)
				insertText(txt + "\t");
			else if (code == 257)
				insertText(txt + "\n");
		} else
			insertText(txt + event.getText());
	}

	public void initSize(final Graphics g) {
		super.initSize(g);
		final Vector2i dim = getFontDimension(g);
		int height = dim.y, width = dim.x;

		width += getAllInsetsWidth();
		height += getAllInsetsHeight();

		setSize(width, height);
	}
}

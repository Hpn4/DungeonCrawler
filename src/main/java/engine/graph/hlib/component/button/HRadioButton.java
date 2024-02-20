package engine.graph.hlib.component.button;

import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Rectangle;

public class HRadioButton extends CheckableButton {

	private Paint colCheck = colorDefault;

	/**
	 ************************************
	 ************ CONSTRUCTOR ***********
	 ************************************
	 */
	public HRadioButton(final boolean selected) {
		this(selected, "");
	}

	/**
	 * Create a checkbox unchecked, with {@code text} for the text to be rendered
	 * 
	 * @param text
	 */
	public HRadioButton(final String text) {
		this(false, text);
	}

	// Constructeur pour le texte et la taille de police
	public HRadioButton(final boolean selected, final String text) {
		this(selected, text, Color.BLACK, 17);
	}

	// Constructeur pour le texte, la couleur et la taille de police
	public HRadioButton(final boolean select, final String text, final Paint fg, final int p) {
		super(select, text, fg, p);
		colCheck = colorDefault;
	}

	public void cleanUp(final long ctx) {
		super.cleanUp(ctx);
		colCheck.cleanUp(ctx);
	}

	/**
	 ******************************
	 ************ EVENT ***********
	 ******************************
	 */
	public void mousePressed(final MouseEvent e) {
		super.mousePressed(e);
		colCheck = colorPressed;
	}

	public void mouseReleased(final MouseEvent e) {
		super.mouseReleased(e);
		colCheck = colorEntered;
	}

	public void mouseEntered(final MouseEvent e) {
		super.mouseEntered(e);
		colCheck = colorEntered;
	}

	public void mouseExited(final MouseEvent e) {
		super.mouseExited(e);
		colCheck = colorDefault;
	}

	/**
	 ******************************
	 ************ PAINT ***********
	 ******************************
	 */
	public void paintComponent(final Graphics g) {
		final String txt = getText();
		final Rectangle rect = g.getFontBounds(0, 0, txt);
		final int r = 13, x = r * 2 + 5, y = getHeight() / 2 + 3;

		g.setPaint(colCheck);
		g.fillCircle(r, y, r);

		g.setPaint(Color.BLACK);
		g.fillCircle(r, y, r - 1);

		if (isSelected()) {
			g.setPaint(colCheck);
			g.fillCircle(r, y, r - 7);
		}

		g.setPaint(getForeground());
		g.drawText(x, (y + rect.height) / 2 - r, txt);
	}

	public void initSize(final Graphics g) {
		final Rectangle rect = g.getFontBounds(0, 0, getText());
		int height = rect.height, width = rect.width;

		height += getAllInsetsHeight();
		width += getAllInsetsWidth();

		setSize(width, height);
	}
}

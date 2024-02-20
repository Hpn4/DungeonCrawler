package engine.graph.hlib.component.button;

import org.joml.Vector2i;

import engine.graph.hlib.border.BevelBorder;
import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Image;
import engine.graph.hlib.graphics.paint.ImageCache;
import engine.graph.hlib.graphics.paint.LinearGradient;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Background;
import engine.graph.hlib.utils.Rectangle;

public class HButton extends AbstractButton {

	private boolean colorSpec;

	private Image img;

	// Vide
	public HButton() {
		this("");
	}

	// Texte
	public HButton(final String txt) {
		super(txt);
		initListener();
	}

	// Texte et font
	public HButton(final String txt, final int font) {
		super(txt, Color.BLACK, font);
		initListener();
	}

	// Texte image et couleur
	public HButton(final String txt, final String img, final Paint color) {
		super(txt, color);
		if (img != null)
			setImage(img);
		if (color != null)
			colorSpec = true;
		initListener();
	}

	// Texte et couleur
	public HButton(final String txt, final Paint color) {
		this(txt, null, color);
	}

	// Texte et image
	public HButton(final String txt, final String img) {
		this(txt, img, null);
	}

	public void mousePressed(final MouseEvent e) {
		super.mousePressed(e);
		color(isEnabled() ? Color.GRAY : colorDisabled);
	}

	public void mouseReleased(final MouseEvent e) {
		super.mouseReleased(e);
		color(isEnabled() ? Color.YELLOW : colorDisabled);
	}

	public void mouseEntered(final MouseEvent e) {
		super.mouseEntered(e);
		color(isEnabled() ? Color.YELLOW : colorDisabled);
	}

	public void mouseExited(final MouseEvent e) {
		super.mouseExited(e);
		color(isEnabled() ? colorDefault : colorDisabled);
	}

	private void initListener() {
		setBackground(
				new Background(new LinearGradient(this, new Color(255, 238, 0, 30), new Color(156, 138, 61, 30))));
		setBorder(new BevelBorder(BevelBorder.RAISED, colorDefault));

		if (!colorSpec)
			setForeground(colorDefault);

		setRecalcSizeNeeded(true);
	}

	private void color(final Color col) {
		getBorder().setPaint(col);
		if (!colorSpec)
			setForeground(col);
	}

	// Redefinir les icone le texte et la couleur
	public void setImage(final String image) {
		if (!image.equals(""))
			img = ImageCache.getImage(image);
		setRecalcSizeNeeded(true);
	}

	public void setEnabled(final boolean choix) {
		super.setEnabled(choix);
		color(choix ? colorDefault : colorDisabled);
	}

	public void initSize(final Graphics g) {
		super.initSize(g);
		final Rectangle rect = g.getFontBounds(0, 0, getText());
		int height = rect.height + Math.abs(rect.y), width = rect.width + Math.abs(rect.x);

		if (img != null) {
			width += img.getWidth() + 2;
			height = Math.max(height, img.getHeight()) + 4;
		}

		width += getAllInsetsWidth() + 15;
		height += getAllInsetsHeight() + 15;

		setSize(width, height);
	}

	public void paintComponent(final Graphics g) {
		final Vector2i fontSize = getFontSize();
		final String txt = getText();
		final int fontWidth = fontSize.x, height = getPaintHeight(), fontHeight = fontSize.y;

		int x, y;

		if (img != null) {
			x = (getPaintWidth() - img.getWidth() - fontWidth) / 2;
			y = (height - img.getHeight()) / 2;
			g.drawImage(img, x, y);

			y = (height - fontHeight) / 2;
			x += img.getWidth() + 2;
		} else {
			x = (getPaintWidth() - fontWidth) / 2;
			y = (height - fontHeight) / 2;
		}

		g.drawText(x, y, txt, getForeground(), getFontHeight());
	}
}
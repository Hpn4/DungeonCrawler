package engine.graph.hlib.component;

import org.joml.Vector2i;

import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Image;
import engine.graph.hlib.graphics.paint.ImageCache;
import engine.graph.hlib.graphics.paint.Paint;

public class HLabel extends Labeled {

	private Image img;

	// Vide
	public HLabel() {
		this("");
	}

	// Texte
	public HLabel(final String txt) {
		this(txt, Graphics.FONT_SIZE);
	}

	// Texte et font
	public HLabel(final String txt, final int font) {
		this(txt, null, font, Color.BLACK);
	}

	public HLabel(final String txt, final String img, final int font, final Paint color) {
		text = txt;
		setFontHeight(font);
		setForeground(color);
		setImage(img);
	}

	// Texte et couleur
	public HLabel(final String txt, final Paint color) {
		this(txt, null, Graphics.FONT_SIZE, color);
	}

	// Texte et image
	public HLabel(final String txt, final String img) {
		this(txt, img, Graphics.FONT_SIZE, Color.BLACK);
	}

	// Redefinir les icone le texte et la couleur
	public void setImage(final String image) {
		if (image != null && !image.equals(""))
			img = ImageCache.getImage(image);

		setRecalcSizeNeeded(true);
	}

	public void initSize(final Graphics g) {
		super.initSize(g);
		final Vector2i dim = getFontDimension(g);
		int height = dim.x, width = dim.y;

		if (img != null) {
			width += img.getWidth() + 2;
			height = Math.max(height, img.getHeight()) + 4;
		}

		width += getAllInsetsWidth();
		height += getAllInsetsHeight();

		setSize(width, height);
	}

	public void paintComponent(final Graphics g) {
		final Vector2i dim = getFontSize();
		final int fontWidth = dim.x, height = getPaintHeight(), fontHeight = dim.y;

		int x, y;

		if (img != null) {
			x = (getWidth() - img.getWidth() - fontWidth) / 2;
			y = (height - Math.max(img.getHeight(), fontHeight)) / 2;
			g.drawImage(img, x, y);

			x += img.getWidth() + 2;
		} else {
			x = (getWidth() - fontWidth) / 2;
			y = (height - fontHeight + 14) / 2;
		}

		g.drawText(x, y, getText(), getForeground(), getFontHeight());
	}
}
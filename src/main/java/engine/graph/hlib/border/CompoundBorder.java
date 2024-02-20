package engine.graph.hlib.border;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;

public class CompoundBorder implements Border {

	private final Border outsideBorder;

	private final Border insideBorder;

	public CompoundBorder(final Border outsideBorder, final Border insideBorder) {
		this.outsideBorder = outsideBorder;
		this.insideBorder = insideBorder;
	}

	@Override
	public void paintBorder(final HComponent h, final Graphics g, final int x, final int y, final int width,
			final int height) {
		int px = x;
		int py = y;
		int pw = width;
		int ph = height;

		if (outsideBorder != null) {
			outsideBorder.paintBorder(h, g, px, py, pw, ph);

			final Insets nextInsets = outsideBorder.getBorderInsets();
			px += nextInsets.left;
			py += nextInsets.top;
			pw = pw - nextInsets.right - nextInsets.left;
			ph = ph - nextInsets.bot - nextInsets.top;
		}

		if (insideBorder != null)
			insideBorder.paintBorder(h, g, px, py, pw, ph);
	}

	@Override
	public void setPaint(final Paint paint) {
	}

	@Override
	public void cleanUp(final long ctx) {
		outsideBorder.cleanUp(ctx);
		insideBorder.cleanUp(ctx);
	}

	@Override
	public Insets getBorderInsets() {
		final Insets insets = new Insets(0);

		if (outsideBorder != null)
			insets.add(outsideBorder.getBorderInsets());

		if (insideBorder != null)
			insets.add(insideBorder.getBorderInsets());

		return insets;
	}

}

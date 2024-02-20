package engine.graph.hlib.border;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;;

public class LineBorder implements Border {

	private final Insets inset;

	private Paint color;

	public LineBorder(final Paint paint) {
		this(paint, 1);
	}

	public LineBorder(final Paint paint, final int width) {
		setPaint(paint);
		inset = new Insets(width);
	}

	@Override
	public void paintBorder(final HComponent h, final Graphics g, int x, int y, final int width, final int height) {
		g.setPaint(color);

		final float tmpStrokeWidth = g.getStrokeWidth();
		g.setStrokeWidth(inset.bot);

		g.drawRect(x, y, width - 2 * x, height - 2 * y);
		g.setStrokeWidth(tmpStrokeWidth);
	}

	@Override
	public Insets getBorderInsets() {
		return inset;
	}

	@Override
	public void setPaint(final Paint paint) {
		color = paint;
	}

	public void cleanUp(final long ctx) {
		color.cleanUp(ctx);
	}
}

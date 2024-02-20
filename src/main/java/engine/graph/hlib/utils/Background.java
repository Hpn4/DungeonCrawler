package engine.graph.hlib.utils;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Paint;

public class Background {

	private Paint paint;

	public Background(final Paint paint) {
		this.paint = paint;
	}

	public void paintBackground(final HComponent c, final Graphics g, final int x, final int y, final int width,
			final int height) {

		g.setPaint(paint);
		g.fillRect(x, y, width, height);
	}

	public Paint getPaint() {
		return paint;
	}

	public void cleanUp(final long ctx) {
		paint.cleanUp(ctx);
	}
}

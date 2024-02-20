package engine.graph.hlib.border;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;

public class EmptyBorder implements Border {

	public EmptyBorder() {
	}

	public void paintBorder(final HComponent h, final Graphics g, final int x, final int y, final int width,
			final int height) {
	}

	public void setPaint(Paint paint) {
	}

	public Insets getBorderInsets() {
		return new Insets(0);
	}

	public void cleanUp(final long ctx) {
	}
}

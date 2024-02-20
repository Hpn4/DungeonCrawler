package engine.graph.hlib.border;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.utils.Insets;

public interface Border {

	void paintBorder(final HComponent h, final Graphics g, final int x, final int y, final int width, final int height);

	void setPaint(final Paint paint);

	void cleanUp(final long ctx);

	Insets getBorderInsets();
}

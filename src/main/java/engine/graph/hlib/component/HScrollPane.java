package engine.graph.hlib.component;

import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.graphics.Graphics;

public class HScrollPane extends HComponent {

	private HComponent comp;

	private int xOff;

	private int yOff;

	public HScrollPane() {
	}

	public HScrollPane(final HComponent comp) {
		setComp(comp);
		setRecalcSizeNeeded(true);
	}

	public void setComp(final HComponent comp) {
		this.comp = comp;
		this.comp.setRecalcSizeNeeded(true);
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		comp.setVisible(visible);
	}

	public void fireEvent(final MouseEvent event) {
		super.fireEvent(event);
		event.add(xOff, yOff);
		if (comp != null)
			comp.fireEvent(event);
	}

	public void mouseWheel(final MouseEvent event) {
		super.mouseWheel(event);
		final float xOff = -event.getOffX(), yOff = -event.getOffY();
		int x = comp.getX(), y = comp.getY();

		x += (int) ((float) Math.max(getWidth(), comp.getWidth()) / Math.min(getWidth(), comp.getWidth()) * xOff);
		y += (int) ((float) Math.max(getHeight(), comp.getHeight()) / Math.min(getHeight(), comp.getHeight()) * yOff);

		if (x < getPaintX())
			comp.setX(getPaintX());
		else if (x > getPaintHeight())
			comp.setX(getPaintHeight());
		else
			comp.setX(x);

		if (y < getPaintY())
			comp.setY(getPaintY());
		else if (y > getPaintWidth())
			comp.setY(getPaintWidth());
		else
			comp.setY(y);

		this.xOff = comp.getX();
		this.yOff = comp.getY();
	}

	public void initSize(final Graphics g) {
		int width = getAllInsetsWidth(), height = getAllInsetsHeight();

		height += comp.getHeight();
		width += comp.getWidth();

		setSize(width, height);
	}

	public void paintComponent(final Graphics g) {
		comp.paint(g);
	}
}

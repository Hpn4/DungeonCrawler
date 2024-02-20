package engine.graph.hlib.event.listener;

import engine.graph.hlib.event.MouseEvent;

@FunctionalInterface
public interface MouseListener extends EventListener {

	public void fire(final MouseEvent mouse);
}

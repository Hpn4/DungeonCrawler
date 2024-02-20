package engine.graph.hlib.event.listener;

import engine.graph.hlib.event.KeyEvent;

@FunctionalInterface
public interface KeyListener extends EventListener {

	public void fire(final KeyEvent key);
}

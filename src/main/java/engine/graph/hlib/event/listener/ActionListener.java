package engine.graph.hlib.event.listener;

@FunctionalInterface
public interface ActionListener extends EventListener {

	public void actionPerformed();
}

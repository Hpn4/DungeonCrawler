package engine.graph.hlib.component.button;

import engine.graph.hlib.component.HComponent;

@FunctionalInterface
public interface HComponentAction {

	public void action(final HComponent c);
}

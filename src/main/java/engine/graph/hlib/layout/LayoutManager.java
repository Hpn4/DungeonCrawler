package engine.graph.hlib.layout;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.component.HContainer;
import engine.graph.hlib.utils.Alignement;

public interface LayoutManager {

	void doLayout(final HContainer cont);

	public default void addComp(final HComponent comp, final Alignement align) {

	}
}

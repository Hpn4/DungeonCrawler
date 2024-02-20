package engine.graph.hlib.component;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import engine.graph.hlib.component.button.CheckableButton;
import engine.graph.hlib.component.button.HButtonGroup;
import engine.graph.hlib.component.button.HComponentAction;
import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.layout.LayoutManager;
import engine.graph.hlib.utils.Alignement;

public class HContainer extends HComponent {

	private final ArrayList<HComponent> comp = new ArrayList<>();

	private LayoutManager layout;

	public HContainer() {
		super();
		setSize(HWindow.frameWidth, HWindow.frameHeight);
		setRecalcSizeNeeded(true);
	}

	public HContainer(final HComponent... components) {
		super();
		addAll(components);
		setSize(HWindow.frameWidth, HWindow.frameHeight);
		setRecalcSizeNeeded(true);
	}

	/**
	 **********************************
	 ********** ADD COMPONENT *********
	 **********************************
	 */
	public void addComp(final HComponent h) {
		comp.add(h);

		if (h instanceof HContainer)
			((HContainer) h).doLayout();
		doLayout();
	}

	public void addComp(final HComponent h, final Alignement align) {
		comp.add(h);

		if (h instanceof HContainer)
			((HContainer) h).doLayout();
		layout.addComp(h, align);
	}

	public void addAll(final HComponent... components) {
		for (final HComponent h : components) {
			comp.add(h);

			if (h instanceof HContainer)
				((HContainer) h).doLayout();
		}

		doLayout();
	}

	public void addFromButtonGroup(final HButtonGroup bg) {
		for (final CheckableButton but : bg.getButtons()) {
			comp.add(but);
		}

		doLayout();
	}

	/**
	 *************************************
	 ********** REMOVE COMPONENT *********
	 *************************************
	 */
	public void removeComp(final HComponent h) {
		comp.remove(h);
		doLayout();
	}

	public void removeAll(final HComponent... components) {
		comp.removeAll(List.of(components));
		doLayout();
	}

	public HComponent getComp(final int index) {
		return comp.get(index);
	}

	public int getComponentCount() {
		return comp.size();
	}

	public ArrayList<HComponent> getComponents() {
		return comp;
	}

	public void forEach(final HComponentAction action) {
		for (int i = 0, c = getComponentCount(); i < c; i++)
			action.action(getComp(i));
	}

	public void forEachContainer(final HComponentAction action) {
		for (int i = 0, c = getComponentCount(); i < c; i++) {
			final HComponent comp = getComp(i);
			if (comp instanceof HContainer)
				action.action(comp);
		}
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		forEach(comp -> comp.setVisible(visible));

		doLayout();
	}

	public void paintComponent(final Graphics g) {
		forEach(comp -> {
			if (comp.isVisible()) {
				final int x = comp.getPaintX() + getX(), y = comp.getPaintY() + getY();
				final Vector2i bef = g.getOrigin();
				g.moveOriginTo(x, y);
				comp.paint(g);
				g.moveOriginTo(bef);
			}
		});
	}

	public void fireEvent(final MouseEvent event) {
		super.fireEvent(event);

		forEach(comp -> comp.fireEvent(event));
	}

	public void setSize(final int width, final int height) {
		final int w = getWidth(), h = getHeight();

		// On remet a jour la taile de tout les autres container
		forEachContainer(container -> {
			// On garde le meme ratio de taille
			final int xDiff = w - container.getWidth();
			final int yDiff = h - container.getHeight();

			container.setSize(width - xDiff, height - yDiff);
		});

		super.setSize(width, height);

		doLayout();
	}

	public void initSize(final Graphics g) {
		forEachContainer(container -> container.initSize(g));
		doLayout();
	}

	public void recalcSize(final Graphics g) {
		forEach(comp -> {
			if (comp instanceof HContainer)
				((HContainer) comp).recalcSize(g);
			else if (comp.isRecalcSizeNeeded()) {
				comp.initSize(g);
				comp.setRecalcSizeNeeded(false);
			}
		});

		if (isRecalcSizeNeeded()) {
			setRecalcSizeNeeded(false);
			doLayout();
		}
	}


	public void cleanUp(final long ctx) {
		super.cleanUp(ctx);

		forEach(comp -> comp.cleanUp(ctx));
	}

	/**
	 *************************************
	 *********** LAYOUT MANAGER **********
	 *************************************
	 */
	public void setLayout(final LayoutManager layout) {
		this.layout = layout;
	}

	public LayoutManager getLayoutManager() {
		return layout;
	}

	public void doLayout() {
		if (layout != null && isVisible()) {
			layout.doLayout(this);

			forEach(comp -> {
				if (comp instanceof HContainer)
					((HContainer) comp).doLayout();
			});
		}
	}
}

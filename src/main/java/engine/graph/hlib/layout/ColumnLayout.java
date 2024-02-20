package engine.graph.hlib.layout;

import java.util.ArrayList;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.component.HContainer;
import engine.graph.hlib.utils.Alignement;

public class ColumnLayout implements LayoutManager {

	/* L'ecart min entre les differents composant */
	private int yGap;

	/*
	 * Si les composant doivent tous avoir la meme largeur ( la largeur des
	 * composants est définie par rapport au composant ayant la plus grande largeur)
	 */
	private boolean widthEqual;

	/* L'alignement horizontal de la colonne (a gauche, au centre, a droite) */
	private Alignement alignX;

	private Alignement alignY;

	public ColumnLayout(final int yGap) {
		this(yGap, true, Alignement.CENTER, Alignement.CENTER);
	}

	public ColumnLayout(final int xGap, final Alignement alignX) {
		this(xGap, false, alignX, Alignement.CENTER);
	}

	public ColumnLayout(final int yGap, final boolean widthEqual, final Alignement alignX, final Alignement alignY) {
		setYGap(yGap);
		setWidthEqual(widthEqual);
		setAlignX(alignX);
		setAlignY(alignY);
	}

	public void doLayout(final HContainer cont) {
		final ArrayList<HComponent> comps = cont.getComponents();
		int y = cont.getPaintY();

		final int startX = cont.getPaintX(), width = cont.getWidth();
		int maxWidth = 0, totalHeight = 0;

		// On recupere la largeur la plus grande et on calcul la hauteur totale
		final int count = comps.size();
		for (int i = 0; i < count; i++) {
			final HComponent comp = comps.get(i);
			maxWidth = Math.max(maxWidth, comp.getWidth());
			totalHeight += comp.getHeight();
		}

		int yGap = this.yGap;
		switch (alignY) {
		case CENTER:
			yGap = (int) Math.round((float) (cont.getPaintHeight() - totalHeight) / (count + 1));
			y += yGap;
			break;
		case BOT:
			y = cont.getPaintHeight() - totalHeight - (count * this.yGap);
			break;
		default:
			break;
		}

		for (int i = 0; i < count; i++) {
			final HComponent comp = comps.get(i);
			final int widthComp = comp.getWidth(), heightComp = comp.getHeight();
			int x = 0;

			switch (alignX) {
			case CENTER:
				x = (width - widthComp) / 2;
				break;
			case RIGHT:
				x = cont.getPaintWidth() - widthComp;
				break;
			default:
				x = startX;
				break;
			}

			if (widthEqual) {
				//x = (width - maxWidth) / 2;
				comp.setBounds(startX + x, y, maxWidth, heightComp);
			} else
				comp.setPos(x, y);

			y += heightComp + yGap;
		}

	}

	public int getYGap() {
		return yGap;
	}

	public void setYGap(final int yGap) {
		this.yGap = yGap;
	}

	public boolean getWidthEqual() {
		return widthEqual;
	}

	public void setWidthEqual(final boolean widthEqual) {
		this.widthEqual = widthEqual;
	}

	public Alignement getAlignX() {
		return alignX;
	}

	public void setAlignX(final Alignement alignX) {
		this.alignX = alignX;
	}

	public Alignement getAligneY() {
		return alignY;
	}

	public void setAlignY(final Alignement alignY) {
		this.alignY = alignY;
	}
}

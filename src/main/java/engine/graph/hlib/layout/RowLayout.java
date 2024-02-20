package engine.graph.hlib.layout;

import java.util.ArrayList;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.component.HContainer;
import engine.graph.hlib.utils.Alignement;

public class RowLayout implements LayoutManager {

	/* L'ecart min entre les differents composant */
	private int xGap;

	/*
	 * Si les composants doivent tous avoir le meme hauteur ( le composant avec la
	 * plus grande hauteur est selectionné et tout les autres sont mis a cette
	 * hauteur)
	 */
	private boolean heightEqual;

	/* L'alignement vertical des composants (en haut, au centre, en bas) */
	private Alignement alignY;

	/*
	 * L'alignement de la ligne (A gauche, a droite ou au centre. Lorsque centre,
	 * les elements de la ligne sont place de tel maniere a occupe tous l'espace
	 * disponible avec un meme eccart entre eux. Ils gardent la meme taille, juste
	 * leur position change)
	 */
	private Alignement alignX;

	public RowLayout(final int xGap) {
		this(xGap, true, Alignement.CENTER, Alignement.CENTER);
	}

	public RowLayout(final int xGap, final Alignement alignY) {
		this(xGap, false, alignY, Alignement.CENTER);
	}

	public RowLayout(final int xGap, final boolean heightEqual, final Alignement alignY, final Alignement alignX) {
		setXGap(xGap);
		setHeightEqual(heightEqual);
		setAlignY(alignY);
		setAlignX(alignX);
	}

	public void doLayout(final HContainer cont) {
		final ArrayList<HComponent> comps = cont.getComponents();
		int x = cont.getPaintX();

		final int startY = cont.getPaintY(), height = cont.getPaintHeight();
		int maxHeight = 0, totalWidth = 0;

		final int count = comps.size();
		for (int i = 0; i < count; i++) {
			final HComponent comp = comps.get(i);
			maxHeight = Math.max(maxHeight, comp.getHeight());
			totalWidth += comp.getWidth();
		}

		int xGap = this.xGap;
		switch (alignX) {
		case CENTER:
			xGap = (int) Math.round((float) (cont.getPaintWidth() - totalWidth) / (count + 1));
			x += xGap;
			break;
		case RIGHT:
			x = cont.getPaintWidth() - totalWidth - (count * this.xGap);
			break;
		default:
			break;
		}

		for (int i = 0; i < count; i++) {
			final HComponent comp = comps.get(i);
			final int widthComp = comp.getWidth(), heightComp = comp.getHeight();
			int y = 0;

			switch (alignY) {
			case CENTER:
				y = (height - heightComp) / 2;
				break;
			case BOT:
				y = height - heightComp - cont.getAllInsetsYMax() - comp.getAllInsetsYMax();
				break;
			default:
				y = startY;
				break;
			}

			if (heightEqual) {
				y = (height - maxHeight) / 2;
				comp.setBounds(x, startY + y, widthComp, maxHeight);
			} else
				comp.setPos(x, startY + y);

			x += widthComp + xGap;
		}

	}

	public int getXGap() {
		return xGap;
	}

	public void setXGap(final int xGap) {
		this.xGap = xGap;
	}

	public boolean getHeightEqual() {
		return heightEqual;
	}

	public void setHeightEqual(final boolean heightEqual) {
		this.heightEqual = heightEqual;
	}

	public Alignement getAligneY() {
		return alignY;
	}

	public void setAlignY(final Alignement alignY) {
		this.alignY = alignY;
	}

	public Alignement getAlignX() {
		return alignX;
	}

	public void setAlignX(final Alignement alignX) {
		this.alignX = alignX;
	}
}

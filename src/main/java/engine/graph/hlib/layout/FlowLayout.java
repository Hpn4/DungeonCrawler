package engine.graph.hlib.layout;

import java.util.ArrayList;

import engine.graph.hlib.component.HComponent;
import engine.graph.hlib.component.HContainer;
import engine.graph.hlib.utils.Alignement;

public class FlowLayout implements LayoutManager {

	private int xGap, yGap;
	private Alignement alignX, alignY;

	public FlowLayout() {
		this(Alignement.CENTER, Alignement.CENTER);
	}

	public FlowLayout(final Alignement xAlign, final Alignement yAlign) {
		this(xAlign, yAlign, 5, 5);
	}

	public FlowLayout(final Alignement xAlign, final Alignement yAlign, final int xGap, final int yGap) {
		setAlignement(xAlign, yAlign);
		setGap(xGap, yGap);
	}

	public void setGap(final int x, final int y) {
		xGap = x;
		yGap = y;
	}

	public void setAlignement(final Alignement xAlign, final Alignement yAlign) {
		setXAlignement(xAlign);
		setYAlignement(yAlign);
	}

	public void setXAlignement(final Alignement align) {
		alignX = align;
	}

	public void setYAlignement(final Alignement align) {
		alignY = align;
	}

	public void doLayout(HContainer cont) {
		// Longueur exacte du composant
		final int width = cont.getPaintWidth();

		int defaultX, count = 0, maxHeight = 0, startX = cont.getPaintX(), startY = cont.getPaintY(), totalWidth = 0;

		// La position de debut en X en fonction de alignX
		if (alignX == Alignement.RIGHT)
			startX += width;
		else if (alignX == Alignement.CENTER)
			startX += width / 2;

		defaultX = startX;

		final ArrayList<HComponent> comps = cont.getComponents();
		for (int x = 0, size = comps.size(); x < size; x++) {
			count++;
			HComponent comp = comps.get(x);
			final int addWidth = comp.getWidth() + xGap;

			if (alignX == Alignement.RIGHT)
				startX -= addWidth;
			comp.setPos(startX, startY);

			maxHeight = Math.max(maxHeight, comp.getHeight());
			startX += (alignX == Alignement.LEFT) ? addWidth : (alignX == Alignement.CENTER) ? addWidth / 2 : 0;

			totalWidth += addWidth;
			if (startX + (x < size - 1 ? comps.get(x + 1) : comp).getWidth() > width || count >= comps.size()) {
				startX = -1;
				for (int y = x - count + 1; y < count; y++) {
					comp = comps.get(y);
					final int height = comp.getHeight();

					// La position en Y en fonction de alignY
					if (alignY == Alignement.TOP)
						comp.setY(startY);
					else if (alignY == Alignement.BOT)
						comp.setY(startY + (maxHeight - height));
					else
						comp.setY(startY + (maxHeight - height) / 2);

					if (alignX == Alignement.CENTER) {
						if (startX == -1)
							startX = (width - totalWidth) / 2;
						comps.get(y).setX(startX);
						startX += comp.getWidth() + xGap;
					}
				}
				count = 0;
				startX = defaultX;
				startY += maxHeight + yGap;
			}
		}
	}
}
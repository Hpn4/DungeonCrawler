package engine.graph.renderer.forge;

import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;

public class Extraction {

	private final Color metalA = new Color(46, 44, 42);
	
	private final Color metalB = new Color(56, 28, 10);
	
	private final Color metalC = new Color(99, 43, 8);

	private final Color metal0 = new Color(140, 60, 10);

	private final Color metal1 = new Color(173, 75, 14);

	private final Color metal2 = new Color(224, 94, 13);

	private final Color metal3 = new Color(255, 98, 0);

	private final Color metal4 = new Color(255, 132, 0);

	private final Color metal5 = new Color(255, 174, 0);

	private final Color metal6 = new Color(255, 213, 0);

	private final Color metal7 = new Color(255, 229, 97);

	private final Color metal8 = new Color(255, 238, 153);

	private final Color metal9 = new Color(250, 240, 192);

	private final Color[] anim = { metalA, metalB, metalC, metal0, metal1, metal2, metal3, metal4, metal5, metal6, metal7, metal8, metal9 };

	private float compteur;

	private int state;

	public Extraction() {

	}

	public void update(final float interval) {
		compteur += interval;

		if (compteur >= 2) {
			compteur = 0;
			state++;
		}

		if (state >= anim.length)
			state = 0;
	}

	public void render(final HWindow window) {
		final Graphics g = window.getGraphics();

		g.startRendering(window);

		g.setPaint(anim[state]);
		g.fillRect(100, 100, 500, 100);
		g.endRendering(window);
	}
}

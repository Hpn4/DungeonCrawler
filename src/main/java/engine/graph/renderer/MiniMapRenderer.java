package engine.graph.renderer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.event.MouseInput;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import loader.generator.Dungeon;
import loader.generator.Salle;

public class MiniMapRenderer {

	private final Color bg;

	private boolean miniMap;

	public MiniMapRenderer() {
		bg = new Color(0, 0, 0, 120);
	}

	public void input(final HWindow window, final MouseInput mouseInput) {
		miniMap = window.isKeyPressed(GLFW_KEY_TAB);
	}

	public void render(final HWindow window, final Dungeon dungeon) {
		if (miniMap) {
			final Graphics g = window.getGraphics();

			final int porteSize = 10;
			final int initX = (window.getWidth() - 220 * dungeon.getWidth()) / 2;

			int startX = initX, startY = (window.getHeight() - 120 * dungeon.getHeight()) / 2;
			g.startRendering(window);

			g.setPaint(bg);
			g.fillRect(startX - 10, startY - 10, dungeon.getWidth() * 220, dungeon.getHeight() * 120);

			g.setPaint(Color.WHITE);

			for (int y = 0; y < dungeon.getHeight(); y++) {
				for (int x = 0; x < dungeon.getWidth(); x++) {
					final Salle salle = dungeon.get(x, y);

					// Si on a visite la salle on l'affiche sur la carte
					if (salle != null && salle.isVisited()) {
						g.drawRect(startX, startY, 200, 100);

						// Si la salle qu'on dessine est la salle danns laquel est le joueur, on déssine
						// un petit rond blanc
						if (dungeon.getPos().x == x && dungeon.getPos().y == y)
							g.fillCircle(startX + 100, startY + 50, 15);

						// On affiche des petits rectangles lorsqu'il y a une porte
						if (salle.getMap().havePorteE())
							g.fillRect(startX + 200, startY + 45, 20, porteSize);

						if (salle.getMap().havePorteO())
							g.fillRect(startX - 20, startY + 45, 20, porteSize);

						if (salle.getMap().havePorteN())
							g.fillRect(startX + 95, startY - 20, porteSize, 20);

						if (salle.getMap().havePorteS())
							g.fillRect(startX + 95, startY + 100, porteSize, 20);
					}

					startX += 220;
				}

				startY += 120;
				startX = initX;
			}

			g.endRendering(window);
		}
	}
}

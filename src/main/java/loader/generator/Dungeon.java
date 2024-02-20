package loader.generator;

import org.joml.Vector2i;

import engine.graph.hlib.component.HWindow;

public class Dungeon {

	private final Salle[][] salles;

	private final Vector2i posInDungeon;

	public Dungeon(final HWindow window, final int minSalle, final int maxSalle, final int width, final int height)
			throws Exception {
		// On genere les salles du donjon
		salles = SalleGen.genereDungeon(minSalle, maxSalle, width, height);

		// On commence dans la salle centrale
		posInDungeon = new Vector2i(width / 2, height / 2);
	}

	public Salle get(final int x, final int y) {
		return salles[y][x];
	}

	public Salle getActual() {
		return salles[posInDungeon.y][posInDungeon.x];
	}

	public Salle move(final int x, final int y) {
		posInDungeon.add(x, y);
		return getActual();
	}

	public Vector2i getPos() {
		return posInDungeon;
	}

	public int getWidth() {
		return salles[0].length;
	}

	public int getHeight() {
		return salles[0].length;
	}

	public void cleanup() {
		for (int x = 0; x < salles[0].length; x++)
			for (int y = 0; y < salles.length; y++)
				if (salles[y][x] != null)
					salles[y][x].cleanup();
	}
}

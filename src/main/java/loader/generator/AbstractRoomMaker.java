package loader.generator;

import java.util.ArrayList;

import org.joml.Vector2i;

import loader.map.AbstractLayer;
import loader.map.Map;
import loader.map.Tile;

public abstract class AbstractRoomMaker {

	protected Map map;

	protected void combine(final Map second) {
		combine(map, second);
	}

	protected void combine(final Map first, final Map second) {
		for (int layer = 0; layer < first.getLayersCount(); layer++) {
			final AbstractLayer f = first.getLayers().get(layer), s = second.getLayer(f.getName());

			if (s != null) {
				for (int x = 0; x < first.getWidth(); x++)
					for (int y = 0; y < first.getHeight(); y++) {
						final Tile tile = s.get(x, y);
						if (tile != Tile.AIR)
							f.set(x, y, tile);
					}

				s.setAtlasId(f.getAtlasId());
			}
		}
	}

	protected Map getRandomFrom(final ArrayList<String> patterns, final String path) throws Exception {
		final float random = (float) Math.random();
		final int index = Math.round(random * (patterns.size() - 1));

		return new Map(path + patterns.get(index));
	}

	protected void replace(final int[][] toSearch, final int[][] newP, final int layer, final int count) {
		final AbstractLayer l = map.getLayers().get(layer);
		final ArrayList<Vector2i> matchedPatterns = searchPattern(toSearch, l);

		for (int i = 0; i < count; i++) {
			final int index = (int) (Math.random() * (matchedPatterns.size() - 1));
			final Vector2i start = matchedPatterns.remove(index);

			for (int x = 0; x < toSearch.length; x++)
				for (int y = 0; y < toSearch[0].length; y++) {
					l.set(start.x + x, start.y + y, new Tile(newP[y][x], 0));
				}
		}
	}

	protected ArrayList<Vector2i> searchPattern(final int[][] toSearch, final AbstractLayer l) {
		final ArrayList<Vector2i> matchedPatterns = new ArrayList<>();

		for (int x = 1; x < map.getWidth() - 3; x++)
			for (int y = 4; y < map.getHeight() - 5; y++) {
				final Tile tile = l.get(x, y);
				if (tile.getTile() == toSearch[0][0]) {

					boolean bon = true;
					for (final Vector2i start : matchedPatterns)
						if (y > start.y && y < start.y + toSearch[0].length && x > start.x
								&& x < start.x + toSearch.length) {
							bon = false;
							break;
						}

					// Ce n'est pas un pattern déjà trouve
					if (bon) {
						bon = confirmPattern(toSearch, x, y, l);
						if (bon) {
							matchedPatterns.add(new Vector2i(x, y));
							x += toSearch.length;
						}
					}
				}
			}

		return matchedPatterns;
	}

	protected boolean confirmPattern(final int[][] toSearch, final int x, final int y, final AbstractLayer l) {
		final int w = map.getWidth(), h = map.getHeight();
		final int wP = toSearch.length, hP = toSearch[0].length;

		if (wP >= w - x)
			return false;
		if (hP >= h - y)
			return false;

		for (int xP = 0; xP < wP; xP++)
			for (int yP = 0; yP < hP; yP++)
				if (toSearch[xP][yP] != l.get(xP + x, yP + y).getTile())
					return false;

		return true;
	}

	public Map getFinalMap() {
		return map;
	}
}

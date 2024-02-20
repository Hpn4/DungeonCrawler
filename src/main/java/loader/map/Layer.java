package loader.map;

import java.util.function.Consumer;

public class Layer extends AbstractLayer {

	private final Tile[][] tiles;

	public Layer(final int width, final int height) {
		this(width, height, "");
	}

	public Layer(final int width, final int height, final String name) {
		super(name);

		tiles = new Tile[height][width];

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				tiles[y][x] = Tile.AIR;
	}

	public Tile get(final int x, final int y) {
		return tiles[y][x];
	}

	public Tile set(final int x, final int y, final Tile tile) {
		final Tile tmp = tiles[y][x];
		tiles[y][x] = tile;

		return tmp;
	}

	public void forEach(final Consumer<Tile> consumer) {
		final int width = tiles[0].length, height = tiles.length;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				consumer.accept(tiles[y][x]);
	}

}

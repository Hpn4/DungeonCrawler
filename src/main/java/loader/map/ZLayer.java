package loader.map;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ZLayer extends AbstractLayer {

	private final ArrayList<ZTile> tiles;

	private Tileset tileset;

	public ZLayer(final String name) {
		super(name);

		tiles = new ArrayList<>();
	}

	@Override
	public Tile set(final int x, final int y, final Tile tile) {
		tiles.add(new ZTile(x, y, tile.getTile(), tile.getAtlasId()));
		return null;
	}

	@Override
	public Tile get(final int x, final int y) {
		for (final ZTile tile : tiles)
			if (tile.getX() == x && tile.getY() == y)
				return tile;

		return null;
	}

	public ZTile get(final int x) {
		return tiles.get(x);
	}

	@Override
	public void forEach(final Consumer<Tile> consumer) {
		tiles.forEach(consumer);
	}

	public void sort() {
		tiles.sort(null);
	}

	public int size() {
		return tiles.size();
	}

	public void setTileset(final Tileset tileset) {
		this.tileset = tileset;
	}

	public Tileset getTileset() {
		return tileset;
	}

}

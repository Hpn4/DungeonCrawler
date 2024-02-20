package loader.map;

import java.util.Objects;

public class Tile {

	public final static Tile AIR = new Tile(0, -1);

	// Le numéro de la tile sur la texture d'atlas
	private short tile;

	// Le numéro de la texture d'atlas
	private short atlasId;

	public Tile(final int tile, final int atlasId) {
		this.tile = (short) tile;
		this.atlasId = (short) atlasId;
	}
	
	public Tile(final Tile tile) {
		this.tile = tile.tile;
		this.atlasId = tile.atlasId;
	}

	public short getTile() {
		return tile;
	}
	
	public void setTile(final int tile) {
		this.tile = (short) tile;
	}

	public short getAtlasId() {
		return atlasId;
	}
	
	public void setAtlasId(final int atlasId) {
		this.atlasId = (short) atlasId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(atlasId, tile);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Tile))
			return false;
		final Tile other = (Tile) obj;
		return atlasId == other.atlasId && tile == other.tile;
	}

}

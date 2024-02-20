package loader.map;

import java.util.Objects;

public class ZTile extends Tile implements Comparable<ZTile> {

	private int x;

	private int y;

	public ZTile(final int tile, final int atlasId) {
		super(tile, atlasId);
	}

	public ZTile(final int x, final int y, final int tile, final int atlasId) {
		super(tile, atlasId);

		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + Objects.hash(x, y);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZTile other))
			return false;

		return x == other.x && y == other.y;
	}

	@Override
	public int compareTo(final ZTile tile) {
		return Integer.compare(y, tile.y);
	}

}

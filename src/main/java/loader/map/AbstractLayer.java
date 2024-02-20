package loader.map;

import java.util.function.Consumer;

public abstract class AbstractLayer {

	private short atlasId;

	private boolean visible;

	private final String name;

	public AbstractLayer(final String name) {
		this.name = name;

		visible = true;
	}

	public abstract Tile get(final int x, final int y);

	public abstract Tile set(final int x, final int y, final Tile tile);

	public abstract void forEach(final Consumer<Tile> consumer);

	public short getAtlasId() {
		return atlasId;
	}

	public void setAtlasId(final short atlasId) {
		this.atlasId = atlasId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}

}

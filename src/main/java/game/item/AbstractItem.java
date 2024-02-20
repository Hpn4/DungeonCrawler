package game.item;

import java.io.Serializable;

import engine.graph.hlib.graphics.paint.Color;

public abstract class AbstractItem implements Serializable, Comparable<AbstractItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5951550411308881818L;

	private String texturePath;

	private String name;

	private Rarity rarity;

	private Color color;

	private boolean isStackable;

	private boolean isNew;

	private int quantity;

	private float weight;

	private int price;

	public AbstractItem(final String path) {
		this(new ItemReader(path));
	}

	public AbstractItem(final ItemReader reader) {
		// Le nom et le chemin d'accès de l'image
		name = reader.get("nom");
		texturePath = reader.get("img");

		// La couleur autour de l'objet
		final String[] col = reader.get("color").split(",");
		color = new Color(Integer.parseInt(col[0]), Integer.parseInt(col[1]), Integer.parseInt(col[2]));

		// La rarete
		rarity = reader.getRarity("rarity");

		weight = reader.getFloat("poid");
		price = reader.getInt("prix");

		// Si précise pas le contraire l'item se stack
		isStackable = reader.getBool("isStackable", true);

		quantity = 1;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(final String texturePath) {
		this.texturePath = texturePath;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(final Rarity rarity) {
		this.rarity = rarity;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public boolean isStackable() {
		return isStackable;
	}

	public void setStackable(final boolean isStackable) {
		this.isStackable = isStackable;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(final boolean isNew) {
		this.isNew = isNew;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(final int quantity) {
		this.quantity = quantity;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(final float weight) {
		this.weight = weight;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public int compareTo(AbstractItem item) {
		return (rarity.getSortOrder() + name).compareTo((item.rarity.getSortOrder() + item.name));
	}
}

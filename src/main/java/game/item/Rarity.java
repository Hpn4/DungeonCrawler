package game.item;

import java.awt.Color;

public enum Rarity {
	COMMON(Color.WHITE, 'e'), UNCO(new Color(198, 198, 198), 'd'), RARE(new Color(78, 145, 212), 'c'),
	EPIC(new Color(198, 59, 219), 'b'), LEGENDARY(new Color(194, 98, 25), 'a');

	private final Color color;

	private final char sortOrder;

	Rarity(final Color col, final char sort) {
		color = col;
		sortOrder = sort;
	}

	/**
	 * 
	 * @return La couleur du nom de l'objet en fonction de sa rareté
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 * @return L'ordre dans lequel les rareté sont triés
	 */
	public char getSortOrder() {
		return sortOrder;
	}
}

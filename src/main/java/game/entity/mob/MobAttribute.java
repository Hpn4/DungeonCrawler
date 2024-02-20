package game.entity.mob;

import game.entity.Stat;

public class MobAttribute {

	public final HitBoxInsets hit;

	public final int HAUT;

	public final int BAS;

	public final int GAUCHE;

	public final int DROITE;

	public final int COLS;

	public final int ROWS;

	public boolean twoDirectionOnly; // Si il bouge sur 2 ou quatres direction

	public float moveSpeed;

	public float scaleTexture;

	public float yPosModifier; // Ce nombre permet de corriger le calcul de la tile sur lequel le monsre es.

	/**
	 * Si le monstre à comme stat de vie de base de 300 et la variance de vie de 20.
	 * Il peut donc avoir entre 280 et 320 point de vie.
	 */
	public Stat stat; // Les stats de base du monstre. Ils peuvent varier en fonction de

	public Stat variance; // Les variation de stat du monstre (peut être en plus ou en moins)

	public MobAttribute(final int haut, final int bas, final int gauche, final int droite, final int cols,
			final int rows) {
		this.HAUT = haut;
		this.BAS = bas;
		this.GAUCHE = gauche;
		this.DROITE = droite;
		this.COLS = cols;
		this.ROWS = rows;

		scaleTexture = 1f;

		yPosModifier = 0f;

		hit = new HitBoxInsets(0.5f);
		stat = new Stat();
		variance = new Stat();
	}
}

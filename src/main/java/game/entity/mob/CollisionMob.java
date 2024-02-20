package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.Player;
import loader.generator.Dungeon;

public abstract class CollisionMob extends Mob {

	protected int saveTo;

	protected int dirCant;

	public CollisionMob(final HWindow window, final String path, final MobAttribute mob) throws Exception {
		super(window, path, mob);
	}

	public int testMove(final int saveDir, final boolean[] col) {
		if (saveDir == mobAttrib.HAUT) { // GAUCHE
			if (col[mobAttrib.HAUT]) { // on peut pas aller en haut
				final boolean g = col[mobAttrib.GAUCHE];
				if (g && col[mobAttrib.DROITE])
					return mobAttrib.BAS; // Si gauche droite pas possible -> bas
				else if (g)
					return mobAttrib.DROITE; // Si !gauche -> droite
				else
					return mobAttrib.GAUCHE; // Sinon gauche
			}

			return mobAttrib.HAUT;
		}

		if (saveDir == mobAttrib.DROITE) { // DROITE
			if (col[mobAttrib.DROITE]) {
				final boolean h = col[mobAttrib.HAUT], b = col[mobAttrib.BAS];
				if (h && b)
					return mobAttrib.GAUCHE;
				else if (h)
					return mobAttrib.BAS;
				else
					return mobAttrib.HAUT;
			}

			return mobAttrib.DROITE;
		}

		if (saveDir == mobAttrib.BAS) { // Bas
			if (col[mobAttrib.BAS]) { // Si on peut pas aller en bas
				final boolean g = col[mobAttrib.GAUCHE];
				if (g && col[mobAttrib.DROITE])
					return mobAttrib.HAUT; // Si gauche droite pas possible -> haut
				else if (g)
					return mobAttrib.DROITE; // Si !gauche -> droite
				else
					return mobAttrib.GAUCHE; // Sinon gauche
			}

			return mobAttrib.BAS;
		}
		if (saveDir == mobAttrib.GAUCHE) { // Gauche
			if (col[mobAttrib.GAUCHE]) {
				final boolean h = col[mobAttrib.HAUT], b = col[mobAttrib.BAS];
				if (h && b)
					return mobAttrib.DROITE;
				else if (h)
					return mobAttrib.BAS;
				else
					return mobAttrib.HAUT;
			}

			return mobAttrib.GAUCHE;
		}

		return -1;
	}

	public void move(final float interval, final Dungeon dungeon, final Player perso, final boolean[] col) {
		final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;

		// On veut savoir où aller sur les deux axes
		final int veutAllerX = deltaX < 0 ? mobAttrib.GAUCHE : mobAttrib.DROITE;
		final int veutAllerY = deltaY < 0 ? mobAttrib.HAUT : mobAttrib.BAS;

		final int casX = testMove(veutAllerX, col); // Cas pour les X
		final int casY = testMove(veutAllerY, col); // Cas pour les Y

		int want = -1;

		if (saveTo != -1)
			want = saveTo;
		// Si le chemin que l'on veut prendre en X est accessible, on le prend
		// On vérifie aussi si la distance qui nous sépare est assez grande
		if (want == -1 && casX == veutAllerX && Math.abs(deltaX) > .25f && !col[casX])
			want = casX;

		// Si le chemin que l'on veut prendre en Y est accessible, on le prend
		// Mais le chemin sur le X est prioritaire
		// On vérifie aussi si la distance est suffisamment grande
		if (want == -1 && casY == veutAllerY && Math.abs(deltaY) > .25f && !col[casY])
			want = casY;

		// Arrivé ici, cela veut dire qu'il y a un obstacle dans les deux axes (angle)
		// Ou alors qu'on est ligné (ligne, colonne) et qu'il y a un obstacle
		if (want == -1) {
			dirCant = -1;

			// Si collision en X et que le chemin alternatif accessible, on y va
			if (col[veutAllerX] && !col[casX]) {
				dirCant = veutAllerX;
				want = saveTo = casX;
			}

			if (dirCant == -1 && col[veutAllerY]) { // Obstacle en Y
				dirCant = veutAllerY;
				want = saveTo = casY;
			}
		}

		if (mobAttrib.twoDirectionOnly && want != mobAttrib.GAUCHE && want != mobAttrib.DROITE)
			direction = veutAllerX;
		else
			direction = want;

		// Lorsqu'il n'y a plus d'obstacle la ou on voulait alleer, on y va
		if (dirCant != -1 && !col[dirCant]) {
			want = dirCant; // Lorsque la voie est libre, on marche dans la direction qui était bloquée.
			saveTo = -1;
			dirCant = -1;
		}

		pos.add(deplacement(want, mobAttrib.moveSpeed));
		anim.update(interval, speed);
	}
}

package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.Animation;
import game.entity.Player;
import loader.generator.Dungeon;

public class Bat extends Mob {

	private static final int COLS = 3;

	private static final int ROWS = 4;

	private static final int BAS = 0;

	private static final int HAUT = 3;

	private static final int DROITE = 2;

	private static final int GAUCHE = 1;

	private final static int[] WALK = { 0, 3 };

	private final static MobAttribute mob = new MobAttribute(HAUT, BAS, GAUCHE, DROITE, COLS, ROWS);

	private float moveSpeed;

	public Bat(final HWindow window) throws Exception {
		super(window, "red-bat.png", mob);

		anim = new Animation(WALK);

		moveSpeed = 0.025f;
	}

	public void update(final float interval, final Dungeon dungeon, final Player perso) {
		final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;

		final float xFac = Math.abs(Math.abs(perso.getX()) - Math.abs(pos.x)),
				yFac = Math.abs(Math.abs(perso.getY()) - Math.abs(pos.y));

		int wantTo = 0;
		if (deltaX < 0) // Gauche
			wantTo = GAUCHE;
		else // Droite
			wantTo = DROITE;

		if (xFac < 0.25f) {
			if (deltaY < 0) // Haut
				wantTo = HAUT;
			else // Bas
				wantTo = BAS;
		}

		if (Math.abs(xFac - yFac) <= 0.5f) { // Déplacement en diagonale
			if (deltaX <= 0f) // Gauche
				direction = GAUCHE;
			else // Droite
				direction = DROITE;
		} else
			direction = wantTo;

		if (Math.abs(deltaY) <= 0.5f && Math.abs(deltaX) <= 0.5f) {// On est à cote du joueur donc on l'attaque !
			// ATTACK
		} else {
			anim.setMoves(WALK);
			pos.add(deplacement(direction, moveSpeed));
		}

		anim.update(interval, speed);
	}

}

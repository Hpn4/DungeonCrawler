package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.Animation;
import game.entity.Player;
import loader.generator.Dungeon;
import loader.generator.Salle;

public class Sprout extends CollisionMob {

	private static final int DROITE = 0;

	private static final int GAUCHE = 1;

	private static final int BAS = 2;

	private static final int HAUT = 3;

	private static final MobAttribute mob = new MobAttribute(HAUT, BAS, GAUCHE, DROITE, 8, 10);

	static {
		mob.twoDirectionOnly = true;
		mob.moveSpeed = 0.025f;
		mob.scaleTexture = 1.3f;
	}

	private final static int[] IDLE = { 0, 4 };

	private final static int[] WALK = { 2, 5 };

	private final static int[] DAMAGED = { 4, 5 };

	private final static int[] ATTACK = { 6, 6 };

	private final static int[] DEATH = { 8, 8 };

	public Sprout(final HWindow window) throws Exception {
		super(window, "sprout.png", mob);
		pos.x = 1;

		anim = new Animation(WALK);
		state = 0;
	}

	// 0 : walk, 1 : wait for damage, 2 : damage, 3 : joueur touché
	int state;

	float timer;

	public void update(final float interval, final Dungeon dungeon, final Player perso) {
		final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;
		// On laisse un petit temps de lateence avant que la créature attaque
		if (state == 1) {
			timer += interval;
			if (timer >= 0.15f) {
				timer = 0f;
				state = 2;
				anim.setIndex(0);
			}
		} else if (state == 2 || state == 3) { // Phase d'attaque
			anim.setMoves(ATTACK);
			anim.update(interval, speed);

			if (state == 2 && (anim.getIndex() == 3 || anim.getIndex() == 4) && Math.abs(deltaY) <= 0.7f) {
				// On vérifie que les joueur est au bon endroit par rapport a l'attaque (il peut
				// flash in pour esquiver)
				if ((direction == GAUCHE && deltaX >= -1.3f && deltaX <= 0f) || (deltaX <= 1.3f && deltaX >= 0f)) {
					System.out.println("touché");
					state = 3;
				}
			}

			if (anim.isFinish())
				state = 0;
		} else if (state == 0) {
			anim.setMoves(WALK);
			final Salle salle = dungeon.getActual();
			final float sp = mob.moveSpeed;

			final boolean[] col = { obstacle(pos.x + sp, pos.y, salle), obstacle(pos.x - sp, pos.y, salle),
					obstacle(pos.x, pos.y + sp, salle), obstacle(pos.x, pos.y - sp, salle) };

			move(interval, dungeon, perso, col);

			// Quand on est près du joueur, on attaque
			if (Math.abs(deltaY) <= 0.4f && Math.abs(deltaX) <= 0.8f)
				state = 1;

		}
	}

}

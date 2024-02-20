package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.utils.Animation;
import game.entity.Player;
import loader.generator.Dungeon;
import loader.generator.Salle;

public class Imp extends CollisionMob {

	private static final int BAS = 0;

	private static final int HAUT = 1;

	private static final int DROITE = 2;

	private static final int GAUCHE = 3;

	private final static int[] ATTACK = { 0, 4 };

	private final static int[] WALK = { 4, 4 };

	private final static MobAttribute mob = new MobAttribute(HAUT, BAS, GAUCHE, DROITE, 4, 8);

	static {
		mob.twoDirectionOnly = false;
		mob.moveSpeed = 0.025f;
	}

	public Imp(final HWindow window) throws Exception {
		super(window, "imp/vanilla.png", mob);

		anim = new Animation(WALK);
	}

	public void update(final float interval, final Dungeon dungeon, final Player perso) {
		final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;

		if (Math.abs(deltaY) <= 0.5f && Math.abs(deltaX) <= 0.5f) {
			anim.setMoves(ATTACK);
			anim.update(interval, speed);
		} else {
			anim.setMoves(WALK);
			final float sp = mob.moveSpeed;
			final Salle salle = dungeon.getActual();

			final boolean[] col = { obstacle(pos.x, pos.y + sp, salle), obstacle(pos.x, pos.y - sp, salle),
					obstacle(pos.x + sp, pos.y, salle), obstacle(pos.x - sp, pos.y, salle) };

			move(interval, dungeon, perso, col);
		}
	}
}

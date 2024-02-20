package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.Animation;
import game.entity.Player;
import game.entity.Stat;
import loader.generator.Dungeon;
import loader.generator.Salle;

public class PlantBomb extends CollisionMob {

    private static final int DROITE = 1;

    private static final int GAUCHE = 0;

    private static final int BAS = 2;

    private static final int HAUT = 3;

    private static final MobAttribute mob = new MobAttribute(HAUT, BAS, GAUCHE, DROITE, 12, 4);

    static {
        mob.twoDirectionOnly = true;
        mob.moveSpeed = 0.04f;
        mob.scaleTexture = 0.9f;

        // Pour le calul de la position
        mob.yPosModifier = -0.5f;

        // Pour les collisions
        mob.hit.left = 0.9f;
        mob.hit.top = 0.8f;
        mob.hit.right = 0.1f;
        mob.hit.bot = 0.5f;

        // 70 point de vie, pas de mana et 4% de chance de crit
        mob.stat = new Stat(70, 0, 0.04f);

        // C'est une bombe, bcp de degat
        mob.stat.atkMin = 47;
        mob.stat.atkMax = 54;

        // Peu de défense, c'est une bombe donc vulnérable
        mob.stat.defMin = 5;
        mob.stat.defMax = 11;

        // Les stats ne varient pas beacoup
        mob.variance = new Stat(12, 0, 0.02f);
        mob.variance.atkMin = 7;
        mob.variance.defMin = 3;
    }

    private final static int[] WALK = {2, 12};

    private final static int[] DEATH = {0, 11};

    // 0 : walk, 1 : bomb, 2 : joueur touché
    private int state;

    private float time;

    public PlantBomb(final HWindow window) throws Exception {
        super(window, "plantBomb.png", mob);

        anim = new Animation(WALK);
        state = 0;
    }

    public void update(final float interval, final Dungeon dungeon, final Player perso) {
        final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;

        // Lorsque elle a plus de vie, no AI
        if (stat.vie <= 0) {
            stat.vie = 0.1f;
            state = 1;
            anim.setIndex(0);
        }

        if (state == 0) {
            anim.setMoves(WALK);
            final Salle salle = dungeon.getActual();
            final float sp = mob.moveSpeed;

            final boolean[] col = {obstacle(pos.x - sp, pos.y, salle), obstacle(pos.x + sp, pos.y, salle),
                    obstacle(pos.x, pos.y + sp, salle), obstacle(pos.x, pos.y - sp, salle)};

            move(interval, dungeon, perso, col);

            // Lorsqu'on est près du joueur, ça pete !!
            if (Math.abs(deltaY) <= 1.1f && Math.abs(deltaX) <= 1.1f) {
                anim.setIndex(0);
                state = 1;
            }
        } else if (state >= 1) {
            time += interval;

            if (time >= 0.3f) {
                stat.vie = 0.1f;
                anim.setMoves(DEATH);
                anim.update(interval, speed);

                // Lorsque l'explosion est finie
                if (anim.isFinish()) {
                    anim.setIndex(0);
                    state = -1;
                    stat.vie = 0;
                }

                if (Math.abs(deltaY) <= 1.3f && Math.abs(deltaX) <= 1.6f
                        && (anim.getIndex() >= 3 && anim.getIndex() <= 8) && state == 1) {
                    doDamage(perso);
                    state = 2; // On a eue le joueur
                }
            }
        }
    }

}

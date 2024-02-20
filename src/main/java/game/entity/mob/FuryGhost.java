package game.entity.mob;

import engine.graph.hlib.component.HWindow;
import game.entity.utils.Animation;
import game.entity.Player;
import game.entity.utils.Stat;
import loader.generator.Dungeon;

public class FuryGhost extends Mob {

    private static final int BAS = 2;

    private static final int HAUT = 3;

    private static final int DROITE = 0;

    private static final int GAUCHE = 1;

    private final static int[] DAMAGE = {0, 4};

    private final static int[] IDLE = {2, 8};

    private final static int[] RUN = {4, 8};

    private final static int[] WALK = {6, 8};

    private final static int[] DEATH = {8, 10};

    private final static int[] ATTACK = {10, 10};

    private final static float timeRange = 3;

    private final static MobAttribute mob = new MobAttribute(HAUT, BAS, GAUCHE, DROITE, 10, 12);

    static {
        mob.twoDirectionOnly = true;
        mob.moveSpeed = 0.03f;
        mob.scaleTexture = 1.4f;

        // Il a pas mal de vie
        mob.stat = new Stat(150, 0, 0.05f);

        // C'est un esprit, il n'a pas d'armure
        mob.stat.defMin = 2;
        mob.stat.defMin = 7;

        // Il n'attaque qu'une seule fois, mais bcp degat
        mob.stat.atkMin = 80;
        mob.stat.atkMin = 90;

        // Les stats ne varient pas beacoup
        mob.variance = new Stat(12, 0, 0.02f);
        mob.variance.atkMin = 17;
        mob.variance.defMin = 2;
    }

    public FuryGhost(final HWindow window) throws Exception {
        super(window, "furyGhost.png", mob);

        anim = new Animation(WALK);
        state = 0;
        timeBefore = (float) (Math.random() * timeRange + 1);
    }

    // 0 : idle, 1 : walk, 2 : run, 3 : attack, 4 : joueur touché, 5 : meurt, 6 :
    // prends un coup
    int state;

    float time;

    float timeBefore;

    int saveDir;

    float savedX;

    public void update(final float interval, final Dungeon dungeon, final Player perso) {
        final float deltaX = perso.getX() - pos.x, deltaY = perso.getY() - pos.y;

        if (state != 5 && stat.vie <= 0) {
            anim.setIndex(0);
            state = 5;
        }

        if (state == 0) {
            anim.setMoves(IDLE);
            anim.update(interval, speed);

            time += interval;

            // Lorsq'il a été idle suffisement longtemps on le passe en mode marche
            if (time >= timeBefore) {
                time = 0;
                state = 1;
                timeBefore = (float) (Math.random() * timeRange + 1);
            }
            // Lorsque le fantome est dans la phase de marche, il se déplace aléatoirement
        } else if (state == 1) {
            if (deltaX < 0) // Gauche
                direction = GAUCHE;
            else // Droite
                direction = DROITE;

            if (saveDir == -1) {
                // Le fantome va aléatoirement dans une direction. Il tient cette direction
                // jusqu'au prochain cycle
                // On a 70% de chance d'aller dans la bonne direction et 10% d'aller dans une
                // autre direction.
                final float rand = (float) Math.random();
                if (rand <= 0.1f)
                    direction = saveDir = GAUCHE;
                else if (rand <= 0.2f)
                    direction = saveDir = DROITE;
                else if (rand <= 0.9f) {
                    saveDir = deltaY < 0 ? HAUT : BAS;
                    direction = Math.random() > 0.5f ? GAUCHE : DROITE;
                } else {
                    saveDir = deltaY < 0 ? BAS : HAUT;
                    direction = Math.random() > 0.5f ? GAUCHE : DROITE;
                }
            }

            boolean can = false;
            switch (saveDir) {
                case GAUCHE:
                    can = obstacle(pos.x - mob.moveSpeed, pos.y, dungeon.getActual());
                    break;
                case DROITE:
                    can = obstacle(pos.x + mob.moveSpeed, pos.y, dungeon.getActual());
                    break;
                case HAUT:
                    can = obstacle(pos.x, pos.y - mob.moveSpeed, dungeon.getActual());
                    break;
                case BAS:
                    can = obstacle(pos.x, pos.y + mob.moveSpeed, dungeon.getActual());
                    break;
            }

            if (!can) {
                anim.setMoves(WALK);
                pos.add(deplacement(saveDir, mob.moveSpeed));
                anim.update(interval, speed);
            }

            time += interval;

            // Lorsqu'il à marcher et pas réussi a etteindre le joueur, il repasse en idle
            if (can || time >= timeBefore) {
                time = 0;
                state = 0;
                timeBefore = (float) (Math.random() * timeRange + 1);
                saveDir = -1;
            }

            if (Math.abs(deltaY) <= 0.6) {
                state = 2;
                savedX = perso.getX();
                anim.setIndex(0);
            }
        } else if (state == 2) { // Phase de course
            final float difX = savedX - pos.x;
            if (difX < 0) // Gauche
                direction = GAUCHE;
            else // Droite
                direction = DROITE;

            // Il va treeees vite 10X plus rapidement
            anim.setMoves(RUN);
            pos.add(deplacement(direction, mob.moveSpeed * 20));
            anim.update(interval, speed * 4);

            if (Math.abs(difX) <= 2) {// Lorsqu'on est suffisement proche, phase d'attaque
                state = 3;
                anim.setIndex(0);
            }
        } else if (state == 3 || state == 4) {
            anim.setMoves(ATTACK);
            anim.update(interval, speed);

            // Bon pour calculer si l'attaque touche
            if (state == 3 && (anim.getIndex() == 6 || anim.getIndex() == 7) && Math.abs(deltaY) <= 0.7f) {
                if ((direction == GAUCHE && deltaX >= -2f && deltaX <= 0f) || (deltaX <= 2f && deltaX >= 0f)) {
                    dealDamageTo(perso);
                    state = 4;
                }
            }

            // Après l'attaque on revient en idle
            if (anim.isFinish()) {
                time = 0;
                state = 0;
                savedX = 0;
                timeBefore = (float) (Math.random() * timeRange + 1);
            }
        } else if (state == 5) {
            stat.vie = 0.1f;
            anim.setMoves(DEATH);
            anim.update(interval, speed * 0.9f);

            if (anim.isFinish()) {
                state = -1;
                stat.vie = 0f;
            }
        } else if (state == 6) {
            anim.setMoves(DAMAGE);
            anim.update(interval, speed * 1.5f);

            if (anim.isFinish()) {
                state = 1;
                anim.setIndex(0);
            }
        }
    }

    public void setDamaged() {
        // Si il n'est pas mort
        if (state != 5) {
            state = 6;
            anim.setIndex(0);
        }
    }
}

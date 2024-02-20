package game.entity;

import game.entity.utils.Animation;
import game.entity.utils.Stat;
import org.joml.Vector2f;

import game.entity.utils.HitBoxInsets;
import loader.generator.Salle;
import loader.map.AbstractLayer;

public abstract class Entity implements Comparable<Entity> {

    // Position of the entity
    protected final Vector2f pos;

    protected int direction;

    // Current played animation
    protected Animation anim;

    protected Stat stat;

    public Entity() {
        direction = 0;
        pos = new Vector2f();
    }

    public Vector2f getPos() {
        return pos;
    }

    public Stat getStat() {
        return stat;
    }

    @Override
    public int compareTo(final Entity entity) {
        return Float.compare(pos.y, entity.pos.y);
    }

    public abstract void render();

    public abstract void spawn(final Salle salle);

    public boolean collide(final Salle salle, final float x, final float y, final HitBoxInsets hit) {
        final AbstractLayer layer = salle.getMap().getLayer("collision");

        // Get the tile where the entity holds
        final float ecartX = x - (int) x, ecartY = y - (int) y;
        final int tile = layer.get((int) x, (int) y).getTile();

        // Si c'est de l'air, il n'y a pas de collision
        if (tile == 0)
            return false;

        return switch (tile) {

            // Moitié haute
            case 5 -> ecartY <= hit.top;

            // Moitié basse
            case 6 -> ecartY >= hit.bot;

            // Côté droit
            case 7 -> ecartX >= hit.right;

            // Côté gauche
            case 8 -> ecartX <= hit.left;

            // Carré bas droit
            case 10 -> ecartY >= hit.bot && ecartX >= hit.right;

            // Carré bas gauche
            case 11 -> ecartY >= hit.bot && ecartX <= hit.left;

            // Carré haut droit
            case 15 -> ecartY <= hit.top && ecartX >= hit.right;

            // Carré haut gauche
            case 16 -> ecartY <= hit.top && ecartX <= hit.left;

            // Angle haut gauche
            case 12 -> ecartY <= hit.top || ecartX <= hit.left;

            // Angle haut droit
            case 13 -> ecartY <= hit.top || ecartX >= hit.right;

            // Angle bas gauche
            case 17 -> ecartY >= hit.bot || ecartX <= hit.left;

            // Angle bas droit
            case 18 -> ecartY >= hit.bot || ecartX >= hit.right;

            // Case pleine ou triangle pas encore fait
            case 400 -> true;
            default -> false;
        };
    }

    public void cleanup() {

    }

    protected float halfRound(float n) {
        final int defInt = (int) n;
        float round = n - defInt;
        if (round <= 0.25f)
            n = defInt;
        else if (round <= 0.75f)
            n = defInt + 0.5f;
        else
            n = defInt + 1;

        return n;
    }

    protected boolean fallInHole(float x, float y, Salle salle, float yFeet) {
        final float yPos = y + yFeet;
        final int pX = (int) x, pY = (int) y;

        final float ecartX = x - (int) x;
        final float ecartY = yPos - (int) yPos;
        final int tile = salle.getTileId(pX, pY, 0);
        if (tile == 432 && ecartY >= 0.2f)
            return true;
        else if (tile == 456 && ecartX <= 0.3f)
            return true;
        else if (tile == 478 && ecartY <= 0.4f)
            return true;
        else if (tile == 454 && ecartX >= 0.2f)
            return true;

        return tile == 455;

        // if (tile == 479 || tile == 477 || tile == 431 || tile == 433 || tile == 386)
        // return true;
    }

    public boolean dealDamageTo(Entity entity) {
        // Our attack
        float atk = (float) (Math.random() * (stat.atkMax - stat.atkMin) + stat.atkMin);

        if (Math.random() <= stat.crit)
            atk *= 2;
        atk = halfRound(atk);

        // Calculate the mob's defense
        float def = (float) (Math.random() * (entity.getStat().defMax - entity.getStat().defMin) + entity.getStat().defMin);

        if (Math.random() <= entity.getStat().crit)
            def *= 2;
        def = halfRound(def);

        // Reduce the mob's life
        entity.getStat().vie -= atk - def;
        return entity.getStat().vie > 0;
    }
}

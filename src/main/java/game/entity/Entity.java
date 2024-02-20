package game.entity;

import org.joml.Vector2f;

import game.entity.mob.HitBoxInsets;
import loader.generator.Salle;
import loader.map.AbstractLayer;

public abstract class Entity implements Comparable<Entity> {

    protected final Vector2f pos;

    protected int direction;

    protected Animation anim;

    public Entity() {
        direction = 0;
        pos = new Vector2f();
    }

    public Vector2f getPos() {
        return pos;
    }

    @Override
    public int compareTo(final Entity entity) {
        return Float.compare(pos.y, entity.pos.y);
    }

    public abstract void render();

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
}

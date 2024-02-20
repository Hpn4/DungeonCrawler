package game.entity.mob;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector2f;

import engine.graph.hlib.component.HWindow;
import engine.graph.renderer.mesh.Mesh;
import engine.graph.renderer.ShaderProgram;
import game.entity.Entity;
import game.entity.Player;
import game.entity.Stat;
import loader.generator.Dungeon;
import loader.generator.Salle;
import loader.texture.Texture;
import loader.texture.TextureCache;

public abstract class Mob extends Entity {

    protected ShaderProgram bodyShaderProgram;

    protected float speed;

    protected Texture body;

    protected MobAttribute mobAttrib;

    protected Stat stat;

    public Mob(final HWindow window, final String path, final MobAttribute mob) throws Exception {
        super();
        this.mobAttrib = mob;

        body = (Texture) TextureCache.getTexture("images/mob/" + path);

        bodyShaderProgram = new ShaderProgram("perso");

        bodyShaderProgram.createUniform("matrix");

        bodyShaderProgram.createUniform("pos");
        bodyShaderProgram.createUniform("scale");

        bodyShaderProgram.createUniform("numCols");
        bodyShaderProgram.createUniform("numRows");
        bodyShaderProgram.createUniform("texOff");

        bodyShaderProgram.createUniform("texBody");

        bodyShaderProgram.bind();

        bodyShaderProgram.setMatrix4f("matrix", window.getOrthoMatrix());
        bodyShaderProgram.setFloat("scale", mob.scaleTexture);
        bodyShaderProgram.setInt("texBody", 0);
        bodyShaderProgram.setInt("numCols", mob.COLS);
        bodyShaderProgram.setInt("numRows", mob.ROWS);

        bodyShaderProgram.unbind();

        speed = 1f;

        stat = new Stat(mobAttrib.stat, mobAttrib.variance);

        pos.x = 5;
        pos.y = 15;
    }

    public Vector2f deplacement(final int direction, final float sp) {
        if (direction == mobAttrib.HAUT) // Haut
            return new Vector2f(0, -sp);
        if (direction == mobAttrib.DROITE) // Droite
            return new Vector2f(sp, 0);
        if (direction == mobAttrib.BAS) // Bas
            return new Vector2f(0, sp);
        if (direction == mobAttrib.GAUCHE) // Gauche
            return new Vector2f(-sp, 0);

        return new Vector2f(0, 0);
    }

    public abstract void update(final float interval, final Dungeon dungeon, final Player perso);

    public void render() {
        bodyShaderProgram.bind();

        bodyShaderProgram.setVector2f("texOff", anim.getPos(direction, mobAttrib.COLS, mobAttrib.ROWS));
        bodyShaderProgram.setVector2f("pos", pos);

        glActiveTexture(GL_TEXTURE0);
        body.bind();

        Mesh.quad.render();

        bodyShaderProgram.unbind();
    }

    public void cleanup() {
        body.cleanup();
        bodyShaderProgram.cleanup();
    }

    // 0 : haut, 1 : gauche, 2 : bas, 3 : droite
    public boolean obstacle(final float x, final float y, final Salle salle) {
        // On calcul les futur coordonnées
        final float xPos = x, yPos = y + mobAttrib.yPosModifier;

        if (collide(salle, xPos, yPos, mobAttrib.hit))
            return true;

        return trou(x, y, salle);
    }

    public boolean trou(final float x, final float y, final Salle salle) {
        final float xPos = x, yPos = y + mobAttrib.yPosModifier;
        int pX = (int) xPos, pY = (int) yPos;

        // Holes
        final float ecartX = xPos - (int) xPos;
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

    public Stat getStat() {
        return stat;
    }

    /**
     * Effectue les degats au joueur
     *
     * @param perso Le joueur
     */
    public void doDamage(final Player perso) {
        // On extrait l'attaque du mob
        float atk = (float) (Math.random() * (stat.atkMax - stat.atkMin) + stat.atkMin);

        // Si coup critique on double l'attaque
        if (Math.random() <= stat.crit)
            atk *= 2;
        atk = halfRound(atk);

        // On extrait la defense du joueur
        float def = (float) (Math.random() * (perso.getStat().defMax - perso.getStat().defMin)
                + perso.getStat().defMin);

        // Si coup critique on double la defense (def critique)
        if (Math.random() <= perso.getStat().crit)
            def *= 2;
        def = halfRound(def);

        // On enleve la vie au joueur
        perso.getStat().vie -= atk - def;
    }

    public MobAttribute getAttribute() {
        return mobAttrib;
    }

    public void setDamaged() {

    }
}

package game.entity.mob;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Random;
import org.joml.Vector2f;

import engine.graph.hlib.component.HWindow;
import engine.graph.renderer.mesh.Mesh;
import engine.graph.renderer.ShaderProgram;
import game.entity.Entity;
import game.entity.Player;
import game.entity.utils.Stat;
import loader.generator.Dungeon;
import loader.generator.Salle;
import loader.texture.Texture;
import loader.texture.TextureCache;

public abstract class Mob extends Entity {

    protected ShaderProgram bodyShaderProgram;

    protected float speed;

    protected Texture body;

    protected MobAttribute mobAttrib;

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
    }

    @Override
    public void spawn(Salle salle) {
        Random rand = new Random();
        do {
            pos.x = rand.nextInt(26) + 2;
            pos.y = rand.nextInt(14) + 4;
        } while (obstacle(pos.x, pos.y, salle));
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
        final float yPos = y + mobAttrib.yPosModifier;

        if (collide(salle, x, yPos, mobAttrib.hit))
            return true;

        return fallInHole(x, y, salle, mobAttrib.yPosModifier);
    }

    public MobAttribute getAttribute() {
        return mobAttrib;
    }

    public void setDamaged() {

    }
}

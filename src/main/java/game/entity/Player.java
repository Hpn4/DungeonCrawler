package game.entity;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import game.entity.utils.Animation;
import game.entity.utils.Stat;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.graph.hlib.component.HWindow;
import engine.graph.renderer.DungeonRenderer;
import engine.graph.renderer.mesh.Mesh;
import engine.graph.renderer.ShaderProgram;
import game.entity.utils.HitBoxInsets;
import game.entity.mob.Mob;
import loader.generator.Dungeon;
import loader.generator.Salle;
import loader.map.AbstractLayer;
import loader.texture.Texture;
import loader.texture.TextureCache;
import loader.texture.TextureCombine;

public class Player extends Entity {

    // Là où commences l'animation (ligne), le nombre de frame
    private final static int[] SPELL = {0, 7};

    private final static int[] WALK = {8, 9};

    private final static int[] SWORD = {12, 6};

    private final static int COLS = 13;

    private final static int ROWS = 21;

    private final static HitBoxInsets hits = new HitBoxInsets(0.5f);

    private boolean dash;

    private boolean isAtk;

    private boolean needAnim;

    private float xInc;

    private float yInc;

    private float speed;

    // 0 : haut, 1 : gauche, 2 : bas, 3 : droite
    private float timerForDash;

    private float timer;

    // Le corp ainsi que tout les accessoires, armures...
    private final Texture body;

    private final Texture weapon;

    private final ShaderProgram perso;

    private final ShaderProgram weaponShaderProgram;

    public Player(final HWindow window) throws Exception {
        super();

        // Load textures
        body = TextureCombine.combine("resources/test/corp.png", "resources/test/jambe.png", "resources/test/arme.png");
        weapon = (Texture) TextureCache.getTexture("test/attaque.png");

        anim = new Animation(WALK);

        // The shader program for the body
        perso = new ShaderProgram("player");

        perso.createUniform("matrix");
        perso.createUniform("isDashing");
        perso.createUniform("pos", "scale");
        perso.createUniform("numCols", "numRows");
        perso.createUniform("texOff", "texBody");

        perso.bind();

        perso.setMatrix4f("matrix", window.getOrthoMatrix());
        perso.setFloat("scale", 1);
        perso.setInt("texBody", 0);
        perso.setInt("numCols", COLS);
        perso.setInt("numRows", ROWS);

        perso.unbind();

        // The sahder program for the weapon
        weaponShaderProgram = new ShaderProgram("weapon");

        weaponShaderProgram.createUniform("pos", "scale");
        weaponShaderProgram.createUniform("matrix");
        weaponShaderProgram.createUniform("texOff", "texWeapon");

        weaponShaderProgram.bind();

        weaponShaderProgram.setMatrix4f("matrix", window.getOrthoMatrix());
        weaponShaderProgram.setFloat("scale", 3f);
        weaponShaderProgram.setInt("texWeapon", 0);

        weaponShaderProgram.unbind();

        direction = 1;

        stat = new Stat(250, 100, 0.05f);
        stat.atkMin = 40;
        stat.atkMax = 45;
        stat.defMin = 10;
        stat.defMax = 15;
        stat.regenMana = 0.5f;
        stat.regenVie = 1;
    }

    public void input(final HWindow window) {
        if (dash)
            isAtk = dealDamage = false;
        else {
            if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {
                needAnim = true;
                anim.setMoves(SWORD);
                isAtk = true;
            }

            if (window.isKeyPressed(GLFW.GLFW_KEY_F)) {
                needAnim = true;
                anim.setMoves(SPELL);
            }
        }

        if (window.isKeyReleassed(GLFW.GLFW_KEY_F) || window.isKeyReleassed(GLFW.GLFW_KEY_Q))
            anim.setMoves(WALK);

        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            direction = 0;
            yInc = -0.05f;
            needAnim = true;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            direction = 2;
            yInc = 0.05f;
            needAnim = true;
        } else
            yInc = 0f;

        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            direction = 3;
            xInc = 0.05f;
            needAnim = true;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            direction = 1;
            xInc = -0.05f;
            needAnim = true;
        } else
            xInc = 0f;

        if (timerForDash >= 2f && window.isKeyPressed(GLFW.GLFW_KEY_SPACE) && stat.mana > 5) {
            speed = 7f;
            anim.setMoves(WALK);
            anim.setIndex(0);
            timerForDash = 0f;
            needAnim = dash = true;
            stat.mana -= 5;
        }
    }

    private boolean dealDamage;

    public Salle update(final float interval, final HWindow window, final DungeonRenderer dungeonRenderer) {
        final Dungeon dungeon = dungeonRenderer.getDungeon();
        final Salle salle = dungeon.getActual();
        salle.setVisited(true);

        if (dash && anim.getIndex() >= 4) {
            dash = false;
            speed = 1.5f;
        }

        timerForDash += interval;
        timer += interval;

        if (isAtk) {

            if (anim.isFinish())
                dealDamage = isAtk = false;

            if (anim.getIndex() == 4 && !dealDamage) {
                for (final Mob mob : dungeon.getActual().getMobs()) {
                    final float deltaX = mob.pos.x - pos.x, deltaY = mob.pos.y - pos.y;

                    switch (direction) {
                        case 0: // UP
                            if (deltaY >= (-1.9f + mob.getAttribute().hit.bot) && deltaY <= 0 && Math.abs(deltaX) <= 0.7)
                                dealDamage = true;
                            break;
                        case 1: // LEFT
                            if (deltaX >= -1.9f && deltaX <= 0 && Math.abs(deltaY) <= 0.6)
                                dealDamage = true;
                            break;
                        case 2: // DOWN
                            if (deltaY <= 1.1f && deltaY >= 0 && Math.abs(deltaX) <= 0.7)
                                dealDamage = true;
                            break;
                        case 3: // RIGHT
                            if (deltaX <= 1.9f && deltaX >= 0 && Math.abs(deltaY) <= 0.6)
                                dealDamage = true;
                            break;
                    }

                    if (dealDamage) {
                        dealDamageTo(mob);
                        mob.setDamaged();
                    }
                }
            }
        }

        if (xInc != 0 || yInc != 0 || needAnim) {
            anim.update(interval, speed);

            if (xInc != 0 && yInc != 0)
                speed = dash ? 3.5f : 1f;
            else if (!dash)
                speed = 1.5f;

            final float dX = xInc * speed, dY = yInc * speed;
            final float xPos = pos.x, yPos = pos.y + 0.8f;
            final int pX = (int) pos.x, pY = (int) pos.y;

            if (!collide(salle, xPos + dX, yPos + dY, hits)) {
                pos.x += dX;
                pos.y += dY;
            }
            needAnim = false;

            // Holes
            if (fallInHole(pos.x, pos.y, salle, 0.8f)) {
                pos.x = 2.3f;
                pos.y = 10;
            }

            // Les portes
            final AbstractLayer murLayer = salle.getMap().getLayer("mur");
            final int tileP = murLayer.get(pX, pY).getTile();

            // Haut
            if (yInc != 0) {
                int tileDessus = murLayer.get(pX + 1, pY).getTile(), tileDessous = murLayer.get(pX - 1, pY).getTile();
                if (yInc < 0 && (tileP == 384 || tileDessus == 384 || tileDessous == 384)) {
                    pos.x = 15;
                    pos.y = 18;
                    return dungeon.move(0, -1);
                }

                // Bas
                if (yInc > 0 && (tileP == 383 || tileDessus == 383 || tileDessous == 383)) {
                    pos.x = 15;
                    pos.y = 2;
                    return dungeon.move(0, 1);
                }
            }

            int tileDessus = murLayer.get(pX, pY + 1).getTile(), tileDessous = murLayer.get(pX, pY - 1).getTile();
            // Gauche
            if (xInc < 0 && (tileP == 305 || tileDessus == 305 || tileDessous == 305)) {
                pos.y = 10;
                pos.x = 28;
                return dungeon.move(-1, 0);
            }

            // Droite
            if (xInc > 0 && (tileP == 306 || tileDessus == 306 || tileDessous == 306)) {
                pos.y = 10;
                pos.x = 2;
                return dungeon.move(1, 0);
            }
        }

        // Regen de vie et de mana
        if (timer >= 1.0f) {
            timer = 0.0f;
            stat.vie += stat.regenVie;
            stat.mana += stat.regenMana;

            if (stat.vie > stat.vieMax)
                stat.vie = stat.vieMax;

            if (stat.mana > stat.manaMax)
                stat.mana = stat.manaMax;
        }

        return null;
    }

    public void render() {
        perso.bind();

        perso.setVector2f("texOff", anim.getPos(direction, COLS, ROWS));
        perso.setVector2f("pos", pos.x, pos.y);

        perso.setBoolean("isDashing", dash);

        glActiveTexture(GL_TEXTURE0);
        body.bind();

        Mesh.quad.render();

        perso.unbind();

        if (isAtk) {
            weaponShaderProgram.bind();

            final int tilePos = 8 * direction + anim.getIndex();
            final int col = tilePos % 8, row = tilePos / 8;

            weaponShaderProgram.setVector2f("texOff", new Vector2f((float) col / 8, (float) row / 4));
            weaponShaderProgram.setVector2f("pos", pos.x, pos.y);

            glActiveTexture(GL_TEXTURE0);
            weapon.bind();

            Mesh.quad.render();

            weaponShaderProgram.unbind();
        }
    }

    @Override
    public void spawn(Salle salle) {
        pos.x = 15;
        pos.y = 10;
    }

    public void cleanup() {
        body.cleanup();
        weapon.cleanup();
        perso.cleanup();
    }

    public float getTimerForDash() {
        return timerForDash;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }
}

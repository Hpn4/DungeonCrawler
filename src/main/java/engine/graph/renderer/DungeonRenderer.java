package engine.graph.renderer;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;

import engine.graph.renderer.mesh.Mesh;

import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.event.MouseInput;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import game.entity.Entity;
import game.entity.Player;
import game.entity.utils.Stat;
import game.entity.mob.FuryGhost;
import game.entity.mob.Mob;
import game.entity.mob.PlantBomb;
import loader.generator.Dungeon;
import loader.generator.Salle;
import loader.map.ZLayer;
import loader.map.ZTile;
import org.joml.Vector2i;

public class DungeonRenderer {

    private static final int MOB_HUD = 100;

    private Dungeon dungeon;

    private final MapRenderer mapRenderer;

    private final MiniMapRenderer miniMapRenderer;

    private final Player perso;

    private final ArrayList<Entity> entities;

    public DungeonRenderer(final HWindow window, final Dungeon dungeon) throws Exception {
        mapRenderer = new MapRenderer(window);
        miniMapRenderer = new MiniMapRenderer();
        perso = new Player(window);
        perso.spawn(null);

        this.dungeon = dungeon;

        dungeon.getActual().getMobs().add(new PlantBomb(window));
        dungeon.getActual().getMobs().add(new FuryGhost(window));

        entities = new ArrayList<>();

        setupRoom(dungeon.getActual());
    }

    private void setupRoom(Salle salle) {
        try {
            mapRenderer.setSalle(salle);
        } catch (final Exception e) {
            System.err.println(e.getMessage());
        }
        entities.clear();
        entities.addAll(salle.getMobs());
        for (Entity entitie : entities)
            entitie.spawn(salle);
        entities.add(perso);
    }

    public void setDungeon(final Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void input(final HWindow window, final MouseInput mouseInput) {
        perso.input(window);
        miniMapRenderer.input(window, mouseInput);
    }

    public void update(final float interval, final HWindow window) {
        final Salle salle = perso.update(interval, window, this);
        if (salle != null)
            setupRoom(salle);

        for (int i = 0; i < entities.size(); ) {
            if (entities.get(i) instanceof Mob mob) {
                mob.update(interval, dungeon, perso);
                if (mob.getStat().vie <= 0) {
                    dungeon.getActual().getMobs().remove(mob);
                    entities.remove(i);
                    continue;
                }
            }

            ++i;
        }

        entities.sort(null);
        if (!mapRenderer.getZLayers().isEmpty())
            mapRenderer.getZLayers().get(0).sort();
    }

    public void render(final HWindow window, final Dungeon dungeon) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        mapRenderer.render();

        // ZLayer
        final ArrayList<ZLayer> zLayers = mapRenderer.getZLayers();
        if (!zLayers.isEmpty()) {
            final ZLayer l = zLayers.get(0);
            final int cols = l.getTileset().getCols(), rows = l.getTileset().getRows();
            final int size = l.size();

            int index = 0;
            for (Entity entity : entities) {
                final float y = entity.getPos().y * 3.0f / 2.0f + 11.2f;

                mapRenderer.getShader().bind();

                mapRenderer.getShader().setInt("numCols", cols);
                mapRenderer.getShader().setInt("numRows", rows);

                glActiveTexture(GL_TEXTURE0);
                l.getTileset().getTexture().bind();

                while (index < size) {
                    final ZTile tile = l.get(index);
                    final int pos = tile.getTile();
                    if (tile.getY() < (int) y) {
                        if (pos != 0) {
                            mapRenderer.getShader().setVector3f("tilePos", tile.getX(), tile.getY(),
                                    0.0f);

                            final int col = pos % cols, row = pos / cols;
                            mapRenderer.getShader().setVector2f("texOff", (float) col / cols, (float) row / rows);

                            l.getTileset().getTexture().bind();

                            Mesh.quad.render();
                        }
                    } else
                        break;
                    index++;
                }

                mapRenderer.getShader().unbind();

                entity.render();
            }

            if (index < size) {
                mapRenderer.getShader().bind();

                mapRenderer.getShader().setInt("numCols", cols);
                mapRenderer.getShader().setInt("numRows", rows);

                glActiveTexture(GL_TEXTURE0);

                for (; index < size; index++) {
                    final ZTile tile = l.get(index);
                    final int pos = tile.getTile();
                    if (pos != 0) {
                        mapRenderer.getShader().setVector3f("tilePos", tile.getX(), tile.getY(), 0.0f);

                        final int col = pos % cols, row = pos / cols;
                        mapRenderer.getShader().setVector2f("texOff", (float) col / cols, (float) row / rows);

                        l.getTileset().getTexture().bind();

                        Mesh.quad.render();
                    }
                }

                mapRenderer.getShader().unbind();
            }
        } else
            for (final Entity entity : entities)
                entity.render();

        glDisable(GL_BLEND);

        renderHud(window, dungeon);
    }

    public void renderHud(final HWindow window, final Dungeon dungeon) {
        miniMapRenderer.render(window, dungeon);

        final Graphics g = window.getGraphics();
        g.startRendering(window);

        // HUD du joueur
        final Stat stat = perso.getStat();

        // BG
        g.setPaint(Color.GRAY);
        g.fillRoundRect(10, 10, 420, 120, 10);

        // Barres de vie et de mana
        g.setPaint(Color.RED);
        g.fillRect(20, 20, (int) (stat.vie * 400.0f / stat.vieMax), 35);

        g.setPaint(Color.CORNFLOWERBLUE);
        g.fillRect(20, 60, (int) (stat.mana * 300.0f / stat.manaMax), 20);

        // Text pour indiquer la vie et le mana
        g.setPaint(Color.YELLOW);
        g.drawText(170, 45, stat.vie + "/" + stat.vieMax, Color.YELLOW, 30);
        g.drawText(100, 80, stat.mana + "/" + stat.manaMax);

        g.setPaint(Color.BLACK);
        g.drawRect(20, 20, 400, 35);
        g.drawRect(20, 60, 300, 20);

        // Timer pour le dash
        g.fillRect(20, 85, 40, 40);

        if (perso.getTimerForDash() >= 2f) {
            g.setPaint(Color.YELLOW);
            g.fillRect(21, 86, 38, 38);
        } else {
            g.setPaint(Color.ALICEBLUE);
            g.fillRect(21, 86, (int) (perso.getTimerForDash() * 19.0f), 38);
        }

        // MOB hud
        for (final Entity entity : entities)
            if (entity instanceof Mob mob) {
                final Vector2i pos = tileToScreen(window, entity.getPos().x, entity.getPos().y);
                pos.y -= 20;
                pos.x -= MOB_HUD / 2;

                g.setPaint(Color.CORNFLOWERBLUE);
                float life = mob.getStat().vie < 0 ? 0 : mob.getStat().vie;
                life = life * MOB_HUD / mob.getStat().vieMax;
                g.fillRect(pos.x, pos.y, (int) life, 8);

                g.setPaint(Color.BLACK);
                g.drawRect(pos.x, pos.y, MOB_HUD, 8);
            }

        g.endRendering(window);
    }

    private Vector2i tileToScreen(HWindow window, final float x, final float y) {
        int xScreen = (int) (window.getWidth() / 2 + (x - 15) * HWindow.SCALE) - 15;
        int yScreen = (int) (y * HWindow.SCALE) - 5;

        return new Vector2i(xScreen, yScreen);
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void cleanup() {
        mapRenderer.cleanup();
        dungeon.cleanup();

        perso.cleanup();
    }
}

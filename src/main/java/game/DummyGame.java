package game;

import engine.IGameLogic;
import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.event.MouseInput;
import engine.graph.renderer.DungeonRenderer;
import engine.graph.renderer.Renderer;
import engine.graph.renderer.forge.Extraction;
import loader.generator.Dungeon;
import loader.texture.TextureCache;

public class DummyGame implements IGameLogic {

    private final Renderer renderer;

    private Dungeon dungeon;

    private DungeonRenderer dungeonR;

    private final Extraction extraction;

    public DummyGame() {
        renderer = new Renderer();
        extraction = new Extraction();
    }

    @Override
    public void init(final HWindow window) throws Exception {
        renderer.init(window);

        dungeon = new Dungeon(window, 5, 10, 5, 5);
        dungeonR = new DungeonRenderer(window, dungeon);

        renderer.setDungeonRenderer(dungeonR);
    }

    @Override
    public void input(final HWindow window, final MouseInput mouseInput) {
        dungeonR.input(window, mouseInput);
    }

    @Override
    public void update(final float interval, final MouseInput mouseInput, final HWindow window) {
        dungeonR.update(interval, window);
        extraction.update(interval);
    }

    @Override
    public void render(final HWindow window) {
        window.render();

        //extraction.render(window);
        renderer.render(window, dungeon);
    }

    @Override
    public void cleanup(final HWindow window) {
        renderer.cleanup();

        TextureCache.cleanup();
    }

}
